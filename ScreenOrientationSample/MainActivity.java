package idv.haojun.screenorientaionsample;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    OrientationEventListener mOrientationListener;

    private int lastOrientationDegree = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d("onCreate()");
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleOrientation();
            }
        });

        mOrientationListener = new OrientationEventListener(this,
                SensorManager.SENSOR_DELAY_NORMAL) {

            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                    return;
                }

                if (orientation > 350 || orientation < 10) { //0度
                    orientation = 0;
                } else if (orientation > 80 && orientation < 100) { //90度
                    orientation = 90;
                } else if (orientation > 170 && orientation < 190) { //180度
                    orientation = 180;
                } else if (orientation > 260 && orientation < 280) { //270度
                    orientation = 270;
                } else {
                    return;
                }
                setLastOrientationDegree(orientation);
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.d("onDestroy()");
    }

    private void toggleOrientation() {
        int ori = getResources().getConfiguration().orientation;
        if (ori == Configuration.ORIENTATION_PORTRAIT) {
            toLandscape();
        } else if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            toPortrait();
        }
    }

    private void toPortrait() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void toLandscape() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void toReverseLandscape() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
    }

    private void setLastOrientationDegree(int o) {
        if (lastOrientationDegree == o) return;
        lastOrientationDegree = o;
        L.d("lastOrientationDegree:" + lastOrientationDegree);
        if (lastOrientationDegree == 0) {
            toPortrait();
        } else if (lastOrientationDegree == 90) {
            toReverseLandscape();
        } else if (lastOrientationDegree == 270) {
            toLandscape();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        L.d("onConfigurationChanged()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.d("onResume()");
        mOrientationListener.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.d("onPause()");
        mOrientationListener.disable();
    }
}