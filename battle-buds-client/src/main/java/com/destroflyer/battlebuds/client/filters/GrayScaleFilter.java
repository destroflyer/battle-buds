package com.destroflyer.battlebuds.client.filters;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

public class GrayScaleFilter extends Filter {

    public GrayScaleFilter() {
        super("GrayScaleFilter");
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort viewPort, int width, int height) {
        material = new Material(manager, "shaders/grayscale/grayscale.j3md");
    }

    @Override
    protected Material getMaterial() {
        return material;
    }
}
