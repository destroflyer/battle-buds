package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class SpikedFlail extends Item {

    public SpikedFlail() {
        name = "Spiked Flail";
        visualName = "spiked_flail";
        description = "+250 Health, +30 Attack Damage, TODO: Shield";
    }

    @Override
    public float getBonusMaximumHealthFlat(Unit unit) {
        return 250;
    }

    @Override
    public float getBonusAttackDamageFlat(Unit unit) {
        return 30;
    }
}
