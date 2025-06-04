package com.destroflyer.battlebuds.shared.game;

import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.GameSerializable;
import com.destroflyer.battlebuds.shared.network.OptimizedBits;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class GameObject implements GameSerializable, GameEventListener {

    protected Game game;
    @Getter
    protected int id;
    @Getter
    @Setter
    protected Board board;
    @Getter
    private boolean removedFromBoard;

    public boolean isRegistered() {
        return game != null;
    }

    public void register(Game game, int id) {
        this.game = game;
        this.id = id;
    }

    public void update(float tpf) {

    }

    public void requestRemoveFromBoard() {
        removedFromBoard = true;
    }

    public void resetRemoveFromBoard() {
        removedFromBoard = false;
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        outputStream.writeObject_Nullable(game);
        outputStream.writeObject_Nullable(board);
        outputStream.writeBits(id, OptimizedBits.SIGNED_INT_TO_1048576);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        game = inputStream.readObject_Nullable();
        board = inputStream.readObject_Nullable();
        id = inputStream.readBits(OptimizedBits.SIGNED_INT_TO_1048576);
    }
}
