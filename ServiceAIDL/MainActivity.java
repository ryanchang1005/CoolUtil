package idv.haojun.servicedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private IMyClient iMyClient;

    private TextView tv;

    private IMyClientCallback iMyClientCallback = new BlankIMyClientCallback() {
        // override the method you want to listen on...
        @Override
        public void onConnect() {
            tv.setText("onConnect()");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);

        iMyClient = MyClientFactory.getInstance();
        iMyClient.registerCallback(iMyClientCallback);
    }

}
