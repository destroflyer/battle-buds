package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class MoltenHeart extends Item {

    public MoltenHeart() {
        name = "Molten Heart";
        visualName = "molten_heart";
        description = "+250 Health, +20 Armor, TODO: Burn passive";
    }

    @Override
    public float getBonusMaximumHealthFlat(Unit unit) {
        return 250;
    }

    @Override
    public float getBonusArmorFlat(Unit unit) {
        return 20;
    }
}
