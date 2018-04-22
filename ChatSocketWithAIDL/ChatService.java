package idv.haojun.chatsocket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class ChatService extends Service {

    public static final String TAG = "ChatService";

    private IChatServiceCallback serviceCallback;

    private ChatSocketHelper socketHelper;

    private boolean isConnecting = false;

    private class ChatSocketHelperCallback implements ChatSocketHelper.Callback {
        @Override
        public void onConnect(Object... args) throws RemoteException{
            serviceCallback.onConnect();
            isConnecting = true;
        }

        @Override
        public void onDisconnect(Object... args) throws RemoteException{
            serviceCallback.onDisconnect();
            isConnecting = false;
        }

        @Override
        public void onConnectError(Object... args) throws RemoteException{
            serviceCallback.onConnectError();
        }

        @Override
        public void onConnectTimeout(Object... args) throws RemoteException{
            serviceCallback.onDisconnect();
        }

        @Override
        public void onLogin(Object... args) throws RemoteException{
            serviceCallback.onLogin(args[0].toString());
        }

        @Override
        public void onNewMessage(Object... args) throws RemoteException{
            serviceCallback.onNewMessage(args[0].toString());
        }

        @Override
        public void onUserJoined(Object... args)throws RemoteException {
            serviceCallback.onUserJoined(args[0].toString());
        }

        @Override
        public void onUserLeft(Object... args) throws RemoteException{
            serviceCallback.onUserLeft(args[0].toString());
        }

        @Override
        public void onTyping(Object... args)throws RemoteException {
            serviceCallback.onTyping(args[0].toString());
        }

        @Override
        public void onStopTyping(Object... args) throws RemoteException{
            serviceCallback.onStopTyping(args[0].toString());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        socketHelper = new ChatSocketHelper(new ChatSocketHelperCallback());
        socketHelper.on();
        socketHelper.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        socketHelper.disconnect();
        socketHelper.off();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private final IChatService.Stub binder = new IChatService.Stub() {
        @Override
        public void registerCallback(IChatServiceCallback callback) {
            Log.d(TAG, "registerCallback");
            serviceCallback = callback;
        }

        @Override
        public boolean isConnecting() {
            Log.d(TAG, "isConnecting");
            return isConnecting;
        }

        @Override
        public void addUser(String username) {
            Log.d(TAG, "addUser");
            socketHelper.emit("add user", username);
        }

        @Override
        public void typing() {
            Log.d(TAG, "typing");
            socketHelper.emit("typing");
        }

        @Override
        public void stopTyping() {
            Log.d(TAG, "stopTyping");
            socketHelper.emit("stop typing");
        }

        @Override
        public void newMessage(String message) {
            Log.d(TAG, "newMessage");
            socketHelper.emit("new message", message);
        }
    };

}
