package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.ChanceUtil;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class RebellionGiant extends Unit {

    public RebellionGiant() {
        name = "Rebellion Giant";
        visualName = "rebellion_giant";
        cost = 2;
        baseTraits = new Class[0];
        baseMaximumHealth = 1200f;
        baseMaximumMana = null;
        baseInitialMana = null;
        baseArmor = 50;
        baseMagicResistance = 25;
        baseAttackDamage = 110;
        baseAttackSpeed = 0.8f;
        baseAttackRange = ATTACK_RANGE_MELEE;
    }

    @Override
    protected void onDeath() {
        super.onDeath();
        ChanceUtil.ifChance(0.9f, () -> dropGold(1, 3));
        ChanceUtil.ifChance(0.55f, this::dropRandomComponent);
    }
}
