package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.ChanceUtil;
import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.traits.AssassinTrait;

public class RebellionAssasin extends Unit {

    public RebellionAssasin() {
        name = "Rebellion Assassin";
        visualName = "rebellion_assassin";
        cost = 3;
        baseTraits = new Class[] { AssassinTrait.class };
        baseMaximumHealth = 700f;
        baseMaximumMana = null;
        baseInitialMana = null;
        baseArmor = 15;
        baseMagicResistance = 15;
        baseAttackDamage = 154;
        baseAttackSpeed = 1;
        baseAttackRange = ATTACK_RANGE_MELEE;
    }

    @Override
    protected void onDeath() {
        super.onDeath();
        ChanceUtil.ifChance(0.7f, () -> dropGold(1, 3));
        ChanceUtil.ifChance(0.5f, this::dropRandomComponent);
    }
}
