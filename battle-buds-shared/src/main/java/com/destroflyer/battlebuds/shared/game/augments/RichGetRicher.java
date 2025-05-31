package com.destroflyer.battlebuds.shared.game.augments;

import com.destroflyer.battlebuds.shared.game.Augment;
import com.destroflyer.battlebuds.shared.game.Tier;

public class RichGetRicher extends Augment {

    public RichGetRicher() {
        tier = Tier.GOLD;
        name = "Rich Get Richer";
        description = "Gain 12 gold. Your max interest is increased to 7.";
    }

    @Override
    public void onAdd() {
        super.onAdd();
        player.addGold(12);
    }

    @Override
    public int getBonusMaximumInterestGold() {
        return 2;
    }
}
