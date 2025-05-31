package com.destroflyer.battlebuds.server.appstates;

import com.destroflyer.battlebuds.shared.Account;
import com.destroflyer.battlebuds.shared.Util;
import com.destroflyer.battlebuds.shared.game.Game;
import com.destroflyer.battlebuds.shared.game.objects.Player;
import com.destroflyer.battlebuds.shared.lobby.LobbyGame;
import com.destroflyer.battlebuds.shared.network.NetworkUtil;
import com.destroflyer.battlebuds.shared.network.messages.*;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ServerGamesAppState extends BaseServerAppState {

    private ArrayList<Game> games = new ArrayList<>();

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        ServerNetworkAppState serverNetworkAppState = getAppState(ServerNetworkAppState.class);
        serverNetworkAppState.addMessageListener(WatchPlayerMessage.class, (account, watchPlayerMessage) -> {
            enqueueGameAction(account, player -> {
                player.tryWatchPlayer(watchPlayerMessage.getPlayerId());
            });
        });
        serverNetworkAppState.addMessageListener(WalkMessage.class, (account, walkMessage) -> {
            enqueueGameAction(account, player -> {
                player.setTargetPosition(walkMessage.getTargetPosition(), 0);
            });
        });
        serverNetworkAppState.addMessageListener(BuyExperienceMessage.class, (account, _) -> {
            enqueueGameAction(account, player -> {
                player.tryBuyExperience();
            });
        });
        serverNetworkAppState.addMessageListener(BuyRerollMessage.class, (account, _) -> {
            enqueueGameAction(account, player -> {
                player.tryBuyReroll();
            });
        });
        serverNetworkAppState.addMessageListener(BuyUnitMessage.class, (account, buyUnitMessage) -> {
            enqueueGameAction(account, player -> {
                player.tryBuyUnit(buyUnitMessage.getShopSlotIndex());
            });
        });
        serverNetworkAppState.addMessageListener(SellUnitMessage.class, (account, sellUnitMessage) -> {
            enqueueGameAction(account, player -> {
                player.trySellUnit(sellUnitMessage.getUnitId());
            });
        });
        serverNetworkAppState.addMessageListener(MoveUnitMessage.class, (account, moveUnitMessage) -> {
            enqueueGameAction(account, player -> {
                player.tryMoveUnit(moveUnitMessage.getUnitId(), moveUnitMessage.getPositionSlot());
            });
        });
        serverNetworkAppState.addMessageListener(CombineBenchItemsMessage.class, (account, combineBenchItemsMessage) -> {
            enqueueGameAction(account, player -> {
                player.tryCombineBenchItems(combineBenchItemsMessage.getItemIndex1(), combineBenchItemsMessage.getItemIndex2());
            });
        });
        serverNetworkAppState.addMessageListener(UseItemMessage.class, (account, combineItemsMessage) -> {
            enqueueGameAction(account, player -> {
                player.tryUseItem(combineItemsMessage.getBenchItemIndex(), combineItemsMessage.getUnitId());
            });
        });
        serverNetworkAppState.addMessageListener(DecideMessage.class, (account, decideMessage) -> {
            enqueueGameAction(account, player -> {
                player.tryDecide(decideMessage.getDecisionOptionIndex());
            });
        });
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        for (Game game : games.toArray(Game[]::new)) {
            game.update(tpf);
            sendGameUpdate(game);
            if (game.isFinished()) {
                stopGame(game);
            }
        }
    }

    public void startGame(LobbyGame lobbyGame) {
        Game game = new Game(lobbyGame);
        games.add(game);
    }

    public void stopGame(Game game) {
        games.remove(game);
        getAppState(ServerLobbyAppState.class).stopGame(game.getLobbyGame());
    }

    private void enqueueGameAction(Account account, Consumer<Player> onPlayerFound) {
        mainApplication.enqueue(() -> {
            Player player = getPlayer(account.getId());
            if (player != null) {
                onPlayerFound.accept(player);
            }
        });
    }

    private Player getPlayer(int accountId) {
        for (Game game : games) {
            Player player = game.getPlayerByAccountId(accountId);
            if (player != null) {
                return player;
            }
        }
        return null;
    }

    private void sendGameUpdate(Game game) {
        byte[] gameBytes = NetworkUtil.writeForClient(game);
        GameMessage[] gameMessages = getGameMessages(gameBytes);
        Predicate<Account> gameAccountFilter = account -> game.getLobbyGame().getPlayers().stream().anyMatch(lobbyPlayer -> lobbyPlayer.getAccount().getId() == account.getId());
        getAppState(ServerNetworkAppState.class).send(gameAccountFilter, gameMessages);
    }

    private GameMessage[] getGameMessages(byte[] gameBytes) {
        // It started crashing from roughly 32000 (too many bytes for one message), so 20000 should be safe
        ArrayList<byte[]> chunks = Util.split(gameBytes, 20000);
        GameMessage[] gameMessages = new GameMessage[chunks.size()];
        for (int i = 0; i < chunks.size(); i++) {
            gameMessages[i] = new GameMessage(chunks.get(i), (i == (chunks.size() - 1)));
        }
        return gameMessages;
    }
}
