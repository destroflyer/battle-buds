package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class BlueGem extends Item {

    public BlueGem() {
        name = "Blue Gem";
        visualName = "blue_gem";
        description = "+15 Mana";
    }

    @Override
    public float getBonusInitialManaFlat(Unit unit) {
        return 15;
    }
}
