package com.destroflyer.battlebuds.shared.game;

public class ChanceUtil {

    public static void ifChance(float chance, Runnable runnable) {
        if (Math.random() < chance) {
            runnable.run();
        }
    }
}
