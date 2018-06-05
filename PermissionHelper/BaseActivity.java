package idv.haojun.permissionhelper;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class BaseActivity extends AppCompatActivity implements PermissionHelper.PermissionCallbacks {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (perms.size() != 1) return;
        String perm = perms.get(0);
        if (PermissionHelper.permissionPermanentlyDenied(this, perm)) {
            DialogHelper.showPermissionDeniedToSettingDialog(this, perm);
        }
    }
}
