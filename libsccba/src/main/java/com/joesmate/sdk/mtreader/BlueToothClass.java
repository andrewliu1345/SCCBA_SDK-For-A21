package com.joesmate.sdk.mtreader;

/**
 * Created by andre on 2017/7/25 .
 */

public class BlueToothClass {
    static {
        System.loadLibrary("joesmate.a21.mt3x32.1.0.0");
    }

    public static native int WriteBMP(byte[] var0, byte[] var1, int var2, int var3);

    public static native int ParseTLV(byte[] var0, byte[] var1, byte[] var2);

    public static native int mt8desencrypt(byte[] var0, byte[] var1, int var2, byte[] var3);

    public static native int mt8desdecrypt(byte[] var0, byte[] var1, int var2, byte[] var3);
}
