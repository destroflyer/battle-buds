package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.FairyTrait;
import com.destroflyer.battlebuds.shared.game.traits.WizardTrait;

public class Alice extends Unit {

    public Alice() {
        name = "Alice";
        visualName = "alice";
        cost = 3;
        baseTraits = new Class[] { FairyTrait.class, WizardTrait.class };
        baseMaximumHealth = 650f;
        baseMaximumMana = 55f;
        baseInitialMana = 0f;
        baseArmor = 25;
        baseMagicResistance = 25;
        baseAttackDamage = 40;
        baseAttackSpeed = 0.75f;
        baseAttackRange = ATTACK_RANGE_RANGED;
    }
}
