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
    private Vector2f targetPosition;
    private float targetPositionRange;
    private Float forcedMovementSpeed;
    protected float baseMovementSpeed = 6;
    @Getter
    private ActionState actionState = ActionState.IDLE;

    @Override
    public void update(float tpf) {
        super.update(tpf);
        moveToTargetPosition(tpf);
        actionState = calculateActionState();
    }

    private void moveToTargetPosition(float tpf) {
        if (targetPosition != null)  {
            float movementSpeed = ((forcedMovementSpeed != null) ? forcedMovementSpeed : getMovementSpeed());
            if (movementSpeed > 0) {
                boolean targetReached;
                Vector2f distanceToTarget = targetPosition.subtract(position);
                float oldDistanceToTargetLengthSquared = distanceToTarget.lengthSquared();
                float targetPositionRangeSquared = targetPositionRange * targetPositionRange;
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
                    if (targetPositionRange == 0) {
                        position.set(targetPosition);
                    }
                    targetPosition = null;
                    forcedMovementSpeed = null;
                }
            }
        }
    }

    public void setPosition(Vector2f position) {
        this.position.set(position);
    }

    public void lookAtTargetPosition() {
        this.direction.set(targetPosition.subtract(position).normalizeLocal());
    }

    public void setDirection(Vector2f direction) {
        this.direction.set(direction);
    }

    public void setTargetPosition(Vector2f targetPosition, float targetPositionRange) {
        setTargetPosition(targetPosition, targetPositionRange, null);
    }

    public void setTargetPosition(Vector2f targetPosition, float targetPositionRange, Float forcedMovementSpeed) {
        this.targetPosition = targetPosition;
        this.targetPositionRange = targetPositionRange;
        this.forcedMovementSpeed = forcedMovementSpeed;
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
