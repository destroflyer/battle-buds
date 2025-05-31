package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class HellsScream extends Item {

    public HellsScream() {
        name = "Hells Scream";
        visualName = "hells_scream";
        description = "+15% Attack Damage, +15 Ability Power, +20 Magic Resistance, +20% Omnivamp, TODO: Shield";
    }

    @Override
    public float getBonusAttackDamagePercent(Unit unit) {
        return 0.15f;
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        return 15;
    }

    @Override
    public float getBonusMagicResistanceFlat(Unit unit) {
        return 20;
    }

    @Override
    public float getBonusOmnivampFlat(Unit unit) {
        return 0.2f;
    }
}
