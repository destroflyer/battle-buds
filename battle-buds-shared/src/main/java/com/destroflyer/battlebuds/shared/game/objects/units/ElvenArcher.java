package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.FairyTrait;
import com.destroflyer.battlebuds.shared.game.traits.RangerTrait;

public class ElvenArcher extends Unit {

    public ElvenArcher() {
        name = "Elven Archer";
        visualName = "elven_archer";
        cost = 4;
        baseTraits = new Class[] { FairyTrait.class, RangerTrait.class };
        baseMaximumHealth = 800f;
        baseMaximumMana = 150f;
        baseInitialMana = 50f;
        baseArmor = 30;
        baseMagicResistance = 30;
        baseAttackDamage = 50;
        baseAttackSpeed = 0.75f;
        baseAttackRange = ATTACK_RANGE_RANGED;
        hasProjectileAttacks = true;
        attackProjectileVisualName = "elven_archer_arrow";
    }
}
