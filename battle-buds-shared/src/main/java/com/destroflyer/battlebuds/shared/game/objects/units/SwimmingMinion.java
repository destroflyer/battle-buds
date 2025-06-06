package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.FairyTrait;
import com.destroflyer.battlebuds.shared.game.traits.MinionTrait;

public class SwimmingMinion extends Unit {

    public SwimmingMinion() {
        name = "Swimming Minion";
        visualName = "swimming_minion";
        cost = 2;
        baseTraits = new Class[] { MinionTrait.class, FairyTrait.class };
        baseMaximumHealth = 800f;
        baseMaximumMana = 80f;
        baseInitialMana = 30f;
        baseArmor = 45;
        baseMagicResistance = 45;
        baseAttackDamage = 55;
        baseAttackSpeed = 0.6f;
        baseAttackRange = ATTACK_RANGE_MELEE;
        hasProjectileAttacks = false;
    }
}
