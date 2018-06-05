package idv.haojun.permissionhelper;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_pick_file).setOnClickListener(this);
        findViewById(R.id.bt_take_photo).setOnClickListener(this);
        findViewById(R.id.bt_audio_call).setOnClickListener(this);
        findViewById(R.id.bt_video_call).setOnClickListener(this);
    }

    private void pickFile() {
        if (!PermissionHelper.hasReadStoragePermission(this)) {
            return;
        }
        Toast.makeText(this, "pickFile", Toast.LENGTH_SHORT).show();
    }

    private void takePhoto() {
        if (!PermissionHelper.hasCameraPermission(this)) {
            return;
        }
        Toast.makeText(this, "takePhoto", Toast.LENGTH_SHORT).show();
    }

    private void audioCall() {
        if (!PermissionHelper.hasRecordAudioPermission(this)) {
            return;
        }
        Toast.makeText(this, "audioCall", Toast.LENGTH_SHORT).show();
    }

    private void videoCall() {
        if (!PermissionHelper.hasCameraPermission(this) || !PermissionHelper.hasRecordAudioPermission(this)) {
            return;
        }
        Toast.makeText(this, "videoCall", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_pick_file:
                pickFile();
                break;
            case R.id.bt_take_photo:
                takePhoto();
                break;
            case R.id.bt_audio_call:
                audioCall();
                break;
            case R.id.bt_video_call:
                videoCall();
                break;
        }
    }
}