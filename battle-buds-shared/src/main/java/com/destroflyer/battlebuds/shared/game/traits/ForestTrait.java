package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.game.Trait;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.OptimizedBits;

import java.io.IOException;

public class ForestTrait extends Trait {

    public ForestTrait() {
        name = "Forest";
    }
    private int stacks;

    @Override
    public String getDescription() {
        return "At the start of each round, the forest grows new roots. Each root gives your team 0.2 Health regeneration per second.\n\n(3) 3 roots\n(5) 5 roots\n(7) 10 roots\n\nCurrent roots: " + stacks;
    }

    @Override
    public Tier getTier() {
        return getThreshholdTier(null, 3, 5, 7);
    }

    @Override
    public void onPlanningRoundStart() {
        super.onPlanningRoundStart();
        int units = getUniqueUnitsOfTraitOnBoard();
        if (units >= 7) {
            stacks += 10;
        } else if (units >= 5) {
            stacks += 5;
        } else if (units >= 3) {
            stacks += 3;
        }
    }

    @Override
    public float getBonusHealthRegenerationFlat(Unit unit) {
        int units = getUniqueUnitsOfTraitOnBoard();
        if (units >= 3) {
            return stacks * 0.2f;
        }
        return 0;
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
