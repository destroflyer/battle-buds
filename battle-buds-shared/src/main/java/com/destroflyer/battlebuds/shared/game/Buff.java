package com.destroflyer.battlebuds.shared.game;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.GameSerializable;
import com.destroflyer.battlebuds.shared.network.OptimizedBits;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class Buff implements GameSerializable, StatModifier {

    @Setter
    private Unit unit;
    @Getter
    @Setter
    private Float remainingTime;
    @Getter
    protected Integer maximumStacks = 1;

    public void update(float tpf) {
        if (remainingTime != null) {
            remainingTime -= tpf;
            if (remainingTime <= 0) {
                unit.removeBuff(this);
            }
        }
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        outputStream.writeObject(unit);
        outputStream.writeFloat_Unprecise_Nullable(remainingTime);
        outputStream.writeBits_Nullable(maximumStacks, OptimizedBits.SIGNED_INT_TO_1048576);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        unit = inputStream.readObject();
        remainingTime = inputStream.readFloat_Unprecise_Nullable();
        maximumStacks = inputStream.readBits_Nullable(OptimizedBits.SIGNED_INT_TO_1048576);
    }
}
