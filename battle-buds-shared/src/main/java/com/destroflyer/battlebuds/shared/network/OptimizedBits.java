package com.destroflyer.battlebuds.shared.network;

public class OptimizedBits {
    public static final int SIGNED_INT_TO_8 = 4;
    public static final int SIGNED_INT_TO_16 = 5;
    public static final int SIGNED_INT_TO_32 = 6;
    public static final int SIGNED_INT_TO_64 = 7;
    public static final int SIGNED_INT_TO_128 = 8;
    public static final int SIGNED_INT_TO_2048 = 12;
    public static final int SIGNED_INT_TO_4096 = 13;
    public static final int SIGNED_INT_TO_1048576 = 21;
    public static final int SIGNED_INT_FULL = 32;

    public static final int SIGNED_LONG_FULL = 64;

    public static final int SIGNED_FLOAT_FULL = 32;
    public static final int SIGNED_FLOAT_FULL_MANTISSA = 23;
    public static final int SIGNED_FLOAT_FULL_EXPONENT = 8;
    public static final int SIGNED_FLOAT_UNPRECISE_MANTISSA = 20;
    public static final int SIGNED_FLOAT_UNPRECISE_EXPONENT = 8;

    public static final int STRING_CHARACTERS_TO_128 = 10;

    public static final int SIGNED_INT_FOR_OBJECT_ID = SIGNED_INT_TO_1048576;
}
