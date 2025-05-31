package com.destroflyer.battlebuds.shared.game;

import com.destroflyer.battlebuds.shared.game.objects.Player;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.GameSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class Augment implements GameSerializable, StatModifier, GameEventListener {

    @Getter
    protected Tier tier;
    @Getter
    protected String name;
    @Getter
    protected String description;
    @Setter
    protected Player player;

    public void onAdd() {

    }

    public int getBonusMaximumInterestGold() {
        return 0;
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {

    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {

    }
}
