package com.destroflyer.battlebuds.client.models.modifiers;

import com.destroflyer.battlebuds.client.controls.RotateYControl;
import com.destroflyer.battlebuds.client.models.ModelModifier;
import com.destroflyer.battlebuds.client.models.RegisteredModel;
import com.jme3.asset.AssetManager;

public class ModelModifier_RotateY extends ModelModifier {

    @Override
    public void modify(RegisteredModel registeredModel, AssetManager assetManager) {
        registeredModel.getNode().addControl(new RotateYControl());
    }
}
