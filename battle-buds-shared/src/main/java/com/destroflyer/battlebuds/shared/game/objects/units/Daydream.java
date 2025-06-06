package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.BrawlerTrait;
import com.destroflyer.battlebuds.shared.game.traits.GuardianTrait;

public class Daydream extends Unit {

    public Daydream() {
        name = "Daydream";
        visualName = "daydream";
        cost = 2;
        baseTraits = new Class[] { BrawlerTrait.class, GuardianTrait.class };
        baseMaximumHealth = 800f;
        baseMaximumMana = 75f;
        baseInitialMana = 25f;
        baseArmor = 50;
        baseMagicResistance = 50;
        baseAttackDamage = 60;
        baseAttackSpeed = 0.55f;
        baseAttackRange = ATTACK_RANGE_MELEE;
        hasProjectileAttacks = false;
    }
}
