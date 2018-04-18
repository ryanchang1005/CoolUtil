package idv.haojun.servicedemo;

interface IMyServiceCallback {
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
