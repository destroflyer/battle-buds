package com.destroflyer.battlebuds.client;

import com.destroflyer.battlebuds.client.appstates.*;
import com.destroflyer.battlebuds.shared.lobby.LobbyGame;
import lombok.Getter;

public class ClientApplication extends BaseApplication {

    public ClientApplication(String jwt, String serverHost, int serverPort) {
        this.jwt = jwt;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }
    @Getter
    private String jwt;
    @Getter
    private String serverHost;
    @Getter
    private int serverPort;

    @Override
    public void simpleInitApp() {
        super.simpleInitApp();
        stateManager.attach(new ClientNetworkAppState());
        stateManager.attach(new ClientLobbyAppState());
    }

    public void joinGame(LobbyGame lobbyGame) {
        stateManager.getState(MainMenuAppState.class).setEnabled(false);
        stateManager.attach(new GameAppState(lobbyGame));
    }

    public void exitGame() {
        stateManager.detach(stateManager.getState(GameAppState.class));
        stateManager.getState(MainMenuAppState.class).setEnabled(true);
    }
}
