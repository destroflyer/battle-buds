package com.destroflyer.battlebuds.client.models.modifiers;

import com.destroflyer.battlebuds.client.models.ModelModifier;
import com.destroflyer.battlebuds.client.models.RegisteredModel;
import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;

public class ModelModifier_EnergyProjectile extends ModelModifier {

    @Override
    public void modify(RegisteredModel registeredModel, AssetManager assetManager) {
        ParticleEmitter particleEmitter = (ParticleEmitter) registeredModel.getNode().getChild(0);
        particleEmitter.updateLogicalState(10);
    }
}
