package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class SwiftBow extends Item {

    public SwiftBow() {
        name = "Swift Bow";
        visualName = "swift_bow";
        description = "+15% Attack Damage, +25% Attack Speed, +20% Crit Chance, TODO: Armor shred";
    }

    @Override
    public float getBonusAttackDamagePercent(Unit unit) {
        return 0.15f;
    }

    @Override
    public float getBonusAttackSpeedFlat(Unit unit) {
        return 0.25f;
    }

    @Override
    public float getBonusCritChanceFlat(Unit unit) {
        return 0.2f;
    }
}
