package com.destroflyer.battlebuds.client.models.modifiers;

import com.destroflyer.battlebuds.client.JMonkeyUtil;
import com.destroflyer.battlebuds.client.models.ModelModifier;
import com.destroflyer.battlebuds.client.models.ModelSkin;
import com.destroflyer.battlebuds.client.models.RegisteredModel;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class ModelModifier_Minion_Gentleman extends ModelModifier {

    @Override
    public void modify(RegisteredModel registeredModel, AssetManager assetManager) {
        Node headNode = registeredModel.requestBoneAttachmentsNode("head");
        // Moustache
        Spatial funnyMoustache = ModelSkin.get("models/funny_moustache/skin.xml").load(assetManager);
        funnyMoustache.setLocalTranslation(0, 0.05f, -0.65f);
        JMonkeyUtil.setLocalRotation(funnyMoustache, new Vector3f(0, 0, -1));
        headNode.attachChild(funnyMoustache);
        // Hat
        Spatial gentlemanHat = ModelSkin.get("models/gentleman_hat/skin.xml").load(assetManager);
        gentlemanHat.setLocalTranslation(0.2f, 0.95f, 0.3f);
        JMonkeyUtil.setLocalRotation(gentlemanHat, new Vector3f(-1, 1, -1));
        headNode.attachChild(gentlemanHat);
    }
}
