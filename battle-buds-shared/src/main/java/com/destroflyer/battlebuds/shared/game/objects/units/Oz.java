package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.BrawlerTrait;
import com.destroflyer.battlebuds.shared.game.traits.GolemTrait;

public class Oz extends Unit {

    public Oz() {
        name = "Oz";
        visualName = "oz";
        cost = 2;
        baseTraits = new Class[] { BrawlerTrait.class, GolemTrait.class };
        baseMaximumHealth = 800f;
        baseMaximumMana = 80f;
        baseInitialMana = 30f;
        baseArmor = 45;
        baseMagicResistance = 45;
        baseAttackDamage = 65;
        baseAttackSpeed = 0.6f;
        baseAttackRange = ATTACK_RANGE_MELEE;
        hasProjectileAttacks = false;
    }
}
