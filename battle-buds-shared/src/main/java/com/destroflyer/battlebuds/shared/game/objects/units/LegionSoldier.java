package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.BrawlerTrait;
import com.destroflyer.battlebuds.shared.game.traits.LegionTrait;

public class LegionSoldier extends Unit {

    public LegionSoldier() {
        name = "Legion Soldier";
        visualName = "3dsa_medieval_knight";
        cost = 1;
        baseTraits = new Class[] { LegionTrait.class, BrawlerTrait.class };
        baseMaximumHealth = 650f;
        baseMaximumMana = 70f;
        baseInitialMana = 20f;
        baseArmor = 40;
        baseMagicResistance = 40;
        baseAttackDamage = 50;
        baseAttackSpeed = 0.6f;
        baseAttackRange = ATTACK_RANGE_MELEE;
        hasProjectileAttacks = false;
    }
}
