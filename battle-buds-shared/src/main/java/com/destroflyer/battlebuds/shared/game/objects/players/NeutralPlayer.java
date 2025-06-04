package com.destroflyer.battlebuds.shared.game.objects.players;

import com.destroflyer.battlebuds.shared.game.PhaseMath;
import com.destroflyer.battlebuds.shared.game.PositionSlot;
import com.destroflyer.battlebuds.shared.game.objects.Player;
import com.destroflyer.battlebuds.shared.game.objects.units.*;

public class NeutralPlayer extends Player {

    @Override
    protected void autofillUnitSlots() {
        super.autofillUnitSlots();
        clearSlotUnits();
        int phase = game.getPhase();
        int stage = PhaseMath.getStage(phase);
        int round = PhaseMath.getRound(phase);
        switch (stage) {
            case 1:
                if (round > 3) {
                    addNewUnitTo(new RebellionArcher(), new PositionSlot(PositionSlot.Type.BOARD, 5, 2));
                }
                if (round > 2) {
                    addNewUnitTo(new RebellionArcher(), new PositionSlot(PositionSlot.Type.BOARD, 1, 2));
                }
                addNewUnitTo(new RebellionSoldier(), new PositionSlot(PositionSlot.Type.BOARD, 2, 1));
                addNewUnitTo(new RebellionSoldier(), new PositionSlot(PositionSlot.Type.BOARD, 4, 1));
                break;
            case 2:
                addNewUnitTo(new RebellionGiant(), new PositionSlot(PositionSlot.Type.BOARD, 0, 0));
                addNewUnitTo(new RebellionGiant(), new PositionSlot(PositionSlot.Type.BOARD, 1, 2));
                addNewUnitTo(new RebellionGiant(), new PositionSlot(PositionSlot.Type.BOARD, 5, 0));
                break;
            case 3:
                addNewUnitTo(new RebellionAssasin(), new PositionSlot(PositionSlot.Type.BOARD, 1, 3));
                addNewUnitTo(new RebellionAssasin(), new PositionSlot(PositionSlot.Type.BOARD, 2, 3));
                addNewUnitTo(new RebellionAssasin(), new PositionSlot(PositionSlot.Type.BOARD, 3, 1));
                addNewUnitTo(new RebellionAssasin(), new PositionSlot(PositionSlot.Type.BOARD, 4, 3));
                addNewUnitTo(new RebellionAssasin(), new PositionSlot(PositionSlot.Type.BOARD, 5, 3));
                break;
            case 4:
                addNewUnitTo(new RebellionElite(), new PositionSlot(PositionSlot.Type.BOARD, 1, 1));
                addNewUnitTo(new RebellionElite(), new PositionSlot(PositionSlot.Type.BOARD, 2, 2));
                addNewUnitTo(new RebellionElite(), new PositionSlot(PositionSlot.Type.BOARD, 3, 3));
                addNewUnitTo(new RebellionElite(), new PositionSlot(PositionSlot.Type.BOARD, 4, 2));
                addNewUnitTo(new RebellionElite(), new PositionSlot(PositionSlot.Type.BOARD, 5, 1));
                break;
            case 5:
            default:
                addNewUnitTo(new RebellionBaron(), new PositionSlot(PositionSlot.Type.BOARD, 3, 1));
                break;
        }
    }
}
