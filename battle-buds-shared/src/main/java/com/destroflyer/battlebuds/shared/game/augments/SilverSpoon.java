package com.destroflyer.battlebuds.shared.game.augments;

import com.destroflyer.battlebuds.shared.game.Augment;
import com.destroflyer.battlebuds.shared.game.Tier;

public class SilverSpoon extends Augment {

    public SilverSpoon() {
        tier = Tier.SILVER;
        name = "Silver Spoon";
        description = "Gain 10 XP.";
    }

    @Override
    public void onAdd() {
        super.onAdd();
        player.tryAddExperience(10);
    }
}
