package com.destroflyer.battlebuds.shared.game.boards;

import com.destroflyer.battlebuds.shared.game.Board;

public class TimeBoard extends Board {

    public static final float DEFAULT_DURATION = 30;

    public void reset() {
        time = 0;
    }

    @Override
    public boolean isFinished() {
        return time >= getDuration();
    }

    public float getDuration() {
        return DEFAULT_DURATION;
    }
}
