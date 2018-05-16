package idv.haojun.cacheimagesample;


import org.json.JSONException;
import org.json.JSONObject;

public class MyImage {
    private String url;
    private String path;

    MyImage() {
        url = "";
        path = "";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setJSONObject(JSONObject jsonObject) {
        try {
            setUrl(jsonObject.getString("url"));
            setUrl(jsonObject.getString("path"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject getJSONObject() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", url);
            jsonObject.put("path", path);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
