package ryan.net.safetyprefdemo;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtil {

    public static final String NAME = "pref";

    private static SharedPreferences getPref() {
        return App.getContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    private static void setEncryptString(String key, String data) {
        String encryptData = KeyStoreManager.getInstance().encrypt(data);
        getPref().edit()
                .putString(key, encryptData)
                .apply();
    }

    private static String getDecryptString(String key) {
        String encryptData = getPref().getString(key, null);
        if (encryptData == null) {
            return null;
        }
        return KeyStoreManager.getInstance().decrypt(encryptData);
    }

    public static void setAESKey(String aesKey) {
        getPref().edit()
                .putString("aesKey", aesKey)
                .apply();
    }

    public static String getAESKey() {
        return getPref().getString("aesKey", null);
    }


    public static void setIV(String iv) {
        getPref().edit()
                .putString("iv", iv)
                .apply();
    }

    public static String getIV() {
        return getPref().getString("iv", null);
    }

    public static void setPassword(String password) {
        getPref().edit()
                .putString("password", password)
                .apply();
    }

    public static String getPassword() {
        return getPref().getString("password", null);
    }

    public static void setEncryptPassword(String password) {
        setEncryptString("encrypt_password", password);
    }

    public static String getEncryptPassword() {
        return getDecryptString("encrypt_password");
    }
}
