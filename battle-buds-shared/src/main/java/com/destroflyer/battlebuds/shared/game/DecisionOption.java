package com.destroflyer.battlebuds.shared.game;

import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import com.destroflyer.battlebuds.shared.network.GameSerializable;
import com.destroflyer.battlebuds.shared.network.OptimizedBits;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DecisionOption implements GameSerializable {

    @Getter
    private Tier tier;
    @Getter
    private String text;
    private Runnable onSelect;

    public void select() {
        onSelect.run();
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        outputStream.writeEnum(tier);
        outputStream.writeString_UTF8(text, OptimizedBits.STRING_CHARACTERS_TO_128);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        tier = inputStream.readEnum(Tier.class);
        text = inputStream.readString_UTF8(OptimizedBits.STRING_CHARACTERS_TO_128);
    }
}
