package com.example.pickimage;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class FileUtil {

    public static File createFile(File dir, String fileName) {
        try {
            File f = new File(dir, fileName);
            if (f.exists()) {
                f.delete();
            }
            return f.createNewFile() ? f : null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File writeFile(byte[] bArr, File file) {
        try {
            if (file == null) return null;
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bArr);
            fos.flush();
            fos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Uri file2Uri(File file) {
        return Uri.fromFile(file);
    }

    public static File getPicturesDir(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    public static boolean inputStreamToFile(InputStream is, File targetFile) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = is;

            outputStream = new FileOutputStream(targetFile);

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return targetFile.exists();
    }
}
