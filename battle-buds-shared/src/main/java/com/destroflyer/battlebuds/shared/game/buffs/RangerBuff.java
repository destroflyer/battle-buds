package com.destroflyer.battlebuds.shared.game.buffs;

import com.destroflyer.battlebuds.shared.game.Buff;
import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RangerBuff extends Buff {

    public RangerBuff(float bonusAttackSpeed) {
        this.bonusAttackSpeed = bonusAttackSpeed;
        maximumStacks = 10;
    }
    private float bonusAttackSpeed;

    @Override
    public float getBonusAttackSpeedFlat(Unit unit) {
        return bonusAttackSpeed;
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        super.writeForClient(outputStream);
        outputStream.writeFloat_Unprecise(bonusAttackSpeed);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        super.readForClient(inputStream);
        bonusAttackSpeed = inputStream.readFloat_Unprecise();
    }
}
