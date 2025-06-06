package com.destroflyer.battlebuds.shared.game.objects.projectiles;

import com.destroflyer.battlebuds.shared.game.objects.Unit;

public class AttackProjectile extends TargetedProjectile {

    public AttackProjectile() {
        hasDynamicVisualName = true;
    }

    @Override
    protected void onImpact(Unit target) {
        source.executeAttack(target);
    }
}
