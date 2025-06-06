package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.ChanceUtil;
import com.destroflyer.battlebuds.shared.game.items.Items;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class RebellionArcher extends Unit {

    public RebellionArcher() {
        name = "Rebellion Archer";
        visualName = "rebellion_archer";
        cost = 1;
        baseTraits = new Class[0];
        baseMaximumHealth = 150f;
        baseMaximumMana = null;
        baseInitialMana = null;
        baseArmor = 0;
        baseMagicResistance = 0;
        baseAttackDamage = 40;
        baseAttackSpeed = 0.8f;
        baseAttackRange = ATTACK_RANGE_RANGED;
        hasProjectileAttacks = true;
        attackProjectileVisualName = "rebellion_archer_arrow";
    }

    @Override
    protected void onDeath() {
        super.onDeath();
        ChanceUtil.ifChance(0.4f, () -> dropGoldForEnemy(1));
        ChanceUtil.ifChance(0.5f, () -> dropForEnemy(Items.createRandomComponentLoot()));
    }
}
