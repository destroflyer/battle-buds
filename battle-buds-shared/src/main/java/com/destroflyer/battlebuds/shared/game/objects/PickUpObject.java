package com.destroflyer.battlebuds.shared.game.objects;

import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public abstract class PickUpObject extends PhysicsObject {

    @Getter
    @Setter
    private Player owner;

    @Override
    public void update(float tpf) {
        super.update(tpf);
        Player pickingUpPlayer = getPickingUpPlayer();
        if (pickingUpPlayer != null) {
            onPickUp(pickingUpPlayer);
            requestRemoveFromBoard();
        }
    }

    private Player getPickingUpPlayer() {
        if (isForcedMoving()) {
            return null;
        }
        return game.getActualPlayers().stream()
                .filter(player -> player.getBoard() == board)
                .filter(this::canBePickupedBy)
                .filter(this::isColliding)
                .findAny().orElse(null);
    }

    public boolean canBePickupedBy(Player player) {
        return (owner == null) || (player == owner);
    }

    protected abstract void onPickUp(Player player);

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
