package com.destroflyer.battlebuds.client.controls;

import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class RotateYControl extends AbstractControl {

    // 4 seconds to do one full rotation
    private float speed = FastMath.HALF_PI;

    @Override
    protected void controlUpdate(float tpf) {
        spatial.rotate(0, tpf * speed, 0);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
