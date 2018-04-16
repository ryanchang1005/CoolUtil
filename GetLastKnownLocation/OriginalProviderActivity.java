package idv.haojun.getlastknownlocationdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Date;

public class OriginalProviderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_original_provider);

        TextView tvResult = (TextView) findViewById(R.id.tvOriginalProviderResult);

        Location location = getLocation();
        if (location != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Provider:").append(location.getProvider()).append("\n");
            sb.append("Latitude:").append(location.getLatitude()).append("\n");
            sb.append("Longitude:").append(location.getLongitude()).append("\n");
            sb.append("Time:").append(new Date(location.getTime()));
            tvResult.setText(sb.toString());
        } else {
            tvResult.setText("null");
        }

    }

    public Location getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {

            Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location lastKnownLocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location lastKnownLocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (lastKnownLocationGPS != null) return lastKnownLocationGPS;
            if (lastKnownLocationNetwork != null) return lastKnownLocationNetwork;
            if (lastKnownLocationPassive != null) return lastKnownLocationPassive;
            return null;
        } else {
            return null;
        }
    }
}
