package com.destroflyer.battlebuds.client.appstates;

import com.destroflyer.battlebuds.shared.Account;
import com.destroflyer.battlebuds.shared.lobby.Lobby;
import com.destroflyer.battlebuds.shared.lobby.LobbyGame;
import com.destroflyer.battlebuds.shared.lobby.LobbyPlayer;
import com.destroflyer.battlebuds.shared.lobby.LobbyPlayerStatus;
import com.destroflyer.battlebuds.shared.network.messages.LobbyMessage;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import lombok.Getter;

public class ClientLobbyAppState extends MenuAppState {

    @Getter
    private Lobby lobby;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        ClientNetworkAppState clientNetworkAppState = getAppState(ClientNetworkAppState.class);
        clientNetworkAppState.addMessageListener(LobbyMessage.class, lobbyMessage -> {
            mainApplication.enqueue(() -> {
                if (lobby == null) {
                    mainApplication.getStateManager().attach(new MainMenuAppState());
                    mainApplication.getStateManager().attach(new SettingsMenuAppState());
                }
                lobby = lobbyMessage.getLobby();
            });
        });
    }

    public LobbyGame getOwnLobbyGame() {
        Account account = getAppState(ClientNetworkAppState.class).getAccount();
        return lobby.getGames().stream().filter(lobbyGame -> lobbyGame.getPlayers().stream().anyMatch(lobbyPlayer -> lobbyPlayer.getAccount().getId() == account.getId())).findAny().orElse(null);
    }

    public boolean isQueueing() {
        LobbyPlayer ownLobbyPlayer = getOwnLobbyPlayer();
        return ownLobbyPlayer.getStatus() == LobbyPlayerStatus.QUEUE;
    }

    public LobbyPlayer getOwnLobbyPlayer() {
        Account account = getAppState(ClientNetworkAppState.class).getAccount();
        return lobby.getPlayers().stream().filter(lobbyPlayer -> lobbyPlayer.getAccount().getId() == account.getId()).findAny().orElse(null);
    }
}
