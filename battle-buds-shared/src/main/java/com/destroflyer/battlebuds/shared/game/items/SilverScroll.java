package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class SilverScroll extends Item {

    public SilverScroll() {
        name = "Silver Scroll";
        visualName = "silver_scroll";
        description = "+150 Health, +10% Attack Speed, +30 Ability Power, TODO: Attack Speed on cast";
    }

    @Override
    public float getBonusMaximumHealthFlat(Unit unit) {
        return 150;
    }

    @Override
    public float getBonusAttackSpeedFlat(Unit unit) {
        return 0.1f;
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        return 30;
    }
}
