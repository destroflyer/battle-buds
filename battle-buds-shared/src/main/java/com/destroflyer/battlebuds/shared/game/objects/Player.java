package com.destroflyer.battlebuds.shared.game.objects;

import com.destroflyer.battlebuds.shared.Util;
import com.destroflyer.battlebuds.shared.game.*;
import com.destroflyer.battlebuds.shared.game.Augment;
import com.destroflyer.battlebuds.shared.game.boards.PlanningBoard;
import com.destroflyer.battlebuds.shared.game.items.Items;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class Player extends Character implements GameEventListener {

    private Player() {
        hasDynamicName = true;
        hasDynamicVisualName = true;
        baseMaximumHealth = 100f;
        baseMovementSpeed = 8;
    }

    public Player(Integer accountId) {
        this();
        this.accountId = accountId;
        for (Class<? extends Trait> traitClass : Traits.CLASSES) {
            Trait trait = Util.createObjectByClass(traitClass);
            trait.setPlayer(this);
            allTraits.add(trait);
        }
    }
    private static final int MAXIMUM_LEVEL = 10;
    private static final int[] REQUIRED_EXPERIENCE_FOR_NEXT_LEVEL = new int[] {
        2, 2, 6, 10, 20, 36, 48, 72, 84
    };
    private static final int[][] SHOP_PROBABILITIES = new int[][] {
        new int[] { 100, 0, 0, 0, 0 },
        new int[] { 100, 0, 0, 0, 0 },
        new int[] { 75, 25, 0, 0, 0 },
        new int[] { 55, 30, 15, 0, 0 },
        new int[] { 45, 33, 20, 2, 0 },
        new int[] { 30, 40, 25, 5, 0 },
        new int[] { 19, 30, 40, 10, 1 },
        new int[] { 17, 24, 32, 24, 3 },
        new int[] { 15, 18, 25, 30, 12 },
        new int[] { 5, 10, 20, 40, 25 },
        new int[] { 1, 2, 12, 50, 35 },
    };
    public static final int BENCH_SLOTS = 9;
    public static final int BOARD_SLOTS_X = 7;
    public static final int BOARD_SLOTS_Y = 4;
    private static final int COMPATIBLE_UNITS_FOR_UPGRADE = 3;
    public static final int MAXIMUM_ITEM_COUNT = 10;
    public static final int DECISION_OPTIONS_COUNT = 3;
    @Getter
    private Integer accountId;
    @Getter
    private int level = 1;
    @Getter
    private int experience;
    @Getter
    @Setter
    private int gold;
    @Getter
    @Setter
    private PlanningBoard planningBoard;
    @Getter
    private Unit[] benchUnits = new Unit[BENCH_SLOTS];
    @Getter
    private Unit[][] boardUnits = new Unit[BOARD_SLOTS_X][BOARD_SLOTS_Y];
    @Getter
    @Setter
    private Unit[] shopUnits = new Unit[Game.SHOP_SLOTS];
    @Getter
    private ArrayList<Trait> allTraits = new ArrayList<>();
    @Getter
    private ArrayList<Item> items = new ArrayList<>();
    @Getter
    private ArrayList<Augment> augments = new ArrayList<>();
    private ArrayList<Decision> decisions = new ArrayList<>();
    @Getter
    private Player watchedPlayer;

    @Override
    public void update(float tpf) {
        super.update(tpf);
        Board ownBoard = getOwnBoard();
        if (ownBoard != null) {
            updateSlotUnitsBoardStates(false);
            tryUpgradeUnits();
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

    public void tryWatchPlayer(int playerId) {
        Player player = game.getPlayerById(playerId);
        if (player != null) {
            watchPlayer(player);
        }
    }

    public void watchPlayer(Player player) {
        watchedPlayer = player;
        Board watchedBoard = getWatchedBoard();
        if (watchedBoard != null) {
            watchedBoard.addObject(this);
        }
    }

    public Board getWatchedBoard() {
        return watchedPlayer.getOwnBoard();
    }

    public void tryBuyExperience() {
        if (!isMaximumLevel()) {
            int buyExperienceCost = getBuyExperienceCost();
            if (canPayGold(buyExperienceCost)) {
                payGold(buyExperienceCost);
                addExperience(4);
            }
        }
    }

    public void tryBuyReroll() {
        int rerollCost = getRerollCost();
        if (canPayGold(rerollCost)) {
            payGold(rerollCost);
            game.reroll(this);
        }
    }

    public int getBuyExperienceCost() {
        return 4;
    }

    public int getRerollCost() {
        return 2;
    }

    public void addExperience(int bonusExperience) {
        this.experience += bonusExperience;
        int requiredExperienceForNextLevel = getRequiredExperienceForNextLevel();
        while (this.experience >= requiredExperienceForNextLevel) {
            this.experience -= requiredExperienceForNextLevel;
            this.level++;
            if (isMaximumLevel()) {
                this.experience = 0;
                break;
            }
        }
    }

    public Integer getRequiredExperienceForNextLevel() {
        return isMaximumLevel() ? null : REQUIRED_EXPERIENCE_FOR_NEXT_LEVEL[level - 1];
    }

    private boolean isMaximumLevel() {
        return level >= MAXIMUM_LEVEL;
    }

    public int getGoldIncome() {
        // TODO: Streak gold
        return getPhaseGoldIncome() + getGoldInterest();
    }

    private int getPhaseGoldIncome() {
        int phase = game.getPhase();
        if (phase < 5) {
            return 2;
        } else if (phase < 7) {
            return 3;
        } else if (phase < 9) {
            return 4;
        } else {
            return 5;
        }
    }

    public int getGoldInterest() {
        return Math.min(gold / 10, getMaximumInterestGold());
    }

    public int getMaximumInterestGold() {
        int maximumInterestGold = 5;
        for (Augment augment : augments) {
            maximumInterestGold += augment.getBonusMaximumInterestGold();
        }
        return maximumInterestGold;
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

    public void payGold(int gold) {
        this.gold -= gold;
    }

    public boolean canPayGold(int gold) {
        return this.gold >= gold;
    }

    public int[] getShopProbabilities() {
        return SHOP_PROBABILITIES[level - 1];
    }

    public void tryBuyUnit(int shopSlotIndex) {
        Unit unit = shopUnits[shopSlotIndex];
        if ((unit != null) && canBuyUnit(unit)) {
            shopUnits[shopSlotIndex] = null;
            addNewUnit(unit);
        }
    }

    public void trySellUnit(int unitId) {
        if (canSellUnit(unitId)) {
            Unit unit = (Unit) game.getObjectById(unitId);
            unit.reset();
            removeSlotUnit(getUnitPositionSlot(unit), unit);
            addGold(unit.getCost());
            // TODO: Put back units in pool
            // TODO: Put back items on bench
        }
    }

    public boolean canSellUnit(int unitId) {
        Unit unit = (Unit) game.getObjectById(unitId);
        // Not allowed to sell units that don't exist
        if (unit == null) {
            return false;
        }
        // Not allowed to sell units of other players
        if (unit.getPlayer() != this) {
            return false;
        }
        PositionSlot positionSlot = getUnitPositionSlot(unit);
        // Not allowed to sell non-slot units
        if (positionSlot == null) {
            return false;
        }
        // Not allowed to sell board units during battle
        return (getOwnBoard() == planningBoard) || (positionSlot.getType() != PositionSlot.Type.BOARD);
    }

    public boolean canBuyUnit(Unit unit) {
        return canPayGold(unit.getCost()) && canAddUnit();
    }

    public boolean canAddUnit() {
        return getFreeBenchIndex() != null;
    }

    public void clearUnits() {
        Arrays.fill(benchUnits, null);
        for (Unit[] boardUnitsRow : boardUnits) {
            Arrays.fill(boardUnitsRow, null);
        }
    }

    public void addNewUnit(Unit unit) {
        addNewUnit(unit, null);
    }

    public void addNewUnit(Unit unit, PositionSlot positionSlot) {
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

    private void tryUpgradeUnits() {
        boolean includingBoard = (getOwnBoard() == planningBoard);
        AtomicReference<Boolean> tryToUpgrade = new AtomicReference<>(true);
        while (tryToUpgrade.get()) {
            tryToUpgrade.set(false);
            forEachUnitSlot((slotWithUnit) -> {
                if (slotWithUnit.getUnit() != null) {
                    if (tryUpgradeUnit(slotWithUnit.getUnit(), includingBoard)) {
                        tryToUpgrade.set(true);
                    }
                }
            }, includingBoard);
        }
    }

    private boolean tryUpgradeUnit(Unit unit, boolean includingBoard) {
        ArrayList<PositionSlotWithUnit> compatibleSlotsWithUnits = new ArrayList<>();
        forEachUnitSlot((slotWithUnit) -> {
            if ((slotWithUnit.getUnit() != null) && (slotWithUnit.getUnit() != unit) && slotWithUnit.getUnit().isCompatibleWith(unit)) {
                compatibleSlotsWithUnits.add(slotWithUnit);
            }
        }, includingBoard);
        if (compatibleSlotsWithUnits.size() >= (COMPATIBLE_UNITS_FOR_UPGRADE - 1)) {
            for (int i = 0; i < (COMPATIBLE_UNITS_FOR_UPGRADE - 1); i++) {
                PositionSlotWithUnit compatibleSlotWithUnit = compatibleSlotsWithUnits.get(i);
                removeSlotUnit(compatibleSlotWithUnit.getPositionSlot(), compatibleSlotWithUnit.getUnit());
            }
            unit.setStars(unit.getStars() + 1);
            return true;
        }
        return false;
    }

    public void fillBoardFromBenchIfSpaceLeft() {
        int slotsToFill = getRemainingUnitsOnBoardCount();
        int benchIndex = 0;
        for (int y = (BOARD_SLOTS_Y - 1); y >= 0; y--) {
            for (int x = 0; x < BOARD_SLOTS_X; x++) {
                if ((slotsToFill > 0) && (boardUnits[x][y] == null)) {
                    for (; benchIndex < benchUnits.length; benchIndex++) {
                        Unit benchUnit = benchUnits[benchIndex];
                        if (benchUnit != null) {
                            moveUnit(new PositionSlot(PositionSlot.Type.BENCH, benchIndex, 0), new PositionSlot(PositionSlot.Type.BOARD, x, y));
                            slotsToFill--;
                            break;
                        }
                    }
                }
            }
        }
    }

    private int getRemainingUnitsOnBoardCount() {
        return getMaximumUnitsOnBoard() - getUnitsOnBoard();
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

    private int getMaximumUnitsOnBoard() {
        return level;
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
            return getRemainingUnitsOnBoardCount() > 0;
        }
        return true;
    }

    public void moveUnit(Unit unit, PositionSlot newPositionSlot) {
        PositionSlot oldPositionSlot = getUnitPositionSlot(unit);
        moveUnit(oldPositionSlot, newPositionSlot);
    }

    private void moveUnit(PositionSlot oldPositionSlot, PositionSlot newPositionSlot) {
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

    private void removeSlotUnit(PositionSlot positionSlot, Unit unit) {
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

    private void forEachUnitSlot(Consumer<PositionSlotWithUnit> handleSlot, boolean includingBoard) {
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
    public float getMovementSpeed() {
        float movementSpeed = super.getMovementSpeed();
        if (board.getObjects().stream().anyMatch(gameObject -> (gameObject instanceof PickUpObject pickUpObject) && pickUpObject.canBePickupedBy(this))) {
            movementSpeed *= 2;
        }
        return movementSpeed;
    }

    public void tryCombineBenchItems(int itemIndex1, int itemIndex2) {
        if (canCombineBenchItems(itemIndex1, itemIndex2)) {
            Item ingredient1 = items.get(itemIndex1);
            Item ingredient2 = items.get(itemIndex2);
            removeItem(ingredient1);
            removeItem(ingredient2);
            addItem(Items.createCombinedItem(ingredient1, ingredient2));
        }
    }

    private boolean canCombineBenchItems(int itemIndex1, int itemIndex2) {
        // Invalid bench index
        if ((itemIndex1 >= items.size()) || (itemIndex2 >= items.size()) || (itemIndex1 == itemIndex2)) {
            return false;
        }
        Item ingredient1 = items.get(itemIndex1);
        Item ingredient2 = items.get(itemIndex2);
        return (Items.getCombinedItemClass(ingredient1, ingredient2) != null);
    }

    public void tryUseItem(int benchItemIndex, int unitId) {
        if (canUseItem(benchItemIndex, unitId)) {
            Item item = items.get(benchItemIndex);
            Unit unit = (Unit) game.getObjectById(unitId);

            removeItem(item);

            Item unitFirstComponent = unit.getFirstComponentItem();
            if (item.isComponent() && (unitFirstComponent != null)) {
                unit.removeItem(unitFirstComponent);
                unit.addItem(Items.createCombinedItem(item, unitFirstComponent));
            } else {
                unit.addItem(item);
            }
        }
    }

    public boolean canUseItem(int benchItemIndex, int unitId) {
        // Invalid bench index
        if (benchItemIndex >= items.size()) {
            return false;
        }
        Item item = items.get(benchItemIndex);
        GameObject object = game.getObjectById(unitId);
        // Invalid unit id
        if (!(object instanceof Unit unit)) {
            return false;
        }
        // Not allowed to use items on non-owned units
        if (unit.getPlayer() != this) {
            return false;
        }
        Item unitFirstComponent = unit.getFirstComponentItem();
        if (item.isComponent() && (unitFirstComponent != null)) {
            return (Items.getCombinedItemClass(item, unitFirstComponent) != null);
        } else {
            return unit.canAddItem();
        }
    }

    public void tryAddItem(Item item) {
        if (canAddItem()) {
            addItem(item);
        }
    }

    public boolean canAddItem() {
        return items.size() < MAXIMUM_ITEM_COUNT;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    private void removeItem(Item item) {
        items.remove(item);
    }

    public void addDecision(Decision decision) {
        decisions.add(decision);
    }

    public void tryDecide(int decisionOptionIndex) {
        if (canDecide(decisionOptionIndex)) {
            decide(decisionOptionIndex);
        }
    }

    public boolean canDecide(int decisionOptionIndex) {
        Decision nextDecision = getNextDecision();
        return ((nextDecision != null) && (decisionOptionIndex < nextDecision.getOptions().size()));
    }

    public void decide(int decisionOptionIndex) {
        Decision nextDecision = getNextDecision();
        decisions.remove(nextDecision);
        DecisionOption decisionOption = nextDecision.getOptions().get(decisionOptionIndex);
        decisionOption.select();
    }

    public Decision getNextDecision() {
        return ((decisions.size() > 0) ? decisions.getFirst() : null);
    }

    public void addAugment(Augment augment) {
        augment.setPlayer(this);
        augments.add(augment);
        augment.onAdd();
    }

    @Override
    public List<? extends GameEventListener> getEventListenersToForwardTo() {
        ArrayList<GameEventListener> eventListeners = new ArrayList<>();
        eventListeners.addAll(augments);
        eventListeners.addAll(allTraits);
        eventListeners.addAll(board.getUnits(this));
        return eventListeners;
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        super.writeForClient(outputStream);
        outputStream.writeBits_Nullable(accountId, OptimizedBits.SIGNED_INT_TO_1048576);
        outputStream.writeBits(level, OptimizedBits.SIGNED_INT_TO_16);
        outputStream.writeBits(experience, OptimizedBits.SIGNED_INT_TO_128);
        outputStream.writeBits(gold, OptimizedBits.SIGNED_INT_TO_2048);
        outputStream.writeObjectArray_Nullables(benchUnits, OptimizedBits.SIGNED_INT_TO_16);
        outputStream.writeObjectArray_Array_Nullables(boardUnits, OptimizedBits.SIGNED_INT_TO_8, OptimizedBits.SIGNED_INT_TO_8);
        outputStream.writeObjectArray_Nullables(shopUnits, OptimizedBits.SIGNED_INT_TO_8);
        outputStream.writeObjectList(allTraits, OptimizedBits.SIGNED_INT_TO_32);
        outputStream.writeObjectList(items, OptimizedBits.SIGNED_INT_TO_16);
        outputStream.writeObjectList(augments, OptimizedBits.SIGNED_INT_TO_8);
        outputStream.writeObjectList(decisions, OptimizedBits.SIGNED_INT_TO_64);
        // Watched player can be null for neutral players (during phases in which they are not participating)
        outputStream.writeObject_Nullable(watchedPlayer);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        super.readForClient(inputStream);
        accountId = inputStream.readBits_Nullable(OptimizedBits.SIGNED_INT_TO_1048576);
        level = inputStream.readBits(OptimizedBits.SIGNED_INT_TO_16);
        experience = inputStream.readBits(OptimizedBits.SIGNED_INT_TO_128);
        gold = inputStream.readBits(OptimizedBits.SIGNED_INT_TO_2048);
        benchUnits = inputStream.readObjectArray_Nullables(Unit.class, OptimizedBits.SIGNED_INT_TO_16);
        boardUnits = inputStream.readObjectArray_Array_Nullables(Unit.class, OptimizedBits.SIGNED_INT_TO_8, OptimizedBits.SIGNED_INT_TO_8);
        shopUnits = inputStream.readObjectArray_Nullables(Unit.class, OptimizedBits.SIGNED_INT_TO_8);
        allTraits = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_32);
        items = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_16);
        augments = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_8);
        decisions = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_64);
        watchedPlayer = inputStream.readObject_Nullable();
    }
}
