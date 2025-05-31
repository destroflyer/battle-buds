package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.game.Trait;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class FairyTrait extends Trait {

    public FairyTrait() {
        name = "Fairy";
        description = "Your teams gains bonus Mana regeneration. Fairies gain double.\n\n(2) 2 Mana per second\n(4) 3 Mana per second\n(6) 10 Mana per second";
    }

    @Override
    public Tier getTier() {
        return getThreshholdTier(null, 2, 4, 6);
    }

    @Override
    public float getBonusManaRegenerationFlat(Unit unit) {
        int bonusManaRegeneration = 0;
        int units = getUniqueUnitsOfTraitOnBoard();
        if (units >= 6) {
            bonusManaRegeneration = 10;
        } else if (units >= 4) {
            bonusManaRegeneration = 3;
        } else if (units >= 2) {
            bonusManaRegeneration = 2;
        }
        if (unit.hasTrait(FairyTrait.class)) {
            bonusManaRegeneration *= 2;
        }
        return bonusManaRegeneration;
    }
}
