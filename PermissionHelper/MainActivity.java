package idv.haojun.permissionsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_camera).setOnClickListener(this);
        findViewById(R.id.bt_storage).setOnClickListener(this);
        findViewById(R.id.bt_location).setOnClickListener(this);
        findViewById(R.id.bt_audio).setOnClickListener(this);
    }

    private void openCamera() {
        if (!PermUtil.hasCameraPermission(this)) {
            return;
        }
        Toast.makeText(this, "Using camera", Toast.LENGTH_SHORT).show();
    }

    private void openStorage() {
        if (!PermUtil.hasStoragePermission(this)) {
            return;
        }
        Toast.makeText(this, "Using storage", Toast.LENGTH_SHORT).show();
    }

    private void openLocation() {
        if (!PermUtil.hasLocationPermission(this)) {
            return;
        }
        Toast.makeText(this, "Using location", Toast.LENGTH_SHORT).show();
    }

    private void openAudio() {
        if (!PermUtil.hasAudioPermission(this)) {
            return;
        }
        Toast.makeText(this, "Using audio", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_camera:
                openCamera();
                break;
            case R.id.bt_storage:
                openStorage();
                break;
            case R.id.bt_location:
                openLocation();
                break;
            case R.id.bt_audio:
                openAudio();
                break;
        }
    }
}
