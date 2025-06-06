package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.RangerTrait;
import com.destroflyer.battlebuds.shared.game.traits.UndeadTrait;

public class Varus extends Unit {

    public Varus() {
        name = "Varus";
        visualName = "varus";
        cost = 2;
        baseTraits = new Class[] { UndeadTrait.class, RangerTrait.class };
        baseMaximumHealth = 550f;
        baseMaximumMana = 60f;
        baseInitialMana = 20f;
        baseArmor = 20;
        baseMagicResistance = 20;
        baseAttackDamage = 50;
        baseAttackSpeed = 0.7f;
        baseAttackRange = ATTACK_RANGE_RANGED;
        hasProjectileAttacks = true;
        attackProjectileVisualName = "erika_arrow";
    }
}
