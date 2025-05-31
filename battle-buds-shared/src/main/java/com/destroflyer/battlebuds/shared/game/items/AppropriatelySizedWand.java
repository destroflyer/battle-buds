package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class AppropriatelySizedWand extends Item {

    public AppropriatelySizedWand() {
        name = "Appropriately Sized Wand";
        visualName = "appropriately_sized_wand";
        description = "+20 Ability Power";
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        return 20;
    }
}
