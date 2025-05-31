package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class PrecisionGloves extends Item {

    public PrecisionGloves() {
        name = "Precision Gloves";
        visualName = "precision_gloves";
        description = "+20% Crit Chance";
    }

    @Override
    public float getBonusCritChanceFlat(Unit unit) {
        return 0.2f;
    }
}
