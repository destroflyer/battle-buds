package com.destroflyer.battlebuds.shared.game.traits;

import com.destroflyer.battlebuds.shared.game.Trait;
import com.destroflyer.battlebuds.shared.game.Tier;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.OptimizedBits;

import java.io.IOException;

public class DragonTrait extends Trait {

    public DragonTrait() {
        name = "Dragon";
    }
    private int stacks;

    @Override
    public String getDescription() {
        return "[TODO: Is not implemented yet!]\n\nDragons protect an egg, which grows stronger each round. Losing combat makes it grow faster. After winning the third combat in total, the egg hatches.\n\n(2) 1 egg level per win, 2 per loss\n(3) 2 egg levels per win, 4 per loss\n\nCurrent egg level: " + stacks;
    }

    @Override
    public Tier getTier() {
        return getThreshholdTier(null, null, 2, 3);
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
