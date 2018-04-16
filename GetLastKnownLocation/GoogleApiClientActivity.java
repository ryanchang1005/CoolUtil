package idv.haojun.getlastknownlocationdemo;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Date;

public class GoogleApiClientActivity extends AppCompatActivity implements GoogleLocationApiClientHelper.OnLocationChangeListener {

    private TextView providerTextView, latitudeTextView, longitudeTextView, timeTextView;

    private GoogleLocationApiClientHelper helper;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_api);

        providerTextView = (TextView) findViewById(R.id.providerTextView);
        latitudeTextView = (TextView) findViewById(R.id.latitudeTextView);
        longitudeTextView = (TextView) findViewById(R.id.longitudeTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);

        helper = new GoogleLocationApiClientHelper(this, this);
        helper.setUpGoogleClient();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        if (mLocation != null) {
            providerTextView.setText("Provider : " + mLocation.getProvider());
            latitudeTextView.setText("Latitude : " + mLocation.getLatitude());
            longitudeTextView.setText("Longitude : " + mLocation.getLongitude());
            timeTextView.setText("Time : " + new Date(mLocation.getTime()));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GoogleLocationApiClientHelper.REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        helper.requestLastKnownLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }
}