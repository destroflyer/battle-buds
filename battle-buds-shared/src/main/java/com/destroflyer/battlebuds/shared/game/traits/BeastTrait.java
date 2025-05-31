package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.game.Trait;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.buffs.BeastBuff;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

import java.util.List;

public class BeastTrait extends Trait {

    public BeastTrait() {
        name = "Beast";
        description = "When a Beast dies, all ally Beasts get enraged and gain stacking Attack Damage and Attack Speed.\n\n(3) 10 Attack Damage, 10% Attack Speed\n(5) 20 Attack Damage, 20% Attack Speed\n(7) 50 Attack Damage, 50% Attack Speed";
    }

    @Override
    public Tier getTier() {
        return getThreshholdTier(null, 3, 5, 7);
    }

    @Override
    public void onAllyUnitDeath(Unit unit) {
        super.onAllyUnitDeath(unit);
        int units = getUniqueUnitsOfTraitOnBoard();
        if ((units >= 3) && unit.hasTrait(BeastTrait.class)) {
            float bonusAttackDamage;
            float bonusAttackSpeed;
            if (units >= 7) {
                bonusAttackDamage = 50;
                bonusAttackSpeed = 0.5f;
            } else if (units >= 5) {
                bonusAttackDamage = 20;
                bonusAttackSpeed = 0.2f;
            } else {
                bonusAttackDamage = 10;
                bonusAttackSpeed = 0.1f;
            }
            List<Unit> otherBeasts = unit.getBoard().getUnits(player, otherUnit -> otherUnit.isActive() && otherUnit.hasTrait(BeastTrait.class) && (otherUnit != unit));
            for (Unit otherBeast : otherBeasts) {
                otherBeast.addBuff(new BeastBuff(bonusAttackDamage, bonusAttackSpeed));
            }
        }
    }
}
