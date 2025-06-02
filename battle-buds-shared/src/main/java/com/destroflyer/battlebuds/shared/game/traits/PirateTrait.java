package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.game.Trait;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class PirateTrait extends Trait {

    public PirateTrait() {
        name = "Pirate";
        description = "Enemies have a 30% chance to drop gold when killed.\n\n... and a 1% chance to drop 20 gold instead.";
    }

    @Override
    public Tier getTier() {
        return getThreshholdTier(null, null, 1, null);
    }

    @Override
    public void onEnemyUnitDeath(Unit unit) {
        super.onEnemyUnitDeath(unit);
        int units = getUniqueUnitsOfTraitOnBoard();
        if (units > 0) {
            double roll = Math.random();
            if (roll < 0.01) {
                unit.dropGoldForEnemy(20);
            } else if (roll < 0.3) {
                unit.dropGoldForEnemy(1);
            }
        }
    }
}
