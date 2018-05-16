package idv.haojun.cacheimagesample;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptHelper {

    public static String md5(String text) {
        return getMessageDigest("MD5", text);
    }

    private static String getMessageDigest(String algorithm, String text) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(text.getBytes());
            byte[] bArr = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bArr) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
