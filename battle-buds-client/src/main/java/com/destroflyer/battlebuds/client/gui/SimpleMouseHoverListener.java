package com.destroflyer.battlebuds.client.gui;

import com.jme3.input.event.MouseMotionEvent;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.event.DefaultMouseListener;
import lombok.Setter;

@Setter
public class SimpleMouseHoverListener extends DefaultMouseListener {

    private Runnable onEnter;
    private Runnable onExit;

    @Override
    public void mouseEntered(MouseMotionEvent event, Spatial target, Spatial capture) {
        super.mouseEntered(event, target, capture);
        if (onEnter != null) {
            onEnter.run();
        }
    }

    @Override
    public void mouseExited(MouseMotionEvent event, Spatial target, Spatial capture) {
        super.mouseExited(event, target, capture);
        if (onExit != null) {
            onExit.run();
        }
    }
}
