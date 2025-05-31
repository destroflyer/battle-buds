package com.destroflyer.battlebuds.shared.network;

import com.destroflyer.battlebuds.shared.Account;
import com.destroflyer.battlebuds.shared.game.PositionSlot;
import com.destroflyer.battlebuds.shared.lobby.Lobby;
import com.destroflyer.battlebuds.shared.lobby.LobbyGame;
import com.destroflyer.battlebuds.shared.lobby.LobbyPlayer;
import com.destroflyer.battlebuds.shared.network.messages.*;
import com.jme3.network.serializing.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NetworkUtil {

    public static final int PORT = 34000;

    public static void registerSerializers() {
        Serializer.registerClasses(
            BuyExperienceMessage.class,
            BuyRerollMessage.class,
            BuyUnitMessage.class,
            CombineBenchItemsMessage.class,
            DecideMessage.class,
            GameMessage.class,
            LobbyMessage.class,
                Lobby.class,
                LobbyPlayer.class,
                LobbyGame.class,
            LoginMessage.class,
            LoginSuccessMessage.class,
                Account.class,
            MoveUnitMessage.class,
                PositionSlot.class,
            QueueMessage.class,
            SelectCharacterMessage.class,
            SelectGameModeMessage.class,
            SellUnitMessage.class,
            UseItemMessage.class,
            WatchPlayerMessage.class,
            UnqueueMessage.class,
            WalkMessage.class
        );
    }

    public static byte[] writeForClient(GameSerializable serializable) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BitOutputStream outputStream = new BitOutputStream(byteArrayOutputStream);
        try {
            outputStream.writeObject(serializable);
            outputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T readForClient(byte[] bytes) {
        BitInputStream inputStream = new BitInputStream(new ByteArrayInputStream(bytes));
        try {
            return inputStream.readObject();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
