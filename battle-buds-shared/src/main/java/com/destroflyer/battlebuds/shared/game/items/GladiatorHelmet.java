package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class GladiatorHelmet extends Item {

    public GladiatorHelmet() {
        name = "Gladiator Helmet";
        visualName = "gladiator_helmet";
        description = "+10% Attack Damage, +20 Armor, TODO: Untargetability";
    }

    @Override
    public float getBonusAttackDamagePercent(Unit unit) {
        return 0.1f;
    }

    @Override
    public float getBonusArmorFlat(Unit unit) {
        return 20;
    }
}
