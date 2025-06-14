package com.destroflyer.battlebuds.client;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.collision.CollisionResults;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import lombok.Getter;

import java.awt.image.BufferedImage;

public class BaseApplication extends SimpleApplication {

    public BaseApplication() {
        settings = new AppSettings(true);
        settings.setWidth(1600);
        settings.setHeight(900);
        settings.setVSync(true);
        settings.setFrameRate(144);
        settings.setTitle("Battle Buds");
        settings.setIcons(new BufferedImage[] {
            FileAssets.getImage("textures/icon/16.png"),
            FileAssets.getImage("textures/icon/32.png"),
            FileAssets.getImage("textures/icon/64.png"),
            FileAssets.getImage("textures/icon/128.png")
        });
        setShowSettings(false);
        setPauseOnLostFocus(false);
    }
    @Getter
    private FilterPostProcessor filterPostProcessor;

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator(FileAssets.ROOT, FileLocator.class);
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        setDisplayStatView(false);

        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setColor(ColorRGBA.White.mult(0.4f));
        rootNode.addLight(ambientLight);

        DirectionalLight directionalLight = new DirectionalLight();
        Vector3f lightDirection = new Vector3f(1, -5, -1).normalizeLocal();
        directionalLight.setDirection(lightDirection);
        directionalLight.setColor(ColorRGBA.White.mult(1.1f));
        rootNode.addLight(directionalLight);

        // TODO: Add setting which uses a filter instead of renderer
        DirectionalLightShadowRenderer shadowRenderer = new DirectionalLightShadowRenderer(assetManager, 4096, 3);
        shadowRenderer.setLight(directionalLight);
        shadowRenderer.setShadowIntensity(0.4f);
        viewPort.addProcessor(shadowRenderer);

        filterPostProcessor = new FilterPostProcessor(assetManager);
        viewPort.addProcessor(filterPostProcessor);

        WaterFilter waterFilter = new WaterFilter(rootNode, lightDirection);
        waterFilter.setWaterHeight(-2);
        waterFilter.setWaterTransparency(0.5f);
        waterFilter.setUseFoam(false);
        waterFilter.setUseRipples(false);
        waterFilter.setUseSpecular(false);
        waterFilter.setReflectionDisplace(0);
        filterPostProcessor.addFilter(waterFilter);

        SSAOFilter ssaoFilter = new SSAOFilter(3, 25, 6, 0.1f);
        filterPostProcessor.addFilter(ssaoFilter);

        addSky("miramar");

        flyCam.setMoveSpeed(100);
        flyCam.setEnabled(false);

        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
    }

    private void addSky(String skyName) {
        Texture textureWest = assetManager.loadTexture("textures/skies/" + skyName + "/left.png");
        Texture textureEast = assetManager.loadTexture("textures/skies/" + skyName + "/right.png");
        Texture textureNorth = assetManager.loadTexture("textures/skies/" + skyName + "/front.png");
        Texture textureSouth = assetManager.loadTexture("textures/skies/" + skyName + "/back.png");
        Texture textureUp = assetManager.loadTexture("textures/skies/" + skyName + "/up.png");
        Texture textureDown = assetManager.loadTexture("textures/skies/" + skyName + "/down.png");
        rootNode.attachChild(SkyFactory.createSky(assetManager, textureWest, textureEast, textureNorth, textureSouth, textureUp, textureDown));
    }

    public CollisionResults getRayCastingResults_Cursor(Spatial spatial) {
        return getRayCastingResults_Screen(spatial, inputManager.getCursorPosition());
    }

    public CollisionResults getRayCastingResults_Screen(Spatial spatial, Vector2f screenLocation) {
        Vector3f cursorRayOrigin = cam.getWorldCoordinates(screenLocation, 0);
        Vector3f cursorRayDirection = cam.getWorldCoordinates(screenLocation, 1).subtractLocal(cursorRayOrigin).normalizeLocal();
        return getRayCastingResults(spatial, new Ray(cursorRayOrigin, cursorRayDirection));
    }

    private CollisionResults getRayCastingResults(Spatial spatial, Ray ray) {
        CollisionResults results = new CollisionResults();
        spatial.collideWith(ray, results);
        return results;
    }
}
