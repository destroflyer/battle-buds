package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class EgosShield extends Item {

    public EgosShield() {
        name = "Ego's Shield";
        visualName = "egos_shield";
        description = "+250 Health, +20 Armor, +20% Crit Chance, 8% Damage Reduction, TODO: Bonus Damage Reduction based on current Health";
    }

    @Override
    public float getBonusMaximumHealthFlat(Unit unit) {
        return 250;
    }

    @Override
    public float getBonusArmorFlat(Unit unit) {
        return 20;
    }

    @Override
    public float getBonusCritChanceFlat(Unit unit) {
        return 0.2f;
    }

    @Override
    public float getBonusDamageTakenAmplificationFlat(Unit unit) {
        return -0.08f;
    }
}
