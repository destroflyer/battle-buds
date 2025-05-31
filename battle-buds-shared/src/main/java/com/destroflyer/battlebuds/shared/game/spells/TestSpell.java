package com.destroflyer.battlebuds.shared.game.spells;

import com.destroflyer.battlebuds.shared.game.DamageType;
import com.destroflyer.battlebuds.shared.game.Spell;
import com.destroflyer.battlebuds.shared.game.buffs.TestAttackSpeedBuff;

public class TestSpell extends Spell {

    public TestSpell() {
        name = "Test Spell";
        description = "Deal 50 Magic Damage to your current target, heal 50 Health, and gain 20% Attack Speed for 2 seconds.";
    }

    @Override
    public boolean isCastable() {
        return caster.getAttackTarget() != null;
    }

    @Override
    public void cast() {
        caster.heal(50);
        caster.dealDamage(caster.getAttackTarget(), DamageType.MAGIC, 50, false);
        caster.addBuff(new TestAttackSpeedBuff(), 2f);
    }
}
