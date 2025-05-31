package com.destroflyer.battlebuds.shared.game.augments;

import com.destroflyer.battlebuds.shared.game.Augment;
import com.destroflyer.battlebuds.shared.game.Tier;

public class ItemGrabBag1 extends Augment {

    public ItemGrabBag1() {
        tier = Tier.SILVER;
        name = "Item Grab Bag";
        description = "Gain 1 random completed item.";
    }

    @Override
    public void onAdd() {
        super.onAdd();
        player.dropRandomFullItem();
    }
}
