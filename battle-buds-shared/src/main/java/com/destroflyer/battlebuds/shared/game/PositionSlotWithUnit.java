package com.destroflyer.battlebuds.shared.game;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PositionSlotWithUnit {
    private PositionSlot positionSlot;
    private Unit unit;
}
