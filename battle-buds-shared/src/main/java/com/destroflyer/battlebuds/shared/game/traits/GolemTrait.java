package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.game.Trait;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class GolemTrait extends Trait {

    public GolemTrait() {
        name = "Golem";
        description = "Your teams gains Health. Golems gain double.\n\n(2) 100 Health\n(4) 200 Health\n(6) 400 Health\n(8) 1000 Health";
    }

    @Override
    public Tier getTier() {
        return getThreshholdTier( 2, 4, 6, 8);
    }

    @Override
    public float getBonusMaximumHealthFlat(Unit unit) {
        int bonusMaximumHealth = 0;
        int units = getUniqueUnitsOfTraitOnBoard();
        if (units >= 8) {
            bonusMaximumHealth = 1000;
        } else if (units >= 6) {
            bonusMaximumHealth = 400;
        } else if (units >= 4) {
            bonusMaximumHealth = 200;
        } else if (units >= 2) {
            bonusMaximumHealth = 100;
        }
        if (unit.hasTrait(GolemTrait.class)) {
            bonusMaximumHealth *= 2;
        }
        return bonusMaximumHealth;
    }
}
