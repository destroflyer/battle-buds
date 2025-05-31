package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class RunicBlades extends Item {

    public RunicBlades() {
        name = "RunicBlades";
        visualName = "runic_blades";
        description = "+15 Mana, +20% Attack Damage, +20 Ability Power, TODO: Mana on attack";
    }

    @Override
    public float getBonusInitialManaFlat(Unit unit) {
        return 15;
    }

    @Override
    public float getBonusAttackDamageFlat(Unit unit) {
        return 15;
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        return 20;
    }
}
