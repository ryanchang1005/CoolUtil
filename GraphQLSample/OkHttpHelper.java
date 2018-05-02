package idv.haojun.graphqlsample;


import okhttp3.OkHttpClient;

public class OkHttpHelper {
    public static OkHttpHelper instance = new OkHttpHelper();
    private OkHttpClient okHttpClient;

    public static OkHttpHelper getInstance() {
        return instance;
    }

    private OkHttpHelper() {
        okHttpClient = new OkHttpClient.Builder()
                .build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
