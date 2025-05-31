package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class Requiem extends Item {

    public Requiem() {
        name = "Requiem";
        visualName = "requiem";
        description = "+150 Health, +15 Ability Power, +25 Magic Resistance, TODO: Magic resistance shred";
    }

    @Override
    public float getBonusMaximumHealthFlat(Unit unit) {
        return 150;
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        return 15;
    }

    @Override
    public float getBonusMagicResistanceFlat(Unit unit) {
        return 25;
    }
}
