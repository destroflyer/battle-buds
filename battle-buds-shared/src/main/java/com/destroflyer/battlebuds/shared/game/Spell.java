package com.destroflyer.battlebuds.shared.game;

import com.destroflyer.battlebuds.shared.game.objects.Unit;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.GameSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public abstract class Spell implements GameSerializable {

    @Getter
    protected String name;
    @Getter
    protected String description;
    @Setter
    protected Unit caster;
    @Getter
    @Setter
    protected float castDuration = 0.5f;

    public boolean isCastable() {
        return true;
    }

    public abstract void cast();

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        outputStream.writeFloat_Unprecise(castDuration);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        castDuration = inputStream.readFloat_Unprecise();
    }
}
