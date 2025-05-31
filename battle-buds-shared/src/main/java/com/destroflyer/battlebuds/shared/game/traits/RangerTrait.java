package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.game.Trait;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.buffs.RangerBuff;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class RangerTrait extends Trait {

    public RangerTrait() {
        name = "Ranger";
        description = "Rangers gain Attack Speed on each attack, stacking up to 10 times.\n\n(2) 4% Attack Speed per stack\n(4) 11% Attack Speed per stack\n(6) 27% Attack Speed per stack";
    }

    @Override
    public Tier getTier() {
        return getThreshholdTier(null, 2, 4, 6);
    }

    @Override
    public void onAllyUnitAttack(Unit unit, Unit target) {
        super.onAllyUnitAttack(unit, target);
        if (unit.hasTrait(RangerTrait.class)) {
            int units = getUniqueUnitsOfTraitOnBoard();
            if (units >= 2) {
                float bonusAttackSpeed;
                if (units >= 6) {
                    bonusAttackSpeed = 0.27f;
                } else if (units >= 4) {
                    bonusAttackSpeed = 0.11f;
                } else {
                    bonusAttackSpeed = 0.04f;
                }
                unit.addBuff(new RangerBuff(bonusAttackSpeed));
            }
        }
    }
}
