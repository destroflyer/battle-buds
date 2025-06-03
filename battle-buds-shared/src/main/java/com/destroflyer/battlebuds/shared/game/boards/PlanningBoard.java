package com.destroflyer.battlebuds.shared.game.boards;

import com.destroflyer.battlebuds.shared.game.objects.Player;

public class PlanningBoard extends TimeBoard {

    @Override
    public void update(float tpf) {
        super.update(tpf);
        for (Player player : owners) {
            // Easiest way to keep health and mana updated if stat modifiers change
            player.resetUnitsHealthAndMana();
        }
    }

    @Override
    public float getDuration() {
        switch (game.getPhase()) {
            case 1: return 6;
            case 3: return 15;
            case 5: return 20;
        }
        return DEFAULT_DURATION;
    }
}
