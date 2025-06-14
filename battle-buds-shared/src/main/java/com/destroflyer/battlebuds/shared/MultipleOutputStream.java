package com.destroflyer.battlebuds.shared;

import java.io.IOException;
import java.io.OutputStream;

public class MultipleOutputStream extends OutputStream {
 
    public MultipleOutputStream(OutputStream... outputStreams) {
        this.outputStreams = outputStreams;
    }
    private OutputStream[] outputStreams;

    @Override
    public void write(int i) throws IOException{
        for (OutputStream outputStream : outputStreams) {
            outputStream.write(i);
        }
    }
}
