package idv.haojun.permissionhelper;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {

    public static final int REQUEST_PERMISSION = 0;

    public interface PermissionCallbacks {

        void onPermissionsGranted(int requestCode, List<String> perms);

        void onPermissionsDenied(int requestCode, List<String> perms);
    }

    public static boolean hasReadStoragePermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            return false;
        }
        return true;
    }

    public static boolean hasCameraPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION);
            return false;
        }
        return true;
    }

    public static boolean hasRecordAudioPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION);
            return false;
        }
        return true;
    }

    public static void onRequestPermissionsResult(int requestCode,
                                                  String[] permissions,
                                                  int[] grantResults,
                                                  PermissionCallbacks callbacks) {
        // Make a collection of granted and denied permissions from the request.
        List<String> granted = new ArrayList<>();
        List<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                denied.add(perm);
            }
        }

        // Report granted permissions, if any.
        if (!granted.isEmpty()) {
            callbacks.onPermissionsGranted(requestCode, granted);
        }

        // Report denied permissions, if any.
        if (!denied.isEmpty()) {
            callbacks.onPermissionsDenied(requestCode, denied);
        }
    }

    public static boolean permissionPermanentlyDenied(Activity activity, String perms) {
        return !ActivityCompat.shouldShowRequestPermissionRationale(activity, perms);
    }
}
