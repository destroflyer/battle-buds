package com.destroflyer.battlebuds.shared.game.objects.pickup;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.PickUpObject;
import com.destroflyer.battlebuds.shared.game.objects.Player;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;

import java.io.IOException;

public class ItemLoot extends PickUpObject {

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
    public boolean canBePickupedBy(Player player) {
        return super.canBePickupedBy(player) && player.canAddItem();
    }

    @Override
    protected void onPickUp(Player player) {
        player.addItem(item);
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
