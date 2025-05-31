package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class EnchantedCloak extends Item {

    public EnchantedCloak() {
        name = "Enchanted Cloak";
        visualName = "enchanted_cloak";
        description = "+20 Magic Resistance";
    }

    @Override
    public float getBonusMagicResistanceFlat(Unit unit) {
        return 20;
    }
}
