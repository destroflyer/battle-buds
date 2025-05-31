package com.destroflyer.battlebuds.client;

import com.destroflyer.battlebuds.shared.LogUtil;
import com.destroflyer.battlebuds.shared.network.NetworkUtil;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        LogUtil.setupLogFile();

        HashMap<String, String> settings = Settings.readSettings();
        if (args.length == 0) {
            throw new RuntimeException("No JWT provided.");
        }
        String jwt = args[0];
        String serverHost = ((args.length > 1) ? args[1] : settings.getOrDefault("serverHost", "destrostudios.com"));
        int serverPort = Integer.parseInt((args.length > 2) ? args[2] : settings.getOrDefault("serverPort", "" + NetworkUtil.PORT));
        String assetsRoot = settings.getOrDefault("assetsRoot", "./assets/");

        NetworkUtil.registerSerializers();
        FileAssets.ROOT = assetsRoot;
        JMonkeyUtil.disableLogger();
        new ClientApplication(jwt, serverHost, serverPort).start();
    }
}
