package com.destroflyer.battlebuds.shared.game.boards;

import com.destroflyer.battlebuds.shared.game.objects.Player;
import com.destroflyer.battlebuds.shared.game.objects.players.ActualPlayer;

public class NeutralCombatBoard extends CombatBoard {

    @Override
    public void onFinish() {
        super.onFinish();
        Player winner = getWinner();
        if (winner instanceof ActualPlayer winnerActualPlayer) {
            winnerActualPlayer.addGold(1);
        }
    }
}
