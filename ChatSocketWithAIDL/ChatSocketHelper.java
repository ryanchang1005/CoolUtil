package idv.haojun.chatsocket;

import android.os.RemoteException;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatSocketHelper {

    private Socket socket;
    private Callback callback;

    public ChatSocketHelper(Callback Callback) {
        try {
            socket = IO.socket(Constant.URL);
            this.callback = Callback;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect() {
        if (socket != null)
            socket.connect();
    }

    public void disconnect() {
        if (socket != null)
            socket.disconnect();
    }

    public void on() {
        socket.on(Socket.EVENT_CONNECT, onConnect);
        socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeout);
        socket.on(Constant.ACTION_LOGIN, onLogin);
        socket.on(Constant.ACTION_NEW_MESSAGE, onNewMessage);
        socket.on(Constant.ACTION_USER_JOINED, onUserJoined);
        socket.on(Constant.ACTION_USER_LEFT, onUserLeft);
        socket.on(Constant.ACTION_TYPING, onTyping);
        socket.on(Constant.ACTION_STOP_TYPING, onStopTyping);
    }

    public void off() {
        socket.off(Socket.EVENT_CONNECT, onConnect);
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeout);
        socket.off("login", onLogin);
        socket.off("new message", onNewMessage);
        socket.off("user joined", onUserJoined);
        socket.off("user left", onUserLeft);
        socket.off("typing", onTyping);
        socket.off("stop typing", onStopTyping);
    }

    public void emit(String event, Object... args){
        socket.emit(event, args);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                callback.onConnect(args);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                callback.onDisconnect(args);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                callback.onConnectError(args);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onConnectTimeout = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                callback.onConnectTimeout(args);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                callback.onLogin(args);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                callback.onNewMessage(args);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                callback.onUserJoined(args);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                callback.onUserLeft(args);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                callback.onTyping(args);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                callback.onStopTyping(args);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    public interface Callback {
        void onConnect(Object... args) throws RemoteException;

        void onDisconnect(Object... args) throws RemoteException;

        void onConnectError(Object... args) throws RemoteException;

        void onConnectTimeout(Object... args) throws RemoteException;

        void onLogin(Object... args) throws RemoteException;

        void onNewMessage(Object... args) throws RemoteException;

        void onUserJoined(Object... args) throws RemoteException;

        void onUserLeft(Object... args) throws RemoteException;

        void onTyping(Object... args) throws RemoteException;

        void onStopTyping(Object... args) throws RemoteException;
    }
}
