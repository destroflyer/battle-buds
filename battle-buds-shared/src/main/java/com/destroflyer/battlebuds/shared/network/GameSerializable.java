package com.destroflyer.battlebuds.shared.network;

import java.io.IOException;

public interface GameSerializable {

    void writeForClient(BitOutputStream outputStream) throws IOException;

    void readForClient(BitInputStream inputStream) throws IOException;
}
