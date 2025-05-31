package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class Icecold extends Item {

    public Icecold() {
        name = "Icecold";
        visualName = "icecold";
        description = "+15 Mana, +15% Attack Speed, +15 Ability Power, TODO: Magic resistance shred";
    }

    @Override
    public float getBonusInitialManaFlat(Unit unit) {
        return 15;
    }

    @Override
    public float getBonusAttackSpeedFlat(Unit unit) {
        return 0.15f;
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        return 15;
    }
}
