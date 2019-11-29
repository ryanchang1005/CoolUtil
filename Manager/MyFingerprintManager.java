package com.example.tmp.manager;

import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;

public class MyFingerprintManager {

    public interface Callback {
        void onSuccess();

        void onError(int errorCode);

        void onFail();
    }

    private KeyguardManager mKeyguardManager;
    private FingerprintManager mFingerprintManager;
    private CancellationSignal cancellationSignal;
    private Callback callback;
    private boolean isListening;

    public MyFingerprintManager(Context context, Callback callback) {
        mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mFingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        }
        this.callback = callback;
    }

    public boolean isListening() {
        return isListening;
    }

    public boolean isSupportFingerprint() {
        // version not support
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }

        // no set screen lock
        if (!mKeyguardManager.isKeyguardSecure()) {
            return false;
        }

        // version not support(construct)
        if (mFingerprintManager == null) {
            return false;
        }

        // hardware not support
        if (!mFingerprintManager.isHardwareDetected()) {
            return false;
        }

        // system list not exist any fingerprint
        return mFingerprintManager.hasEnrolledFingerprints();
    }

    public void startListening() {
        if (cancellationSignal == null) {
            cancellationSignal = new CancellationSignal();
        }
        isListening = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mFingerprintManager.authenticate(null,
                    cancellationSignal,
                    0,
                    new FingerprintManager.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationError(int errorCode, CharSequence errString) {
                            isListening = false;
                            if (callback != null) {
                                callback.onError(errorCode);
                            }
                        }

                        @Override
                        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                            isListening = false;
                            if (callback != null) {
                                callback.onSuccess();
                            }
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            isListening = false;
                            if (callback != null) {
                                callback.onFail();
                            }
                        }
                    },
                    null);
        }

    }

    public void stopListening() {
        if (cancellationSignal != null) {
            isListening = false;
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }
}
