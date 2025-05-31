package com.destroflyer.battlebuds.server;

import com.destroflyer.battlebuds.server.appstates.ServerGamesAppState;
import com.destroflyer.battlebuds.server.appstates.ServerLobbyAppState;
import com.destroflyer.battlebuds.server.appstates.ServerNetworkAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

public class ServerApplication extends SimpleApplication {

    public ServerApplication() {
        settings = new AppSettings(false);
        settings.setFrameRate(60);
    }

    @Override
    public void simpleInitApp() {
        stateManager.attach(new ServerNetworkAppState());
        stateManager.attach(new ServerLobbyAppState());
        stateManager.attach(new ServerGamesAppState());
    }
}
