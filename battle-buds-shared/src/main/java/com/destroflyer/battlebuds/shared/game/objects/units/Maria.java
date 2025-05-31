package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.LeaderTrait;
import com.destroflyer.battlebuds.shared.game.traits.LegionTrait;

public class Maria extends Unit {

    public Maria() {
        name = "Maria";
        visualName = "maria";
        cost = 5;
        baseTraits = new Class[] { LegionTrait.class, LeaderTrait.class };
        baseMaximumHealth = 1100f;
        baseMaximumMana = 100f;
        baseInitialMana = 30f;
        baseArmor = 60;
        baseMagicResistance = 60;
        baseAttackDamage = 70;
        baseAttackSpeed = 0.9f;
        baseAttackRange = ATTACK_RANGE_MELEE;
    }
}
