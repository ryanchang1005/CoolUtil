package com.example.pickimage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.theartofdev.edmodo.cropper.CropImageView;

import butterknife.BindView;
import butterknife.OnClick;

public class CropActivity extends BaseActivity {

    @BindView(R.id.cropImageView)
    CropImageView cropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.crop_activity);
        super.onCreate(savedInstanceState);

        Uri uri = Uri.parse(getIntent().getStringExtra("uri"));

        cropImageView.setImageUriAsync(uri);
    }

    @SuppressLint("StaticFieldLeak")
    private class SaveCropFile extends AsyncTask<Void, Void, String> {
        Bitmap bitmap;

        SaveCropFile() {
            this.bitmap = cropImageView.getCroppedImage();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            Bitmap resizedBitmap = BitmapUtil.resize(
                    bitmap,
                    PickImageConst.IMAGE_RESIZE_WIDTH,
                    PickImageConst.IMAGE_RESIZE_WIDTH
            );
            return BitmapUtil.bitmap2JPGFile(getCacheDir(), resizedBitmap, PickImageConst.CROP_IMAGE_FILE_NAME).getAbsolutePath();
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            if (!isFinishing()) {
                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @OnClick(R.id.btnConfirm)
    public void onConfirm(View v) {
        new SaveCropFile().execute();
    }
}
