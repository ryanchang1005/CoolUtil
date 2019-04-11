package ryan.net.safetyprefdemo;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static App getInstance() {
        return app;
    }

    public static Context getContext(){
        return app.getApplicationContext();
    }
}
