package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.ChanceUtil;
import com.destroflyer.battlebuds.shared.game.items.Items;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class RebellionElite extends Unit {

    public RebellionElite() {
        name = "Rebellion Elite";
        visualName = "rebellion_elite";
        cost = 4;
        baseTraits = new Class[0];
        baseMaximumHealth = 2400f;
        baseMaximumMana = null;
        baseInitialMana = null;
        baseArmor = 25;
        baseMagicResistance = 25;
        baseAttackDamage = 230;
        baseAttackSpeed = 0.8f;
        baseAttackRange = ATTACK_RANGE_RANGED;
    }

    @Override
    protected void onDeath() {
        super.onDeath();
        ChanceUtil.ifChance(0.7f, () -> dropGoldForEnemy(1, 4));
        ChanceUtil.ifChance(0.7f, () -> dropForEnemy(Items.createRandomComponentLoot()));
    }
}
