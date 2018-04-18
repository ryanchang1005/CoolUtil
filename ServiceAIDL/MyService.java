package idv.haojun.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

public class MyService extends Service {

    private IMyServiceCallback serviceCallback;

    private boolean isConnecting = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private final IMyService.Stub binder = new IMyService.Stub() {
        @Override
        public void registerCallback(IMyServiceCallback callback) {
            serviceCallback = callback;
        }

        @Override
        public boolean isConnecting() {
            return isConnecting;
        }

        @Override
        public void addUser(String name) {

        }

        @Override
        public void typing() {

        }

        @Override
        public void stopTyping() {

        }

        @Override
        public void addMessage(String message) {

        }
    };
}
