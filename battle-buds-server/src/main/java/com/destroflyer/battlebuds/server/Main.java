package com.destroflyer.battlebuds.server;

import com.destroflyer.battlebuds.shared.LogUtil;
import com.destroflyer.battlebuds.shared.network.NetworkUtil;
import com.jme3.system.JmeContext;

public class Main {

    public static void main(String[] args) {
        LogUtil.setupLogFile();
        NetworkUtil.registerSerializers();
        new ServerApplication().start(JmeContext.Type.Headless);
    }
}
