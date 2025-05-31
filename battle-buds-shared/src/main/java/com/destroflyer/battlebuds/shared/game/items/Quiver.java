package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class Quiver extends Item {

    public Quiver() {
        name = "Quiver";
        visualName = "quiver";
        description = "+10% Attack Speed";
    }

    @Override
    public float getBonusAttackSpeedFlat(Unit unit) {
        return 0.1f;
    }
}
