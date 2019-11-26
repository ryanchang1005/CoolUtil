package com.example.pickimage;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class PickImageActivity extends BaseActivity {

    // type params(string)
    public static final String TYPE_PICK_IMAGE = "TYPE_PICK_IMAGE"; // option
    public static final String TYPE_TAKE_PICTURE = "TYPE_TAKE_PICTURE"; // option

    private String mType;
    private boolean mIsCrop;

    // request
    public static final int RC_FROM_GALLERY = 1;
    public static final int RC_TAKE_PICTURE = 2;
    public static final int RC_CROP_IMAGE = 3;
    public static final int RC_ASK_PERMISSION = 4;

    private Uri uriTakePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pick_image);
        super.onCreate(savedInstanceState);

        mType = getIntent().getStringExtra("type");
        mIsCrop = getIntent().getBooleanExtra("isCrop", false);

        if (TYPE_PICK_IMAGE.equals(mType)) {
            pickImage();
        } else if (TYPE_TAKE_PICTURE.equals(mType)) {
            takePicture();
        }
    }

    private boolean isExternalStoragePermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isCameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void askExternalStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_ASK_PERMISSION);
    }

    private void askCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, RC_ASK_PERMISSION);
    }

    private void pickImage() {
        if (!isExternalStoragePermissionGranted()) {
            askExternalStoragePermission();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, RC_FROM_GALLERY);
    }

    private void takePicture() {
        if (!isExternalStoragePermissionGranted()) {
            askExternalStoragePermission();
            return;
        }
        if (!isCameraPermissionGranted()) {
            askCameraPermission();
            return;
        }

        File file = FileUtil.createFile(getCacheDir(), PickImageConst.TAKE_PICTURE_FILE_NAME);
        if (file == null) {
            Toast.makeText(this, R.string.pick_image_fail, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        uriTakePicture = FileProvider.getUriForFile(
                this,
                GenericFileProvider.AUTH,
                file
        );
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTakePicture);
        startActivityForResult(intent, RC_TAKE_PICTURE);
    }

    private void openCropActivity(Uri uri) {
        startActivityForResult(
                new Intent(this, CropActivity.class).putExtra("uri", uri.toString()),
                RC_CROP_IMAGE
        );
    }

    private String uriToCompressedFileName(Uri uri) {
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            File file = new File(getCacheDir(), PickImageConst.ORIGINAL_FILE_NAME);
            boolean success = FileUtil.inputStreamToFile(is, file);
            if (success) {
                Bitmap compressedBmp = BitmapUtil.resize(
                        BitmapFactory.decodeFile(file.getAbsolutePath()),
                        PickImageConst.IMAGE_RESIZE_WIDTH,
                        PickImageConst.IMAGE_RESIZE_WIDTH
                );
                return BitmapUtil.bitmap2JPGFile(getCacheDir(), compressedBmp, PickImageConst.COMPRESS_FILE_NAME).getAbsolutePath();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void executeCompressTask(Uri uri) {
        new Thread(() ->
                returnPath(uriToCompressedFileName(uri))
        ).start();
    }

    private void returnPath(String path) {
        setResult(RESULT_OK, new Intent().putExtra("path", path));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String permission = permissions[0];
        int grantResult = grantResults[0];
        if (grantResult == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        switch (requestCode) {
            case RC_FROM_GALLERY:
                if (mIsCrop) {
                    openCropActivity(data.getData());
                } else {
                    executeCompressTask(data.getData());
                }
                break;
            case RC_TAKE_PICTURE:
                if (mIsCrop) {
                    openCropActivity(uriTakePicture);
                } else {
                    executeCompressTask(uriTakePicture);
                }
                break;
            case RC_CROP_IMAGE:
                returnPath(data.getStringExtra("path"));
                break;
        }
    }
}
