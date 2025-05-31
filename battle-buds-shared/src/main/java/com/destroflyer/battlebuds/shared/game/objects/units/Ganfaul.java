package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.UndeadTrait;
import com.destroflyer.battlebuds.shared.game.traits.WizardTrait;

public class Ganfaul extends Unit {

    public Ganfaul() {
        name = "Ganfaul";
        visualName = "ganfaul";
        cost = 4;
        baseTraits = new Class[] { UndeadTrait.class, WizardTrait.class };
        baseMaximumHealth = 800f;
        baseMaximumMana = 75f;
        baseInitialMana = 25f;
        baseArmor = 30;
        baseMagicResistance = 30;
        baseAttackDamage = 35;
        baseAttackSpeed = 0.75f;
        baseAttackRange = ATTACK_RANGE_RANGED;
    }
}
