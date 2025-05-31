package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class ThunderlordTrident extends Item {

    public ThunderlordTrident() {
        name = "ThunderlordTrident";
        visualName = "thunderlord_trident";
        description = "+150 Health, +20% Attack Speed, +20% Crit Chance, +10% Damage Amplification, TODO: Stack Damage Amplification on crit";
    }

    @Override
    public float getBonusMaximumHealthFlat(Unit unit) {
        return 150;
    }

    @Override
    public float getBonusAttackSpeedFlat(Unit unit) {
        return 0.2f;
    }

    @Override
    public float getBonusCritChanceFlat(Unit unit) {
        return 0.2f;
    }

    @Override
    public float getBonusDamageDealtAmplificationFlat(Unit unit) {
        return 0.1f;
    }
}
