package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.MinionTrait;
import com.destroflyer.battlebuds.shared.game.traits.WizardTrait;

public class WizardMinion extends Unit {

    public WizardMinion() {
        name = "Wizard Minion";
        visualName = "wizard_minion";
        cost = 1;
        baseTraits = new Class[] { MinionTrait.class, WizardTrait.class };
        baseMaximumHealth = 500f;
        baseMaximumMana = 60f;
        baseInitialMana = 10f;
        baseArmor = 15;
        baseMagicResistance = 15;
        baseAttackDamage = 35;
        baseAttackSpeed = 0.7f;
        baseAttackRange = ATTACK_RANGE_RANGED;
    }
}
