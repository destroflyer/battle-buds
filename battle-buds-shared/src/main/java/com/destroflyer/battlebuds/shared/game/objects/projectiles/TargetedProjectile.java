package com.destroflyer.battlebuds.shared.game.objects.projectiles;

import com.destroflyer.battlebuds.shared.game.objects.Projectile;
import com.destroflyer.battlebuds.shared.game.objects.Unit;

public abstract class TargetedProjectile extends Projectile {

    @Override
    protected void onTargetLost() {
        super.onTargetLost();
        requestRemoveFromBoard();
    }

    @Override
    protected void onTargetReached() {
        super.onTargetReached();
        // Source could've been removed in the meantime
        if (source.getBoard() != null) {
            onImpact((Unit) targetObject);
        }
        requestRemoveFromBoard();
    }

    protected abstract void onImpact(Unit target);
}
