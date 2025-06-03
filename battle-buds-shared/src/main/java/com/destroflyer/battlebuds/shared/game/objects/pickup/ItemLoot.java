package com.destroflyer.battlebuds.shared.game.objects.pickup;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.PickupObject;
import com.destroflyer.battlebuds.shared.game.objects.players.ActualPlayer;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;

import java.io.IOException;

public class ItemLoot extends PickupObject {

    private ItemLoot() {
        hasDynamicVisualName = true;
    }

    public ItemLoot(Item item) {
        this();
        this.item = item;
        visualName = "loot_" + (item.isComponent() ? "rare" : "legendary");
    }
    private Item item;

    @Override
    public boolean canBePickupedBy(ActualPlayer actualPlayer) {
        return super.canBePickupedBy(actualPlayer) && actualPlayer.canAddItem();
    }

    @Override
    protected void onPickUp(ActualPlayer actualPlayer) {
        actualPlayer.addItem(item);
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        super.writeForClient(outputStream);
        outputStream.writeObject(item);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        super.readForClient(inputStream);
        item = inputStream.readObject();
    }
}
