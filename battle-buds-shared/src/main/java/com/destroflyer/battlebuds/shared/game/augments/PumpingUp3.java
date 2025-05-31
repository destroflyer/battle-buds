package com.destroflyer.battlebuds.shared.game.augments;

import com.destroflyer.battlebuds.shared.game.Augment;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.OptimizedBits;

import java.io.IOException;

public class PumpingUp3 extends Augment {

    public PumpingUp3() {
        tier = Tier.PRISMATIC;
        name = "Pumping Up III";
        description = "Your team gains 16% Attack Speed now. Each round after, they gain 2% more.";
    }
    private int stacks;

    @Override
    public void onPlanningRoundStart() {
        super.onPlanningRoundStart();
        stacks++;
    }

    @Override
    public float getBonusAttackSpeedFlat(Unit unit) {
        return 0.16f + (stacks * 0.02f);
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
