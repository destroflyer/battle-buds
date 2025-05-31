package com.destroflyer.battlebuds.shared;

import com.jme3.network.serializing.Serializable;
import lombok.*;

@Serializable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class Account {
    private int id;
    private String login;
}
