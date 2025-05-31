package com.destroflyer.battlebuds.server.appstates;

import com.destroflyer.battlebuds.server.AuthTokenUtil;
import com.destroflyer.battlebuds.shared.Account;
import com.destroflyer.battlebuds.shared.network.NetworkUtil;
import com.destroflyer.battlebuds.shared.network.messages.LoginMessage;
import com.destroflyer.battlebuds.shared.network.messages.LoginSuccessMessage;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

public class ServerNetworkAppState extends BaseServerAppState implements ConnectionListener, MessageListener<HostedConnection> {

    private Server server;
    private HashMap<Integer, Account> accounts = new HashMap<>();
    private ArrayList<AccountListener> accountListeners = new ArrayList<>();
    private HashMap<Class<?>, ArrayList<ServerMessageListener>> messageListeners = new HashMap<>();

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        try {
            server = Network.createServer(NetworkUtil.PORT);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        server.addMessageListener(this);
        server.addConnectionListener(this);
        server.start();
    }

    public void addAccountListener(AccountListener accountListener) {
        accountListeners.add(accountListener);
    }

    public <T extends Message> void addMessageListener(Class<T> messageClass, ServerMessageListener<T> messageListener) {
        messageListeners.computeIfAbsent(messageClass, _ -> new ArrayList<>()).add(messageListener);
    }

    @Override
    public void connectionAdded(Server server, HostedConnection connection) {
        System.out.println("Connection added: #" + connection.getId() + " (" + connection.getAddress() + ")");
    }

    @Override
    public void messageReceived(HostedConnection connection, Message message) {
        if (message instanceof LoginMessage loginMessage) {
            Account account = AuthTokenUtil.getAccount(loginMessage.getJwt());
            accounts.put(connection.getId(), account);
            accountListeners.forEach(accountListener -> accountListener.onLogin(account));
            System.out.println("Login: " + connection.getId() + " (" + connection.getAddress() + ")" + " -> " + account);
            connection.send(new LoginSuccessMessage(account));
        }
        ArrayList<ServerMessageListener> listeners = messageListeners.get(message.getClass());
        if (listeners != null) {
            Account account = accounts.get(connection.getId());
            listeners.forEach(listener -> listener.onMessageReceived(account, message));
        }
    }

    @Override
    public void connectionRemoved(Server server, HostedConnection connection) {
        System.out.println("Connection removed: #" + connection.getId() + " (" + connection.getAddress() + ")");
        Account account = accounts.remove(connection.getId());
        if (account != null) {
            System.out.println("Logout: " + connection.getId() + " (" + connection.getAddress() + ")" + " -> " + account);
            accountListeners.forEach(accountListener -> accountListener.onLogout(account));
        }
    }

    public void send(Predicate<Account> accountFilter, Message message) {
        send(accountFilter, new Message[] { message });
    }

    public void send(Predicate<Account> accountFilter, Message[] messages) {
        Filter<HostedConnection> connectionFilter = connection -> {
            Account account = accounts.get(connection.getId());
            return ((account != null) && accountFilter.test(account));
        };
        for (Message message : messages) {
            server.broadcast(connectionFilter, message);
        }
    }

    public void send(Message message) {
        server.broadcast(message);
    }
}
