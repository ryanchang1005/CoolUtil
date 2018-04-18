package idv.haojun.servicedemo;

public interface IMyClientCallback {
    void onConnect();
    void onDisconnect();
    void onConnectError();
    void onConnectTimeout();
    void onReceiveMessage();
    void onUserJoin();
    void onUserLeft();
    void onTyping();
    void onStopTyping();
}
