package com.destroflyer.battlebuds.shared.game.objects.players;

import com.destroflyer.battlebuds.shared.game.objects.PickupObject;

import java.util.List;

public class BotPlayer extends ActualPlayer {

    @Override
    public void update(float tpf) {
        super.update(tpf);
        autoWalkToPickupableObject();
        autoBuyUnits();
        autoSellUnits();
        tryBuyExperience();
        tryDecide(0);
    }

    private void autoWalkToPickupableObject() {
        List<PickupObject> pickupObjects = getPickupableObjectsOnBoard();
        if (pickupObjects.size() > 0) {
            PickupObject pickupObject = pickupObjects.getFirst();
            setTargetPosition(pickupObject.getPosition(), 0);
        }
    }

    private void autoBuyUnits() {
        for (int i = 0; i < shopUnits.length; i++) {
            tryBuyUnit(i);
        }
    }

    private void autoSellUnits() {
        if (benchUnits[0] != null) {
            trySellUnit(benchUnits[0].getId());
        }
    }
}
