package idv.haojun.permissionhelper;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DialogHelper {
    public static void showPermissionDeniedToSettingDialog(final Context context, String perm) {
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_permission_denied_to_setting, null);
        TextView message = v.findViewById(R.id.tv_dialog_permission_denied_to_setting_message);
        message.setText(perm);
        v.findViewById(R.id.bt_dialog_permission_denied_to_setting_go_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(
                        new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setData(Uri.fromParts("package", context.getPackageName(), null))
                );
            }
        });
        new AlertDialog.Builder(context)
                .setView(v)
                .show();
    }
}
