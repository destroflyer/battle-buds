package com.destroflyer.battlebuds.shared.game;

import com.destroflyer.battlebuds.shared.game.objects.PickUpObject;
import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.game.objects.Player;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.GameSerializable;
import com.destroflyer.battlebuds.shared.network.OptimizedBits;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class Board implements GameSerializable {

    @Setter
    protected Game game;
    @Getter
    @Setter
    protected ArrayList<Player> owners;
    @Getter
    protected ArrayList<GameObject> objects = new ArrayList<>();
    @Getter
    protected float time;

    public void update(float tpf) {
        time += tpf;
        for (GameObject object : objects.toArray(GameObject[]::new)) {
            object.update(tpf);
        }
        for (GameObject object : objects.toArray(GameObject[]::new)) {
            if (object.isRemovedFromBoard()) {
                removeObject(object);
            }
        }
    }

    public void addObject(GameObject object) {
        game.register(object);
        if (object.getBoard() != null) {
            object.getBoard().removeObject(object);
        }
        object.setBoard(this);
        objects.add(object);
    }

    public void removeObject(GameObject object) {
        object.setBoard(null);
        objects.remove(object);
    }

    public abstract boolean isFinished();

    public void onFinish() {

    }

    public Unit getClosestActiveUnit(Vector2f position, Predicate<Unit> filter) {
        float minimumDistanceSquared = Float.MAX_VALUE;
        Unit closestUnit = null;
        for (GameObject object : objects) {
            if (object instanceof Unit unit) {
                if (unit.isActive() && filter.test(unit)) {
                    float distanceSquared = unit.getPosition().distanceSquared(position);
                    if (distanceSquared < minimumDistanceSquared) {
                        minimumDistanceSquared = distanceSquared;
                        closestUnit = unit;
                    }
                }
            }
        }
        return closestUnit;
    }

    public boolean hasUnit(Player player, Predicate<Unit> filter) {
        return getUnitsStream(player, filter).findAny().isPresent();
    }

    public List<Unit> getUnits(Player player) {
        return getUnits(player, _ -> true);
    }

    public List<Unit> getUnits(Player player, Predicate<Unit> filter) {
        return getUnitsStream(player, filter).toList();
    }

    private Stream<Unit> getUnitsStream(Player player, Predicate<Unit> filter) {
        return objects.stream().filter(gameObject -> (gameObject instanceof Unit unit) && (unit.getPlayer() == player) && filter.test(unit)).map(gameObject -> (Unit) gameObject);
    }

    public GameObject getObjectById(int id) {
        return objects.stream().filter(o -> o.getId() == id).findAny().orElse(null);
    }

    public void drop(Vector2f position, PickUpObject pickUpObject) {
        pickUpObject.setPosition(position);
        float angle = (float) (Math.random() * FastMath.TWO_PI);
        float radius = (float) (2 + (Math.random()) * 5);
        float additionalX = (float) (Math.sin(angle) * radius);
        float additionalY = (float) (Math.cos(angle) * radius);
        Vector2f targetPosition = position.add(new Vector2f(additionalX, additionalY));
        pickUpObject.setTargetPosition(targetPosition, 0, 13f);
        addObject(pickUpObject);
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        outputStream.writeObject(game);
        outputStream.writeObjectList(owners, OptimizedBits.SIGNED_INT_TO_32);
        outputStream.writeObjectList(objects, OptimizedBits.SIGNED_INT_TO_1048576);
        outputStream.writeFloat_Unprecise(time);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        game = inputStream.readObject();
        owners = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_32);
        objects = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_1048576);
        time = inputStream.readFloat_Unprecise();
    }
}
