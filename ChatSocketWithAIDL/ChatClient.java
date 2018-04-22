package idv.haojun.chatsocket;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class ChatClient implements IChatClient {

    public static final String TAG = "ChatClient";

    private Context context;
    private Set<IChatClientCallback> callbacks;
    private IChatService iChatService;
    private IChatServiceCallback iChatServiceCallback;

    // status
    private boolean isBinding = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");

            iChatService = IChatService.Stub.asInterface(service);

            iChatServiceCallback = new IChatServiceCallback.Stub() {
                @Override
                public void onConnect() throws RemoteException {
                    for (IChatClientCallback callback : callbacks)
                        callback.onConnect();
                }

                @Override
                public void onDisconnect() throws RemoteException {
                    for (IChatClientCallback callback : callbacks)
                        callback.onDisconnect();
                }

                @Override
                public void onConnectError() throws RemoteException {
                    for (IChatClientCallback callback : callbacks)
                        callback.onConnectError();
                }

                @Override
                public void onConnectTimeout() throws RemoteException {
                    for (IChatClientCallback callback : callbacks)
                        callback.onConnectTimeout();
                }

                @Override
                public void onLogin(String str) throws RemoteException {
                    for (IChatClientCallback callback : callbacks)
                        callback.onLogin(str);
                }

                @Override
                public void onNewMessage(String str) throws RemoteException {
                    for (IChatClientCallback callback : callbacks)
                        callback.onNewMessage(str);
                }

                @Override
                public void onUserJoined(String str) throws RemoteException {
                    for (IChatClientCallback callback : callbacks)
                        callback.onUserJoined(str);
                }

                @Override
                public void onUserLeft(String str) throws RemoteException {
                    for (IChatClientCallback callback : callbacks)
                        callback.onUserLeft(str);
                }

                @Override
                public void onTyping(String str) throws RemoteException {
                    for (IChatClientCallback callback : callbacks)
                        callback.onTyping(str);
                }

                @Override
                public void onStopTyping(String str) throws RemoteException {
                    for (IChatClientCallback callback : callbacks)
                        callback.onStopTyping(str);
                }
            };

            try {
                iChatService.registerCallback(iChatServiceCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    public ChatClient(Context context) {
        // init
        this.context = context;
        this.callbacks = new HashSet<>();

        // bind service
        context.bindService(
                new Intent(context, ChatService.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE
        );
        isBinding = true;
        Log.d(TAG, "Bind Service");
    }

    @Override
    public void registerCallback(IChatClientCallback callback) {
        callbacks.add(callback);
    }

    @Override
    public void unregisterCallback(IChatClientCallback callback) {
        if (callbacks.contains(callback))
            callbacks.remove(callback);
    }

    @Override
    public boolean isConnecting()  {
        try {
            return iChatService.isConnecting();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isBinding() {
        return isBinding;
    }

    @Override
    public void addUser(String name)  {
        try {
            iChatService.addUser(name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void typing()  {
        try {
            iChatService.typing();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopTyping()  {
        try {
            iChatService.stopTyping();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void newMessage(String message)  {
        try {
            iChatService.newMessage(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
