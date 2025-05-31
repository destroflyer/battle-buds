package com.destroflyer.battlebuds.server.appstates;

import com.destroflyer.battlebuds.shared.Account;

public interface ServerMessageListener<M> {

    void onMessageReceived(Account account, M message);
}
