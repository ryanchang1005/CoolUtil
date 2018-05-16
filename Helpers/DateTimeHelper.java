package idv.haojun.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeHelper {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String now() {
        return SDF.format(new Date());
    }

    public static String date() {
        return now().split(" ")[0];
    }

    public static String time() {
        return now().split(" ")[1];
    }

    public static int compare(String a, String b) {
        try {
            Date d1 = SDF.parse(a);
            Date d2 = SDF.parse(b);
            if (d1.after(d2)) {
                return 1;
            } else if (d1.before(d2)) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
