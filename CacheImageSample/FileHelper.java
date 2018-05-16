package idv.haojun.cacheimagesample;


import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {
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

    public static File getAppFolder(Context context) {
        File appFolder = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_name));
        if (!appFolder.exists()) {
            appFolder.mkdir();
        }
        return appFolder;
    }

    public static boolean isFileNameInAppFolder(Context context, String targetFileName) {
        File appFolder = getAppFolder(context);
        for (File file : appFolder.listFiles()) {
            if (file.isFile() && file.getName().equals(targetFileName))
                return true;
        }
        return false;
    }

    public static File getFileInAppFolder(Context context, String fileName) {
        if (isFileNameInAppFolder(context, fileName)) {
            return new File(getAppFolder(context), fileName);
        }
        return null;
    }
}
