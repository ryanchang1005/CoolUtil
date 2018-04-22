package idv.haojun.chatsocket;

import android.os.RemoteException;

public interface IChatClientCallback {
    void onConnect() throws RemoteException;
    void onDisconnect() throws RemoteException;
    void onConnectError() throws RemoteException;
    void onConnectTimeout() throws RemoteException;
    void onLogin(String str) throws RemoteException;
    void onNewMessage(String str) throws RemoteException;
    void onUserJoined(String str) throws RemoteException;
    void onUserLeft(String str) throws RemoteException;
    void onTyping(String str) throws RemoteException;
    void onStopTyping(String str) throws RemoteException;
}
