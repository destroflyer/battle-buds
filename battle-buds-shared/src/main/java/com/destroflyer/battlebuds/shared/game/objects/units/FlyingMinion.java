package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.MinionTrait;
import com.destroflyer.battlebuds.shared.game.traits.SkybornTrait;

public class FlyingMinion extends Unit {

    public FlyingMinion() {
        name = "Flying Minion";
        visualName = "flying_minion";
        cost = 3;
        baseTraits = new Class[] { MinionTrait.class, SkybornTrait.class };
        baseMaximumHealth = 700f;
        baseMaximumMana = 50f;
        baseInitialMana = 0f;
        baseArmor = 25;
        baseMagicResistance = 25;
        baseAttackDamage = 55;
        baseAttackSpeed = 0.75f;
        baseAttackRange = ATTACK_RANGE_RANGED;
    }
}
