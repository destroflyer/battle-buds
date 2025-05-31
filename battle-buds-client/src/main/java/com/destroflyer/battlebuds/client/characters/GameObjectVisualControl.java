package com.destroflyer.battlebuds.client.characters;

import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class GameObjectVisualControl extends AbstractControl {

    private GameObjectVisual visual;
    private Camera camera;

    public GameObjectVisualControl(GameObjectVisual visual, Camera camera) {
        this.visual = visual;
        this.camera = camera;
    }

    @Override
    protected void controlUpdate(float tpf) {
        visual.updateGuiControlPositions(camera);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
