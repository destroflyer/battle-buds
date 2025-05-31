package com.destroflyer.battlebuds.shared.game;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GameTask {

    private Runnable runnable;
    private float remainingDelay;

    public boolean update(float tpf) {
        remainingDelay -= tpf;
        if (remainingDelay <= 0) {
            runnable.run();
            return true;
        }
        return false;
    }
}
