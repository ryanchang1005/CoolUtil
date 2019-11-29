package com.example.tmp.utils;

public class NativeUtils {

    public static final int DATA = 0;

    static {
        System.loadLibrary("native-lib");
    }

    public static native String getAppKey(int i);
}
