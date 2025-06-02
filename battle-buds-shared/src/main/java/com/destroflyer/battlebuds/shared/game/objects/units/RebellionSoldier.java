package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.ChanceUtil;
import com.destroflyer.battlebuds.shared.game.items.Items;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class RebellionSoldier extends Unit {

    public RebellionSoldier() {
        name = "Rebellion Soldier";
        visualName = "rebellion_soldier";
        cost = 1;
        baseTraits = new Class[0];
        baseMaximumHealth = 250f;
        baseMaximumMana = null;
        baseInitialMana = null;
        baseArmor = 0;
        baseMagicResistance = 0;
        baseAttackDamage = 20;
        baseAttackSpeed = 0.8f;
        baseAttackRange = ATTACK_RANGE_MELEE;
    }

    @Override
    protected void onDeath() {
        super.onDeath();
        ChanceUtil.ifChance(0.4f, () -> dropGoldForEnemy(1));
        ChanceUtil.ifChance(0.5f, () -> dropForEnemy(Items.createRandomComponentLoot()));
    }
}
