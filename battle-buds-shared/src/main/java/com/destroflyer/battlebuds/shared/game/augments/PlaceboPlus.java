package com.destroflyer.battlebuds.shared.game.augments;

import com.destroflyer.battlebuds.shared.game.Augment;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class PlaceboPlus extends Augment {

    public PlaceboPlus() {
        tier = Tier.SILVER;
        name = "Placebo+";
        description = "Your team gains 1% Attack Speed. Gain 15 gold.";
    }

    @Override
    public void onAdd() {
        super.onAdd();
        player.dropGoldForSelf(15);
    }

    @Override
    public float getBonusAttackSpeedFlat(Unit unit) {
        return 0.01f;
    }
}
