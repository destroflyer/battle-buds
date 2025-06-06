package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.BrawlerTrait;
import com.destroflyer.battlebuds.shared.game.traits.LeaderTrait;

public class DwarfWarrior extends Unit {

    public DwarfWarrior() {
        name = "Dwarf Warrior";
        visualName = "dwarf_warrior";
        cost = 4;
        baseTraits = new Class[] { BrawlerTrait.class, LeaderTrait.class };
        baseMaximumHealth = 1100f;
        baseMaximumMana = 100f;
        baseInitialMana = 40f;
        baseArmor = 50;
        baseMagicResistance = 50;
        baseAttackDamage = 75f;
        baseAttackSpeed = 0.85f;
        baseAttackRange = ATTACK_RANGE_MELEE;
        hasProjectileAttacks = false;
    }
}
