package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class BloodCapsule extends Item {

    public BloodCapsule() {
        name = "Blood Capsule";
        visualName = "blood_capsule";
        description = "+600 Health, +8% Health";
    }

    @Override
    public float getBonusMaximumHealthFlat(Unit unit) {
        return 100;
    }

    @Override
    public float getBonusMaximumHealthPercent(Unit unit) {
        return 0.08f;
    }
}
