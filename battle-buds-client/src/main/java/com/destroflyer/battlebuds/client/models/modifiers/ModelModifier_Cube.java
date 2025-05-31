package com.destroflyer.battlebuds.client.models.modifiers;

import com.destroflyer.battlebuds.client.models.MaterialFactory;
import com.destroflyer.battlebuds.client.models.ModelModifier;
import com.destroflyer.battlebuds.client.models.RegisteredModel;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class ModelModifier_Cube extends ModelModifier {

    private String textureFilePath;

    @Override
    public void modify(RegisteredModel registeredModel, AssetManager assetManager) {
        Geometry geometry = new Geometry(null, new Box( 0.5f, 0.5f, 0.5f));

        Material material = new Material(assetManager, MaterialFactory.DEFINITION_NAME_LIGHTING);
        Texture textureDiffuse = assetManager.loadTexture(textureFilePath);
        material.setTexture("DiffuseMap", textureDiffuse);
        geometry.setMaterial(material);
        registeredModel.getNode().attachChild(geometry);
    }
}
