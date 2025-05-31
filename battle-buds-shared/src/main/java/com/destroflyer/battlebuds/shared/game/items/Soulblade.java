package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class Soulblade extends Item {

    public Soulblade() {
        name = "Soulblade";
        visualName = "soulblade";
        description = "+15% Attack Damage, +15 Ability Power, +20% Omnivamp, TODO: Heal lowest percent Health ally";
    }

    @Override
    public float getBonusAttackDamagePercent(Unit unit) {
        return 0.15f;
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        return 15;
    }

    @Override
    public float getBonusOmnivampFlat(Unit unit) {
        return 0.2f;
    }
}
