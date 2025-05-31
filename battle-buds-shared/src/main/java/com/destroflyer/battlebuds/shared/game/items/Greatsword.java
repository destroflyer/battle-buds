package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class Greatsword extends Item {

    public Greatsword() {
        name = "Greatsword";
        visualName = "greatsword";
        description = "+10% Attack Damage";
    }

    @Override
    public float getBonusAttackDamagePercent(Unit unit) {
        return 0.1f;
    }
}
