package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class SilverPlatedPike extends Item {

    public SilverPlatedPike() {
        name = "Silver Plated Pike";
        visualName = "silver_plated_pike";
        description = "+30% Attack Damage, +10% Attack Speed, +20 Ability Power, TODO: Bonus damage based on target Maximum Health";
    }

    @Override
    public float getBonusAttackDamagePercent(Unit unit) {
        return 0.3f;
    }

    @Override
    public float getBonusAttackSpeedFlat(Unit unit) {
        return 0.1f;
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        return 20;
    }
}
