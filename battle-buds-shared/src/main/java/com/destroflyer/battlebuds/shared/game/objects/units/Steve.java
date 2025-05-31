package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.GolemTrait;
import com.destroflyer.battlebuds.shared.game.traits.GuardianTrait;

public class Steve extends Unit {

    public Steve() {
        name = "Steve";
        visualName = "steve";
        cost = 1;
        baseTraits = new Class[] { GolemTrait.class, GuardianTrait.class };
        baseMaximumHealth = 650f;
        baseMaximumMana = 70f;
        baseInitialMana = 30f;
        baseArmor = 40;
        baseMagicResistance = 40;
        baseAttackDamage = 55;
        baseAttackSpeed = 0.6f;
        baseAttackRange = ATTACK_RANGE_MELEE;
    }
}
