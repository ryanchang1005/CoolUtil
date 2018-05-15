package idv.haojun.pickortakepicturesample;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
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

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // request
    public static final int REQUEST_FROM_GALLERY = 1;
    public static final int REQUEST_TAKE_PHOTO = 2;

    // ui
    private ImageView ivPicture;

    private String mCurrentPhotoPath;
    private Uri uriTakePicture;
    private int pictureWidth, pictureHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pictureWidth = getResources().getDisplayMetrics().widthPixels;
        pictureHeight = getResources().getDisplayMetrics().heightPixels;

        ivPicture = findViewById(R.id.iv_picture);
        findViewById(R.id.bt_pick_or_take).setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void showItemDialog() {
        new AlertDialog.Builder(this)
                .setItems(new String[]{"Pick", "Take"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent, REQUEST_FROM_GALLERY);
                        } else if (which == 1) {
                            File file = null;
                            try {
                                file = createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (file != null) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                uriTakePicture = FileProvider.getUriForFile(MainActivity.this, SampleFileProvider.AUTH, file);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTakePicture);
                                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                            }
                        }
                    }
                })
                .show();
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
                Picasso.get()
                        .load(data.getData())
                        .centerCrop()
                        .resize(pictureWidth, pictureHeight)
                        .into(ivPicture);
                break;
            case REQUEST_TAKE_PHOTO:
                Picasso.get()
                        .load(uriTakePicture)
                        .centerCrop()
                        .resize(pictureWidth, pictureHeight)
                        .into(ivPicture);
                break;
        }
    }
}
