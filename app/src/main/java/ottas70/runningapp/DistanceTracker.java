package ottas70.runningapp;

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
    private static final int TIME_BEETWEEN_UPDATES = 3000; //miliseconds
    private static final int MINIMAL_DISTANCE = 10;


    private GoogleApiClient client;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private Context context;
    private TextView distanceTextView;
    private TextView speedTextView;

    private double distance;
    private double averageSpeed;
    private int counter;
    private double speedHelper;
    private int money;

    public DistanceTracker(Context context,TextView distanceTextView, TextView speedTextView) {
        this.context = context;
        this.distanceTextView = distanceTextView;
        this.speedTextView = speedTextView;

        distance = 0.0;
        averageSpeed = 0.0;
        counter = 0;
        speedHelper = 0.0;
        money = 0;

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
        client.connect();
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client,locationRequest,this);
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("DISTANCE ACCURACY:  ", String.valueOf(location.getAccuracy()));
        if(location.getAccuracy() >= 20){
            return;
        }
        if (currentLocation != null) {
            float distance2 = currentLocation.distanceTo(location);
            distance += distance2;
            Log.i("DISTANCE RUN", String.valueOf(distance2));
            double roundedDistanceMeters = (double) Math.round(distance);
            DecimalFormat df = new DecimalFormat("#0.00");
            distanceTextView.setText(String.valueOf(df.format((double) roundedDistanceMeters / 1000.0)));

            if(location.hasSpeed()){
                Log.i("SPEED:  ", String.valueOf(location.getSpeed()));
                double roundedspeedInKm = (double)Math.round((location.getSpeed() * 3.6)*10.0);
                DecimalFormat df2 = new DecimalFormat("#0.0");
                speedTextView.setText(String.valueOf(String.valueOf(df2.format((double) roundedspeedInKm / 10.0))));

                counter++;
                speedHelper += roundedspeedInKm;
                averageSpeed = speedHelper/counter;
            }
            currentLocation = location;
        } else {
            currentLocation = location;
        }
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

    public double getDistance() {
        return distance;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public int getMoney() {
        return money;
    }
}
