package idv.haojun.servicedemo;

public class MyClientFactory {
    private static IMyClient iMyClient;

    public static IMyClient getInstance() {
        if (iMyClient == null) {
            iMyClient = new MyClient(App.getInstance());
        }
        return iMyClient;
    }
}
