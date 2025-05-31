package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class Cataclysm extends Item {

    public Cataclysm() {
        name = "Cataclysm";
        visualName = "cataclysm";
        description = "+40% Attack Speed, +6% Damage Amplification, TODO: Burn damage";
    }

    @Override
    public float getBonusAttackSpeedFlat(Unit unit) {
        return 0.4f;
    }

    @Override
    public float getBonusDamageDealtAmplificationFlat(Unit unit) {
        return 0.06f;
    }
}
