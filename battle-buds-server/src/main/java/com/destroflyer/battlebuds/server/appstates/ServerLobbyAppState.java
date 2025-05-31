package com.destroflyer.battlebuds.server.appstates;

import com.destroflyer.battlebuds.shared.Account;
import com.destroflyer.battlebuds.shared.Characters;
import com.destroflyer.battlebuds.shared.GameMode;
import com.destroflyer.battlebuds.shared.lobby.Lobby;
import com.destroflyer.battlebuds.shared.lobby.LobbyGame;
import com.destroflyer.battlebuds.shared.lobby.LobbyPlayer;
import com.destroflyer.battlebuds.shared.lobby.LobbyPlayerStatus;
import com.destroflyer.battlebuds.shared.network.messages.*;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;

import java.util.ArrayList;
import java.util.List;

public class ServerLobbyAppState extends BaseServerAppState implements AccountListener {

    private Lobby lobby = new Lobby(
        new ArrayList<>(),
        new ArrayList<>()
    );
    private int nextGameId;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        ServerNetworkAppState serverNetworkAppState = getAppState(ServerNetworkAppState.class);
        serverNetworkAppState.addAccountListener(this);
        serverNetworkAppState.addMessageListener(SelectCharacterMessage.class, (account, selectCharacterMessage) -> {
            mainApplication.enqueue(() -> {
                getLobbyPlayer(account).setCharacterName(selectCharacterMessage.getCharacterName());
                sendLobbyUpdate();
            });
        });
        serverNetworkAppState.addMessageListener(SelectGameModeMessage.class, (account, selectGameModeMessage) -> {
            mainApplication.enqueue(() -> {
                getLobbyPlayer(account).setGameMode(selectGameModeMessage.getGameMode());
                checkEnoughPlayers();
                sendLobbyUpdate();
            });
        });
        serverNetworkAppState.addMessageListener(QueueMessage.class, (account, _) -> {
            mainApplication.enqueue(() -> {
                getLobbyPlayer(account).setStatus(LobbyPlayerStatus.QUEUE);
                checkEnoughPlayers();
                sendLobbyUpdate();
            });
        });
        serverNetworkAppState.addMessageListener(UnqueueMessage.class, (account, _) -> {
            mainApplication.enqueue(() -> {
                getLobbyPlayer(account).setStatus(LobbyPlayerStatus.IDLE);
                sendLobbyUpdate();
            });
        });
    }

    @Override
    public void onLogin(Account account) {
        lobby.getPlayers().add(new LobbyPlayer(account, LobbyPlayerStatus.IDLE, GameMode.SINGLE_FOUR, Characters.getRandomCharacterName()));
        sendLobbyUpdate();
    }

    @Override
    public void onLogout(Account account) {
        LobbyPlayer lobbyPlayer = getLobbyPlayer(account);
        if (lobbyPlayer != null) {
            lobby.getPlayers().remove(lobbyPlayer);
            sendLobbyUpdate();
        }
    }

    private LobbyPlayer getLobbyPlayer(Account account) {
        return lobby.getPlayers().stream().filter(player -> player.getAccount().getId() == account.getId()).findAny().orElse(null);
    }

    private void checkEnoughPlayers() {
        List<LobbyPlayer> allQueueingPlayers = lobby.getPlayers().stream().filter(lobbyPlayer -> lobbyPlayer.getStatus() == LobbyPlayerStatus.QUEUE).toList();
        for (GameMode gameMode : GameMode.values()) {
            List<LobbyPlayer> remainingGameModeQueueingPlayers = allQueueingPlayers.stream().filter(lobbyPlayer -> lobbyPlayer.getGameMode() == gameMode).toList();
            while (remainingGameModeQueueingPlayers.size() >= gameMode.getPlayerCount()) {
                ArrayList<LobbyPlayer> gamePlayers = new ArrayList<>(remainingGameModeQueueingPlayers.subList(0, gameMode.getPlayerCount()));
                for (LobbyPlayer lobbyPlayer : gamePlayers) {
                    lobbyPlayer.setStatus(LobbyPlayerStatus.INGAME);
                }
                startGame(gameMode, gamePlayers);
                remainingGameModeQueueingPlayers = remainingGameModeQueueingPlayers.subList(gameMode.getPlayerCount(), remainingGameModeQueueingPlayers.size());
            }
        }
    }

    private void startGame(GameMode gameMode, ArrayList<LobbyPlayer> players) {
        LobbyGame lobbyGame = new LobbyGame(nextGameId++, gameMode, players);
        lobby.getGames().add(lobbyGame);
        getAppState(ServerGamesAppState.class).startGame(lobbyGame);
    }

    public void stopGame(LobbyGame lobbyGame) {
        lobby.getGames().remove(lobbyGame);
        for (LobbyPlayer lobbyPlayer : lobbyGame.getPlayers()) {
            lobbyPlayer.setStatus(LobbyPlayerStatus.IDLE);
        }
        sendLobbyUpdate();
    }

    private void sendLobbyUpdate() {
        getAppState(ServerNetworkAppState.class).send(new LobbyMessage(lobby));
    }
}
