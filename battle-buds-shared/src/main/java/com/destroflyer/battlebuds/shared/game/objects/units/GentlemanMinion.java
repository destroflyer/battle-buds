package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.MinionTrait;
import com.destroflyer.battlebuds.shared.game.traits.TricksterTrait;

public class GentlemanMinion extends Unit {

    public GentlemanMinion() {
        name = "Gentleman Minion";
        visualName = "gentleman_minion";
        cost = 5;
        baseTraits = new Class[] { MinionTrait.class, TricksterTrait.class };
        baseMaximumHealth = 1000f;
        baseMaximumMana = 80f;
        baseInitialMana = 20f;
        baseArmor = 60;
        baseMagicResistance = 60;
        baseAttackDamage = 70;
        baseAttackSpeed = 0.9f;
        baseAttackRange = ATTACK_RANGE_MELEE;
    }
}
