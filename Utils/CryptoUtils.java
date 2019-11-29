package com.example.tmp.utils;

import android.os.Build;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class CryptoUtils {
    public static final String RSA = "RSA";
    public static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";
    public static final String SHA_256_WITH_RSA = "SHA256withRSA";

    // generate rsa 2048, keypair
    public static KeyPair generateRSAKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    // get public key string(base64) from keypair
    public static String getPublicKeyString(KeyPair keyPair) {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        return StringUtils.toBase64(rsaPublicKey.getEncoded());
    }

    // get private key string(base64) from keypair
    public static String getPrivateKeyString(KeyPair keyPair) {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        return StringUtils.toBase64(rsaPrivateKey.getEncoded());
    }

    // get PublicKey obj by string(base64)
    private static PublicKey getPublicKeyByString(String key) throws Exception {
        byte[] keyBytes = Base64.decode(key, Base64.NO_WRAP);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(keySpec);
    }

    // get PrivateKey obj by string(base64)
    private static PrivateKey getPrivateKeyByString(String key) throws Exception {
        byte[] keyBytes = Base64.decode(key, Base64.NO_WRAP);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory;
        // for lower android version
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            keyFactory = KeyFactory.getInstance(RSA, "BC");
        } else {
            keyFactory = KeyFactory.getInstance(RSA);
        }
        return keyFactory.generatePrivate(keySpec);
    }

    // encrypt data
    public static String encryptByPublicKey(String data, String publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKeyByString(publicKey));
            return StringUtils.toBase64(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // decrypt data
    public static String decrypt(String data, String privateKye) {
        try {
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKeyByString(privateKye));
            return new String(cipher.doFinal(Base64.decode(data, Base64.URL_SAFE)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // sign data
    public static String sign(String data, String privateKey) {
        try {
            String encodeStr = StringUtils.toBase64(data.getBytes(StandardCharsets.UTF_8));
            Signature signature = Signature.getInstance(SHA_256_WITH_RSA);
            signature.initSign(getPrivateKeyByString(privateKey));
            signature.update(encodeStr.getBytes(StandardCharsets.UTF_8));
            return StringUtils.toBase64(signature.sign());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // verify data
    public static boolean verify(String payload, String signedPayload, String publicKey) {
        try {
            Signature signature = Signature.getInstance(SHA_256_WITH_RSA);
            signature.initVerify(getPublicKeyByString(publicKey));

            signature.update(StringUtils.toBase64(payload.getBytes(StandardCharsets.UTF_8)).getBytes());

            byte[] signedPayloadContent = Base64.decode(signedPayload, Base64.NO_WRAP);
            return signature.verify(signedPayloadContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
