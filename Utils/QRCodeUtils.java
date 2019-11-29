package com.example.tmp.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

public class QRCodeUtils {

    public static Bitmap generate(Context context, String text) {
        int width = (int) (context.getResources().getDisplayMetrics().density * 250);

        Map<EncodeHintType, Object> hintsMap = new HashMap<>();
        hintsMap.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hintsMap.put(EncodeHintType.MARGIN, 0);
        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, width, hintsMap);
            int[] pixels = new int[width * width];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < width; j++) {
                    if (bitMatrix.get(j, i)) {
                        pixels[i * width + j] = 0xFF000000;
                    } else {
                        pixels[i * width + j] = 0xFFFFFFFF;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, width, width, Bitmap.Config.ARGB_8888);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
