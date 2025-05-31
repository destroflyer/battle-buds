package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class RapidfireTower extends Item {

    public RapidfireTower() {
        name = "Rapidfire Tower";
        visualName = "rapidfire_tower";
        description = "+15% Attack Speed, +15 Ability Power, TODO: Gain attackspeed on hit";
    }

    @Override
    public float getBonusAttackSpeedFlat(Unit unit) {
        return 0.15f;
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        return 15;
    }
}
