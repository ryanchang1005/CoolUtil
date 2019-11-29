package com.example.tmp.utils;

import android.util.Base64;

public class StringUtils {

    public static String toBase64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }
}
