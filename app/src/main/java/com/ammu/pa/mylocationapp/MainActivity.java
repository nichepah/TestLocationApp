package com.ammu.pa.mylocationapp;
/* Gets lat and long
 * Using LocationListener
 * Uses criteria, LocationManager, LocationProvider
 * Written by PA on 01 July 2018
 *
 * Initially focus only on getting the providers
 * Then focus on lat and long
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    LocationProvider locationProvider;
    // list of location service providers
    List<String> enabledProviders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get location manager; check getSystemService for other options
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // container for providers, not used
        StringBuffer stringBuffer = null;

        Criteria criteria = new Criteria();
        // look for gps as well as network
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        enabledProviders = locationManager.getProviders(criteria, true);
        if (enabledProviders.isEmpty()) {
            Toast.makeText(this, "No location service provider found", Toast.LENGTH_SHORT).show();
        } else {
            for (String enPro : enabledProviders) {
                //stringBuffer.append(enPro);
                //stringBuffer.append(" ");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Toast.makeText(this, "Grant Fine_Location permission", Toast.LENGTH_SHORT).show();
                    return;
                }
                locationManager.requestSingleUpdate(enPro, this, null);
                System.out.println("enabledProviders: " + enPro);
            }

        }

    }

    @Override
    public void onLocationChanged(Location location) {
        TextView latVal = findViewById(R.id.textViewLat);
        TextView longVal = findViewById(R.id.textViewLong);

        TextView accuracyVal = findViewById(R.id.textViewAccuracy);
        TextView providerVal = findViewById(R.id.textViewProvider);

        latVal.setText(String.valueOf(location.getLatitude()));
        longVal.setText(String.valueOf(location.getLongitude()));

        providerVal.setText(location.getProvider());
        accuracyVal.setText(String.valueOf(location.getAccuracy()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Provider enabled", Toast.LENGTH_SHORT);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "No provider available", Toast.LENGTH_SHORT);

    }

    @Override
    protected void onPause(){
        super.onPause();
        locationManager.removeUpdates(this);
    }
}
