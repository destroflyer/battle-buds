package com.destroflyer.battlebuds.shared.game;

import com.destroflyer.battlebuds.shared.game.items.Items;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.GameSerializable;
import lombok.Getter;

import java.io.IOException;

public class Item implements GameSerializable, StatModifier, GameEventListener {

    @Getter
    protected String name;
    @Getter
    protected String visualName;
    @Getter
    protected String description;

    public boolean isComponent() {
        return Items.isComponent(this);
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {

    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {

    }
}
