package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.BeastTrait;
import com.destroflyer.battlebuds.shared.game.traits.GuardianTrait;

public class Cow extends Unit {

    public Cow() {
        name = "Cow";
        visualName = "cow";
        cost = 4;
        baseTraits = new Class[] { BeastTrait.class, GuardianTrait.class };
        baseMaximumHealth = 1100f;
        baseMaximumMana = 120f;
        baseInitialMana = 60f;
        baseArmor = 50;
        baseMagicResistance = 50;
        baseAttackDamage = 65;
        baseAttackSpeed = 0.6f;
        baseAttackRange = ATTACK_RANGE_MELEE;
    }
}
