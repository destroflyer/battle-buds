package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.game.Trait;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class LeaderTrait extends Trait {

    public LeaderTrait() {
        name = "Leader";
        description = "Leaders gain damage amplification and damage reduction.\n\n(Exactly 1) 25% Damage Amplification, 25% Damage Reduction\n(4 or more) 50% Damage Amplification, 50% Damage Reduction";
    }

    @Override
    public Tier getTier() {
        int units = getUniqueUnitsOfTraitOnBoard();
        if (units >= 4) {
            return Tier.GOLD;
        } else if (units == 1) {
            return Tier.BRONZE;
        }
        return null;
    }

    @Override
    public float getBonusDamageDealtAmplificationFlat(Unit unit) {
        if (unit.hasTrait(LeaderTrait.class)) {
            int units = getUniqueUnitsOfTraitOnBoard();
            if (units >= 4) {
                return 0.5f;
            } else if (units == 1) {
                return 0.25f;
            }
        }
        return 0;
    }

    @Override
    public float getBonusDamageTakenAmplificationFlat(Unit unit) {
        if (unit.hasTrait(LeaderTrait.class)) {
            int units = getUniqueUnitsOfTraitOnBoard();
            if (units == 1) {
                return -0.25f;
            } else if (units >= 4) {
                return -0.5f;
            }
        }
        return 0;
    }
}
