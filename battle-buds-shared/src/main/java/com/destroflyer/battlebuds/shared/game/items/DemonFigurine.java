package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class DemonFigurine extends Item {

    public DemonFigurine() {
        name = "Demon Figurine";
        visualName = "demon_figurine";
        description = "+150 Health, +20 Magic Resistance, TODO: Resistances at combat start, armor shred";
    }

    @Override
    public float getBonusMaximumHealthFlat(Unit unit) {
        return 150;
    }

    @Override
    public float getBonusMagicResistanceFlat(Unit unit) {
        return 20;
    }
}
