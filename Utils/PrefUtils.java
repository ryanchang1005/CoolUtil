package com.example.tmp.utils;

import android.content.SharedPreferences;

import java.util.Set;

public class PrefUtils {

    public static final String PREF_NAME = "LpXHwRw_YIxhFnQ4NSI";

    public static final String AES_KEY = "LpXHwRwgwX1vI9dG2tr";
    public static final String IV = "LpXHwRw2VFywKHs3TdK";
    public static final String USER_ID = "LpXHwRx46FsI00Cryqv";
    public static final String USER_TOKEN = "LpXHwRxgWYoOjqMRyRc";
    public static final String USER_PRIVATE_KEY = "LpXHwRx2oiBb25qFKxc";
    public static final String USER_PUBLIC_KEY = "LpXHwRxZ2rUAYtzUKcg";
    public static final String USE_FINGERPRINT = "LpXHwRxG-MMIEenJIU_";
    public static final String USE_APP_PIN = "LpXHwRxLFrkkEzu9ukY";
    public static final String IS_LOGIN = "LpXHwRx1PxsTWy7c78p";

    private final SharedPreferences mSharedPreferences;

    public PrefUtils(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    public void setString(String key, String data) {
        mSharedPreferences.edit()
                .putString(key, data)
                .apply();
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, null);
    }

    public void setStringSet(String key, Set<String> set) {
        mSharedPreferences.edit()
                .putStringSet(key, set)
                .apply();
    }

    public Set<String> getStringSet(String key) {
        return mSharedPreferences.getStringSet(key, null);
    }

    public void setBoolean(String key, boolean data) {
        mSharedPreferences.edit()
                .putBoolean(key, data)
                .apply();
    }

    public boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public void clear() {
        mSharedPreferences.edit().clear().apply();
    }
}
