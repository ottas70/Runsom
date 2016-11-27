package ottas70.runningapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class RunningActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback, LocationListener,
                                                         GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    private static final int DISTANCE_CHANGE = 10; //meter
    private static final int TIME_BEETWEEN_UPDATES = 10000; //milisekund
    private static final int HAVE_LOCATION_PERMISSION = 1;

    private TextView distanceTextView;
    private Button start;

    private GoogleApiClient client;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private double distance = 0.000;
    private boolean isRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        distanceTextView = (TextView) findViewById(R.id.distanceEditText);
        start = (Button) findViewById(R.id.StartButton);

        distanceTextView.setText(String.valueOf(distance));
        locationRequest = new LocationRequest();
        locationRequest.setInterval(TIME_BEETWEEN_UPDATES);
        //locationRequest.setSmallestDisplacement(DISTANCE_CHANGE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isRunning){
                    start.setText("Stop");
                    if(client == null){
                        createGoogleAPIClient();
                    }else{
                        startLocationUpdates();
                    }
                    isRunning = true;
                }else{
                    start.setText("Start");
                    isRunning = false;
                    stopLocationUpdates();
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
        createGoogleAPIClient();

    }

    protected void onStart() {
        super.onStart();
        client.connect();
    }

    protected void onStop() {
        super.onStop();
        client.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(client.isConnected()){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }else{
                startLocationUpdates();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(client.isConnected()){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }else{
                currentLocation = null;
                startLocationUpdates();
            }
        }
    }

    private void createGoogleAPIClient(){
            if (client == null) {
                client = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .build();
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(client);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
    }

    private void stopLocationUpdates(){
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, HAVE_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case HAVE_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Je možné pokračovat
                } else {
                    Toast toast = Toast.makeText(this, "This permission is necessery for this app", Toast.LENGTH_SHORT);
                    requestPermission();
                }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(currentLocation != null) {
            float distance2 = currentLocation.distanceTo(location);
            //distance2 = Math.round(distance2/100);
            Log.i("Distance", String.valueOf(distance2));
            distance += distance2;
            double roundedDistanceMeters = (double)Math.round(distance);
            distanceTextView.setText(String.valueOf((double)roundedDistanceMeters/1000.0));

            currentLocation = location;
        }else{
            currentLocation = location;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
      Log.i("App","Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"Error while connecting to Google Play Services",Toast.LENGTH_SHORT);
        Log.i("App","No connected");
    }
}
