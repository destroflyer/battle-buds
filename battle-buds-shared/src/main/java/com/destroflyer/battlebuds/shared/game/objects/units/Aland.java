package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.AssassinTrait;
import com.destroflyer.battlebuds.shared.game.traits.TricksterTrait;

public class Aland extends Unit {

    public Aland() {
        name = "Aland";
        visualName = "aland";
        cost = 4;
        baseTraits = new Class[] { AssassinTrait.class, TricksterTrait.class };
        baseMaximumHealth = 700f;
        baseMaximumMana = 50f;
        baseInitialMana = 10f;
        baseArmor = 25;
        baseMagicResistance = 25;
        baseAttackDamage = 60;
        baseAttackSpeed = 0.8f;
        baseAttackRange = ATTACK_RANGE_MELEE;
        hasProjectileAttacks = false;
    }
}
