package com.example.okhttpdemo.okhttp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Response;

public abstract class CallBackUtil<T> {
    public static Handler mMainHandler = new Handler(Looper.getMainLooper());


    public void onProgress(float progress, long total) {
    }

    public void onError(final Call call, final Exception e) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                onFailure(call, e);
            }
        });
    }

    public void onSuccess(Call call, Response response) {
        final T obj = onParseResponse(call, response);
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                onResponse(obj);
            }
        });
    }


    /**
     * 解析response，在子執行續執行
     */
    public abstract T onParseResponse(Call call, Response response);

    /**
     * 網路請求失敗，在UI執行續執行
     */
    public abstract void onFailure(Call call, Exception e);

    /**
     * 網路請求成功，在UI執行續執行
     */
    public abstract void onResponse(T response);


    /**
     * 回傳預設的Response
     */
    public static abstract class CallBackDefault extends CallBackUtil<Response> {
        @Override
        public Response onParseResponse(Call call, Response response) {
            return response;
        }
    }

    /**
     * 回傳Response的字串
     */
    public static abstract class CallBackString extends CallBackUtil<String> {
        @Override
        public String onParseResponse(Call call, Response response) {
            try {
                return response.body().string();
            } catch (IOException e) {
                new RuntimeException("failure");
                return "";
            }
        }
    }

    public static abstract class CallBackBitmap extends CallBackUtil<Bitmap> {
        private int mTargetWidth;
        private int mTargetHeight;

        public CallBackBitmap() {
        }

        public CallBackBitmap(int targetWidth, int targetHeight) {
            mTargetWidth = targetWidth;
            mTargetHeight = targetHeight;
        }

        public CallBackBitmap(ImageView imageView) {
            int width = imageView.getWidth();
            int height = imageView.getHeight();
            if (width <= 0 || height <= 0) {
                throw new RuntimeException("無法取得ImageView的width或height");
            }
            mTargetWidth = width;
            mTargetHeight = height;
        }

        @Override
        public Bitmap onParseResponse(Call call, Response response) {
            if (mTargetWidth == 0 || mTargetHeight == 0) {
                return BitmapFactory.decodeStream(response.body().byteStream());
            } else {
                return getZoomBitmap(response);
            }
        }

        /**
         * 壓縮圖片，避免OOM
         */
        private Bitmap getZoomBitmap(Response response) {
            byte[] data = null;
            try {
                data = response.body().bytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeByteArray(data, 0, data.length, options);
            int picWidth = options.outWidth;
            int picHeight = options.outHeight;
            int sampleSize = 1;
            int heightRatio = (int) Math.floor((float) picWidth / (float) mTargetWidth);
            int widthRatio = (int) Math.floor((float) picHeight / (float) mTargetHeight);
            if (heightRatio > 1 || widthRatio > 1) {
                sampleSize = Math.max(heightRatio, widthRatio);
            }
            options.inSampleSize = sampleSize;
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

            if (bitmap == null) {
                throw new RuntimeException("Failed to decode stream.");
            }
            return bitmap;
        }
    }

    /**
     * 下載檔案
     */
    public static abstract class CallBackFile extends CallBackUtil<File> {

        private final String mDestFileDir;
        private final String mDestFileName;

        /**
         * @param destFileDir:目標資料夾
         * @param destFileName：目標檔案名稱
         */
        public CallBackFile(String destFileDir, String destFileName) {
            mDestFileDir = destFileDir;
            mDestFileName = destFileName;
        }

        @Override
        public File onParseResponse(Call call, Response response) {

            InputStream is = null;
            byte[] buf = new byte[1024 * 8];
            int len;
            FileOutputStream fos = null;
            try {
                is = response.body().byteStream();
                final long total = response.body().contentLength();

                long sum = 0;

                File dir = new File(mDestFileDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, mDestFileName);
                fos = new FileOutputStream(file);
                while ((len = is.read(buf)) != -1) {
                    sum += len;
                    fos.write(buf, 0, len);
                    final long finalSum = sum;
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onProgress(finalSum * 100.0f / total, total);
                        }
                    });
                }
                fos.flush();

                return file;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    response.body().close();
                    if (is != null) is.close();
                } catch (IOException e) {
                }
                try {
                    if (fos != null) fos.close();
                } catch (IOException e) {
                }

            }
            return null;
        }
    }

}