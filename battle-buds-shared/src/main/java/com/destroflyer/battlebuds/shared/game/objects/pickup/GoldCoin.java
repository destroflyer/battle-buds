package com.destroflyer.battlebuds.shared.game.objects.pickup;

import com.destroflyer.battlebuds.shared.game.objects.PickupObject;
import com.destroflyer.battlebuds.shared.game.objects.players.ActualPlayer;

public class GoldCoin extends PickupObject {

    public GoldCoin() {
        visualName = "gold_coin";
    }

    @Override
    protected void onPickUp(ActualPlayer actualPlayer) {
        actualPlayer.addGold(1);
    }
}
