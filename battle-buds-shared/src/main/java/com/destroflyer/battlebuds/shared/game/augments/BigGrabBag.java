package com.destroflyer.battlebuds.shared.game.augments;

import com.destroflyer.battlebuds.shared.game.Augment;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.items.Items;

public class BigGrabBag extends Augment {

    public BigGrabBag() {
        tier = Tier.GOLD;
        name = "Item Grab Bag";
        description = "Gain 3 random components, 2 gold, and TODO: 1 Reforger. ";
    }

    @Override
    public void onAdd() {
        super.onAdd();
        for (int i = 0; i < 3; i++) {
            player.dropForSelf(Items.createRandomComponentLoot());
        }
        player.dropGoldForSelf(2);
    }
}
