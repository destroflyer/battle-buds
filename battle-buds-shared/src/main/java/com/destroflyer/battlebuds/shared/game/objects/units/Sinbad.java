package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.LeaderTrait;
import com.destroflyer.battlebuds.shared.game.traits.PirateTrait;

public class Sinbad extends Unit {

    public Sinbad() {
        name = "Sinbad";
        visualName = "sinbad";
        cost = 5;
        baseTraits = new Class[] { PirateTrait.class, LeaderTrait.class };
        baseMaximumHealth = 1000f;
        baseMaximumMana = 50f;
        baseInitialMana = 0f;
        baseArmor = 60;
        baseMagicResistance = 60;
        baseAttackDamage = 70;
        baseAttackSpeed = 0.85f;
        baseAttackRange = ATTACK_RANGE_MELEE;
        hasProjectileAttacks = false;
    }
}
