package com.destroflyer.battlebuds.shared.game.objects;

import com.destroflyer.battlebuds.shared.game.*;
import com.destroflyer.battlebuds.shared.game.boards.PlanningBoard;
import com.destroflyer.battlebuds.shared.game.objects.players.ActualPlayer;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.OptimizedBits;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Unit extends Character {

    public static final float ATTACK_RANGE_MELEE = 4;
    public static final float ATTACK_RANGE_RANGED = 4 * ATTACK_RANGE_MELEE;
    public static final int MAXIMUM_ITEM_COUNT = 3;

    @Getter
    protected int cost;
    @Getter
    protected Class[] baseTraits;
    @Getter
    @Setter
    protected Float baseMaximumMana;
    @Getter
    @Setter
    protected Float baseInitialMana;
    @Getter
    @Setter
    private Float currentMana;
    @Getter
    private Spell spell;
    protected float baseHealthRegeneration;
    protected float baseManaRegeneration;
    protected float baseArmor;
    protected float baseMagicResistance;
    protected float baseAttackDamage = 150;
    protected float baseAttackSpeed = 0.75f;
    protected float baseAttackRange = ATTACK_RANGE_MELEE;
    protected float baseAbilityPower;
    protected float baseDodgeChance;
    protected float baseCritChance = 0.25f;
    protected float baseCritDamage = 1.3f;
    protected float baseOmnivamp;
    protected float baseDamageDealtAmplification = 1;
    protected float baseDamageTakenAmplification = 1;
    @Getter
    @Setter
    private Player player;
    @Getter
    @Setter
    private int stars = 1;
    @Getter
    @Setter
    protected boolean active;
    @Getter
    private Unit attackTarget;
    private float timeUntilNextAttack;
    private float remainingCastDuration;
    private ArrayList<Buff> buffs = new ArrayList<>();
    @Getter
    private ArrayList<Item> items = new ArrayList<>();

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (active) {
            regenerateHealth(tpf);
            regenerateMana(tpf);
            updateBuffs(tpf);
            if (!isForcedMoving()) {
                updateAttackTarget();
                tryAttack(tpf);
                tryCastSpell(tpf);
            }
        }
    }

    private void regenerateHealth(float tpf) {
        heal(tpf * getHealthRegeneration());
    }

    private void regenerateMana(float tpf) {
        addMana(tpf * getManaRegeneration());
    }

    private void updateAttackTarget() {
        if ((attackTarget != null) && (attackTarget.getBoard() == null)) {
            attackTarget = null;
        }
        if (attackTarget == null) {
            attackTarget = board.getClosestActiveUnit(position, unit -> unit.getPlayer() != player);
        }
        if (attackTarget != null) {
            setTargetPosition(attackTarget.getPosition(), getAttackRange());
            lookAtTargetPosition();
        } else {
            setTargetPosition(null, 0);
        }
    }

    private void tryAttack(float tpf) {
        timeUntilNextAttack -= tpf;
        if ((timeUntilNextAttack <= 0) && (attackTarget != null) && canAttack()) {
            float attackDuration = (1 / getAttackSpeed());
            timeUntilNextAttack = attackDuration;
            Unit assignedAttackTarget = attackTarget;
            game.enqueue(() -> {
                // Both attacker and target could've been removed in the meantime
                if ((board != null) && (assignedAttackTarget.getBoard() != null) && (Math.random() >= assignedAttackTarget.getDodgeChance())) {
                    attack(assignedAttackTarget);
                }
            }, (attackDuration / 2));
        }
    }

    private boolean canAttack() {
        return isInAttackTargetRange() && !isCasting();
    }

    private void attack(Unit target) {
        dealDamage(target, DamageType.PHYSICAL, getAttackDamage(), true);
        addMana(10);
        for (Trait trait : player.getAllTraits()) {
            trait.onAllyUnitAttack(this, target);
        }
    }

    private boolean isCasting() {
        return remainingCastDuration > 0;
    }

    private boolean isInAttackTargetRange() {
        float attackRange = getAttackRange();
        return (attackTarget.getPosition().distanceSquared(position) <= (attackRange * attackRange));
    }

    private void tryCastSpell(float tpf) {
        remainingCastDuration = Math.max(0, remainingCastDuration - tpf);
        if ((spell != null) && (currentMana >= getMaximumMana()) && spell.isCastable()) {
            currentMana = 0f;
            remainingCastDuration = spell.getCastDuration();
            spell.cast();
        }
    }

    public void dealDamage(Unit target, DamageType damageType, float damage, boolean canCrit) {
        float preMitigationDamage = damage * getDamageDealtAmplification();
        if (canCrit && (Math.random() < getCritChance())) {
            preMitigationDamage *= getCritDamage();
        }
        float postMitigationDamage = preMitigationDamage * target.getDamageTakenAmplification() * getResistanceDamageMitigationFactor(target.getResistance(damageType));
        target.takeDamage(preMitigationDamage, postMitigationDamage);
        heal(getOmnivamp() * postMitigationDamage);
    }

    private float getResistance(DamageType damageType) {
        return switch (damageType) {
            case PHYSICAL -> getArmor();
            case MAGIC -> getMagicResistance();
            default -> 0;
        };
    }

    private static float getResistanceDamageMitigationFactor(float resistance) {
        if (resistance >= 0) {
            return (100 / (100 + resistance));
        } else {
            return (2 - (100 / (100 - resistance)));
        }
    }

    @Override
    public void takeDamage(float preMitigationDamage, float postMitigationDamage) {
        super.takeDamage(preMitigationDamage, postMitigationDamage);
        addMana(Math.min(0.06f * preMitigationDamage, 42.5f));
    }

    public void addMana(float mana) {
        if (currentMana != null) {
            currentMana += mana;
        }
    }

    @Override
    public Float getMaximumHealth() {
        return getStatValue(baseMaximumHealth, statModifier -> statModifier.getBonusMaximumHealthPercent(this), statModifier -> statModifier.getBonusMaximumHealthFlat(this), 0f, null, true);
    }

    public Float getHealthRegeneration() {
        return getStatValue(baseHealthRegeneration, statModifier -> statModifier.getBonusHealthRegenerationPercent(this), statModifier -> statModifier.getBonusHealthRegenerationFlat(this), 0f, null, false);
    }

    public Float getMaximumMana() {
        return getStatValue(baseMaximumMana, statModifier -> statModifier.getBonusMaximumManaPercent(this), statModifier -> statModifier.getBonusMaximumManaFlat(this), 0f, null, false);
    }

    public Float getInitialMana() {
        return getStatValue(baseInitialMana, statModifier -> statModifier.getBonusInitialManaPercent(this), statModifier -> statModifier.getBonusInitialManaFlat(this), 0f, getMaximumMana(), false);
    }

    public Float getManaRegeneration() {
        return getStatValue(baseManaRegeneration, statModifier -> statModifier.getBonusManaRegenerationPercent(this), statModifier -> statModifier.getBonusManaRegenerationFlat(this), 0f, null, false);
    }

    public float getArmor() {
        return getStatValue(baseArmor, statModifier -> statModifier.getBonusArmorPercent(this), statModifier -> statModifier.getBonusArmorFlat(this), null, null, false);
    }

    public float getMagicResistance() {
        return getStatValue(baseMagicResistance, statModifier -> statModifier.getBonusMagicResistancePercent(this), statModifier -> statModifier.getBonusMagicResistanceFlat(this), null, null, false);
    }

    public float getAttackDamage() {
        return getStatValue(baseAttackDamage, statModifier -> statModifier.getBonusAttackDamagePercent(this), statModifier -> statModifier.getBonusAttackDamageFlat(this), 0f, null, true);
    }

    public float getAttackSpeed() {
        return getStatValue(baseAttackSpeed, statModifier -> statModifier.getBonusAttackSpeedPercent(this), statModifier -> statModifier.getBonusAttackSpeedFlat(this), 0f, null, false);
    }

    public float getAttackRange() {
        return getStatValue(baseAttackRange, statModifier -> statModifier.getBonusAttackRangePercent(this), statModifier -> statModifier.getBonusAttackRangeFlat(this), ATTACK_RANGE_MELEE, null, false);
    }

    public float getDodgeChance() {
        return getStatValue(baseDodgeChance, statModifier -> statModifier.getBonusDodgeChancePercent(this), statModifier -> statModifier.getBonusDodgeChanceFlat(this), 0f, 1f, false);
    }

    public float getAbilityPower() {
        return getStatValue(baseAbilityPower, statModifier -> statModifier.getBonusAbilityPowerPercent(this), statModifier -> statModifier.getBonusAbilityPowerFlat(this), 0f, null, false);
    }

    public float getCritChance() {
        return getStatValue(baseCritChance, statModifier -> statModifier.getBonusCritChancePercent(this), statModifier -> statModifier.getBonusCritChanceFlat(this), 0f, 1f, false);
    }

    public float getCritDamage() {
        return getStatValue(baseCritDamage, statModifier -> statModifier.getBonusCritDamagePercent(this), statModifier -> statModifier.getBonusCritDamageFlat(this), 1f, 2f, false);
    }

    public float getOmnivamp() {
        return getStatValue(baseOmnivamp, statModifier -> statModifier.getBonusOmnivampPercent(this), statModifier -> statModifier.getBonusOmnivampFlat(this), 0f, null, false);
    }

    public float getDamageDealtAmplification() {
        return getStatValue(baseDamageDealtAmplification, statModifier -> statModifier.getBonusDamageDealtAmplificationPercent(this), statModifier -> statModifier.getBonusDamageDealtAmplificationFlat(this), 0f, null, false);
    }

    public float getDamageTakenAmplification() {
        return getStatValue(baseDamageTakenAmplification, statModifier -> statModifier.getBonusDamageTakenAmplificationPercent(this), statModifier -> statModifier.getBonusDamageTakenAmplificationFlat(this), 0f, null, false);
    }

    @Override
    public float getMovementSpeed() {
        return getStatValue(baseMovementSpeed, statModifier -> statModifier.getBonusMovementPercent(this), statModifier -> statModifier.getBonusMovementFlat(this), 0f, null, false);
    }

    private Float getStatValue(Float baseValue, Function<StatModifier, Float> getBonusValuePercent, Function<StatModifier, Float> getBonusValueFlat, Float minimumValue, Float maximumValue, boolean enhancedByStars) {
        if (baseValue == null) {
            return null;
        }
        final float[] bonusValuePercent = {0};
        final float[] bonusValueFlat = {0};
        Consumer<List<? extends StatModifier>> applyStatModifiers = (statModifiers -> {
            for (StatModifier statModifier : statModifiers) {
                bonusValuePercent[0] += getBonusValuePercent.apply(statModifier);
                bonusValueFlat[0] += getBonusValueFlat.apply(statModifier);
            }
        });
        if (isAffectingOrAffectedByBonuses()) {
            applyStatModifiers.accept(player.getAllTraits());
            if (player instanceof ActualPlayer actualPlayer) {
                applyStatModifiers.accept(actualPlayer.getAugments());
            }
        }
        applyStatModifiers.accept(items);
        applyStatModifiers.accept(buffs);
        float finalValue = (baseValue + (baseValue * bonusValuePercent[0]) + bonusValueFlat[0]);
        if ((minimumValue != null) && (finalValue < minimumValue)) {
            finalValue = minimumValue;
        }
        if ((maximumValue != null) && (finalValue > maximumValue)) {
            finalValue = maximumValue;
        }
        if (enhancedByStars) {
            for (int i = 0; i < (stars - 1); i++) {
                finalValue *= 1.8f;
            }
        }
        return finalValue;
    }

    public boolean isAffectingOrAffectedByBonuses() {
        // Shop and neutral units
        if (player == null) {
            return false;
        }
        if (board instanceof PlanningBoard) {
            PositionSlot positionSlot = player.getUnitPositionSlot(this);
            // Position slot can be null if:
            // - It's simply a non-slot unit, potentially spawned by a trait
            // - The unit was just sold (Already removed from the slot, but the removal from board was just requested and not executed yet)
            return ((positionSlot != null) && (positionSlot.getType() == PositionSlot.Type.BOARD));
        } else {
            return active;
        }
    }

    @Override
    protected ActionState calculateActionState() {
        if (active && board.isFinished()) {
            return ActionState.DANCE;
        }
        if (isCasting()) {
            return ActionState.CAST;
        }
        if ((attackTarget != null) && isInAttackTargetRange()) {
            return canAttack() ? ActionState.ATTACK : ActionState.IDLE;
        }
        return super.calculateActionState();
    }

    public void setSpell(Spell spell) {
        spell.setCaster(this);
        this.spell = spell;
    }

    public boolean isCompatibleWith(Unit unit) {
        return (getClass() == unit.getClass()) && (stars == unit.getStars());
    }

    public boolean hasTrait(Class<? extends Trait> traitClass) {
        return getTraits().stream().anyMatch(trait -> traitClass.isAssignableFrom(trait.getClass()));
    }

    public ArrayList<Trait> getTraits() {
        ArrayList<Trait> traits = new ArrayList<>(baseTraits.length);
        if (player != null) {
            for (Class traitClass : baseTraits) {
                traits.add(player.getTrait(traitClass));
            }
        }
        return traits;
    }

    @Override
    protected void onDeath() {
        super.onDeath();
        for (Player boardOwner : board.getOwners()) {
            if (boardOwner == player) {
                boardOwner.onAllyUnitDeath(this);
            } else {
                boardOwner.onEnemyUnitDeath(this);
            }
        }
    }

    public void addBuff(Buff buff) {
        addBuff(buff, null);
    }

    public void addBuff(Buff buff, Float duration) {
        List<Buff> existingBuffs = buffs.stream().filter(otherBuff -> otherBuff.getClass() == buff.getClass()).toList();
        if ((buff.getMaximumStacks() != null) && (existingBuffs.size() >= buff.getMaximumStacks())) {
            if (duration != null) {
                Buff buffToRefresh = existingBuffs.stream().min((b1, b2) -> {
                    float difference = b1.getRemainingTime() - b2.getRemainingTime();
                    return ((difference == 0) ? 0 : ((difference < 0) ? -1 : 1));
                }).orElse(null);
                buffToRefresh.setRemainingTime(duration);
            }
        } else {
            buff.setUnit(this);
            buff.setRemainingTime(duration);
            buffs.add(buff);
        }
    }

    public void removeBuff(Buff buff) {
        buffs.remove(buff);
    }

    private void updateBuffs(float tpf) {
        for (Buff buff : buffs.toArray(Buff[]::new)) {
            buff.update(tpf);
        }
    }

    public boolean canAddItem() {
        return items.size() < MAXIMUM_ITEM_COUNT;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public void removeItems() {
        items.clear();
    }

    public Item getFirstComponentItem() {
        for (Item item : items) {
            if (item.isComponent()) {
                return item;
            }
        }
        return null;
    }

    public void dropGoldForOwner(int minimumGold, int maximumGold) {
        ifActualPlayer(player, actualPlayer -> dropGoldFor(minimumGold, maximumGold, actualPlayer));
    }

    public void dropGoldForEnemy(int minimumGold, int maximumGold) {
        ifActualPlayer(getBoardEnemy(), enemyActualPlayer -> dropGoldFor(minimumGold, maximumGold, enemyActualPlayer));
    }

    public void dropGoldForOwner(int gold) {
        ifActualPlayer(player, actualPlayer -> dropGoldFor(gold, actualPlayer));
    }

    public void dropGoldForEnemy(int gold) {
        ifActualPlayer(getBoardEnemy(), enemyActualPlayer -> dropGoldFor(gold, enemyActualPlayer));
    }

    public void dropForOwner(PickupObject pickupObject) {
        ifActualPlayer(player, actualPlayer -> dropFor(pickupObject, actualPlayer));
    }

    public void dropForEnemy(PickupObject pickupObject) {
        ifActualPlayer(getBoardEnemy(), actualPlayerEnemy -> dropFor(pickupObject, actualPlayerEnemy));
    }

    private static void ifActualPlayer(Player player, Consumer<ActualPlayer> handleActualPlayer) {
        if (player instanceof ActualPlayer actualPlayer) {
            handleActualPlayer.accept(actualPlayer);
        }
    }

    private Player getBoardEnemy() {
        return board.getOwners().stream().filter(owner -> owner != player).findAny().orElse(null);
    }

    @Override
    public List<? extends GameEventListener> getEventListenersToForwardTo() {
        return items;
    }

    public void reset() {
        resetRemoveFromBoard();
        setTargetPosition(null, 0);
        resetHealthAndMana();
        attackTarget = null;
        timeUntilNextAttack = 0;
        remainingCastDuration = 0;
        buffs.clear();
    }

    public void resetHealthAndMana() {
        currentHealth = getMaximumHealth();
        currentMana = getInitialMana();
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        super.writeForClient(outputStream);
        outputStream.writeBits(cost, OptimizedBits.SIGNED_INT_TO_8);
        outputStream.writeFloat_Unprecise_Nullable(currentMana);
        outputStream.writeObject_Nullable(spell);
        outputStream.writeObject_Nullable(player);
        outputStream.writeBits(stars, OptimizedBits.SIGNED_INT_TO_8);
        outputStream.writeBoolean(active);
        outputStream.writeObjectList(buffs, OptimizedBits.SIGNED_INT_TO_4096);
        outputStream.writeObjectList(items, OptimizedBits.SIGNED_INT_TO_8);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        super.readForClient(inputStream);
        cost = inputStream.readBits(OptimizedBits.SIGNED_INT_TO_8);
        currentMana = inputStream.readFloat_Unprecise_Nullable();
        spell = inputStream.readObject_Nullable();
        player = inputStream.readObject_Nullable();
        stars = inputStream.readBits(OptimizedBits.SIGNED_INT_TO_8);
        active = inputStream.readBoolean();
        buffs = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_4096);
        items = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_8);
    }
}
