package ryan.net.safetyprefdemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class ImageVerifyManager {
    private static final char[] CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
    };

    private int paddingLeft, paddingTop;
    private StringBuilder stringBuilder = new StringBuilder();
    private Random random = new Random();
    // 驗證碼長度
    private static final int DEFAULT_CODE_LENGTH = 4;
    // 驗證碼字體大小
    private static final int DEFAULT_FONT_SIZE = 60;
    // 干擾線
    private static final int DEFAULT_LINE_NUMBER = 3;
    // 左邊距
    private static final int BASE_PADDING_LEFT = 40;
    // 左邊距範圍值
    private static final int RANGE_PADDING_LEFT = 30;
    // 上邊距
    private static final int BASE_PADDING_TOP = 70;
    // 上邊距範圍值
    private static final int RANGE_PADDING_TOP = 15;
    // 默認寬度，圖片總寬
    private int DEFAULT_WIDTH = 300;
    // 默認高度，圖片總高
    private int DEFAULT_HEIGHT = 100;
    // 默認背景顏色
    private static final int DEFAULT_COLOR = 0xDF;
    private String code;

    public ImageVerifyManager setDefaultSize(int width, int height) {
        this.DEFAULT_WIDTH = width;
        this.DEFAULT_HEIGHT = height;
        return this;
    }

    // 生成bitmap形式的驗證碼圖片可通過image.setBitmap();獲得展示圖片
    public Bitmap createBitmap() {
        paddingLeft = 0;
        paddingTop = 0;

        Bitmap bitmap = Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        code = createCode();
        canvas.drawColor(Color.rgb(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR));
        Paint paint = new Paint();
        paint.setTextSize(DEFAULT_FONT_SIZE);

        // 驗證碼
        for (int i = 0; i < code.length(); i++) {
            randomTextStyle(paint);
            randomPadding();
            canvas.drawText(String.valueOf(code.charAt(i)), paddingLeft, paddingTop, paint);
        }
        // 干擾線
        for (int j = 0; j < DEFAULT_LINE_NUMBER; j++) {
            drawLine(canvas, paint);
        }
        canvas.save();
        canvas.restore();
        return bitmap;
    }

    /**
     * 得到圖片驗證碼字串資料
     */
    public String getCode() {
        return code;
    }

    // 產生干擾線
    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();
        int startX = random.nextInt(DEFAULT_WIDTH);
        int startY = random.nextInt(DEFAULT_HEIGHT);
        int stopX = random.nextInt(DEFAULT_WIDTH);
        int stopY = random.nextInt(DEFAULT_HEIGHT);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    // 隨機顏色
    private int randomColor() {
        // 使用前先清空內容
        stringBuilder.delete(0, stringBuilder.length());
        String haxString;
        for (int i = 0; i < 3; i++) {
            haxString = Integer.toHexString(random.nextInt(0xFF));
            if (haxString.length() == 1) {
                haxString = "0" + haxString;
            }
            stringBuilder.append(haxString);
        }
        return Color.parseColor("#" + stringBuilder.toString());
    }

    // 隨機間距
    private void randomPadding() {
        paddingLeft += BASE_PADDING_LEFT + random.nextInt(RANGE_PADDING_LEFT);
        paddingTop = BASE_PADDING_TOP + random.nextInt(RANGE_PADDING_TOP);
    }

    // 隨機文本樣式
    private void randomTextStyle(Paint paint) {
        int color = randomColor();
        paint.setColor(color);
        // true為粗體，false為非粗體
        paint.setFakeBoldText(random.nextBoolean());
        float skewX = random.nextInt(11) / 10;
        skewX = random.nextBoolean() ? skewX : -skewX;
        // float類型參數，負數表示右斜，整數左斜
        paint.setTextSkewX(skewX);
        // true為下滑線，false為非下滑線
//        paint.setUnderlineText(true);
        //true為刪除線，false為非刪除線
//        paint.setStrikeThruText(true);
    }

    // 產生驗證碼
    private String createCode() {
        stringBuilder.delete(0, stringBuilder.length());
        for (int i = 0; i < DEFAULT_CODE_LENGTH; i++) {
            stringBuilder.append(CHARS[random.nextInt(CHARS
                    .length)]);
        }
        return stringBuilder.toString();
    }
}