package com.destroflyer.battlebuds.client.appstates;

public interface ClientMessageListener<M> {

    void onMessageReceived(M message);
}
