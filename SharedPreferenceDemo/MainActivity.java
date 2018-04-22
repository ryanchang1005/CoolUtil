package idv.haojun.sharedpreferencesdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText etUserApiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUserApiKey = findViewById(R.id.editMainUserApiKey);

        etUserApiKey.setText(PrefHelper.getUserApiKey(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        PrefHelper.setUserApiKey(this, etUserApiKey.getText().toString());
    }
}
