package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.AssassinTrait;
import com.destroflyer.battlebuds.shared.game.traits.LeaderTrait;

public class Scarlet extends Unit {

    public Scarlet() {
        name = "Scarlet";
        visualName = "scarlet";
        cost = 5;
        baseTraits = new Class[] { AssassinTrait.class, LeaderTrait.class };
        baseMaximumHealth = 850f;
        baseMaximumMana = 80f;
        baseInitialMana = 30f;
        baseArmor = 30;
        baseMagicResistance = 30;
        baseAttackDamage = 70;
        baseAttackSpeed = 0.9f;
        baseAttackRange = ATTACK_RANGE_MELEE;
        hasProjectileAttacks = false;
    }
}
