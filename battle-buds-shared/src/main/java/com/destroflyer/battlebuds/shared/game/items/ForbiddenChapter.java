package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class ForbiddenChapter extends Item {

    public ForbiddenChapter() {
        name = "Forbidden Chapter";
        visualName = "forbidden_chapter";
        description = "+150 Health, +25 Ability Power, 10% Attack Speed, TODO: Burn damage";
    }

    @Override
    public float getBonusMaximumHealthFlat(Unit unit) {
        return 150;
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        return 25;
    }

    @Override
    public float getBonusAttackSpeedFlat(Unit unit) {
        return 0.1f;
    }
}
