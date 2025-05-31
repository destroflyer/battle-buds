package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class RedGem extends Item {

    public RedGem() {
        name = "Red Gem";
        visualName = "red_gem";
        description = "+150 Health";
    }

    @Override
    public float getBonusMaximumHealthFlat(Unit unit) {
        return 150;
    }
}
