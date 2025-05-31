package com.destroflyer.battlebuds.shared.lobby;

import com.destroflyer.battlebuds.shared.GameMode;
import com.jme3.network.serializing.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Serializable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class LobbyGame {
    private int id;
    private GameMode gameMode;
    private ArrayList<LobbyPlayer> players;
}
