package idv.haojun.chatsocket;


public interface IChatClient {

    void registerCallback(IChatClientCallback callback);

    void unregisterCallback(IChatClientCallback callback);

    boolean isConnecting();

    boolean isBinding();

    // action
    void addUser(String name);

    void typing();

    void stopTyping();

    void newMessage(String message);
}
