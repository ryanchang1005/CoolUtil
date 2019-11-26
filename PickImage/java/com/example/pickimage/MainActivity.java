package com.example.pickimage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    public static final int RC_PICK_IMAGE = 0;
    public static final int RC_CAMERA = 1;

    @BindView(R.id.tvPath)
    TextView tvPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.btnFromGallery)
    public void onFromGalleryClick(View view) {
        startActivityForResult(
                new Intent(this, PickImageActivity.class)
                        .putExtra("type", PickImageActivity.TYPE_PICK_IMAGE)
                        .putExtra("isCrop", false),
                RC_PICK_IMAGE
        );
    }

    @OnClick(R.id.btnFromCamera)
    public void onFromCameraClick(View view) {
        startActivityForResult(
                new Intent(this, PickImageActivity.class)
                        .putExtra("type", PickImageActivity.TYPE_TAKE_PICTURE)
                        .putExtra("isCrop", false),
                RC_CAMERA
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        if (requestCode == RC_PICK_IMAGE || requestCode == RC_CAMERA) {
            String path = data.getStringExtra("path");
            if (path == null) {
                tvPath.setText(R.string.pick_image_fail);
            } else {
                tvPath.setText(path);
                Bitmap bmp = BitmapFactory.decodeFile(path);
                L.d("len:" + BitmapUtil.bitmap2Base64(bmp).length());
                L.d("len2:" + new File(path).length());
            }
        }
    }
}
