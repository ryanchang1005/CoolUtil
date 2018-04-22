package idv.haojun.sharedpreferencesdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefHelper {

    public static final String USER_API_KEY = "user_api_key";

    public static void setUserApiKey(Context context, String apiKey) {
        putString(context, USER_API_KEY, apiKey);
    }

    public static String getUserApiKey(Context context) {
        return getString(context, USER_API_KEY, "");
    }

    /////////////////////////////////////////////////////////////////////////

    public static int getInt(Context context, String key, int defaultValue) {
        return getSharedPreferences(context).getInt(key, defaultValue);
    }

    public static void putInt(Context context, String key, int value) {
        getSharedPreferences(context).edit().putInt(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        getSharedPreferences(context).edit().putBoolean(key, value).apply();
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getSharedPreferences(context).getString(key, defaultValue);
    }

    public static void putString(Context context, String key, String value) {
        getSharedPreferences(context).edit().putString(key, value).apply();
    }

    public static float getFloat(Context context, String key, Float defaultValue) {
        return getSharedPreferences(context).getFloat(key, defaultValue);
    }

    public static void putFloat(Context context, String key, float value) {
        getSharedPreferences(context).edit().putFloat(key, value).apply();
    }

    public static long getLong(Context context, String key, Long defaultValue) {
        return getSharedPreferences(context).getLong(key, defaultValue);
    }

    public static void putLong(Context context, String key, Long value) {
        getSharedPreferences(context).edit().putLong(key, value).apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
