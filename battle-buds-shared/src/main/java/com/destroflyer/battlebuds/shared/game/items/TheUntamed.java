package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class TheUntamed extends Item {

    public TheUntamed() {
        name = "The Untamed";
        visualName = "the_untamed";
        description = "+50 Ability Power Health, +20% Damage Amplification";
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        return 50;
    }

    @Override
    public float getBonusDamageDealtAmplificationFlat(Unit unit) {
        return 0.2f;
    }
}
