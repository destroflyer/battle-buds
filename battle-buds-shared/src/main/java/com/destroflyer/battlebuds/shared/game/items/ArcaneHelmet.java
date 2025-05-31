package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class ArcaneHelmet extends Item {

    public ArcaneHelmet() {
        name = "Arcane Helmet";
        visualName = "arcane_helmet";
        description = "+100 Health, +20 Ability Power, +20 Armor, TODO: Shield";
    }

    @Override
    public float getBonusMaximumHealthFlat(Unit unit) {
        return 100;
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        return 20;
    }

    @Override
    public float getBonusArmorFlat(Unit unit) {
        return 20;
    }
}
