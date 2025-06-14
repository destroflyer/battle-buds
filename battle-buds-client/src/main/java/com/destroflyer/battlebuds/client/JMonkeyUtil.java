package com.destroflyer.battlebuds.client;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.SkeletonControl;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JMonkeyUtil {

    public static void disableLogger() {
        Logger.getLogger("").setLevel(Level.SEVERE);
        Logger.getLogger(SkeletonControl.class.getName()).setLevel(Level.SEVERE);
    }

    public static void setAttached(Node parent, Spatial child, boolean attached) {
        if (attached) {
            parent.attachChild(child);
        } else {
            parent.detachChild(child);
        }
    }

    public static LinkedList<Geometry> getAllGeometryChilds(Spatial spatial) {
        LinkedList<Geometry> geometryChilds = new LinkedList<>();
        if (spatial instanceof Node) {
            Node node = (Node) spatial;
            for (int i = 0; i < node.getChildren().size(); i++) {
                Spatial child = node.getChild(i);
                if (child instanceof Geometry) {
                    Geometry geometry = (Geometry) child;
                    geometryChilds.add(geometry);
                } else {
                    geometryChilds.addAll(getAllGeometryChilds(child));
                }
            }
        }
        return geometryChilds;
    }

    public static void lookAtDirection(Spatial spatial, Vector3f direction) {
        Vector3f lookAtLocation = spatial.getWorldTranslation().add(direction);
        spatial.lookAt(lookAtLocation, Vector3f.UNIT_Y);
    }

    public static Vector3f getWorldSize(Spatial spatial){
        if (spatial.getWorldBound() instanceof BoundingBox) {
            BoundingBox boundingBox = (BoundingBox) spatial.getWorldBound();
            return new Vector3f(2 * boundingBox.getXExtent(), 2 * boundingBox.getYExtent(), 2 * boundingBox.getZExtent());
        }
        return new Vector3f();
    }

    public static void copyAnimation(AnimChannel sourceAnimationChannel, AnimChannel targetAnimationChannel) {
        targetAnimationChannel.setAnim(sourceAnimationChannel.getAnimationName(), 0);
        targetAnimationChannel.setSpeed(sourceAnimationChannel.getSpeed());
        targetAnimationChannel.setTime(sourceAnimationChannel.getTime());
        targetAnimationChannel.setLoopMode(sourceAnimationChannel.getLoopMode());
    }

    public static void setHardwareSkinningPreferred(Spatial spatial, boolean isPreferred) {
        SkeletonControl skeletonControl = spatial.getControl(SkeletonControl.class);
        if (skeletonControl != null) {
            skeletonControl.setHardwareSkinningPreferred(isPreferred);
        }
    }

    public static void setLocalRotation(Spatial spatial, Vector3f rotation){
        Vector3f lookAtLocation = spatial.getWorldTranslation().add(rotation);
        spatial.lookAt(lookAtLocation, Vector3f.UNIT_Y);
    }
}
