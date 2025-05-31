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
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Decision implements GameSerializable {

    @Getter
    private List<DecisionOption> options;

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        outputStream.writeObjectList(options, OptimizedBits.SIGNED_INT_TO_8);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        options = inputStream.readObjectList(OptimizedBits.SIGNED_INT_TO_8);
    }
}
