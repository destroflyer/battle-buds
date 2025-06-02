package com.destroflyer.battlebuds.shared.game.objects.players;

import com.destroflyer.battlebuds.shared.game.*;
import com.destroflyer.battlebuds.shared.game.items.Items;
import com.destroflyer.battlebuds.shared.game.objects.PickUpObject;
import com.destroflyer.battlebuds.shared.game.objects.Player;
import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.OptimizedBits;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ActualPlayer extends Player {

    protected ActualPlayer() {
        hasDynamicName = true;
        hasDynamicVisualName = true;
        baseMaximumHealth = 20f;
        baseMovementSpeed = 8;
    }
    private static final int MAXIMUM_LEVEL = 10;
    private static final int[] REQUIRED_EXPERIENCE_FOR_NEXT_LEVEL = new int[] {
        // TODO: For now, start with 4 instead of 2 XP required from level 1 to 2 (because of the free 2 XP from the very first planning phase) (but it shows as 2/4 therefore currently)
        4, 2, 6, 10, 20, 36, 48, 72, 84
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
    private static final int COMPATIBLE_UNITS_FOR_UPGRADE = 3;
    public static final int MAXIMUM_ITEM_COUNT = 10;
    public static final int DECISION_OPTIONS_COUNT = 3;
    @Getter
    private int level = 1;
    @Getter
    private int experience;
    @Getter
    @Setter
    private int gold;
    @Getter
    @Setter
    private Unit[] shopUnits = new Unit[Game.SHOP_SLOTS];
    @Getter
    private ArrayList<Item> items = new ArrayList<>();
    @Getter
    private ArrayList<Augment> augments = new ArrayList<>();
    private ArrayList<Decision> decisions = new ArrayList<>();

    @Override
    public void update(float tpf) {
        super.update(tpf);
        Board ownBoard = getOwnBoard();
        if (ownBoard == planningBoard) {
            tryUpgradeUnits();
        }
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

    private boolean canPayGold(int gold) {
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

    private boolean canSellUnit(int unitId) {
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

    private boolean canBuyUnit(Unit unit) {
        return canPayGold(unit.getCost()) && canAddUnit();
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

    @Override
    public Integer getMaximumUnitsOnBoard() {
        return level;
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

    private boolean canUseItem(int benchItemIndex, int unitId) {
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

    private boolean canDecide(int decisionOptionIndex) {
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

    public void dropGoldForSelf(int minimumGold, int maximumGold) {
        dropGoldFor(minimumGold, maximumGold, this);
    }

    public void dropGoldForSelf(int gold) {
        dropGoldFor(gold, this);
    }

    public void dropForSelf(PickUpObject pickUpObject) {
        dropFor(pickUpObject, this);
    }

    @Override
    public List<GameEventListener> getEventListenersToForwardTo() {
        List<GameEventListener> eventListeners = super.getEventListenersToForwardTo();
        eventListeners.addAll(augments);
        return eventListeners;
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        super.writeForClient(outputStream);
        outputStream.writeBits(level, OptimizedBits.SIGNED_INT_TO_16);
        outputStream.writeBits(experience, OptimizedBits.SIGNED_INT_TO_128);
        outputStream.writeBits(gold, OptimizedBits.SIGNED_INT_TO_2048);
        outputStream.writeObjectArray_Nullables(shopUnits, OptimizedBits.SIGNED_INT_TO_8);
        outputStream.writeObjectList(items, OptimizedBits.SIGNED_INT_TO_16);
        outputStream.writeObjectList(augments, OptimizedBits.SIGNED_INT_TO_8);
        outputStream.writeObjectList(decisions, OptimizedBits.SIGNED_INT_TO_64);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        super.readForClient(inputStream);
        level = inputStream.readBits(OptimizedBits.SIGNED_INT_TO_16);
        experience = inputStream.readBits(OptimizedBits.SIGNED_INT_TO_128);
        gold = inputStream.readBits(OptimizedBits.SIGNED_INT_TO_2048);
        shopUnits = inputStream.readObjectArray_Nullables(Unit.class, OptimizedBits.SIGNED_INT_TO_8);
        items = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_16);
        augments = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_8);
        decisions = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_64);
    }
}
