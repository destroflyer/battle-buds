package com.destroflyer.battlebuds.shared.game;

import com.destroflyer.battlebuds.shared.game.objects.Player;
import com.jme3.math.Vector2f;

public class BoardMath {

    // Whole board/bench alphamap width = 334, whole terrain alphamap width = 1024, terrain world width = 128
    private static final float BOARD_WIDTH = ((334f / 1024) * 128);
    // Whole board alphamap height = 113, whole terrain alphamap height = 1024, terrain world width = 128
    private static final float BOARD_HEIGHT = ((113f / 1024) * 128);
    private static final float BOARD_X = (BOARD_WIDTH / -2);
    private static final float BOARD_Y = 0;
    private static final float BOARD_SLOT_WIDTH = (BOARD_WIDTH / Player.BOARD_SLOTS_X);
    private static final float BOARD_SLOT_HEIGHT = (BOARD_HEIGHT / Player.BOARD_SLOTS_Y);

    private static final float BENCH_WIDTH = BOARD_WIDTH;
    // Whole bench alphamap height = 34, whole terrain alphamap height = 1024, terrain world width = 128
    private static final float BENCH_HEIGHT = ((34f / 1024) * 128);
    private static final float BENCH_X = (BOARD_WIDTH / -2);
    private static final float BENCH_Y = BOARD_HEIGHT;
    private static final float BENCH_SLOT_WIDTH = (BENCH_WIDTH / Player.BENCH_SLOTS);
    private static final float BENCH_SLOT_HEIGHT = BENCH_HEIGHT;

    public static PositionSlot getPositionSlot(Vector2f position) {
        PositionSlot benchPositionSlot = getPositionSlot(position, PositionSlot.Type.BENCH, BENCH_X, BENCH_Y, BENCH_WIDTH, BENCH_HEIGHT, BENCH_SLOT_WIDTH, BENCH_SLOT_HEIGHT);
        if (benchPositionSlot != null) {
            return benchPositionSlot;
        }
        return getPositionSlot(position, PositionSlot.Type.BOARD, BOARD_X, BOARD_Y, BOARD_WIDTH, BOARD_HEIGHT, BOARD_SLOT_WIDTH, BOARD_SLOT_HEIGHT);
    }

    private static PositionSlot getPositionSlot(Vector2f position, PositionSlot.Type type, float areaX, float areaY, float areaWidth, float areaHeight, float slotWidth, float slotHeight) {
        if ((position.getX() >= areaX)
         && (position.getX() <= (areaX + areaWidth))
         && (position.getY() >= areaY)
         && (position.getY() <= (areaY + areaHeight))) {
            int x = (int) ((position.getX() - areaX) / slotWidth);
            int y = (int) ((position.getY() - areaY) / slotHeight);
            return new PositionSlot(type, x, y);
        }
        return null;
    }

    public static Vector2f getBenchPosition(int benchIndex) {
        return getSlotPosition(benchIndex, 0, BENCH_X, BENCH_Y, BENCH_SLOT_WIDTH, BENCH_SLOT_HEIGHT);
    }

    public static Vector2f getBoardPosition(int x, int y) {
        return getSlotPosition(x, y, BOARD_X, BOARD_Y, BOARD_SLOT_WIDTH, BOARD_SLOT_HEIGHT);
    }

    private static Vector2f getSlotPosition(int slotX, int slotY, float areaX, float areaY, float slotWidth, float slotHeight) {
        float x = (areaX + ((slotX + 0.5f) * slotWidth));
        float y = (areaY + ((slotY + 0.5f) * slotHeight));
        return new Vector2f(x, y);
    }
}
