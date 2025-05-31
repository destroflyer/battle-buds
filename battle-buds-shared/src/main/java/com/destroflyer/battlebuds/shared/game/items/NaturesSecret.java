package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class NaturesSecret extends Item {

    public NaturesSecret() {
        name = "Nature's Secret";
        visualName = "natures_secret";
        description = "+150 Health, +20% Crit Chance, TODO: Equip 2 random items each round";
    }

    @Override
    public float getBonusMaximumHealthFlat(Unit unit) {
        return 150;
    }

    @Override
    public float getBonusCritChanceFlat(Unit unit) {
        return 0.2f;
    }
}
