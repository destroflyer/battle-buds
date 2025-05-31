package com.destroflyer.battlebuds.client.appstates;

import com.destroflyer.battlebuds.shared.Account;
import com.destroflyer.battlebuds.shared.network.NetworkUtil;
import com.destroflyer.battlebuds.shared.network.messages.LoginMessage;
import com.destroflyer.battlebuds.shared.network.messages.LoginSuccessMessage;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.*;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientNetworkAppState extends BaseClientAppState implements MessageListener<Client> {

    private Client client;
    private HashMap<Class<?>, ArrayList<ClientMessageListener>> messageListeners = new HashMap<>();
    @Getter
    private Account account;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        try {
            client = Network.connectToServer(mainApplication.getServerHost(), mainApplication.getServerPort());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        client.addMessageListener(this);
        client.start();
        client.send(new LoginMessage(mainApplication.getJwt()));
    }

    public <T extends Message> void addMessageListener(Class<T> messageClass, ClientMessageListener<T> messageListener) {
        messageListeners.computeIfAbsent(messageClass, _ -> new ArrayList<>()).add(messageListener);
    }

    @Override
    public void messageReceived(Client c, Message message) {
        if (message instanceof LoginSuccessMessage loginSuccessMessage) {
            this.account = loginSuccessMessage.getAccount();
            System.out.println("Login: " + this.account);
        }
        ArrayList<ClientMessageListener> listeners = messageListeners.get(message.getClass());
        if (listeners != null) {
            listeners.forEach(listener -> listener.onMessageReceived(message));
        }
    }

    public void send(Message message) {
        client.send(message);
    }
}
