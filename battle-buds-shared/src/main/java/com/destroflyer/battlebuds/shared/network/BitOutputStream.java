package com.destroflyer.battlebuds.shared.network;

import com.destroflyer.battlebuds.shared.Util;
import com.jme3.math.Vector2f;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author pspeed, destroflyer
 */
public class BitOutputStream {
 
    public BitOutputStream(OutputStream out){
        this.out = out;
    }
    private OutputStream out;
    private int currentByte = 0;
    private int bits = 8;
    private int nextObjectId;
    private HashMap<Object, Integer> objectIds = new HashMap<>();

    public void writeObjectArray_Array_Nullables(GameSerializable[][] objects, int count1, int count2) throws IOException {
        writeBits(objects.length, count1);
        writeBits(((objects.length > 0) ? objects[0].length : 0), count2);
        for (GameSerializable[] innerObjects : objects) {
            for (GameSerializable object : innerObjects) {
                writeObject_Nullable(object);
            }
        }
    }

    public void writeObjectArray_Nullables(GameSerializable[] objects, int count) throws IOException {
        writeBits(objects.length, count);
        for (GameSerializable object : objects) {
            writeObject_Nullable(object);
        }
    }

    public void writeObjectList(Collection<? extends GameSerializable> objects, int count) throws IOException {
        writeBits(objects.size(), count);
        for (GameSerializable object : objects) {
            writeObject(object);
        }
    }

    public void writeObject_Nullable(GameSerializable object) throws IOException {
        writeNullable(object, () -> writeObject(object));
    }

    public void writeObject(GameSerializable object) throws IOException {
        Integer id = objectIds.get(object);
        if (id == null) {
            id = nextObjectId++;
            objectIds.put(object, id);
            writeBits(id, OptimizedBits.SIGNED_INT_FOR_OBJECT_ID);
            writeString_UTF8(object.getClass().getName());
            object.writeForClient(this);
        } else {
            writeBits(id, OptimizedBits.SIGNED_INT_FOR_OBJECT_ID);
        }
    }
 
    public <T extends Enum<T>> void writeEnum(T value) throws IOException {
        T[] enumConstants = value.getDeclaringClass().getEnumConstants();
        int bitsCount = Util.getNeededBitsCount(enumConstants.length);
        writeBits(value.ordinal(), bitsCount);
    }

    public void writeString_UTF8_Nullable(String string, int maximumBytesCountBits) throws IOException {
        writeNullable(string, () -> writeString_UTF8(string, maximumBytesCountBits));
    }

    public void writeString_UTF8(String string) throws IOException {
        writeString_UTF8(string, OptimizedBits.SIGNED_INT_FULL);
    }
 
    public void writeString_UTF8(String string, int maximumBytesCountBits) throws IOException {
        try {
            byte[] bytes = string.getBytes("UTF-8");
            writeBits(bytes.length, maximumBytesCountBits);
            writeBytes(bytes, bytes.length);
        } catch (UnsupportedEncodingException ex) {
            throw new IOException(ex);
        }
    }
 
    public void writeBytes(byte[] bytes, int count) throws IOException {
        for (int i = 0; i < count; i++) {
            byte value = ((i < bytes.length) ? bytes[i] : 0);
            writeBits(value, 8);
        }
    }

    public void writeVector2f(Vector2f value) throws IOException {
        writeFloat(value.getX());
        writeFloat(value.getY());
    }

    public void writeVector2f_Unprecise(Vector2f value) throws IOException {
        writeFloat_Unprecise(value.getX());
        writeFloat_Unprecise(value.getY());
    }

    public void writeFloat_Nullable(Float value) throws IOException {
        writeNullable(value, () -> writeFloat(value));
    }

    public void writeFloat(float value) throws IOException {
        writeBits(Float.floatToIntBits(value), OptimizedBits.SIGNED_FLOAT_FULL);
    }

    public void writeFloat_Unprecise_Nullable(Float value) throws IOException {
        writeNullable(value, () -> writeFloat_Unprecise(value));
    }

    public void writeFloat_Unprecise(float value) throws IOException {
        writeFloat_Unprecise(value, OptimizedBits.SIGNED_FLOAT_UNPRECISE_MANTISSA, OptimizedBits.SIGNED_FLOAT_UNPRECISE_EXPONENT);
    }

    public void writeFloat_Unprecise(float value, int mantissaBits, int exponentBits) throws IOException {
        int intBits = Float.floatToIntBits(value);
        int sign = (intBits >>> (OptimizedBits.SIGNED_FLOAT_FULL - 1));
        int mantissa = ((intBits << (1 + OptimizedBits.SIGNED_FLOAT_FULL_EXPONENT)) >>> (1 + OptimizedBits.SIGNED_FLOAT_FULL_EXPONENT + (OptimizedBits.SIGNED_FLOAT_FULL_MANTISSA - mantissaBits)));
        int exponent = ((intBits << 1) >>> (1 + (OptimizedBits.SIGNED_FLOAT_FULL_EXPONENT - exponentBits) + OptimizedBits.SIGNED_FLOAT_FULL_MANTISSA));
        writeBits(sign, 1);
        writeBits(exponent, exponentBits);
        writeBits(mantissa, mantissaBits);
    }
 
    public void writeDouble(double value) throws IOException {
        writeLongBits(Double.doubleToLongBits(value), OptimizedBits.SIGNED_LONG_FULL);
    }

    public void writeInteger_Nullable(Integer value) throws IOException {
        writeNullable(value, () -> writeInteger(value));
    }

    public void writeInteger(int value) throws IOException {
        writeBits(value, OptimizedBits.SIGNED_INT_FULL);
    }

    public void writeLong(long value) throws IOException {
        writeLongBits(value, OptimizedBits.SIGNED_LONG_FULL);
    }

    interface NotNullValueWriter { void writeValue() throws IOException; }
    private <T> void writeNullable(T value, NotNullValueWriter notNullValueWriter) throws IOException {
        boolean isNotNull = value != null;
        writeBoolean(isNotNull);
        if (isNotNull) {
            notNullValueWriter.writeValue();
        }
    }

    public void writeBoolean(boolean value) throws IOException {
        writeBits((value?1:0), 1);
    }

    public void writeBits_Nullable(Integer value, int count) throws IOException {
        writeNullable(value, () -> writeBits(value, count));
    }

    public void writeBits(int value, int count) throws IOException {
        if (count == 0) {
            throw new IllegalArgumentException("Cannot write 0 bits.");
        }
        // Make sure the value is clean of extra high bits
        value = value & (0xffffffff >>> (32 - count));

        int remaining = count;
        while (remaining > 0 ) {
            int bitsToCopy = bits < remaining ? bits : remaining;

            int sourceShift = remaining - bitsToCopy;
            int targetShift = bits - bitsToCopy;

            currentByte |= (value >>> sourceShift) << targetShift;

            remaining -= bitsToCopy;
            bits -= bitsToCopy;                      

            value = value & (0xffffffff >>> (32 - remaining));

            // If there are no more bits left to write to in our
            // working byte then write it out and clear it.
            if (bits == 0) {
                flush();
            }
        }
    }

    public void writeLongBits(long value, int count) throws IOException {
        if (count == 0) {
            throw new IllegalArgumentException("Cannot write 0 bits.");
        }

        // Make sure the value is clean of extra high bits
        value = value & (0xffffffffffffffffL >>> (64 - count));

        int remaining = count;
        while (remaining > 0) {
            int bitsToCopy = bits < remaining ? bits : remaining;
 
            int sourceShift = remaining - bitsToCopy;
            int targetShift = bits - bitsToCopy;
 
            currentByte |= (value >>> sourceShift) << targetShift;
 
            remaining -= bitsToCopy;
            bits -= bitsToCopy;                      
 
            value = value & (0xffffffffffffffffL >>> (64 - remaining));
 
            // If there are no more bits left to write to in our
            // working byte then write it out and clear it.
            if (bits == 0) {
                flush();
            }
        }
    }

    protected void flush() throws IOException {
        out.write(currentByte);
        bits = 8;
        currentByte = 0;
    }
 
    public void close() throws IOException {
        flush();
        out.close();
    }
 
    public int getPendingBits() {
        return bits;
    }
}
