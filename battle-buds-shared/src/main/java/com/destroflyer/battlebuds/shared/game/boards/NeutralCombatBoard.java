package com.destroflyer.battlebuds.shared.game.boards;

import com.destroflyer.battlebuds.shared.game.objects.Player;

public class NeutralCombatBoard extends CombatBoard {

    @Override
    public void onFinish() {
        super.onFinish();
        Player winner = getWinner();
        winner.addGold(1);
    }
}
