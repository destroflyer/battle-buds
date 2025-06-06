package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.ForestTrait;
import com.destroflyer.battlebuds.shared.game.traits.GolemTrait;

public class ForestMonster extends Unit {

    public ForestMonster() {
        name = "Forest Monster";
        visualName = "forest_monster";
        cost = 4;
        baseTraits = new Class[] { GolemTrait.class, ForestTrait.class };
        baseMaximumHealth = 1100f;
        baseMaximumMana = 95f;
        baseInitialMana = 30f;
        baseArmor = 55;
        baseMagicResistance = 55;
        baseAttackDamage = 60;
        baseAttackSpeed = 0.6f;
        baseAttackRange = ATTACK_RANGE_MELEE;
        hasProjectileAttacks = false;
    }
}
