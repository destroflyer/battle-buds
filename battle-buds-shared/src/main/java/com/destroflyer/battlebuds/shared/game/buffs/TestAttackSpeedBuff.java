package com.destroflyer.battlebuds.shared.game.buffs;

import com.destroflyer.battlebuds.shared.game.Buff;
import com.destroflyer.battlebuds.shared.game.objects.Unit;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public class TestAttackSpeedBuff extends Buff {

    @Override
    public float getBonusAttackSpeedFlat(Unit unit) {
        return 0.3f;
    }
}
