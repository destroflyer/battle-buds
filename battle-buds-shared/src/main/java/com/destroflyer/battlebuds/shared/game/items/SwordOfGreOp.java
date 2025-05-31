package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class SwordOfGreOp extends Item {

    public SwordOfGreOp() {
        name = "Sword of Gre'Op";
        visualName = "sword_of_gre_op";
        description = "+66% Attack Damage, +7% Damage Amplification";
    }

    @Override
    public float getBonusAttackDamagePercent(Unit unit) {
        return 0.66f;
    }

    @Override
    public float getBonusDamageDealtAmplificationFlat(Unit unit) {
        return 0.07f;
    }
}
