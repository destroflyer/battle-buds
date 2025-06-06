package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.AssassinTrait;
import com.destroflyer.battlebuds.shared.game.traits.LegionTrait;

public class Soldier extends Unit {

    public Soldier() {
        name = "Soldier";
        visualName = "soldier";
        cost = 2;
        baseTraits = new Class[] { LegionTrait.class, AssassinTrait.class };
        baseMaximumHealth = 650f;
        baseMaximumMana = 100f;
        baseInitialMana = 50f;
        baseArmor = 25;
        baseMagicResistance = 25;
        baseAttackDamage = 65;
        baseAttackSpeed = 0.7f;
        baseAttackRange = ATTACK_RANGE_MELEE;
        hasProjectileAttacks = false;
    }
}
