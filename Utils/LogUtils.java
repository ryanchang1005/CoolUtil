package com.example.tmp.utils;

import android.util.Log;


public class LogUtils {

    public static final String TAG = "@@@@";

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String key, String msg) {
        Log.d(key, msg);
    }
}
