package ryan.net.safetyprefdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                L.d("p:" + PrefUtil.getPassword());
                L.d("enp:" + PrefUtil.getEncryptPassword());
            }
        });

        KeyStoreManager.getInstance().init();

        String password = "1234";

        PrefUtil.setPassword(password);
        PrefUtil.setEncryptPassword(password);
    }
}
