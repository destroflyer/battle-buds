package com.destroflyer.battlebuds.shared;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Util {

    public static int getNeededBitsCount(int value) {
        return (32 - Integer.numberOfLeadingZeros(value));
    }

    public static float[] parseToFloatArray(String[] array) {
        float[] floatArray = new float[array.length];
        for (int i = 0; i < floatArray.length; i++) {
            floatArray[i] = Float.parseFloat(array[i]);
        }
        return floatArray;
    }

    public static <T> T createObjectByClassName(String className) {
        try {
            return (T) createObjectByClass(Class.forName(className));
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T createObjectByClass(Class<? extends T> clazz) {
        try {
            Constructor<T> noArgsConstructor = (Constructor<T>) clazz.getDeclaredConstructor();
            noArgsConstructor.setAccessible(true);
            return noArgsConstructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static int getIndexOfEquals(int[][] array, int[] value) {
        for (int i = 0; i < array.length; i++) {
            if (Arrays.equals(array[i], value)) {
                return i;
            }
        }
        return -1;
    }

    public static ArrayList<byte[]> split(byte[] source, int length) {
        int chunksCount = (int) Math.ceil(((float) source.length) / length);
        ArrayList<byte[]> chunks = new ArrayList<>(chunksCount);
        int sourceIndex = 0;
        for (int i = 0; i < chunksCount; i++) {
            int chunkLength = length;
            if (i == (chunksCount - 1)) {
                chunkLength = (source.length - ((chunksCount - 1) * length));
            }
            byte[] chunk = new byte[chunkLength];
            for (int r = 0; r < chunk.length; r++) {
                chunk[r] = source[sourceIndex++];
            }
            chunks.add(chunk);
        }
        return chunks;
    }

    public static byte[] merge(Collection<byte[]> chunks) {
        int sourceLength = 0;
        for (byte[] chunk : chunks) {
            sourceLength += chunk.length;
        }
        byte[] source = new byte[sourceLength];
        int sourceIndex = 0;
        for (byte[] chunk : chunks) {
            System.arraycopy(chunk, 0, source, sourceIndex, chunk.length);
            sourceIndex += chunk.length;
        }
        return source;
    }
}
