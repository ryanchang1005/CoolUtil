package idv.haojun.chatsocket;

import idv.haojun.chatsocket.IChatServiceCallback;

interface IChatService {

    void registerCallback(IChatServiceCallback callback);

    boolean isConnecting();

    // action
    void addUser(String name);
    void typing();
    void stopTyping();
    void newMessage(String message);
}
