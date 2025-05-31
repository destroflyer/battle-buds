package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class ArcaneCape extends Item {

    public ArcaneCape() {
        name = "Arcane Cape";
        visualName = "arcane_cape";
        description = "+9% Health, +65 Magic Resistance, TODO: Maximum Health regeneration";
    }

    @Override
    public float getBonusMaximumHealthPercent(Unit unit) {
        return 0.09f;
    }

    @Override
    public float getBonusMagicResistanceFlat(Unit unit) {
        return 65;
    }
}
