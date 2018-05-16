package idv.haojun.cacheimagesample;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class BitmapHelper {
    public static Bitmap resize(Bitmap bmp, int maxW, int maxH) {
        if (bmp == null)
            return null;
        // old w , h
        int oldWidth = bmp.getWidth();
        int oldHeight = bmp.getHeight();
        // origin is ok
        if (oldWidth <= maxW && oldHeight <= maxH)
            return bmp;
        // new format
        int newWidth = oldWidth;
        int newHeight = oldHeight;
        do {
            newWidth *= 0.9;
            newHeight *= 0.9;
        } while (newWidth > maxW || newHeight > maxH);
        // scale %
        float scaleWidth = ((float) newWidth) / oldWidth;
        float scaleHeight = ((float) newHeight) / oldHeight;
        // scale matrix params
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bmp, 0, 0, oldWidth, oldHeight, matrix, true);
    }
    public static File bitmap2JPGFile(Context context, Bitmap bitmap, String fileName) {
        // create a jpg file to cache dir
        File f = FileHelper.createFile(FileHelper.getAppFolder(context), fileName);
        // convert to array
        byte[] bArr = bitmap2JPGByteArray(bitmap);
        // write to file
        return FileHelper.writeFile(bArr, f);
    }
    public static byte[] bitmap2JPGByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
