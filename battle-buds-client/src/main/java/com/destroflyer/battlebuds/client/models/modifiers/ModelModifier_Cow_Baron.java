package com.destroflyer.battlebuds.client.models.modifiers;

import com.destroflyer.battlebuds.client.JMonkeyUtil;
import com.destroflyer.battlebuds.client.models.MaterialFactory;
import com.destroflyer.battlebuds.client.models.ModelModifier;
import com.destroflyer.battlebuds.client.models.RegisteredModel;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;

public class ModelModifier_Cow_Baron extends ModelModifier {

    @Override
    public void modify(RegisteredModel registeredModel, AssetManager assetManager) {
        Node headNode = registeredModel.requestBoneAttachmentsNode("head");
        float width = 1.6f;
        float height = (width * (4f / 5));
        Quad quad = new Quad(width, height, true);
        Spatial plane = new Geometry(null, quad);
        plane.setLocalTranslation((width / -2), 0.3f, -0.73f);
        JMonkeyUtil.setLocalRotation(plane, new Vector3f(0, -0.7f, 1));
        Material material = MaterialFactory.generateUnshadedMaterial(assetManager, "models/cow/resources/baron.png");
        material.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        plane.setMaterial(material);
        headNode.attachChild(plane);
    }
}
