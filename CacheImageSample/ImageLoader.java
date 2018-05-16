package idv.haojun.cacheimagesample;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;

public class ImageLoader {
    public static void load(Context context, String url, ImageView imageView) {
        String fileName = EncryptHelper.md5(url).substring(0, 5);
        File file = FileHelper.getFileInAppFolder(context, fileName);
        if (file != null) {
            L.d("load storage:" + fileName);
            // load storage image
            Picasso.get()
                    .load(file)
                    .into(imageView);
        } else {
            L.d("load network:" + url);
            // load network image
            Picasso.get()
                    .load(url)
                    .into(imageView);

            // download image
            new ImageDownloader(context, fileName).execute(url);
        }
    }

    private static class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        private WeakReference<Context> weakReference;
        private String fileName;

        ImageDownloader(Context context, String fileName) {
            this.weakReference = new WeakReference<>(context);
            this.fileName = fileName;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                L.d("download:" + params[0]);
                java.net.URL url = new java.net.URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (weakReference.get() != null) {
                L.d("download finish:" + fileName);
                Context context = weakReference.get();
                Bitmap newBitmap = BitmapHelper.resize(bitmap, 1000, 1000);
                L.d("ready to save:" + fileName);
                File f = BitmapHelper.bitmap2JPGFile(context, newBitmap, fileName);
                L.d("save:" + fileName + (f.exists() ? " success" : " fail"));

            }
        }
    }
}
