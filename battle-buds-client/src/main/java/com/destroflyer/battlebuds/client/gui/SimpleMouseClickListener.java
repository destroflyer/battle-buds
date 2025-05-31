package com.destroflyer.battlebuds.client.gui;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.event.DefaultMouseListener;
import lombok.Setter;

@Setter
public class SimpleMouseClickListener extends DefaultMouseListener {

    private Runnable onClick;

    @Override
    protected void click(MouseButtonEvent event, Spatial target, Spatial capture) {
        super.click(event, target, capture);
        if (onClick != null) {
            onClick.run();
        }
    }
}
