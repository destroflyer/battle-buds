package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.BrawlerTrait;
import com.destroflyer.battlebuds.shared.game.traits.DragonTrait;

public class Shyvana extends Unit {

    public Shyvana() {
        name = "Shyvana";
        visualName = "shyvana";
        cost = 3;
        baseTraits = new Class[] { DragonTrait.class, BrawlerTrait.class };
        baseMaximumHealth = 850f;
        baseMaximumMana = 90f;
        baseInitialMana = 30f;
        baseArmor = 50;
        baseMagicResistance = 50;
        baseAttackDamage = 50;
        baseAttackSpeed = 0.6f;
        baseAttackRange = ATTACK_RANGE_MELEE;
        hasProjectileAttacks = false;
    }
}
