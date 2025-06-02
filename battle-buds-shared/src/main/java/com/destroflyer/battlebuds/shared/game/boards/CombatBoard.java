package com.destroflyer.battlebuds.shared.game.boards;

import com.destroflyer.battlebuds.shared.game.Board;
import com.destroflyer.battlebuds.shared.game.GameObject;
import com.destroflyer.battlebuds.shared.game.PhaseMath;
import com.destroflyer.battlebuds.shared.game.objects.Player;
import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.objects.players.ActualPlayer;

public class CombatBoard extends Board {

    private Float finishTime;

    @Override
    public void update(float tpf) {
        super.update(tpf);
        checkFinishDamage();
    }

    private void checkFinishDamage() {
        if ((finishTime == null) && isCombatOver()) {
            finishTime = time;
            for (Player player : owners) {
                if (player instanceof ActualPlayer) {
                    int remainingEnemyUnits = (int) objects.stream().filter(object -> (object instanceof Unit unit) && unit.isActive() && (unit.getPlayer() != player)).count();
                    if (remainingEnemyUnits > 0) {
                        float damage = PhaseMath.getPlayerCombatLossDamage(game.getPhase(), remainingEnemyUnits);
                        player.takeDamage(damage);
                    }
                }
            }
        }
    }

    private boolean isCombatOver() {
        // TODO: Add overtime and draw
        return getWinner() != null;
    }

    protected Player getWinner() {
        Player playerWithActiveUnit = null;
        for (GameObject object : objects) {
            if ((object instanceof Unit unit) && unit.isActive()) {
                if (playerWithActiveUnit == null) {
                    playerWithActiveUnit = unit.getPlayer();
                } else if (unit.getPlayer() != playerWithActiveUnit) {
                    return null;
                }
            }
        }
        // TODO: Add draw
        if (playerWithActiveUnit == null) {
            return owners.get((int) (Math.random() * owners.size()));
        }
        return playerWithActiveUnit;
    }

    @Override
    public boolean isFinished() {
        return ((finishTime != null) && (time >= (finishTime + 2)));
    }
}
