package com.destroflyer.battlebuds.shared.game.augments;

import com.destroflyer.battlebuds.shared.game.Augment;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.items.Items;

public class ItemGrabBag1 extends Augment {

    public ItemGrabBag1() {
        tier = Tier.SILVER;
        name = "Item Grab Bag";
        description = "Gain 1 random completed item.";
    }

    @Override
    public void onAdd() {
        super.onAdd();
        player.dropForSelf(Items.createRandomFullItemLoot());
    }
}
