package idv.haojun.chatsocket;

interface IChatServiceCallback {
    void onConnect();
    void onDisconnect();
    void onConnectError();
    void onConnectTimeout();
    void onLogin(String str);
    void onNewMessage(String str);
    void onUserJoined(String str);
    void onUserLeft(String str);
    void onTyping(String str);
    void onStopTyping(String str);
}
