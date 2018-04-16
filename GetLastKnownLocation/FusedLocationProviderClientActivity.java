package idv.haojun.getlastknownlocationdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Date;

public class FusedLocationProviderClientActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvResult;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fused_location_provider_client);

        findViewById(R.id.btMainGetLastKnownLocation).setOnClickListener(this);
        tvResult = (TextView) findViewById(R.id.tvMainResult);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onClick(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Provider:").append(location.getProvider()).append("\n");
                            sb.append("Latitude:").append(location.getLatitude()).append("\n");
                            sb.append("Longitude:").append(location.getLongitude()).append("\n");
                            sb.append("Time:").append(new Date(location.getTime()));
                            tvResult.setText(sb.toString());
                        }
                    }
                });
    }
}
