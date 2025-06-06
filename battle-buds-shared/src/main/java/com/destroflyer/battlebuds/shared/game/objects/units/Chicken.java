package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.AssassinTrait;
import com.destroflyer.battlebuds.shared.game.traits.BeastTrait;

public class Chicken extends Unit {

    public Chicken() {
        name = "Chicken";
        visualName = "chicken";
        cost = 1;
        baseTraits = new Class[] { BeastTrait.class, AssassinTrait.class };
        baseMaximumHealth = 500f;
        baseMaximumMana = 50f;
        baseInitialMana = 0f;
        baseArmor = 25;
        baseMagicResistance = 25;
        baseAttackDamage = 50;
        baseAttackSpeed = 0.7f;
        baseAttackRange = ATTACK_RANGE_MELEE;
        hasProjectileAttacks = false;
    }
}
