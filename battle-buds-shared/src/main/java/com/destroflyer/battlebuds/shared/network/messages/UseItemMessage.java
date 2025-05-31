package com.destroflyer.battlebuds.shared.network.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Serializable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class UseItemMessage extends AbstractMessage {
    private int benchItemIndex;
    private int unitId;
}
