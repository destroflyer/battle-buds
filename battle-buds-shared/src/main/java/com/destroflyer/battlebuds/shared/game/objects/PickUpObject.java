package com.destroflyer.battlebuds.shared.game.objects;

import com.destroflyer.battlebuds.shared.game.objects.players.ActualPlayer;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public abstract class PickUpObject extends PhysicsObject {

    @Getter
    @Setter
    private ActualPlayer owner;

    @Override
    public void update(float tpf) {
        super.update(tpf);
        ActualPlayer pickingUpPlayer = getPickingUpPlayer();
        if (pickingUpPlayer != null) {
            onPickUp(pickingUpPlayer);
            requestRemoveFromBoard();
        }
    }

    private ActualPlayer getPickingUpPlayer() {
        if (isForcedMoving()) {
            return null;
        }
        return game.getActualPlayers().stream()
                .filter(actualPlayer -> actualPlayer.getBoard() == board)
                .filter(this::canBePickupedBy)
                .filter(this::isColliding)
                .findAny().orElse(null);
    }

    public boolean canBePickupedBy(ActualPlayer actualPlayer) {
        return (owner == null) || (actualPlayer == owner);
    }

    protected abstract void onPickUp(ActualPlayer actualPlayer);

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        super.writeForClient(outputStream);
        outputStream.writeObject_Nullable(owner);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        super.readForClient(inputStream);
        owner = inputStream.readObject_Nullable();
    }
}
