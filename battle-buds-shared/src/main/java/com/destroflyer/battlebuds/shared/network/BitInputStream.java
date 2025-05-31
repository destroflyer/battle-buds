package com.destroflyer.battlebuds.shared.network;

import com.destroflyer.battlebuds.shared.Util;
import com.jme3.math.Vector2f;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author pspeed, destroflyer
 */
public class BitInputStream {
 
    public BitInputStream(InputStream in) {
        this.in = in;
    }
    private InputStream in;
    private int lastByte;
    private int bits = 0;
    private HashMap<Integer, Object> objects = new HashMap<>();

    public <T extends GameSerializable> T[][] readObjectArray_Array_Nullables(Class<T> clazz, int count1, int count2) throws IOException {
        int lengthX = readBits(count1);
        int lengthY = readBits(count2);
        T[][] objects = (T[][]) Array.newInstance(clazz.arrayType(), lengthX);
        for (int x = 0; x < lengthX; x++) {
            objects[x] = (T[]) Array.newInstance(clazz, lengthY);
            for (int y = 0; y < lengthY; y++) {
                objects[x][y] = readObject_Nullable();
            }
        }
        return objects;
    }

    public <T extends GameSerializable> T[] readObjectArray_Nullables(Class<T> clazz, int count) throws IOException {
        int length = readBits(count);
        T[] objects = (T[]) Array.newInstance(clazz, length);
        for (int i = 0; i < length; i++) {
            objects[i] = readObject_Nullable();
        }
        return objects;
    }

    public <T extends GameSerializable> ArrayList<T> readObjectList(int count) throws IOException {
        int size = readBits(count);
        ArrayList<T> objects = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            objects.add(readObject());
        }
        return objects;
    }

    public <T extends GameSerializable> T readObject_Nullable() throws IOException {
        return readNullable(this::readObject);
    }

    public <T extends GameSerializable> T readObject() throws IOException {
        int id = readBits(OptimizedBits.SIGNED_INT_FOR_OBJECT_ID);
        T object = (T) objects.get(id);
        if (object != null) {
            return object;
        }
        object = Util.createObjectByClassName(readString_UTF8());
        objects.put(id, object);
        object.readForClient(this);
        return object;
    }
 
    public <T extends Enum<T>> T readEnum(Class<T> enumClass) throws IOException {
        T[] enumConstants = enumClass.getEnumConstants();
        int bitsCount = Util.getNeededBitsCount(enumConstants.length);
        return enumConstants[readBits(bitsCount)];
    }

    public String readString_UTF8_Nullable(int maximumBytesCountBits) throws IOException {
        return readNullable(() -> readString_UTF8(maximumBytesCountBits));
    }

    public String readString_UTF8() throws IOException {
        return readString_UTF8(OptimizedBits.SIGNED_INT_FULL);
    }
    
    public String readString_UTF8(int maximumBytesCountBits) throws IOException {
        int bytesCount = readBits(maximumBytesCountBits);
        byte[] bytes = readBytes(bytesCount);
        return new String(bytes, "UTF-8");
    }
    
    public byte[] readBytes(int bytesCount) throws IOException {
        byte[] bytes = new byte[bytesCount];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) readBits(8);
        }
        return bytes;
    }

    public Vector2f readVector2f() throws IOException {
        return new Vector2f(readFloat(), readFloat());
    }

    public Vector2f readVector2f_Unprecise() throws IOException {
        return new Vector2f(readFloat_Unprecise(), readFloat_Unprecise());
    }

    public Float readFloat_Nullable() throws IOException {
        return readNullable(this::readFloat);
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readBits(OptimizedBits.SIGNED_FLOAT_FULL));
    }

    public Float readFloat_Unprecise_Nullable() throws IOException {
        return readNullable(this::readFloat_Unprecise);
    }

    public float readFloat_Unprecise() throws IOException {
        return readFloat_Unprecise(OptimizedBits.SIGNED_FLOAT_UNPRECISE_MANTISSA, OptimizedBits.SIGNED_FLOAT_UNPRECISE_EXPONENT);
    }

    public float readFloat_Unprecise(int mantissaBits, int exponentBits) throws IOException {
        int sign = readBits(1);
        int exponent = readBits(exponentBits);
        int mantissa = readBits(mantissaBits);
        int intBits = ((sign << (OptimizedBits.SIGNED_FLOAT_FULL - 1)) | (exponent << (OptimizedBits.SIGNED_FLOAT_FULL_MANTISSA + (OptimizedBits.SIGNED_FLOAT_FULL_EXPONENT - exponentBits))) | (mantissa << (OptimizedBits.SIGNED_FLOAT_FULL_MANTISSA - mantissaBits)));
        return Float.intBitsToFloat(intBits);
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLongBits(OptimizedBits.SIGNED_LONG_FULL));
    }

    public Integer readInteger_Nullable() throws IOException {
        return readNullable(this::readInteger);
    }

    public int readInteger() throws IOException {
        return readBits(OptimizedBits.SIGNED_INT_FULL);
    }

    public long readLong() throws IOException {
        return readLongBits(OptimizedBits.SIGNED_LONG_FULL);
    }

    interface NotNullValueReader<T> { T readValue() throws IOException; }
    private <T> T readNullable(NotNullValueReader<T> notNullValueReader) throws IOException {
        boolean isNotNull = readBoolean();
        return isNotNull ? notNullValueReader.readValue() : null;
    }

    public boolean readBoolean() throws IOException {
        return (readBits(1) == 1);
    }

    public Integer readBits_Nullable(int count) throws IOException {
        return readNullable(() -> readBits(count));
    }

    public int readBits(int count) throws IOException {
        if (count == 0) {
            throw new IllegalArgumentException("Cannot read 0 bits.");
        } else if (count > 32) {
            throw new IllegalArgumentException("Bit count overflow: " + count);
        }
 
        int result = 0;
 
        // While we still have bits remaining…
        int remainingCount = count;
        while (remainingCount > 0) {
            // See if we need to refill the current read byte
            if (bits == 0) {
                int b = in.read();
                if (b < 0) {
                    throw new IOException("End of stream reached.");
                }
                lastByte = b;
                bits = 8;
            }
 
            // Copy the smaller of the two: remaining bits
            // or bits left in lastByte.
            int bitsToCopy = bits < remainingCount ? bits : remainingCount;
 
            // How much do we have to shift the read byte to just
            // get the high bits we want?
            int sourceShift = bits - bitsToCopy;
 
            // And how much do we have to shift those bits to graft
            // them onto our result?
            int targetShift = remainingCount - bitsToCopy;
 
            // Copy the bits
            result |= (lastByte >> sourceShift) << targetShift;
 
            // Keep track of how many bits we have left
            remainingCount -= bitsToCopy;
            bits -= bitsToCopy;
 
            // Now we need to mask off the bits we just copied from
            // lastByte. Just keep the bits that are left.
            lastByte = lastByte & (0xff >> (8 - bits));
        }
        return result;
    }
 
    public long readLongBits(int count) throws IOException {
        if (count == 0) {
            throw new IllegalArgumentException("Cannot read 0 bits.");
        } else if (count > 64) {
            throw new IllegalArgumentException("Bit count overflow: " + count);
        }
        
        long result = 0;
 
        // While we still have bits remaining…
        int remainingCount = count;
        while (remainingCount > 0) {
            // See if we need to refill the current read byte
            if (bits == 0) {
                int b = in.read();
                if (b < 0) {
                    throw new IOException( "End of stream reached." );
                }
                lastByte = b;
                bits = 8;
            }
 
            // Copy the smaller of the two: remaining bits
            // or bits left in lastByte.
            int bitsToCopy = bits < remainingCount ? bits : remainingCount;
 
            // How much do we have to shift the read byte to just
            // get the high bits we want?
            int sourceShift = bits - bitsToCopy;
 
            // And how much do we have to shift those bits to graft
            // them onto our result?
            int targetShift = remainingCount - bitsToCopy;
 
            // Copy the bits
            result |= ((long) lastByte >> sourceShift) << targetShift;
 
            // Keep track of how many bits we have left
            remainingCount -= bitsToCopy;
            bits -= bitsToCopy;
 
            // Now we need to mask off the bits we just copied from
            // lastByte. Just keep the bits that are left.
            lastByte = lastByte & (0xff >> (8 - bits));
        }
 
        return result;
    }
 
    public void close() throws IOException {
        in.close();
    }
}
