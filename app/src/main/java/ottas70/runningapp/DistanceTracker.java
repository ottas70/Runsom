package ottas70.runningapp;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DecimalFormat;

/**
 * Created by ottovodvarka on 27.11.16.
 */

public class DistanceTracker implements ActivityCompat.OnRequestPermissionsResultCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int HAVE_LOCATION_PERMISSION = 1;
    private static final int TIME_BEETWEEN_UPDATES = 5000; //miliseconds


    private GoogleApiClient client;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private Context context;
    private TextView distanceTextView;

    private double distance = 0.0;

    public DistanceTracker(Context context,TextView distanceTextView) {
        this.context = context;
        this.distanceTextView = distanceTextView;

        createGoogleAPIClient();
        locationRequest = new LocationRequest();
        locationRequest.setInterval(TIME_BEETWEEN_UPDATES);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
    }

    private void createGoogleAPIClient() {
        if (client == null) {
            client = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(client);
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, HAVE_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case HAVE_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Je možné pokračovat
                } else {
                    Toast toast = Toast.makeText(context, "This permission is necessery for this app", Toast.LENGTH_SHORT);
                    requestPermission();
                }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (currentLocation != null) {
            float distance2 = currentLocation.distanceTo(location);
            distance += distance2;
            double roundedDistanceMeters = (double) Math.round(distance);
            DecimalFormat df = new DecimalFormat("#0.00");
            distanceTextView.setText(String.valueOf(df.format((double) roundedDistanceMeters / 1000.0)));

            currentLocation = location;
        } else {
            currentLocation = location;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("App", "Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, "Error while connecting to Google Play Services", Toast.LENGTH_SHORT);
    }

    public GoogleApiClient getClient(){
        return client;
    }

    public void setCurrentLocation(Location currentLocation){
        this.currentLocation = currentLocation;
    }

}
