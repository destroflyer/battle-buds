package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.ForestTrait;
import com.destroflyer.battlebuds.shared.game.traits.GuardianTrait;

public class EarthElemental extends Unit {

    public EarthElemental() {
        name = "Earth Elemental";
        visualName = "earth_elemental";
        cost = 3;
        baseTraits = new Class[] { ForestTrait.class, GuardianTrait.class };
        baseMaximumHealth = 850f;
        baseMaximumMana = 80f;
        baseInitialMana = 30f;
        baseArmor = 50;
        baseMagicResistance = 50;
        baseAttackDamage = 60;
        baseAttackSpeed = 0.6f;
        baseAttackRange = ATTACK_RANGE_MELEE;
    }
}
