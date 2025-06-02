package com.destroflyer.battlebuds.shared.game.objects.players;

import com.destroflyer.battlebuds.shared.game.PhaseMath;
import com.destroflyer.battlebuds.shared.game.PositionSlot;
import com.destroflyer.battlebuds.shared.game.objects.Player;
import com.destroflyer.battlebuds.shared.game.objects.units.*;

public class NeutralPlayer extends Player {

    public void setupSlotUnits() {
        clearSlotUnits();
        int phase = game.getPhase();
        int stage = PhaseMath.getStage(phase);
        int round = PhaseMath.getRound(phase);
        switch (stage) {
            case 1:
                if (round > 3) {
                    addNewUnit(new RebellionArcher(), new PositionSlot(PositionSlot.Type.BOARD, 5, 2));
                }
                if (round > 2) {
                    addNewUnit(new RebellionArcher(), new PositionSlot(PositionSlot.Type.BOARD, 1, 2));
                }
                addNewUnit(new RebellionSoldier(), new PositionSlot(PositionSlot.Type.BOARD, 2, 1));
                addNewUnit(new RebellionSoldier(), new PositionSlot(PositionSlot.Type.BOARD, 4, 1));
                break;
            case 2:
                addNewUnit(new RebellionGiant(), new PositionSlot(PositionSlot.Type.BOARD, 0, 0));
                addNewUnit(new RebellionGiant(), new PositionSlot(PositionSlot.Type.BOARD, 1, 2));
                addNewUnit(new RebellionGiant(), new PositionSlot(PositionSlot.Type.BOARD, 5, 0));
                break;
            case 3:
                addNewUnit(new RebellionAssasin(), new PositionSlot(PositionSlot.Type.BOARD, 1, 3));
                addNewUnit(new RebellionAssasin(), new PositionSlot(PositionSlot.Type.BOARD, 2, 3));
                addNewUnit(new RebellionAssasin(), new PositionSlot(PositionSlot.Type.BOARD, 3, 1));
                addNewUnit(new RebellionAssasin(), new PositionSlot(PositionSlot.Type.BOARD, 4, 3));
                addNewUnit(new RebellionAssasin(), new PositionSlot(PositionSlot.Type.BOARD, 5, 3));
                break;
            case 4:
                addNewUnit(new RebellionElite(), new PositionSlot(PositionSlot.Type.BOARD, 1, 1));
                addNewUnit(new RebellionElite(), new PositionSlot(PositionSlot.Type.BOARD, 2, 2));
                addNewUnit(new RebellionElite(), new PositionSlot(PositionSlot.Type.BOARD, 3, 3));
                addNewUnit(new RebellionElite(), new PositionSlot(PositionSlot.Type.BOARD, 4, 2));
                addNewUnit(new RebellionElite(), new PositionSlot(PositionSlot.Type.BOARD, 5, 1));
                break;
            case 5:
            default:
                addNewUnit(new RebellionBaron(), new PositionSlot(PositionSlot.Type.BOARD, 3, 1));
                break;
        }
    }
}
