package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.DragonTrait;
import com.destroflyer.battlebuds.shared.game.traits.ForestTrait;
import com.destroflyer.battlebuds.shared.game.traits.SkybornTrait;

public class LittleDragon extends Unit {

    public LittleDragon() {
        name = "Little Dragon";
        visualName = "little_dragon";
        cost = 1;
        baseTraits = new Class[] { DragonTrait.class, ForestTrait.class, SkybornTrait.class };
        baseMaximumHealth = 500f;
        baseMaximumMana = 60f;
        baseInitialMana = 10f;
        baseArmor = 15;
        baseMagicResistance = 15;
        baseAttackDamage = 35;
        baseAttackSpeed = 0.7f;
        baseAttackRange = ATTACK_RANGE_RANGED;
        hasProjectileAttacks = true;
    }
}
