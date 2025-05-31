package com.destroflyer.battlebuds.client.characters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ModelInfo {

    private String modelName;
    private String skinName;
    private AnimationInfo idleAnimation;
    private AnimationInfo walkAnimation;
    private Float customWalkStepDistance;
    private AnimationInfo attackAnimation;
    private AnimationInfo castAnimation;
    private AnimationInfo danceAnimation;

    public ModelInfo(String modelName) {
        this(modelName, null);
    }

    public ModelInfo(String modelName, String skinName) {
        this.modelName = modelName;
        this.skinName = skinName;
    }
}
