package com.destroflyer.battlebuds.shared.game.augments;

import com.destroflyer.battlebuds.shared.game.Augment;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.OptimizedBits;

import java.io.IOException;

public class PumpingUp2 extends Augment {

    public PumpingUp2() {
        tier = Tier.GOLD;
        name = "Pumping Up II";
        description = "Your team gains 8% Attack Speed now. Each round after, they gain 1% more.";
    }
    private int stacks;

    @Override
    public void onPlanningRoundStart() {
        super.onPlanningRoundStart();
        stacks++;
    }

    @Override
    public float getBonusAttackSpeedFlat(Unit unit) {
        return 0.08f + (stacks * 0.01f);
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        super.writeForClient(outputStream);
        outputStream.writeBits(stacks, OptimizedBits.SIGNED_INT_TO_2048);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        super.readForClient(inputStream);
        stacks = inputStream.readBits(OptimizedBits.SIGNED_INT_TO_2048);
    }
}
