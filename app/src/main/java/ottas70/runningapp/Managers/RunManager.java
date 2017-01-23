package ottas70.runningapp.Managers;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.google.maps.android.PolyUtil;

import ottas70.runningapp.Duration;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;
import ottas70.runningapp.Run;
import ottas70.runningapp.Services.ActivityRecognitionService;
import ottas70.runningapp.Services.LocationTrackerService;
import ottas70.runningapp.Timer;
import ottas70.runningapp.Utils.DateUtils;

/**
 * Created by ottovodvarka on 13.12.16.
 */

public class RunManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private boolean isRunning;
    private Handler handler;
    private Timer timer;
    private LocationTrackerService locationTracker;
    private MyNotificationManager myNotificationManager;

    private Context context;

    private TextView timerTextView;
    private TextView distanceTextView;
    private TextView speedTextView;
    private TextView moneyTextView;

    private GoogleApiClient googleApiClient;
    private PendingIntent pendingIntent;

    public RunManager(Context context) {
        this.context = context;

        View rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        timerTextView = (TextView) rootView.findViewById(R.id.timerTextView);
        distanceTextView = (TextView) rootView.findViewById(R.id.distanceTextView);
        speedTextView = (TextView) rootView.findViewById(R.id.speedTextView);
        moneyTextView = (TextView) rootView.findViewById(R.id.moneyLogTextView);

        isRunning = false;
        handler = new Handler();
        timer = new Timer(timerTextView, handler);

        createGoogleAPIClient();

        myNotificationManager = new MyNotificationManager(context);
    }

    public void startRun() {
        isRunning = true;

        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(googleApiClient, 6000, pendingIntent);

        handler.postDelayed(timer, 1000);
        locationTracker.startLocationUpdates();
        myNotificationManager.createNotification();

    }

    public void stopRun() {
        if (!isRunning) return;
        isRunning = false;

        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(googleApiClient, pendingIntent);

        handler.removeCallbacks(timer);
        locationTracker.stopLocationUpdates();
        locationTracker.setCurrentLocation(null);
        myNotificationManager.deleteNotification();
    }

    public void finishRun() {
        if (isRunning) {
            stopRun();
        }

        Duration duration = timer.getDuration();
        double distance = Math.round(locationTracker.getDistance()) / 1000.0;
        double averageSpeed = Math.round(locationTracker.getAverageSpeed() * 10) / 10.0;
        int money = locationTracker.getMoney();
        String date = DateUtils.getCurrentDate();
        String encodedPath = PolyUtil.encode(locationTracker.getLatLngList());
        Run run = new Run(duration, distance, averageSpeed, money, date, Run.generateName(), encodedPath);
        if (!locationTracker.getLatLngList().isEmpty()) {
            ServerRequest request = new ServerRequest(context);
            request.uploadRun(run, false, new GetCallback() {
                @Override
                public void done(Object o) {

                }
            });
        }
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
        locationTracker = new LocationTrackerService(context, googleApiClient, distanceTextView, speedTextView, moneyTextView);
        Intent intent = new Intent(context, ActivityRecognitionService.class);
        pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, "Error while connecting to Google Play Services", Toast.LENGTH_SHORT).show();
    }

    public boolean isRunning() {
        return isRunning;
    }
}
