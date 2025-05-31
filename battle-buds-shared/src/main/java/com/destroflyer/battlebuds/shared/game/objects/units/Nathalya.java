package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.FairyTrait;
import com.destroflyer.battlebuds.shared.game.traits.WizardTrait;

public class Nathalya extends Unit {

    public Nathalya() {
        name = "Nathalya";
        visualName = "nathalya";
        cost = 2;
        baseTraits = new Class[] { FairyTrait.class, WizardTrait.class };
        baseMaximumHealth = 550f;
        baseMaximumMana = 50f;
        baseInitialMana = 20f;
        baseArmor = 20;
        baseMagicResistance = 20;
        baseAttackDamage = 35;
        baseAttackSpeed = 0.75f;
        baseAttackRange = ATTACK_RANGE_RANGED;
    }
}
