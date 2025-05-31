package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.game.Trait;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

import java.util.HashSet;

public class UndeadTrait extends Trait {

    public UndeadTrait() {
        name = "Undead";
        description = "When dying for the first time in combat, Undeads are revived with a portion of their Health.\n\n(2) 25% Health\n(4) 50% Health\n(6) 100% Health";
    }
    private HashSet<Unit> alreadyRevivedUnits = new HashSet<>();

    @Override
    public Tier getTier() {
        return getThreshholdTier(null, 2, 4, 6);
    }

    @Override
    public void onPlanningRoundStart() {
        super.onPlanningRoundStart();
        alreadyRevivedUnits.clear();
    }

    @Override
    public void onAllyUnitDeath(Unit unit) {
        super.onAllyUnitDeath(unit);
        int units = getUniqueUnitsOfTraitOnBoard();
        if ((units >= 2) && unit.hasTrait(UndeadTrait.class) && !alreadyRevivedUnits.contains(unit)) {
            float maximumHealthPortion;
            if (units >= 6) {
                maximumHealthPortion = 1;
            } else if (units >= 4) {
                maximumHealthPortion = 0.5f;
            } else {
                maximumHealthPortion = 0.25f;
            }
            unit.setCurrentHealth(maximumHealthPortion * unit.getMaximumHealth());
            alreadyRevivedUnits.add(unit);
        }
    }
}
