package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.MinionTrait;
import com.destroflyer.battlebuds.shared.game.traits.TricksterTrait;

public class RobotMinion extends Unit {

    public RobotMinion() {
        name = "Robot Minion";
        visualName = "robot_minion";
        cost = 2;
        baseTraits = new Class[] { MinionTrait.class, TricksterTrait.class };
        baseMaximumHealth = 700f;
        baseMaximumMana = 175f;
        baseInitialMana = baseMaximumMana;
        baseArmor = 45;
        baseMagicResistance = 45;
        baseAttackDamage = 65;
        baseAttackSpeed = 0.5f;
        baseAttackRange = ATTACK_RANGE_MELEE;
        hasProjectileAttacks = false;
    }
}
