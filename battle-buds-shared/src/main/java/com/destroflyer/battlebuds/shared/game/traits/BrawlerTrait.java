package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.game.Trait;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class BrawlerTrait extends Trait {

    public BrawlerTrait() {
        name = "Brawler";
        description = "Brawlers gain Attack Damage and Omnivamp.\n\n(2) 15% Attack Damage, 15% Omnivamp\n(4) 40% Attack Damage, 15% Omnivamp\n(6) 70% Attack Damage, 20% Omnivamp\n(8) 150% Attack Damage, 40% Omnivamp";
    }

    @Override
    public Tier getTier() {
        return getThreshholdTier(2, 4, 6, 8);
    }

    @Override
    public float getBonusAttackDamagePercent(Unit unit) {
        if (unit.hasTrait(BrawlerTrait.class)) {
            int units = getUniqueUnitsOfTraitOnBoard();
            if (units >= 8) {
                return 1.5f;
            } else if (units >= 6) {
                return 0.70f;
            } else if (units >= 4) {
                return 0.40f;
            } else if (units >= 2) {
                return 0.15f;
            }
        }
        return 0;
    }

    @Override
    public float getBonusOmnivampFlat(Unit unit) {
        if (unit.hasTrait(BrawlerTrait.class)) {
            int units = getUniqueUnitsOfTraitOnBoard();
            if (units >= 8) {
                return 0.4f;
            } else if (units >= 6) {
                return 0.2f;
            } else if (units >= 2) {
                return 0.15f;
            }
        }
        return 0;
    }
}
