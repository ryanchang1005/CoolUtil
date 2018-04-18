package idv.haojun.servicedemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class MyClient implements IMyClient {

    public static final String TAG = "MyClient";

    private Context context;
    private Set<IMyClientCallback> callbacks;
    private IMyService iMyService;
    private IMyServiceCallback iMyServiceCallback;

    // status
    private boolean isBinding = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");

            iMyService = IMyService.Stub.asInterface(service);

            iMyServiceCallback = new IMyServiceCallback.Stub() {
                @Override
                public void onConnect() {
                    for (IMyClientCallback callback : callbacks)
                        callback.onConnect();
                }

                @Override
                public void onDisconnect() {
                    for (IMyClientCallback callback : callbacks)
                        callback.onDisconnect();
                }

                @Override
                public void onConnectError() {
                    for (IMyClientCallback callback : callbacks)
                        callback.onConnectError();
                }

                @Override
                public void onConnectTimeout() {
                    for (IMyClientCallback callback : callbacks)
                        callback.onConnectTimeout();
                }

                @Override
                public void onReceiveMessage() {
                    for (IMyClientCallback callback : callbacks)
                        callback.onReceiveMessage();
                }

                @Override
                public void onUserJoin() {
                    for (IMyClientCallback callback : callbacks)
                        callback.onUserJoin();
                }

                @Override
                public void onUserLeft() {
                    for (IMyClientCallback callback : callbacks)
                        callback.onUserLeft();
                }

                @Override
                public void onTyping() {
                    for (IMyClientCallback callback : callbacks)
                        callback.onTyping();
                }

                @Override
                public void onStopTyping() {
                    for (IMyClientCallback callback : callbacks)
                        callback.onStopTyping();
                }
            };

            try {
                iMyService.registerCallback(iMyServiceCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    public MyClient(Context context) {
        // init
        this.context = context;
        this.callbacks = new HashSet<>();

        // bind service
        context.bindService(
                new Intent(context, MyService.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE
        );
    }

    @Override
    public void registerCallback(IMyClientCallback callback) {
        callbacks.add(callback);
    }

    @Override
    public void unregisterCallback(IMyClientCallback callback) {
        if (callbacks.contains(callback))
            callbacks.remove(callback);
    }

    @Override
    public boolean isConnecting() throws RemoteException {
        return iMyService.isConnecting();
    }

    @Override
    public boolean isBinding() {
        return isBinding;
    }

    @Override
    public void addUser(String name) throws RemoteException {
        iMyService.addUser(name);
    }

    @Override
    public void typing() throws RemoteException {
        iMyService.typing();
    }

    @Override
    public void stopTyping() throws RemoteException {
        iMyService.stopTyping();
    }

    @Override
    public void addMessage(String message) throws RemoteException {
        iMyService.addMessage(message);
    }
}
