package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.game.Trait;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class GuardianTrait extends Trait {

    public GuardianTrait() {
        name = "Guardian";
        description = "Your team gains 10 Armor and Magic Resist. Guardians gain more and for the first 10 seconds of combat, their bonus is doubled.\n\n(2) 20 Armor and Magic Resistance\n(4) 50 Armor and Magic Resistance\n(6) 120 Armor and Magic Resistance";
    }

    @Override
    public Tier getTier() {
        return getThreshholdTier(null, 2, 4, 6);
    }

    @Override
    public float getBonusArmorFlat(Unit unit) {
        return getBonusResistanceFlat(unit);
    }

    @Override
    public float getBonusMagicResistanceFlat(Unit unit) {
        return getBonusResistanceFlat(unit);
    }

    private float getBonusResistanceFlat(Unit unit) {
        int bonusResistance = 0;
        int units = getUniqueUnitsOfTraitOnBoard();
        if (units >= 2) {
            if (unit.hasTrait(GuardianTrait.class)) {
                if (units >= 6) {
                    bonusResistance = 120;
                } else if (units >= 4) {
                    bonusResistance = 50;
                } else {
                    bonusResistance = 20;
                }
                if (unit.getBoard().getTime() < 10) {
                    bonusResistance *= 2;
                }
            } else {
                bonusResistance = 10;
            }
        }
        return bonusResistance;
    }
}
