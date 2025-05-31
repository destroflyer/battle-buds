package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class NewDawn extends Item {

    public NewDawn() {
        name = "New Dawn";
        visualName = "new_dawn";
        description = "+35% Attack Damage, +35% Crit Chance, TODO: Spells can crit";
    }

    @Override
    public float getBonusAttackDamagePercent(Unit unit) {
        return 0.35f;
    }

    @Override
    public float getBonusCritChanceFlat(Unit unit) {
        return 0.35f;
    }
}
