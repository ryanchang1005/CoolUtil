package idv.haojun.chatsocket;

import android.content.Intent;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_LOGIN = 0;

    private static final int TYPING_TIMER_LENGTH = 600;

    private RecyclerView rvChat;
    private EditText etMessage;
    private Button btSend;

    private RecyclerView.Adapter adapter;

    private List<Message> messages;
    private String userName;

    private Handler mTypingHandler = new Handler();
    private boolean typing;

    private IChatClientCallback iChatClientCallback = new BlankIChatClientCallback() {
        // override the method you want to listen on...
        @Override
        public void onConnect() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!iChatClient.isConnecting()) {
                        Toast.makeText(MainActivity.this, R.string.connect, Toast.LENGTH_SHORT).show();
                        iChatClient.addUser(userName);
                    }
                }
            });
        }

        @Override
        public void onDisconnect() {
            Toast.makeText(MainActivity.this, R.string.disconnect, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onConnectError() {
            Toast.makeText(MainActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onConnectTimeout() {
            Toast.makeText(MainActivity.this, R.string.connect_timeout, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNewMessage(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = new JSONObject(str);
                        String username = data.getString("username");
                        String message = data.getString("message");
                        removeTyping(username);
                        addMessage(username, message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onUserJoined(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = new JSONObject(str);
                        String username = data.getString("username");
                        int numUsers = data.getInt("numUsers");
                        addLog(getResources().getString(R.string.message_user_joined, username));
                        addParticipantsLog(numUsers);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onUserLeft(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = new JSONObject(str);
                        String username = data.getString("username");
                        int numUsers = data.getInt("numUsers");
                        addLog(getResources().getString(R.string.message_user_left, username));
                        addParticipantsLog(numUsers);
                        removeTyping(username);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onTyping(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = new JSONObject(str);
                        String username = data.getString("username");
                        addTyping(username);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onStopTyping(final String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = new JSONObject(str);
                        String username = data.getString("username");
                        removeTyping(username);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iChatClient.registerCallback(iChatClientCallback);

        rvChat = findViewById(R.id.rvMainChat);
        etMessage = findViewById(R.id.etMainMessage);
        findViewById(R.id.btMainSend).setOnClickListener(this);

        rvChat.setLayoutManager(new LinearLayoutManager(this));

        messages = new ArrayList<>();
        adapter = new ChatRVAdapter(this, messages);
        rvChat.setAdapter(adapter);

        etMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == EditorInfo.IME_NULL) {
                    attemptSend();
                    return true;
                }
                return false;
            }
        });
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null == userName) return;

                if (!typing) {
                    typing = true;
                    iChatClient.typing();
                }

                mTypingHandler.removeCallbacks(onTypingTimeout);
                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // check have name
        userName = PrefHelper.getUserName(this);

        if (userName.isEmpty()) {
            startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_LOGIN);
        }
    }

    private void addLog(String message) {
        messages.add(new Message.Builder(Message.TYPE_LOG)
                .message(message).build());
        adapter.notifyItemInserted(messages.size() - 1);
        scrollToBottom();
    }

    private void addParticipantsLog(int numUsers) {
        addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
    }

    private void addMessage(String username, String message) {
        messages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .username(username).message(message).build());
        adapter.notifyItemInserted(messages.size() - 1);
        scrollToBottom();
    }

    private void addTyping(String username) {
        messages.add(new Message.Builder(Message.TYPE_ACTION)
                .username(username).build());
        adapter.notifyItemInserted(messages.size() - 1);
        scrollToBottom();
    }

    private void removeTyping(String username) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            Message message = messages.get(i);
            if (message.getType() == Message.TYPE_ACTION && message.getUsername().equals(username)) {
                messages.remove(i);
                adapter.notifyItemRemoved(i);
            }
        }
    }

    private void attemptSend() {
        if (null == userName) return;

        typing = false;

        String message = etMessage.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            etMessage.requestFocus();
            return;
        }

        etMessage.setText("");
        addMessage(userName, message);

        // perform the sending message attempt.
        iChatClient.newMessage(message);
    }

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!typing) return;

            typing = false;
            iChatClient.stopTyping();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btMainSend:
                attemptSend();
                break;
        }
    }

    private void scrollToBottom() {
        rvChat.scrollToPosition(messages.size() - 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_LOGIN:
                    userName = data.getStringExtra("userName");
                    int numUsers = data.getIntExtra("numUsers", 1);

                    addLog(getResources().getString(R.string.message_welcome));
                    addParticipantsLog(numUsers);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (iChatClient != null)
            iChatClient.unregisterCallback(iChatClientCallback);
        super.onDestroy();
    }
}
