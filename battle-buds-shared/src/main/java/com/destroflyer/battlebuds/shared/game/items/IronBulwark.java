package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class IronBulwark extends Item {

    public IronBulwark() {
        name = "Iron Bulwark";
        visualName = "iron_bulwark";
        description = "+100 Health, +25 Armor, +25 Magic Reistance, TODO: Resistances for every attacker";
    }

    @Override
    public float getBonusMaximumHealthFlat(Unit unit) {
        return 100;
    }

    @Override
    public float getBonusArmorFlat(Unit unit) {
        return 25;
    }

    @Override
    public float getBonusMagicResistanceFlat(Unit unit) {
        return 25;
    }
}
