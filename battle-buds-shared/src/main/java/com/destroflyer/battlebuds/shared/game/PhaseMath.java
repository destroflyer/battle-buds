package com.destroflyer.battlebuds.shared.game;

public class PhaseMath {

    public static PhaseType getPhaseType(int phase) {
        switch (phase) {
            // Stage 1
            case 0: return PhaseType.CAROUSEL;
            case 1: return PhaseType.PLANNING;
            case 2: return PhaseType.COMBAT_NEUTRAL;
            case 3: return PhaseType.PLANNING;
            case 4: return PhaseType.COMBAT_NEUTRAL;
            case 5: return PhaseType.PLANNING;
            case 6: return PhaseType.COMBAT_NEUTRAL;
            // Stage 2
            case 7: return PhaseType.PLANNING;
            case 8: return PhaseType.COMBAT_PLAYER;
            case 9: return PhaseType.PLANNING;
            case 10: return PhaseType.COMBAT_PLAYER;
            case 11: return PhaseType.PLANNING;
            case 12: return PhaseType.COMBAT_PLAYER;
            case 13: return PhaseType.PLANNING;
            case 14: return PhaseType.COMBAT_PLAYER;
            case 15: return PhaseType.CAROUSEL;
            case 16: return PhaseType.PLANNING;
            case 17: return PhaseType.COMBAT_PLAYER;
            case 18: return PhaseType.PLANNING;
            case 19: return PhaseType.COMBAT_NEUTRAL;
        }
        // Stage 3 onwards
        int relativePhase = ((phase - 20) % 13);
        switch (relativePhase) {
            case 0: return PhaseType.PLANNING;
            case 1: return PhaseType.COMBAT_PLAYER;
            case 2: return PhaseType.PLANNING;
            case 3: return PhaseType.COMBAT_PLAYER;
            case 4: return PhaseType.PLANNING;
            case 5: return PhaseType.COMBAT_PLAYER;
            case 6: return PhaseType.CAROUSEL;
            case 7: return PhaseType.PLANNING;
            case 8: return PhaseType.COMBAT_PLAYER;
            case 9: return PhaseType.PLANNING;
            case 10: return PhaseType.COMBAT_PLAYER;
            case 11: return PhaseType.PLANNING;
            case 12: return PhaseType.COMBAT_NEUTRAL;
        }
        // Unreachable
        return PhaseType.PLANNING;
    }

    public static boolean isAugmentOffered(int phase) {
        // Planning phase of 2-1, 3-2, 4-2
        return ((phase == 7) || (phase == 22) || (phase == 35));
    }

    public static int getStage(int phase) {
        if (phase < 7) {
            return 1;
        } else if (phase < 20) {
            return 2;
        }
        return 3 + ((phase - 20) / 13);
    }

    public static int getRound(int phase) {
        switch (phase) {
            // Stage 1
            case 0: return 1;
            case 1, 2: return 2;
            case 3, 4: return 3;
            case 5, 6: return 4;
            // Stage 2
            case 7, 8: return 1;
            case 9, 10: return 2;
            case 11, 12: return 3;
            case 13, 14: return 4;
            case 15: return 5;
            case 16, 17: return 6;
            case 18, 19: return 7;
        }
        // Stage 3 onwards
        int relativePhase = ((phase - 20) % 13);
        switch (relativePhase) {
            case 0, 1: return 1;
            case 2, 3: return 2;
            case 4, 5: return 3;
            case 6: return 4;
            case 7, 8: return 5;
            case 9, 10: return 6;
            case 11, 12: return 7;
        }
        // Unreachable
        return 0;
    }

    public static float getPlayerCombatLossDamage(int phase, int remainingEnemyUnits) {
        return getPlayerCombatLossPhaseDamage(phase) + remainingEnemyUnits;
    }

    private static float getPlayerCombatLossPhaseDamage(int phase) {
        int stage = getStage(phase);
        switch (stage) {
            case 1: return 0;
            case 2: return 2;
            case 3: return 5;
            case 4: return 8;
            case 5: return 10;
            case 6: return 12;
            case 7: return 17;
            default: return 150;
        }
    }
}
