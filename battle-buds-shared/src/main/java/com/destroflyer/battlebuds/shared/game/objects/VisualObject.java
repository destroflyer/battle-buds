package com.destroflyer.battlebuds.shared.game.objects;

import com.destroflyer.battlebuds.shared.game.GameObject;
import com.destroflyer.battlebuds.shared.game.objects.pickup.GoldCoin;
import com.destroflyer.battlebuds.shared.game.objects.players.ActualPlayer;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.OptimizedBits;
import com.jme3.math.Vector2f;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class VisualObject extends GameObject {

    private static final float MINIMUM_DISTANCE_TO_TARGET_TO_TURN = 0.01f;

    @Getter
    @Setter
    protected String name;
    protected boolean hasDynamicName;
    @Getter
    @Setter
    protected String visualName;
    protected boolean hasDynamicVisualName;
    @Getter
    protected Vector2f position = new Vector2f();
    @Getter
    protected Vector2f direction = new Vector2f(0, 1);
    @Getter
    protected VisualObject targetObject;
    @Getter
    protected Vector2f targetPosition;
    private Float targetRange;
    private Vector2f forcedMovementDirection;
    private Float forcedMovementSpeed;
    protected float baseMovementSpeed = 6;
    @Getter
    private ActionState actionState = ActionState.IDLE;

    @Override
    public void update(float tpf) {
        super.update(tpf);
        move(tpf);
        actionState = calculateActionState();
    }

    private void move(float tpf) {
        if (targetObject != null) {
            if ((board != null) && (targetObject.getBoard() == board)) {
                targetPosition = targetObject.getPosition();
            } else {
                onTargetLost();
                clearTarget();
            }
        }
        if ((targetPosition != null) || (forcedMovementDirection != null))  {
            float movementSpeed = ((forcedMovementSpeed != null) ? forcedMovementSpeed : getMovementSpeed());
            if (movementSpeed > 0) {
                if (targetPosition != null) {
                    boolean targetReached;
                    Vector2f distanceToTarget = targetPosition.subtract(position);
                    float oldDistanceToTargetLengthSquared = distanceToTarget.lengthSquared();
                    float targetPositionRangeSquared = targetRange * targetRange;
                    if (oldDistanceToTargetLengthSquared <= targetPositionRangeSquared) {
                        targetReached = true;
                    } else {
                        if (oldDistanceToTargetLengthSquared > MINIMUM_DISTANCE_TO_TARGET_TO_TURN) {
                            lookAtTargetPosition();
                        }
                        Vector2f movedDistance = distanceToTarget.normalize().multLocal(tpf * movementSpeed);
                        position.addLocal(movedDistance);
                        float newDistanceToTargetLengthSquared = targetPosition.distanceSquared(position);
                        targetReached = ((newDistanceToTargetLengthSquared <= targetPositionRangeSquared) || (newDistanceToTargetLengthSquared >= oldDistanceToTargetLengthSquared));
                    }
                    if (targetReached) {
                        if (targetRange == 0) {
                            position.set(targetPosition);
                        }
                        onTargetReached();
                        clearTarget();
                    }
                } else {
                    direction.set(forcedMovementDirection);
                    Vector2f movedDistance = forcedMovementDirection.multLocal(tpf * movementSpeed);
                    position.addLocal(movedDistance);
                }
            }
        }
    }

    protected void onTargetLost() {

    }

    protected void onTargetReached() {

    }

    public void setPosition(Vector2f position) {
        this.position.set(position);
    }

    protected void lookAtTargetPosition() {
        this.direction.set(targetPosition.subtract(position).normalizeLocal());
    }

    public void setDirection(Vector2f direction) {
        this.direction.set(direction);
    }

    public void setTargetObject(VisualObject targetObject, float targetPositionRange) {
        setTargetObject(targetObject, targetPositionRange, null);
    }

    public void setTargetObject(VisualObject targetObject, float targetRange, Float forcedMovementSpeed) {
        setMovement(targetObject, null, targetRange, null, forcedMovementSpeed);
    }

    public void setTargetPosition(Vector2f targetPosition, float targetRange) {
        setTargetPosition(targetPosition, targetRange, null);
    }

    public void setTargetPosition(Vector2f targetPosition, float targetRange, Float forcedMovementSpeed) {
        setMovement(null, targetPosition, targetRange, null, forcedMovementSpeed);
    }

    public void setForcedMovementDirection(Vector2f forcedMovementDirection, float forcedMovementSpeed) {
        setMovement(null, null, null, forcedMovementDirection, forcedMovementSpeed);
    }

    private void setMovement(VisualObject targetObject, Vector2f targetPosition, Float targetRange, Vector2f forcedMovementDirection, Float forcedMovementSpeed) {
        this.targetObject = targetObject;
        this.targetPosition = targetPosition;
        this.targetRange = targetRange;
        this.forcedMovementDirection = forcedMovementDirection;
        this.forcedMovementSpeed = forcedMovementSpeed;
    }

    private void clearTarget() {
        this.targetObject = null;
        this.targetPosition = null;
        this.targetRange = null;
        this.forcedMovementDirection = null;
        this.forcedMovementSpeed = null;
    }

    public boolean isForcedMoving() {
        return forcedMovementSpeed != null;
    }

    public float getMovementSpeed() {
        return baseMovementSpeed;
    }

    protected ActionState calculateActionState() {
        return ((getTargetPosition() != null) ? ActionState.WALK : ActionState.IDLE);
    }

    public void dropGoldFor(int minimumGold, int maximumGold, ActualPlayer owner) {
        int gold = minimumGold + (int) (Math.random() * (maximumGold + 1 - minimumGold));
        dropGoldFor(gold, owner);
    }

    public void dropGoldFor(int gold, ActualPlayer owner) {
        for (int i = 0; i < gold; i++) {
            dropFor(new GoldCoin(), owner);
        }
    }

    public void dropFor(PickupObject pickupObject, ActualPlayer owner) {
        pickupObject.setOwner(owner);
        drop(pickupObject);
    }

    private void drop(PickupObject pickupObject) {
        board.drop(position, pickupObject);
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        super.writeForClient(outputStream);
        if (hasDynamicName) {
            outputStream.writeString_UTF8_Nullable(name, OptimizedBits.STRING_CHARACTERS_TO_128);
        }
        if (hasDynamicVisualName) {
            outputStream.writeString_UTF8_Nullable(visualName, OptimizedBits.STRING_CHARACTERS_TO_128);
        }
        outputStream.writeVector2f_Unprecise(position);
        outputStream.writeVector2f_Unprecise(direction);
        outputStream.writeEnum(actionState);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        super.readForClient(inputStream);
        if (hasDynamicName) {
            name = inputStream.readString_UTF8_Nullable(OptimizedBits.STRING_CHARACTERS_TO_128);
        }
        if (hasDynamicVisualName) {
            visualName = inputStream.readString_UTF8_Nullable(OptimizedBits.STRING_CHARACTERS_TO_128);
        }
        position = inputStream.readVector2f_Unprecise();
        direction = inputStream.readVector2f_Unprecise();
        actionState = inputStream.readEnum(ActionState.class);
    }
}
