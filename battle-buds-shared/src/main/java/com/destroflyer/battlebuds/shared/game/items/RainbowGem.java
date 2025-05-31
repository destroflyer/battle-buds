package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class RainbowGem extends Item {

    public RainbowGem() {
        name = "Rainbow Gem";
        visualName = "rainbow_gem";
        description = "+150 Health, +15 Mana, 10% Damage Reduction, TODO: Missing Health regeneration";
    }

    @Override
    public float getBonusMaximumHealthFlat(Unit unit) {
        return 150;
    }

    @Override
    public float getBonusInitialManaFlat(Unit unit) {
        return 20;
    }

    @Override
    public float getBonusDamageTakenAmplificationFlat(Unit unit) {
        return -0.1f;
    }
}
