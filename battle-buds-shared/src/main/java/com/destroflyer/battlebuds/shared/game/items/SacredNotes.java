package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class SacredNotes extends Item {

    public SacredNotes() {
        name = "Sacred Notes";
        visualName = "sacred_notes";
        description = "+35 Ability Power, +35% Crit Chance, TODO: Spells can crit";
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        return 35;
    }

    @Override
    public float getBonusCritChanceFlat(Unit unit) {
        return 0.35f;
    }
}
