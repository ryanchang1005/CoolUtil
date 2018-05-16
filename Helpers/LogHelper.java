package idv.haojun.helpers;


import android.util.Log;

public class LogHelper {
    private static final String TAG = "@@@";

    public static void d(String msg) {
        if (!BuildConfig.DEBUG) return;
        Log.d(TAG, msg);
    }
}
