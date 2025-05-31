package com.destroflyer.battlebuds.client.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import lombok.Getter;

public class ForestBoardAppState extends BaseClientAppState {

    @Getter
    private TerrainQuad terrain;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        initTerrain();
        mainApplication.getCamera().setLocation(new Vector3f(0, 50, 28));
        mainApplication.getCamera().lookAt(new Vector3f(0, 0, 5), Vector3f.UNIT_Y);
    }

    private void initTerrain() {
        AssetManager assetManager = mainApplication.getAssetManager();
        Texture heightMapImage = assetManager.loadTexture("textures/boards/forest_height.png");
        AbstractHeightMap heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();
        terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());

        Material material = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        boolean triPlanarMapping = true;
        material.setBoolean("useTriPlanarMapping", true);
        material.setTexture("AlphaMap", assetManager.loadTexture("textures/boards/forest_alpha.png"));
        material.setTexture("AlphaMap_1", assetManager.loadTexture("textures/boards/forest_alpha_1.png"));

        Texture grass = assetManager.loadTexture("textures/terrain/grass.png");
        grass.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("DiffuseMap", grass);
        material.setFloat("DiffuseMap_0_scale", getTextureScale(terrain, 20, triPlanarMapping));

        Texture dirt = assetManager.loadTexture("textures/terrain/soil.png");
        dirt.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("DiffuseMap_1", dirt);
        material.setFloat("DiffuseMap_1_scale", getTextureScale(terrain, 8, triPlanarMapping));

        Texture rock = assetManager.loadTexture("textures/terrain/concretetiles.png");
        rock.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("DiffuseMap_2", rock);
        material.setFloat("DiffuseMap_2_scale", getTextureScale(terrain, 24, triPlanarMapping));

        Texture slot = assetManager.loadTexture("textures/terrain/rock.png");
        slot.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("DiffuseMap_4", slot);
        material.setFloat("DiffuseMap_4_scale", getTextureScale(terrain, 16, triPlanarMapping));

        Texture border = assetManager.loadTexture("textures/terrain/stone_dark.png");
        border.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("DiffuseMap_5", border);
        material.setFloat("DiffuseMap_5_scale", getTextureScale(terrain, 80, triPlanarMapping));

        Texture hole = assetManager.loadTexture("textures/terrain/stone_dark.png");
        hole.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("DiffuseMap_6", hole);
        material.setFloat("DiffuseMap_6_scale", getTextureScale(terrain, 64, triPlanarMapping));

        terrain.setMaterial(material);
        terrain.setLocalTranslation(0, -2.5f, 0);
        terrain.setLocalScale(0.25f, 0.02f, 0.25f);
        terrain.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        mainApplication.getRootNode().attachChild(terrain);
    }

    private static float getTextureScale(TerrainQuad terrain, float scale, boolean triPlanarMapping) {
        return (triPlanarMapping ? (1 / (terrain.getTotalSize() / scale)) : scale);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getRootNode().detachChild(terrain);
    }
}
