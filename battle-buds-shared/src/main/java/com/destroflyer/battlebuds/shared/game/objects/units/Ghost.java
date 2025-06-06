package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.SkybornTrait;
import com.destroflyer.battlebuds.shared.game.traits.UndeadTrait;

public class Ghost extends Unit {

    public Ghost() {
        name = "Ghost";
        visualName = "ghost";
        cost = 1;
        baseTraits = new Class[] { UndeadTrait.class, SkybornTrait.class };
        baseMaximumHealth = 650f;
        baseMaximumMana = 60f;
        baseInitialMana = 20f;
        baseArmor = 40;
        baseMagicResistance = 40;
        baseAttackDamage = 40;
        baseAttackSpeed = 0.7f;
        baseAttackRange = ATTACK_RANGE_MELEE;
        hasProjectileAttacks = false;
    }
}
