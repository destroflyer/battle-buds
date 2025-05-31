package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.GolemTrait;
import com.destroflyer.battlebuds.shared.game.traits.UndeadTrait;

public class Maw extends Unit {

    public Maw() {
        name = "Maw";
        visualName = "maw";
        cost = 5;
        baseTraits = new Class[] { UndeadTrait.class, GolemTrait.class };
        baseMaximumHealth = 1100f;
        baseMaximumMana = 220f;
        baseInitialMana = 100f;
        baseArmor = 50;
        baseMagicResistance = 50;
        baseAttackDamage = 70;
        baseAttackSpeed = 0.8f;
        baseAttackRange = ATTACK_RANGE_MELEE;
    }
}
