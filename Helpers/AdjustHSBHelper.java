package idv.haojun.helpers;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class AdjustHSBHelper {
    // type
    public static final int HUE = 0;
    public static final int SAT = 1;
    public static final int BRI = 2;

    private static final int MIDDLE_VALUE = 127;
    // value
    private float hueValue = 0.0f;
    private float saturationValue = 1.0f;
    private float brightnessValue = 1.0f;
    // matrix
    private ColorMatrix mLightnessMatrix;
    private ColorMatrix mSaturationMatrix;
    private ColorMatrix mHueMatrix;
    private ColorMatrix mAllMatrix;

    // -180 ~ 180
    public void setHue(int hue) {
        hueValue = (hue - MIDDLE_VALUE) * 1.0F / MIDDLE_VALUE * 180;
    }

    // 0 ~ 2
    public void setSaturation(int sat) {
        saturationValue = sat * 1.0F / MIDDLE_VALUE;
    }

    // 0 ~ 2
    public void setBrightness(int bri) {
        brightnessValue = bri * 1.0F / MIDDLE_VALUE;
    }

    public Bitmap handleBitmap(Bitmap bitmap, int type) {
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        if (null == mAllMatrix) {
            mAllMatrix = new ColorMatrix();
        }

        if (null == mLightnessMatrix) {
            mLightnessMatrix = new ColorMatrix();
        }

        if (null == mSaturationMatrix) {
            mSaturationMatrix = new ColorMatrix();
        }

        if (null == mHueMatrix) {
            mHueMatrix = new ColorMatrix();
        }

        switch (type) {
            case HUE: // hue
                mHueMatrix.reset();
                mHueMatrix.setRotate(0, hueValue); // R
                mHueMatrix.setRotate(1, hueValue); // G
                mHueMatrix.setRotate(2, hueValue); // B
                break;
            case SAT: // saturation
                mSaturationMatrix.reset();
                mSaturationMatrix.setSaturation(saturationValue);
                break;
            case BRI: // brightness
                mLightnessMatrix.reset();
                mLightnessMatrix.setScale(brightnessValue, brightnessValue, brightnessValue, 1);
                break;
        }
        mAllMatrix.reset();
        mAllMatrix.postConcat(mHueMatrix);
        mAllMatrix.postConcat(mSaturationMatrix);
        mAllMatrix.postConcat(mLightnessMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bmp;
    }
}
