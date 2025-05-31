package com.destroflyer.battlebuds.shared.game.objects.pickup;

import com.destroflyer.battlebuds.shared.game.objects.PickUpObject;
import com.destroflyer.battlebuds.shared.game.objects.Player;

public class GoldCoin extends PickUpObject {

    public GoldCoin() {
        visualName = "gold_coin";
    }

    @Override
    protected void onPickUp(Player player) {
        player.addGold(1);
    }
}
