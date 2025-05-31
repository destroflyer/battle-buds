package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.GuardianTrait;
import com.destroflyer.battlebuds.shared.game.traits.LegionTrait;

public class Tristan extends Unit {

    public Tristan() {
        name = "Tristan";
        visualName = "tristan";
        cost = 4;
        baseTraits = new Class[] { LegionTrait.class, GuardianTrait.class };
        baseMaximumHealth = 1000f;
        baseMaximumMana = 110f;
        baseInitialMana = 50f;
        baseArmor = 60;
        baseMagicResistance = 60;
        baseAttackDamage = 60;
        baseAttackSpeed = 0.6f;
        baseAttackRange = ATTACK_RANGE_MELEE;
    }
}
