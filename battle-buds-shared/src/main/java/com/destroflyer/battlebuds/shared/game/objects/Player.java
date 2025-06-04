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
import java.util.function.BiConsumer;
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
    protected Unit[] benchUnits = new Unit[BENCH_SLOTS];
    @Getter
    protected Unit[][] boardUnits = new Unit[BOARD_SLOTS_X][BOARD_SLOTS_Y];
    @Getter
    private ArrayList<Trait> allTraits = new ArrayList<>();

    public void setupBoardForNewPhase() {
        autofillUnitSlots();
        updateSlotUnitsBoardState();
        // Resetting the units needs to be done after adding them to the board, as resetting involves calculations that require the board to be set
        // (Example: A trait bonus for maximum health that looks at the units on the board)
        forEachSlotUnit(Unit::reset, true);
    }

    protected void autofillUnitSlots() {

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

    protected void addNewUnitTo(Unit unit, PositionSlot positionSlot) {
        addNewUnit(unit);
        moveUnit(unit, positionSlot);
    }

    protected void addNewUnit(Unit unit) {
        Integer freeBenchIndex = getFreeBenchIndex();
        setPositionSlotUnit(new PositionSlot(PositionSlot.Type.BENCH, freeBenchIndex, 0), unit);
        unit.setPlayer(this);
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
        if (!canMoveUnit(unitId)) {
            return false;
        }
        // Not allowed to add units to board if not on planning board
        if (!isOnOwnPlanningBoard() && (newPositionSlot.getType() == PositionSlot.Type.BOARD)) {
            return false;
        }
        Unit unit = (Unit) game.getObjectById(unitId);
        PositionSlot oldPositionSlot = getUnitPositionSlot(unit);
        // Not allowed to move a unit from bench to board if no board space remaining
        if ((oldPositionSlot.getType() == PositionSlot.Type.BENCH) && (newPositionSlot.getType() == PositionSlot.Type.BOARD) && (getPositionSlotUnit(newPositionSlot) == null)) {
            Integer remainingUnitsOnBoardCount = getRemainingUnitsOnBoardCount();
            if ((remainingUnitsOnBoardCount != null) && (remainingUnitsOnBoardCount <= 0)) {
                return false;
            }
        }
        return true;
    }

    public boolean canMoveUnit(int unitId) {
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
        // Not allowed to move board units if not on planning board
        if (!isOnOwnPlanningBoard() && (oldPositionSlot.getType() == PositionSlot.Type.BOARD)) {
            return false;
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

    public Unit getPositionSlotUnit(PositionSlot positionSlot) {
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
        if (unit != null) {
            updateSlotUnitBoardState(unit, positionSlot);
        }
    }

    public void updateSlotUnitsBoardState() {
        forEachUnitSlot((positionSlot, unit) -> {
            if (unit != null) {
                updateSlotUnitBoardState(unit, positionSlot);
            }
        }, true);
    }

    private void updateSlotUnitBoardState(Unit unit, PositionSlot positionSlot) {
        Board ownBoard = getOwnBoard();
        int side = ((ownBoard.getOwners().indexOf(this) == 0) ? 1 : -1);
        unit.setPosition(BoardMath.getSlotPosition(positionSlot).mult(side));
        unit.setDirection(new Vector2f(0, 1));
        unit.setActive(isOnOwnPlanningBoard() || (positionSlot.getType() == PositionSlot.Type.BOARD));
        ownBoard.tryAddObject(unit);
    }

    public Board getOwnBoard() {
        return game.getBoardByOwnerId(id);
    }

    public Trait getTrait(Class traitClass) {
        return allTraits.stream().filter(trait -> traitClass.isAssignableFrom(trait.getClass())).findFirst().orElse(null);
    }

    public void forEachSlotUnit(Consumer<Unit> handleUnit, boolean includingBoard) {
        for (Unit benchUnit : benchUnits) {
            if (benchUnit != null) {
                handleUnit.accept(benchUnit);
            }
        }
        if (includingBoard) {
            for (Unit[] boardUnitsRow : boardUnits) {
                for (Unit boardUnit : boardUnitsRow) {
                    if (boardUnit != null) {
                        handleUnit.accept(boardUnit);
                    }
                }
            }
        }
    }

    protected void forEachUnitSlot(BiConsumer<PositionSlot, Unit> handleSlot, boolean includingBoard) {
        for (int i = 0; i < benchUnits.length; i++) {
            handleSlot.accept(new PositionSlot(PositionSlot.Type.BENCH, i, 0), benchUnits[i]);
        }
        if (includingBoard) {
            for (int x = 0; x < boardUnits.length; x++) {
                for (int y = 0; y < boardUnits[x].length; y++) {
                    handleSlot.accept(new PositionSlot(PositionSlot.Type.BOARD, x, y), boardUnits[x][y]);
                }
            }
        }
    }

    public boolean isOnOwnPlanningBoard() {
        return ((board instanceof PlanningBoard) && (board.getOwners().getFirst() == this));
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
