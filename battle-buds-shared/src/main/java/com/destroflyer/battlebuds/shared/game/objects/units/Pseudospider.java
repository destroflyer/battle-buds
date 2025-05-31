package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.BeastTrait;
import com.destroflyer.battlebuds.shared.game.traits.ForestTrait;

public class Pseudospider extends Unit {

    public Pseudospider() {
        name = "Pseudospider";
        visualName = "pseudospider";
        cost = 2;
        baseTraits = new Class[] { BeastTrait.class, ForestTrait.class };
        baseMaximumHealth = 700f;
        baseMaximumMana = 50f;
        baseInitialMana = 0f;
        baseArmor = 45;
        baseMagicResistance = 45;
        baseAttackDamage = 50;
        baseAttackSpeed = 0.8f;
        baseAttackRange = ATTACK_RANGE_MELEE;
    }
}
