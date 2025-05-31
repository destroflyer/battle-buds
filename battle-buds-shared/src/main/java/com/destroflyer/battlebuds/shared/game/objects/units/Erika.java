package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.AssassinTrait;
import com.destroflyer.battlebuds.shared.game.traits.RangerTrait;

public class Erika extends Unit {

    public Erika() {
        name = "Erika";
        visualName = "erika";
        cost = 3;
        baseTraits = new Class[] { AssassinTrait.class, RangerTrait.class };
        baseMaximumHealth = 650f;
        baseMaximumMana = 60f;
        baseInitialMana = 0f;
        baseArmor = 25;
        baseMagicResistance = 25;
        baseAttackDamage = 55;
        baseAttackSpeed = 0.75f;
        baseAttackRange = ATTACK_RANGE_RANGED;
    }
}
