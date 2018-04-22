package idv.haojun.chatsocket;

public class ChatClientFactory {
    private static IChatClient iMyClient;

    public static IChatClient getInstance() {
        if (iMyClient == null) {
            iMyClient = new ChatClient(App.getInstance());
        }
        return iMyClient;
    }
}
