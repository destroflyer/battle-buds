package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.BeastTrait;
import com.destroflyer.battlebuds.shared.game.traits.ForestTrait;
import com.destroflyer.battlebuds.shared.game.traits.GolemTrait;

public class BeetleGolem extends Unit {

    public BeetleGolem() {
        name = "Beetle Golem";
        visualName = "beetle_golem";
        cost = 2;
        baseTraits = new Class[] { BeastTrait.class, GolemTrait.class, ForestTrait.class };
        baseMaximumHealth = 800f;
        baseMaximumMana = 60f;
        baseInitialMana = 0f;
        baseArmor = 45;
        baseMagicResistance = 45;
        baseAttackDamage = 60;
        baseAttackSpeed = 0.6f;
        baseAttackRange = ATTACK_RANGE_MELEE;
    }
}
