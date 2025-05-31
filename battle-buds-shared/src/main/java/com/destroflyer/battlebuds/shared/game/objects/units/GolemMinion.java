package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.GolemTrait;
import com.destroflyer.battlebuds.shared.game.traits.MinionTrait;

public class GolemMinion extends Unit {

    public GolemMinion() {
        name = "Golem Minion";
        visualName = "golem_minion";
        cost = 3;
        baseTraits = new Class[] { MinionTrait.class, GolemTrait.class };
        baseMaximumHealth = 850f;
        baseMaximumMana = 90f;
        baseInitialMana = 30f;
        baseArmor = 50;
        baseMagicResistance = 50;
        baseAttackDamage = 60;
        baseAttackSpeed = 0.6f;
        baseAttackRange = ATTACK_RANGE_MELEE;
    }
}
