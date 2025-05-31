package com.destroflyer.battlebuds.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GameMode {
    SINGLE_ONE("Single Player (1P)", 1),
    SINGLE_TWO("Single Player (2P)", 2),
    SINGLE_FOUR("Single Player (4P)", 4),
    SINGLE_EIGHT("Single Player (8P)", 8),
    DOUBLE_FOUR("Double Up (4P)", 4),
    DOUBLE_EIGHT("Double Up (8P)", 8);

    private String name;
    private int playerCount;
}
