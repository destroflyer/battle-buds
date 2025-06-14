package com.destroflyer.battlebuds.client.models.modifiers;

import com.destroflyer.battlebuds.client.JMonkeyUtil;
import com.destroflyer.battlebuds.client.models.ModelModifier;
import com.destroflyer.battlebuds.client.models.ModelSkin;
import com.destroflyer.battlebuds.client.models.RegisteredModel;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class ModelModifier_Alice_Weapons extends ModelModifier {

    @Override
    public void modify(RegisteredModel registeredModel, AssetManager assetManager) {
        // Wand
        Node rightPalmNode = registeredModel.requestBoneAttachmentsNode("RigRPalm");
        Node wand = ModelSkin.get("models/alice_wand/skin.xml").load(assetManager);
        wand.setLocalTranslation(8, -3, 0);
        JMonkeyUtil.lookAtDirection(wand, new Vector3f(0, 0, 1));
        wand.setLocalScale(100);
        rightPalmNode.attachChild(wand);
    }
}
