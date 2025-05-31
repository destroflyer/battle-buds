package com.destroflyer.battlebuds.shared.game;

import com.destroflyer.battlebuds.shared.game.objects.Player;
import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.GameSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class Trait implements GameSerializable, StatModifier, GameEventListener {

    @Getter
    protected String name;
    @Getter
    protected String description;
    @Setter
    protected Player player;

    public Tier getTier() {
        return null;
    }

    protected Tier getThreshholdTier(Integer bronzeThreshhold, Integer silverThreshhold, Integer goldThreshhold, Integer prismaticThreshhold) {
        int units = getUniqueUnitsOfTraitOnBoard();
        if ((prismaticThreshhold != null) && (units >= prismaticThreshhold)) {
            return Tier.PRISMATIC;
        } else if ((goldThreshhold != null) && (units >= goldThreshhold)) {
            return Tier.GOLD;
        } if ((silverThreshhold != null) && (units >= silverThreshhold)) {
            return Tier.SILVER;
        } if ((bronzeThreshhold != null) && (units >= bronzeThreshhold)) {
            return Tier.BRONZE;
        }
        return null;
    }

    public int getUniqueUnitsOfTraitOnBoard() {
        return player.getUniqueUnitsOnBoardWithTrait(getClass());
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        outputStream.writeObject(player);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        player = inputStream.readObject();
    }
}
