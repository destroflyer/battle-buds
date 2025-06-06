package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.BrawlerTrait;
import com.destroflyer.battlebuds.shared.game.traits.TricksterTrait;

public class Kachujin extends Unit {

    public Kachujin() {
        name = "Kachujin";
        visualName = "kachujin";
        cost = 1;
        baseTraits = new Class[] { BrawlerTrait.class, TricksterTrait.class };
        baseMaximumHealth = 650f;
        baseMaximumMana = 75f;
        baseInitialMana = 25f;
        baseArmor = 40;
        baseMagicResistance = 40;
        baseAttackDamage = 45;
        baseAttackSpeed = 0.65f;
        baseAttackRange = ATTACK_RANGE_MELEE;
        hasProjectileAttacks = false;
    }
}
