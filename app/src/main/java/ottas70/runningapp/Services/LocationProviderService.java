package ottas70.runningapp.Services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import ottas70.runningapp.Interfaces.MyLocationListener;
import ottas70.runningapp.PermissionRequester;

/**
 * Created by ottovodvarka on 19.12.16.
 */

public class LocationProviderService implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int TIME_BEETWEEN_UPDATES = 10000; //miliseconds

    private Context context;
    private MyLocationListener myLocationListener;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private PermissionRequester permissionRequester;

    public LocationProviderService(Context context, MyLocationListener myLocationListener) {
        this.context = context;
        this.myLocationListener = myLocationListener;

        locationRequest = new LocationRequest();
        locationRequest.setInterval(TIME_BEETWEEN_UPDATES);

        permissionRequester = new PermissionRequester(context);

        createGoogleAPIClient();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionRequester.requestPermission(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION});
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        myLocationListener.onLocationReceived(location);
    }


    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionRequester.requestPermission(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION});
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    private void createGoogleAPIClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addApi(ActivityRecognition.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        stopLocationUpdates();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
