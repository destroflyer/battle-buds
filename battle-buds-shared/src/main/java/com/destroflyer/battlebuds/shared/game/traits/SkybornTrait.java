package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.game.Trait;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class SkybornTrait extends Trait {

    public SkybornTrait() {
        name = "Skyborn";
        description = "Your teams gains Attack Speed and Movement Speed.\n\n(2) 10% Attack Speed, 2 Movement Speed\n(3) 20% Attack Speed, 4 Movement Speed\n(4) 30% Attack Speed, 6 Movement Speed\n(5) 50% Attack Speed, 15 Movement Speed";
    }

    @Override
    public Tier getTier() {
        return getThreshholdTier(null, 2, 3, 4);
    }

    @Override
    public float getBonusAttackSpeedFlat(Unit unit) {
        int units = getUniqueUnitsOfTraitOnBoard();
        if (units >= 5) {
            return 0.5f;
        } else if (units >= 4) {
            return 0.3f;
        } else if (units >= 3) {
            return 0.2f;
        } else if (units >= 2) {
            return 0.1f;
        }
        return 0;
    }

    @Override
    public float getBonusMovementFlat(Unit unit) {
        int units = getUniqueUnitsOfTraitOnBoard();
        if (units >= 5) {
            return 15;
        } else if (units >= 4) {
            return 6;
        } else if (units >= 3) {
            return 4;
        } else if (units >= 2) {
            return 2;
        }
        return 0;
    }
}
