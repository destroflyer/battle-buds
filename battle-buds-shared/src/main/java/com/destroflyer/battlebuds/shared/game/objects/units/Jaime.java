package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.BeastTrait;
import com.destroflyer.battlebuds.shared.game.traits.ForestTrait;

public class Jaime extends Unit {

    public Jaime() {
        name = "Jaime";
        visualName = "jaime";
        cost = 3;
        baseTraits = new Class[] { BeastTrait.class, ForestTrait.class };
        baseMaximumHealth = 800f;
        baseMaximumMana = 60f;
        baseInitialMana = 20f;
        baseArmor = 50;
        baseMagicResistance = 50;
        baseAttackDamage = 65;
        baseAttackSpeed = 0.75f;
        baseAttackRange = ATTACK_RANGE_MELEE;
    }
}
