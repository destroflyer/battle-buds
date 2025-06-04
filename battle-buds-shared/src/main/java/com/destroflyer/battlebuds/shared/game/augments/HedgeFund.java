package com.destroflyer.battlebuds.shared.game.augments;

import com.destroflyer.battlebuds.shared.game.Augment;
import com.destroflyer.battlebuds.shared.game.Tier;

public class HedgeFund extends Augment {

    public HedgeFund() {
        tier = Tier.PRISMATIC;
        name = "Hedge Fund";
        description = "Gain 25 gold. Your max interest is increased to 10.";
    }

    @Override
    public void onAdd() {
        super.onAdd();
        player.dropGoldForSelf(25);
    }

    @Override
    public int getBonusMaximumInterestGold() {
        return 5;
    }
}
