package com.destroflyer.battlebuds.shared.game.boards;

import com.destroflyer.battlebuds.shared.game.objects.players.ActualPlayer;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

public class CarouselBoard extends TimeBoard {

    public CarouselBoard() {
        isWalkOnly = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        for (int i = 0; i < owners.size(); i++) {
            ActualPlayer actualPlayer = (ActualPlayer) owners.get(i);
            float angle = ((((float) i) / owners.size()) * FastMath.TWO_PI);
            float radius = 12;
            float x = (float) (Math.sin(angle) * radius);
            float y = (float) (Math.cos(angle) * radius);
            actualPlayer.setPosition(new Vector2f(x, y));
        }
    }
}
