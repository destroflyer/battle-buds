package com.destroflyer.battlebuds.shared.game.items;

import com.destroflyer.battlebuds.shared.game.Item;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class ShardOfTime extends Item {

    public ShardOfTime() {
        name = "Shard of Time";
        visualName = "shard_of_time";
        description = "+15 Mana, +20% Crit Chance, TODO: Damage and heal";
    }

    @Override
    public float getBonusInitialManaFlat(Unit unit) {
        return 15;
    }

    @Override
    public float getBonusCritChanceFlat(Unit unit) {
        return 0.2f;
    }
}
