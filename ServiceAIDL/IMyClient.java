package idv.haojun.servicedemo;

import android.os.RemoteException;

public interface IMyClient {

    void registerCallback(IMyClientCallback callback);
    void unregisterCallback(IMyClientCallback callback);

    boolean isConnecting() throws RemoteException;
    boolean isBinding();

    // action
    void addUser(String name) throws RemoteException;
    void typing() throws RemoteException;
    void stopTyping() throws RemoteException;
    void addMessage(String message) throws RemoteException;
}
