package com.destroflyer.battlebuds.shared.lobby;

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
public class Lobby {
    private ArrayList<LobbyPlayer> players;
    private ArrayList<LobbyGame> games;
}
