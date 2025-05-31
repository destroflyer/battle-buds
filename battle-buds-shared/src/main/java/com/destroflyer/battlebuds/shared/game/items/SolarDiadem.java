package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class SolarDiadem extends Item {

    public SolarDiadem() {
        name = "Solar Diadem";
        visualName = "solar_diadem";
        description = "+30% Attack Speed, +20% Crit Chance, +20 Magic Resistance, TODO: Crowd control immunity";
    }

    @Override
    public float getBonusAttackSpeedFlat(Unit unit) {
        return 0.3f;
    }

    @Override
    public float getBonusCritChanceFlat(Unit unit) {
        return 0.2f;
    }

    @Override
    public float getBonusMagicResistanceFlat(Unit unit) {
        return 20;
    }
}
