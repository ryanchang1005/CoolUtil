package idv.haojun.servicedemo;

import idv.haojun.servicedemo.IMyServiceCallback;

interface IMyService {

    void registerCallback(IMyServiceCallback callback);

    boolean isConnecting();

    // action
    void addUser(String name);
    void typing();
    void stopTyping();
    void addMessage(String message);
}
