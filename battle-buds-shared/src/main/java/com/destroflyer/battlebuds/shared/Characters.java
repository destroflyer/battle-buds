package com.destroflyer.battlebuds.shared;

public class Characters {

    public static String[] CHARACTER_NAMES = new String[]{
        "garmon", "alice", "tristan", "dosaz", "elven_archer", "scarlet"
    };

    public static String getRandomCharacterName() {
        return CHARACTER_NAMES[(int) (Math.random() * CHARACTER_NAMES.length)];
    }
}
