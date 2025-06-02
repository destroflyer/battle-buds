package com.destroflyer.battlebuds.shared.game.objects;

import com.destroflyer.battlebuds.shared.Util;
import com.destroflyer.battlebuds.shared.game.*;
import com.destroflyer.battlebuds.shared.game.boards.PlanningBoard;
import com.destroflyer.battlebuds.shared.game.traits.Traits;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.OptimizedBits;
import com.jme3.math.Vector2f;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

public class Player extends Character implements GameEventListener {

    protected Player() {
        for (Class<? extends Trait> traitClass : Traits.CLASSES) {
            Trait trait = Util.createObjectByClass(traitClass);
            trait.setPlayer(this);
            allTraits.add(trait);
        }
    }
    public static final int BENCH_SLOTS = 9;
    public static final int BOARD_SLOTS_X = 7;
    public static final int BOARD_SLOTS_Y = 4;
    @Getter
    @Setter
    protected PlanningBoard planningBoard;
    @Getter
    protected Unit[] benchUnits = new Unit[BENCH_SLOTS];
    @Getter
    protected Unit[][] boardUnits = new Unit[BOARD_SLOTS_X][BOARD_SLOTS_Y];
    @Getter
    private ArrayList<Trait> allTraits = new ArrayList<>();

    @Override
    public void update(float tpf) {
        super.update(tpf);
        // TODO: Don't do this on carousel? (And remove hiding code in UI then?)
        Board ownBoard = getOwnBoard();
        if (ownBoard != null) {
            updateSlotUnitsBoardStates(false);
        }
    }

    public void updateSlotUnitsBoardStates(boolean forceUpdateBoardTransforms) {
        Board ownBoard = getOwnBoard();
        int side = ((ownBoard.getOwners().indexOf(this) == 0) ? 1 : -1);
        boolean updateBoardTransforms = (forceUpdateBoardTransforms || (ownBoard == planningBoard));
        for (int x = 0; x < boardUnits.length; x++) {
            for (int y = 0; y < boardUnits[x].length; y++) {
                updateSlotUnitBoardState(boardUnits[x][y], ownBoard, PositionSlot.Type.BOARD, updateBoardTransforms, BoardMath.getBoardPosition(x, y), side);
            }
        }
        for (int i = 0; i < benchUnits.length; i++) {
            updateSlotUnitBoardState(benchUnits[i], ownBoard, PositionSlot.Type.BENCH, true, BoardMath.getBenchPosition(i), side);
        }
    }

    private void updateSlotUnitBoardState(Unit unit, Board ownBoard, PositionSlot.Type slotType, boolean updateTransform, Vector2f position, int side) {
        if (unit != null) {
            if ((unit.getBoard() != ownBoard) && !unit.isRemovedFromBoard()) {
                ownBoard.addObject(unit);
                unit.setActive((ownBoard == planningBoard) || (slotType == PositionSlot.Type.BOARD));
            }
            if (updateTransform) {
                unit.setPosition(position.mult(side));
                unit.setDirection(new Vector2f(0, 1));
            }
        }
    }

    protected boolean canAddUnit() {
        return getFreeBenchIndex() != null;
    }

    protected void clearSlotUnits() {
        Arrays.fill(benchUnits, null);
        for (Unit[] boardUnitsRow : boardUnits) {
            Arrays.fill(boardUnitsRow, null);
        }
    }

    protected void addNewUnit(Unit unit) {
        addNewUnit(unit, null);
    }

    protected void addNewUnit(Unit unit, PositionSlot positionSlot) {
        Integer freeBenchIndex = getFreeBenchIndex();
        benchUnits[freeBenchIndex] = unit;
        unit.setPlayer(this);
        if (positionSlot != null) {
            moveUnit(unit, positionSlot);
        }
    }

    private Integer getFreeBenchIndex() {
        for (int i = 0; i < benchUnits.length; i++) {
            if (benchUnits[i] == null) {
                return i;
            }
        }
        return null;
    }

    public int getUnitsOnBoard() {
        int unitsOnBoard = 0;
        for (Unit[] boardUnitsRow : boardUnits) {
            for (Unit boardUnit : boardUnitsRow) {
                if (boardUnit != null) {
                    unitsOnBoard++;
                }
            }
        }
        return unitsOnBoard;
    }

    public int getUniqueUnitsOnBoardWithTrait(Class<? extends Trait> traitClass) {
        HashSet<Class<? extends Unit>> uniqueUnitClasses = new HashSet<>();
        for (Unit[] boardUnitsRow : boardUnits) {
            for (Unit boardUnit : boardUnitsRow) {
                if ((boardUnit != null) && boardUnit.hasTrait(traitClass)) {
                    uniqueUnitClasses.add(boardUnit.getClass());
                }
            }
        }
        return uniqueUnitClasses.size();
    }

    public void tryMoveUnit(int unitId, PositionSlot newPositionSlot) {
        if (canMoveUnit(unitId, newPositionSlot)) {
            Unit unit = (Unit) game.getObjectById(unitId);
            moveUnit(unit, newPositionSlot);
        }
    }

    private boolean canMoveUnit(int unitId, PositionSlot newPositionSlot) {
        GameObject gameObject = game.getObjectById(unitId);
        // Invalid id
        if (!(gameObject instanceof Unit unit)) {
            return false;
        }
        // Not allowed to move units of other players
        if (unit.getPlayer() != this) {
            return false;
        }
        PositionSlot oldPositionSlot = getUnitPositionSlot(unit);
        // Not a slot unit
        if (oldPositionSlot == null) {
            return false;
        }
        // Not allowed to modify the board if not in planning phase
        if ((game.getPhaseType() != PhaseType.PLANNING) && ((oldPositionSlot.getType() == PositionSlot.Type.BOARD) || (newPositionSlot.getType() == PositionSlot.Type.BOARD))) {
            return false;
        }
        // Not allowed to move a unit from bench to board if no board space remaining
        if ((oldPositionSlot.getType() == PositionSlot.Type.BENCH) && (newPositionSlot.getType() == PositionSlot.Type.BOARD) && (getPositionSlotUnit(newPositionSlot) == null)) {
            Integer remainingUnitsOnBoardCount = getRemainingUnitsOnBoardCount();
            if ((remainingUnitsOnBoardCount != null) && (remainingUnitsOnBoardCount <= 0)) {
                return false;
            }
        }
        return true;
    }

    protected Integer getRemainingUnitsOnBoardCount() {
        Integer maximumUnitsOnBoard = getMaximumUnitsOnBoard();
        return ((maximumUnitsOnBoard != null) ? (maximumUnitsOnBoard - getUnitsOnBoard()) : null);
    }

    public Integer getMaximumUnitsOnBoard() {
        return null;
    }

    private void moveUnit(Unit unit, PositionSlot newPositionSlot) {
        PositionSlot oldPositionSlot = getUnitPositionSlot(unit);
        moveUnit(oldPositionSlot, newPositionSlot);
    }

    protected void moveUnit(PositionSlot oldPositionSlot, PositionSlot newPositionSlot) {
        Unit unitAtOldPosition = getPositionSlotUnit(oldPositionSlot);
        Unit unitAtNewPosition = getPositionSlotUnit(newPositionSlot);
        setPositionSlotUnit(oldPositionSlot, unitAtNewPosition);
        setPositionSlotUnit(newPositionSlot, unitAtOldPosition);
    }

    public PositionSlot getUnitPositionSlot(Unit unit) {
        for (int i = 0; i < benchUnits.length; i++) {
            if (benchUnits[i] == unit) {
                return new PositionSlot(PositionSlot.Type.BENCH, i, 0);
            }
        }
        for (int x = 0; x < boardUnits.length; x++) {
            for (int y = 0; y < boardUnits[y].length; y++) {
                if (boardUnits[x][y] == unit) {
                    return new PositionSlot(PositionSlot.Type.BOARD, x, y);
                }
            }
        }
        return null;
    }

    private Unit getPositionSlotUnit(PositionSlot positionSlot) {
        if (positionSlot.getType() == PositionSlot.Type.BENCH) {
            return benchUnits[positionSlot.getX()];
        } else {
            return boardUnits[positionSlot.getX()][positionSlot.getY()];
        }
    }

    protected void removeSlotUnit(PositionSlot positionSlot, Unit unit) {
        setPositionSlotUnit(positionSlot, null);
        unit.requestRemoveFromBoard();
    }

    private void setPositionSlotUnit(PositionSlot positionSlot, Unit unit) {
        if (positionSlot.getType() == PositionSlot.Type.BENCH) {
            benchUnits[positionSlot.getX()] = unit;
        } else {
            boardUnits[positionSlot.getX()][positionSlot.getY()] = unit;
        }
    }

    public Board getOwnBoard() {
        return game.getBoardByOwnerId(id);
    }

    public Trait getTrait(Class traitClass) {
        return allTraits.stream().filter(trait -> traitClass.isAssignableFrom(trait.getClass())).findFirst().orElse(null);
    }

    public void resetUnits() {
        forEachSlotUnit(Unit::reset);
    }

    public void resetUnitsRemovedFromBoard() {
        forEachSlotUnit(Unit::resetRemoveFromBoard);
    }

    public void resetUnitsHealthAndMana() {
        forEachSlotUnit(Unit::resetHealthAndMana);
    }

    private void forEachSlotUnit(Consumer<Unit> handleUnit) {
        for (Unit[] boardUnitsRow : boardUnits) {
            for (Unit boardUnit : boardUnitsRow) {
                if (boardUnit != null) {
                    handleUnit.accept(boardUnit);
                }
            }
        }
        for (Unit benchUnit : benchUnits) {
            if (benchUnit != null) {
                handleUnit.accept(benchUnit);
            }
        }
    }

    protected void forEachUnitSlot(Consumer<PositionSlotWithUnit> handleSlot, boolean includingBoard) {
        for (int i = 0; i < benchUnits.length; i++) {
            handleSlot.accept(new PositionSlotWithUnit(new PositionSlot(PositionSlot.Type.BENCH, i, 0), benchUnits[i]));
        }
        if (includingBoard) {
            for (int x = 0; x < boardUnits.length; x++) {
                for (int y = 0; y < boardUnits[x].length; y++) {
                    handleSlot.accept(new PositionSlotWithUnit(new PositionSlot(PositionSlot.Type.BOARD, x, y), boardUnits[x][y]));
                }
            }
        }
    }

    @Override
    public List<GameEventListener> getEventListenersToForwardTo() {
        ArrayList<GameEventListener> eventListeners = new ArrayList<>();
        eventListeners.addAll(allTraits);
        eventListeners.addAll(board.getUnits(this));
        return eventListeners;
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        super.writeForClient(outputStream);
        outputStream.writeObjectArray_Nullables(benchUnits, OptimizedBits.SIGNED_INT_TO_16);
        outputStream.writeObjectArray_Array_Nullables(boardUnits, OptimizedBits.SIGNED_INT_TO_8, OptimizedBits.SIGNED_INT_TO_8);
        outputStream.writeObjectList(allTraits, OptimizedBits.SIGNED_INT_TO_32);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        super.readForClient(inputStream);
        benchUnits = inputStream.readObjectArray_Nullables(Unit.class, OptimizedBits.SIGNED_INT_TO_16);
        boardUnits = inputStream.readObjectArray_Array_Nullables(Unit.class, OptimizedBits.SIGNED_INT_TO_8, OptimizedBits.SIGNED_INT_TO_8);
        allTraits = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_32);
    }
}
