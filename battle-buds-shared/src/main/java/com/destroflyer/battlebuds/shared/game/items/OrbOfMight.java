package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class OrbOfMight extends Item {

    public OrbOfMight() {
        name = "Orb of Might";
        visualName = "orb_of_might";
        description = "+15 Mana, +15 Ability Power, +20 Magic Resistance, TODO: Mana on attack or attacked";
    }

    @Override
    public float getBonusInitialManaFlat(Unit unit) {
        return 15;
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        return 15;
    }

    @Override
    public float getBonusMagicResistanceFlat(Unit unit) {
        return 20;
    }
}
