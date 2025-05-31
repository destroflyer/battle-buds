package com.destroflyer.battlebuds.shared.lobby;

import com.destroflyer.battlebuds.shared.Account;
import com.destroflyer.battlebuds.shared.GameMode;
import com.jme3.network.serializing.Serializable;
import lombok.*;

@Serializable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class LobbyPlayer {
    private Account account;
    @Setter
    private LobbyPlayerStatus status;
    @Setter
    private GameMode gameMode;
    @Setter
    private String characterName;
}
