package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class Waraxe extends Item {

    public Waraxe() {
        name = "Waraxe";
        visualName = "waraxe";
        description = "+25 Attack Damage, +10% Attack Speed, +20 Magic Resistance, TODO: Stack Attack Damage on attack";
    }

    @Override
    public float getBonusAttackDamageFlat(Unit unit) {
        return 25;
    }

    @Override
    public float getBonusAttackSpeedFlat(Unit unit) {
        return 0.1f;
    }

    @Override
    public float getBonusMagicResistanceFlat(Unit unit) {
        return 20;
    }
}
