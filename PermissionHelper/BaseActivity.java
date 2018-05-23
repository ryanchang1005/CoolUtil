package idv.haojun.permissionsample;


import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class BaseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (perms.size() != 1) return;
        String perm = perms.get(0);
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            needToSettingDialog(getPermissionDeniedMessageId(perm));
        }
    }

    private int getPermissionDeniedMessageId(String perm) {
        switch (perm) {
            case Manifest.permission.CAMERA:
                return R.string.permission_denied_camera;
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                return R.string.permission_denied_storage;
            case Manifest.permission.ACCESS_FINE_LOCATION:
                return R.string.permission_denied_location;
            case Manifest.permission.RECORD_AUDIO:
                return R.string.permission_denied_audio;
        }
        return 0;
    }

    private void needToSettingDialog(int messageId) {

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_nedd_to_setting, null);
        TextView tvMessage = v.findViewById(R.id.tv_dialog_nedd_to_setting_message);
        Button btGoSetting = v.findViewById(R.id.bt_dialog_nedd_to_setting_go_setting);

        tvMessage.setText(messageId);
        btGoSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setData(Uri.fromParts("package", getPackageName(), null)),
                        0);
            }
        });

        new AlertDialog.Builder(this)
                .setView(v)
                .show();
    }
}
