package idv.haojun.chatsocket;

import android.os.RemoteException;

public class BlankIChatClientCallback implements IChatClientCallback {

    @Override
    public void onConnect() throws RemoteException {

    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onConnectError() {

    }

    @Override
    public void onConnectTimeout() {

    }

    @Override
    public void onLogin(String str) {

    }

    @Override
    public void onNewMessage(String str) {

    }

    @Override
    public void onUserJoined(String str) {

    }

    @Override
    public void onUserLeft(String str) {

    }

    @Override
    public void onTyping(String str) {

    }

    @Override
    public void onStopTyping(String str) {

    }
}
