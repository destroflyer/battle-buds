package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.UndeadTrait;
import com.destroflyer.battlebuds.shared.game.traits.WizardTrait;

public class Dosaz extends Unit {

    public Dosaz() {
        name = "Dosaz";
        visualName = "dosaz";
        cost = 3;
        baseTraits = new Class[] { UndeadTrait.class, WizardTrait.class };
        baseMaximumHealth = 700f;
        baseMaximumMana = 75f;
        baseInitialMana = 15f;
        baseArmor = 25;
        baseMagicResistance = 25;
        baseAttackDamage = 40;
        baseAttackSpeed = 0.7f;
        baseAttackRange = ATTACK_RANGE_RANGED;
    }
}
