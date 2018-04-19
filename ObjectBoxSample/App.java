package idv.haojun.objectboxdemo;

import android.app.Application;

import io.objectbox.BoxStore;

public class App extends Application {

    private static App instance;

    private BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        boxStore = MyObjectBox.builder().androidContext(App.this).build();
    }

    public static App getInstance() {
        return instance;
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }
}
