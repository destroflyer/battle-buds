package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class IcyTrinket extends Item {

    public IcyTrinket() {
        name = "IcyTrinket";
        visualName = "icy_trinket";
        description = "-10 Maximum Mana, +20 Mana, +20 Attack Damage, +20 Ability Power, TODO: Damage amplification on takedown or set mana on cast";
    }

    @Override
    public float getBonusMaximumManaFlat(Unit unit) {
        return -10;
    }

    @Override
    public float getBonusInitialManaFlat(Unit unit) {
        return 20;
    }

    @Override
    public float getBonusAttackDamageFlat(Unit unit) {
        return 20;
    }

    @Override
    public float getBonusAbilityPowerFlat(Unit unit) {
        return 20;
    }
}
