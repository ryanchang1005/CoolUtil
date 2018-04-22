package idv.haojun.chatsocket;

import android.content.Intent;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private String userName;

    private IChatClientCallback iChatClientCallback = new BlankIChatClientCallback() {
        @Override
        public void onLogin(String str) {
            try {
                JSONObject json = new JSONObject(str);
                int numUsers = json.getInt("numUsers");

                PrefHelper.setUserName(LoginActivity.this, userName);

                Intent intent = new Intent();
                intent.putExtra("userName", userName);
                intent.putExtra("numUsers", numUsers);
                setResult(RESULT_OK, intent);
                finish();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iChatClient.registerCallback(iChatClientCallback);

        findViewById(R.id.btLogin).setOnClickListener(this);
    }

    private void login(String username) {
        if (username.isEmpty()) return;

        userName = username;

        iChatClient.addUser(username);
    }

    @Override
    public void onClick(View v) {
        login(((EditText) findViewById(R.id.etLoginUserName)).getText().toString());
    }

    @Override
    protected void onDestroy() {
        if (iChatClient != null)
            iChatClient.unregisterCallback(iChatClientCallback);
        super.onDestroy();
    }
}
