package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.LegionTrait;
import com.destroflyer.battlebuds.shared.game.traits.RangerTrait;

public class LegionArcher extends Unit {

    public LegionArcher() {
        name = "Legion Archer";
        visualName = "legion_archer";
        cost = 1;
        baseTraits = new Class[] { LegionTrait.class, RangerTrait.class };
        baseMaximumHealth = 500f;
        baseMaximumMana = 40f;
        baseInitialMana = 0f;
        baseArmor = 15;
        baseMagicResistance = 15;
        baseAttackDamage = 50;
        baseAttackSpeed = 0.7f;
        baseAttackRange = ATTACK_RANGE_RANGED;
    }
}
