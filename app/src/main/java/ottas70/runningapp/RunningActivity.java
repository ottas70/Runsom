package ottas70.runningapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class RunningActivity extends Activity {

    private TextView distanceTextView;
    private TextView timerTextView;
    private TextView speedTextView;
    private FloatingActionButton startButton;

    private boolean isRunning;

    private final int NOTIFICATION_ID = 001;
    private NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;

    private DistanceTracker distanceTracker;
    private Handler handler = new Handler();
    private Timer timer;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        distanceTextView = (TextView) findViewById(R.id.distanceEditText);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        speedTextView = (TextView) findViewById(R.id.speedTextView);
        startButton = (FloatingActionButton) findViewById(R.id.startButton);

        isRunning = false;
        distanceTracker = new DistanceTracker(this, distanceTextView, speedTextView);
        timer = new Timer(timerTextView, handler, 0);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRunning) {
                    startRun();
                } else {
                    stopRun();
                }
            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void startRun() {
        isRunning = true;

        startButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        startButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_stop_black_36dp));

        handler.postDelayed(timer, 1000);
        distanceTracker.startLocationUpdates();
        createNotification();

    }

    private void stopRun() {
        isRunning = false;

        startButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        startButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_arrow_black_36dp));

        handler.removeCallbacks(timer);
        distanceTracker.stopLocationUpdates();
        distanceTracker.setCurrentLocation(null);
        deleteNotification();
    }

    private void createNotification() {
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Running App")
                .setContentText("Running App is tracking your run")
                .setOngoing(true);
        Intent resultIntent = new Intent(this, RunningActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

    }

    private void deleteNotification() {
        notificationManager.cancel(NOTIFICATION_ID);

    }

}
