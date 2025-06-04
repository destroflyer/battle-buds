package com.destroflyer.battlebuds.server;

import com.destroflyer.battlebuds.shared.GameMode;
import com.destroflyer.battlebuds.shared.game.Game;
import com.destroflyer.battlebuds.shared.game.PhaseMath;
import com.destroflyer.battlebuds.shared.lobby.LobbyGame;

import java.util.ArrayList;

public class TestGame {

    public static void main(String[] args) {
        LobbyGame lobbyGame = new LobbyGame(1, GameMode.SINGLE_EIGHT, new ArrayList<>());
        Game game = new Game(lobbyGame);
        while (!game.isFinished()) {
            game.update(1f / 60);
        }
        System.out.println("Game ended at: " + PhaseMath.getStage(game.getPhase()) + "-" + PhaseMath.getRound(game.getPhase()));
    }
}
