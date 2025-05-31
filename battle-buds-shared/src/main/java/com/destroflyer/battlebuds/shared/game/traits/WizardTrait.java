package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.game.Trait;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class WizardTrait extends Trait {

    public WizardTrait() {
        name = "Wizard";
        description = "Your teams gains 10 Ability Power. Wizards gain more.\n\n(2) 20 Ability Power\n(4) 55 Ability Power\n(6) 100 Ability Power\n(8) 200 Ability Power";
    }

    @Override
    public Tier getTier() {
        return getThreshholdTier(2, 4, 6, 8);
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        int bonusAbilityPower = 0;
        int units = getUniqueUnitsOfTraitOnBoard();
        if (units >= 2) {
            if (unit.hasTrait(WizardTrait.class)) {
                if (units >= 8) {
                    bonusAbilityPower = 200;
                } else if (units >= 6) {
                    bonusAbilityPower = 100;
                } else if (units >= 4) {
                    bonusAbilityPower = 55;
                } else {
                    bonusAbilityPower = 20;
                }
            } else {
                bonusAbilityPower = 10;
            }
        }
        return bonusAbilityPower;
    }
}
