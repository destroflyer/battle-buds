package com.destroflyer.battlebuds.shared.game.objects.units;

import com.destroflyer.battlebuds.shared.game.items.Items;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class RebellionBaron extends Unit {

    public RebellionBaron() {
        name = "Rebellion Baron";
        visualName = "rebellion_baron";
        cost = 5;
        baseTraits = new Class[0];
        baseMaximumHealth = 10000f;
        baseMaximumMana = null;
        baseInitialMana = null;
        baseArmor = 50;
        baseMagicResistance = 50;
        baseAttackDamage = 900;
        baseAttackSpeed = 0.8f;
        baseAttackRange = ATTACK_RANGE_MELEE;
    }

    @Override
    protected void onDeath() {
        super.onDeath();
        dropForEnemy(Items.createRandomFullItemLoot());
        dropForEnemy(Items.createRandomFullItemLoot());
        dropGoldForEnemy(5, 10);
    }
}
