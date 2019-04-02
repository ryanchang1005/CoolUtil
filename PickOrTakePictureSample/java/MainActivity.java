package com.example.pickimagedemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // request
    public static final int REQUEST_FROM_GALLERY = 1;
    public static final int REQUEST_TAKE_PHOTO = 2;

    // ui
    private ImageView ivPicture;

    private Uri uriTakePicture;
    private File tmpFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivPicture = findViewById(R.id.iv_picture);
        findViewById(R.id.bt_pick_or_take).setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
        }
    }

    private void showItemDialog() {
        new AlertDialog.Builder(this)
                .setItems(new String[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 暫存圖片的資料夾
                        File tmpDir = FileHelper.getPicturesDir(MainActivity.this);

                        // 從圖庫或拍照
                        if (which == 0) {
                            tmpFile = FileHelper.createFile(tmpDir, "gallery.jpg");
                            if (tmpFile != null) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, REQUEST_FROM_GALLERY);
                            }
                        } else if (which == 1) {
                            tmpFile = FileHelper.createFile(tmpDir, "camera.jpg");

                            if (tmpFile != null) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                uriTakePicture = FileProvider.getUriForFile(MainActivity.this, SampleFileProvider.AUTH, tmpFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTakePicture);
                                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                            }
                        }
                    }
                }).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_pick_or_take:
                showItemDialog();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_FROM_GALLERY:

                // 使用uri顯示圖片
                Glide.with(this)
                        .load(data.getData())
                        .into(ivPicture);

                // 複製uri成file
                copyUriToFile(data.getData());

                // 確認檔案存在
                log(tmpFile.exists() + "");
                log(tmpFile.getAbsolutePath());
                break;
            case REQUEST_TAKE_PHOTO:
                Glide.with(this)
                        .load(uriTakePicture)
                        .into(ivPicture);

                // 確認檔案存在
                log(tmpFile.exists() + "");
                log(tmpFile.getAbsolutePath());
                break;
        }
    }

    private void copyUriToFile(Uri uri) {
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            FileHelper.inputStreamToFile(is, tmpFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void log(String s) {
        Log.d("@@@", s);
    }
}
