package com.destroflyer.battlebuds.shared.game.objects;

import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class PhysicsObject extends VisualObject {

    @Getter
    @Setter
    private Float collisionRadius = 1.5f;

    public boolean isColliding(PhysicsObject physicsObject) {
        return isColliding(physicsObject.getPosition(), physicsObject.getCollisionRadius());
    }

    public boolean isColliding(Vector2f position, float radius) {
        return this.position.distanceSquared(position) <= FastMath.sqr(collisionRadius + radius);
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        super.writeForClient(outputStream);
        outputStream.writeFloat_Unprecise_Nullable(collisionRadius);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        super.readForClient(inputStream);
        collisionRadius = inputStream.readFloat_Unprecise_Nullable();
    }
}
