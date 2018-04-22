package idv.haojun.chatsocket;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefHelper {
    private static SharedPreferences getPref(Context context) {
        return context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    public static String getUserName(Context context) {
        return getPref(context).getString("userName", "");
    }

    public static void setUserName(Context context, String userName) {
        getPref(context).edit().putString("userName", userName).apply();
    }

}
