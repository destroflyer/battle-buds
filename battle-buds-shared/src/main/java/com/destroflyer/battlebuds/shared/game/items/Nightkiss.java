package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class Nightkiss extends Item {

    public Nightkiss() {
        name = "Nightkiss";
        visualName = "nightkiss";
        description = "+15 Mana, +20 Ability Power, TODO: Ability Power per second";
    }

    @Override
    public float getBonusInitialManaFlat(Unit unit) {
        return 15;
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        return 20;
    }
}
