package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class EthersArmor extends Item {

    public EthersArmor() {
        name = "Ether's Armor";
        visualName = "ethers_armor";
        description = "+55 Armor, TODO: +5% Health, reduce damage from attacks, reflect attack damage";
    }

    @Override
    public float getBonusArmorFlat(Unit unit) {
        return 55;
    }
}
