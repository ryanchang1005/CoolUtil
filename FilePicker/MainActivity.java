package com.example.filepicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "@@@@";

    public static final int RC_PICK_IMAGE = 100;
    public static final int RC_PICK_VIDEO = 101;
    public static final int RC_PICK_FILE = 102;
    public static final int RC_REQUEST_PERMISSION = 200;

    private TextView tvResult;

    private AlertDialog loadingAlertDialog;

    // for permission granted auto execute function
    private int lastPickType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);

    }

    private boolean isExternalStoragePermissionDenied() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED;
    }

    private void requestExternalStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_REQUEST_PERMISSION);
    }

    public void pickImage(View view) {
        lastPickType = RC_PICK_IMAGE;
        if (isExternalStoragePermissionDenied()) {
            requestExternalStoragePermission();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(intent, getString(R.string.pick_image));
        startActivityForResult(chooserIntent, RC_PICK_IMAGE);
    }

    public void pickVideo(View view) {
        lastPickType = RC_PICK_VIDEO;
        if (isExternalStoragePermissionDenied()) {
            requestExternalStoragePermission();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");

        Intent chooserIntent = Intent.createChooser(intent, getString(R.string.pick_video));
        startActivityForResult(chooserIntent, RC_PICK_VIDEO);
    }

    public void pickFile(View view) {
        lastPickType = RC_PICK_FILE;
        if (isExternalStoragePermissionDenied()) {
            requestExternalStoragePermission();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");

        Intent chooserIntent = Intent.createChooser(intent, getString(R.string.pick_file));
        startActivityForResult(chooserIntent, RC_PICK_FILE);
    }

    private void showLoadingDialog() {
        if (loadingAlertDialog == null) {
            loadingAlertDialog = new AlertDialog.Builder(this)
                    .setView(LayoutInflater.from(this).inflate(R.layout.loading_dialog, null))
                    .create();
        }
        loadingAlertDialog.show();
    }

    private void dismissLoadingDialog() {
        if (loadingAlertDialog != null && loadingAlertDialog.isShowing()) {
            loadingAlertDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void createTmpFileTask(final Uri uri) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showLoadingDialog();
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // fake loading
                    Thread.sleep(1000);

                    String tmpFilename = "tmp_" + new SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault()).format(new Date());
                    File tmpFile = new File(getCacheDir(), tmpFilename);

                    InputStream inputStream = getContentResolver().openInputStream(uri);

                    boolean success = FileUtils.inputStreamToFile(inputStream, tmpFile);

                    if (success) {
                        return tmpFile.getAbsolutePath();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (isFinishing()) return;
                dismissLoadingDialog();

                tvResult.setText(result == null ? "fail" : result);
            }
        }.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != RC_REQUEST_PERMISSION) return;

        if (PackageManager.PERMISSION_DENIED == grantResults[0]) {
            Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            return;
        }

        // auto execute last function
        switch (lastPickType) {
            case RC_PICK_IMAGE:
                pickImage(null);
                break;
            case RC_PICK_VIDEO:
                pickVideo(null);
                break;
            case RC_PICK_FILE:
                pickFile(null);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (data == null) return;

        createTmpFileTask(data.getData());
    }
}
