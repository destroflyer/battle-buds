package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class PlatedArmor extends Item {

    public PlatedArmor() {
        name = "PlatedArmor";
        visualName = "plated_armor";
        description = "+20 Armor";
    }

    @Override
    public float getBonusArmorFlat(Unit unit) {
        return 20;
    }
}
