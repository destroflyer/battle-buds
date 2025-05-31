package com.destroflyer.battlebuds.client.models.modifiers;

import com.destroflyer.battlebuds.client.JMonkeyUtil;
import com.destroflyer.battlebuds.client.models.ModelModifier;
import com.destroflyer.battlebuds.client.models.RegisteredModel;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class ModelModifier_Nathalya_Fire extends ModelModifier {

    @Override
    public void modify(RegisteredModel registeredModel, AssetManager assetManager) {
        Node wandNode = registeredModel.requestBoneAttachmentsNode("Bip01_R_Hand");
        Spatial fire = assetManager.loadModel("models/fireball/fireball.j3o");
        fire.setLocalTranslation(12, 2, 87);
        fire.setLocalScale(10, 10, 16);
        JMonkeyUtil.setLocalRotation(fire, new Vector3f(1, 1, 4));
        wandNode.attachChild(fire);
    }
}
