package com.destroflyer.battlebuds.client.characters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AnimationInfo {

    private String name;
    private Float loopDuration;

    public AnimationInfo(String name) {
        this(name, null);
    }
}
