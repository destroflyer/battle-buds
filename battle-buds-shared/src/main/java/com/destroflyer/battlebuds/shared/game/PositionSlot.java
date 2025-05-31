package com.destroflyer.battlebuds.shared.game;

import com.jme3.network.serializing.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Serializable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class PositionSlot {

    public enum Type {
        BENCH,
        BOARD
    }
    private Type type;
    private int x;
    private int y;
}
