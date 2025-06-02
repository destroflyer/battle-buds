package com.destroflyer.battlebuds.shared.game.objects.players;

import com.destroflyer.battlebuds.shared.game.Board;
import com.destroflyer.battlebuds.shared.game.objects.Player;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.OptimizedBits;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HumanPlayer extends ActualPlayer {

    public HumanPlayer(int accountId) {
        this.accountId = accountId;
    }
    @Getter
    private int accountId;
    @Getter
    private ActualPlayer watchedPlayer;

    public void tryWatchPlayer(int playerId) {
        Player player = game.getPlayerById(playerId);
        if (player instanceof ActualPlayer actualPlayer) {
            watchPlayer(actualPlayer);
        }
    }

    public void watchPlayer(ActualPlayer actualPlayer) {
        watchedPlayer = actualPlayer;
        Board watchedBoard = getWatchedBoard();
        if (watchedBoard != null) {
            watchedBoard.addObject(this);
        }
    }

    public Board getWatchedBoard() {
        return watchedPlayer.getOwnBoard();
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        super.writeForClient(outputStream);;
        outputStream.writeBits(accountId, OptimizedBits.SIGNED_INT_TO_1048576);
        outputStream.writeObject(watchedPlayer);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        super.readForClient(inputStream);
        accountId = inputStream.readBits(OptimizedBits.SIGNED_INT_TO_1048576);
        watchedPlayer = inputStream.readObject();
    }
}
