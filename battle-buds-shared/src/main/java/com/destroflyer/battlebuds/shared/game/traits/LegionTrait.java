package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.game.Trait;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.objects.units.*;

public class LegionTrait extends Trait {

    public LegionTrait() {
        name = "Legion";
        description = "Legion units grant unique stats to your team, increased for each Legion unit in play. Legion units gain double.\n\n(1) 100% bonus\n(2) 110% bonus\n(3) 125% bonus\n(4) 140% bonus\n(5) 165% bonus\n(6) 190% bonus\n(7) 210% bonus\n\nLegion Solider - 10 Attack Damage\nLegion Archer - 5% Attack Speed\nSoldier - 7% Crit Chance\nGarmon - 10 Ability Power\nTristan - 5 Armor, 5 Magic Resistance\nMaria - 60 Health\nEmblem - 3% Omnivamp";
    }

    @Override
    public Tier getTier() {
        return getThreshholdTier(2, 4, 6, 9);
    }

    @Override
    public float getBonusMaximumHealthFlat(Unit unit) {
        return getBonusStatValue(unit, Maria.class, 60);
    }

    @Override
    public float getBonusAttackDamageFlat(Unit unit) {
        return getBonusStatValue(unit, LegionSoldier.class, 5);
    }

    @Override
    public float getBonusAttackSpeedPercent(Unit unit) {
        return getBonusStatValue(unit, LegionArcher.class, 0.05f);
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        return getBonusStatValue(unit, Garmon.class, 10);
    }

    @Override
    public float getBonusCritChanceFlat(Unit unit) {
        return getBonusStatValue(unit, Soldier.class, 0.07f);
    }

    @Override
    public float getBonusArmorFlat(Unit unit) {
        return getBonusStatValue(unit, Tristan.class, 6);
    }

    @Override
    public float getBonusMagicResistanceFlat(Unit unit) {
        return getBonusStatValue(unit, Tristan.class, 6);
    }

    private float getBonusStatValue(Unit unit, Class<? extends Unit> requiredUnitClass, float baseBonusValue) {
        if (unit.getBoard().hasUnit(unit.getPlayer(), otherUnit -> requiredUnitClass.isAssignableFrom(otherUnit.getClass()) && otherUnit.isAffectingOrAffectedByBonuses())) {
            float factor;
            int units = getUniqueUnitsOfTraitOnBoard();
            if (units >= 7) {
                factor = 2.1f;
            } else if (units >= 6) {
                factor = 1.9f;
            } else if (units >= 5) {
                factor = 1.65f;
            } else if (units >= 4) {
                factor = 1.4f;
            } else if (units >= 3) {
                factor = 1.25f;
            } else if (units >= 2) {
                factor = 1.1f;
            } else {
                factor = 1;
            }
            if (unit.hasTrait(LegionTrait.class)) {
                factor *= 2;
            }
            return factor * baseBonusValue;
        }
        return 0;
    }
}
