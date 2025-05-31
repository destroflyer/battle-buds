package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.game.Trait;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

import java.util.List;

public class AssassinTrait extends Trait {

    public AssassinTrait() {
        name = "Assassin";
        description = "At combat start, Assassins leap to the enemy backline.\n\nAssassins spells can critically strike and they gain Crit Chance and Crit Damage.\n\n(2) 20% Crit Chance, 10% Crit Damage\n(4) 40% Crit Chance, 25% Crit Damage\n(6) 75% Crit Chance, 75% Crit Damage\n(8) 75% Crit Chance, 200% Crit Damage";
    }

    @Override
    public Tier getTier() {
        return getThreshholdTier(2, 4, 6, 8);
    }

    @Override
    public void onCombatRoundStart() {
        super.onCombatRoundStart();
        List<Unit> assassins = player.getOwnBoard().getUnits(player, unit -> unit.isActive() && unit.hasTrait(AssassinTrait.class));
        for (Unit assassin : assassins) {
            assassin.setTargetPosition(assassin.getPosition().mult(1, -1), 0, 35f);
        }
    }

    @Override
    public float getBonusCritChanceFlat(Unit unit) {
        if (unit.hasTrait(AssassinTrait.class)) {
            int units = getUniqueUnitsOfTraitOnBoard();
            if (units >= 6) {
                return 0.75f;
            } else if (units >= 4) {
                return 0.4f;
            } else if (units >= 2) {
                return 0.2f;
            }
        }
        return 0;
    }

    @Override
    public float getBonusCritDamageFlat(Unit unit) {
        if (unit.hasTrait(AssassinTrait.class)) {
            int units = getUniqueUnitsOfTraitOnBoard();
            if (units >= 8) {
                return 2;
            } else if (units >= 6) {
                return 0.75f;
            } else if (units >= 4) {
                return 0.25f;
            } else if (units >= 2) {
                return 0.1f;
            }
        }
        return 0;
    }
}
