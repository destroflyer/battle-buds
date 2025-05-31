package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class BlacksmithsWrath extends Item {

    public BlacksmithsWrath() {
        name = "Blacksmith's Wrath";
        visualName = "blacksmiths_wrath";
        description = "+10% Attack Damage, +20 Armor, TODO: Stack Attack Damage, Ability Power, Armor and Magic Resist";
    }

    @Override
    public float getBonusAttackDamagePercent(Unit unit) {
        return 0.1f;
    }

    @Override
    public float getBonusArmorFlat(Unit unit) {
        return 20;
    }
}
