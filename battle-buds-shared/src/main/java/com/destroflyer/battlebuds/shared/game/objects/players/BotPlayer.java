package com.destroflyer.battlebuds.shared.game.objects.players;

import com.destroflyer.battlebuds.shared.game.objects.PickupObject;

import java.util.List;

public class BotPlayer extends ActualPlayer {

    @Override
    public void update(float tpf) {
        super.update(tpf);
        autoWalkToPickupableObject();
        autoBuyShopUnits();
        autoDecide();
    }

    private void autoWalkToPickupableObject() {
        List<PickupObject> pickupObjects = getPickupableObjectsOnBoard();
        if (pickupObjects.size() > 0) {
            PickupObject pickupObject = pickupObjects.getFirst();
            setTargetPosition(pickupObject.getPosition(), 0);
        }
    }

    private void autoBuyShopUnits() {
        for (int i = 0; i < shopUnits.length; i++) {
            tryBuyUnit(i);
        }
    }

    private void autoDecide() {
        tryDecide(0);
    }
}
