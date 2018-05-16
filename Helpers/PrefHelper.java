package idv.haojun.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefHelper {
    private static final String FILE_SAMPLE = "sample";
    private static final String USER_NAME = "user_name";

    private static SharedPreferences getPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUserName(Context context, String userName) {
        getPref(context).edit().putString(USER_NAME, userName).apply();
    }

    public static String getUserName(Context context) {
        return getPref(context).getString(USER_NAME, "");
    }
}
