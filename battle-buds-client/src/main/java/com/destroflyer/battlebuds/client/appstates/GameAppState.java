package com.destroflyer.battlebuds.client.appstates;

import com.destroflyer.battlebuds.shared.Util;
import com.destroflyer.battlebuds.shared.game.Game;
import com.destroflyer.battlebuds.shared.game.objects.players.HumanPlayer;
import com.destroflyer.battlebuds.shared.lobby.LobbyGame;
import com.destroflyer.battlebuds.shared.network.NetworkUtil;
import com.destroflyer.battlebuds.shared.network.messages.GameMessage;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import lombok.Getter;

import java.util.ArrayList;

public class GameAppState extends BaseClientAppState {

    public GameAppState(LobbyGame lobbyGame) {
        this.lobbyGame = lobbyGame;
    }
    private LobbyGame lobbyGame;
    private ArrayList<byte[]> nextGameChunks = new ArrayList<>();
    @Getter
    private Game game;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        ClientNetworkAppState clientNetworkAppState = getAppState(ClientNetworkAppState.class);
        clientNetworkAppState.addMessageListener(GameMessage.class, gameMessage -> {
            nextGameChunks.add(gameMessage.getChunk());
            if (gameMessage.isFinalChunk()) {
                byte[] gameBytes = Util.merge(nextGameChunks);
                nextGameChunks.clear();
                mainApplication.enqueue(() -> {
                    boolean isInitializing = (game == null);
                    game = NetworkUtil.readForClient(gameBytes);
                    if (isInitializing) {
                        stateManager.attach(new ForestBoardAppState());
                        stateManager.attach(new GameWorldAppState());
                        stateManager.attach(new GameGuiAppState());
                    }
                });
            }
        });
    }

    public HumanPlayer getOwnPlayer() {
        return game.getHumanPlayerByAccountId(getOwnAccountId());
    }

    private int getOwnAccountId() {
        return getAppState(ClientLobbyAppState.class).getOwnLobbyPlayer().getAccount().getId();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        LobbyGame lobbyGame = getAppState(ClientLobbyAppState.class).getOwnLobbyGame();
        if (lobbyGame == null) {
            getAppState(GameGuiAppState.class).onGameOver();
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getStateManager().detach(mainApplication.getStateManager().getState(ForestBoardAppState.class));
        mainApplication.getStateManager().detach(mainApplication.getStateManager().getState(GameWorldAppState.class));
        mainApplication.getStateManager().detach(mainApplication.getStateManager().getState(GameGuiAppState.class));
    }
}
