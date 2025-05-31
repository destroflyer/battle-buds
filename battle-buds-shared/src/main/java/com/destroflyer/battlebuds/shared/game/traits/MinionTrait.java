package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.game.Trait;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class MinionTrait extends Trait {

    public MinionTrait() {
        name = "Minion";
        description = "Minion gain Dodge Chance.\n\n(3) 20% Dodge Chance\n(5) 40% Dodge Chance\n(7) 60% Dodge Chance\n(10) 99% Dodge Chance";
    }

    @Override
    public Tier getTier() {
        return getThreshholdTier(3, 5, 7, 10);
    }

    @Override
    public float getBonusDodgeChanceFlat(Unit unit) {
        if (unit.hasTrait(MinionTrait.class)) {
            int units = getUniqueUnitsOfTraitOnBoard();
            if (units >= 10) {
                return 0.95f;
            } else if (units >= 7) {
                return 0.6f;
            } else if (units >= 5) {
                return 0.4f;
            } else if (units >= 3) {
                return 0.2f;
            }
        }
        return 0;
    }
}
