package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class RunicBracers extends Item {

    public RunicBracers() {
        name = "Runic Bracers";
        visualName = "runic_bracers";
        description = "+30 Mana, +20 Armor, TODO: Shield";
    }

    @Override
    public float getBonusInitialManaFlat(Unit unit) {
        return 30;
    }

    @Override
    public float getBonusArmorFlat(Unit unit) {
        return 20;
    }
}
