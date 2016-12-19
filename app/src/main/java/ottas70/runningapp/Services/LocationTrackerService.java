package ottas70.runningapp.Services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ottas70.runningapp.PermissionRequester;

/**
 * Created by ottovodvarka on 27.11.16.
 */

public class LocationTrackerService implements LocationListener {

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
    private List<LatLng> latLngList;

    private PermissionRequester permissionRequester;

    public LocationTrackerService(Context context, GoogleApiClient client, TextView distanceTextView, TextView speedTextView) {
        this.context = context;
        this.client = client;
        this.distanceTextView = distanceTextView;
        this.speedTextView = speedTextView;

        distance = 0.0;
        averageSpeed = 0.0;
        counter = 0;
        speedHelper = 0.0;
        money = 0;
        latLngList = new ArrayList<>();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(TIME_BEETWEEN_UPDATES);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        permissionRequester = new PermissionRequester(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionRequester.requestPermission(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION});
        }
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionRequester.requestPermission(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION});
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("DISTANCE ACCURACY:  ", String.valueOf(location.getAccuracy()));
        if (location.getAccuracy() >= 20) {
            return;
        }
        if (currentLocation != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            latLngList.add(latLng);
            float distance2 = currentLocation.distanceTo(location);
            distance += distance2;
            Log.i("DISTANCE RUN", String.valueOf(distance2));
            double roundedDistanceMeters = (double) Math.round(distance);
            DecimalFormat df = new DecimalFormat("#0.00");
            distanceTextView.setText(String.valueOf(df.format((double) roundedDistanceMeters / 1000.0)));

            if (location.hasSpeed()) {
                Log.i("SPEED:  ", String.valueOf(location.getSpeed()));
                double roundedspeedInKm = (double) Math.round((location.getSpeed() * 3.6) * 10.0) / 10.0;
                DecimalFormat df2 = new DecimalFormat("#0.0");
                speedTextView.setText(String.valueOf(String.valueOf(df2.format((double) roundedspeedInKm))));

                counter++;
                speedHelper += roundedspeedInKm;
                averageSpeed = speedHelper / counter;
            }
            currentLocation = location;
        } else {
            currentLocation = location;
        }
    }

    public void setCurrentLocation(Location currentLocation) {
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

    public List<LatLng> getLatLngList() {
        return latLngList;
    }
}
