package idv.haojun.cacheimagesample;


import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PrefHelper {

    private static SharedPreferences getPref(Context context) {
        return context.getSharedPreferences("data", Context.MODE_PRIVATE);
    }

    public static void setImages(Context context, List<MyImage> images) {
        JSONArray jsonArray = new JSONArray();
        for (MyImage image : images) {
            jsonArray.put(image.getJSONObject());
        }

        getPref(context).edit().putString("images", jsonArray.toString()).apply();
    }

    public static List<MyImage> getImages(Context context) {
        List<MyImage> images = new ArrayList<>();
        String strJsonArray = getPref(context).getString("images", "");
        if (!strJsonArray.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(strJsonArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    MyImage image = new MyImage();
                    image.setUrl(jsonArray.getJSONObject(i).getString("url"));
                    image.setPath(jsonArray.getJSONObject(i).getString("path"));
                    images.add(image);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return images;
    }
}
