package ottas70.runningapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class RunningActivity extends Activity{

    private TextView distanceTextView;
    private TextView timerTextView;
    private TextView speedTextView;
    private FloatingActionButton startButton;
    private FloatingActionButton cancelButton;
    private FloatingActionButton lockButton;
    private TextView holdTextView;
    private View completeView;

    private MyDialog dialog;

    private boolean isRunning;
    private boolean isLocked;

    private Handler buttonHandler;

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

        completeView = findViewById(android.R.id.content);
        distanceTextView = (TextView) findViewById(R.id.distanceEditText);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        speedTextView = (TextView) findViewById(R.id.speedTextView);
        startButton = (FloatingActionButton) findViewById(R.id.startButton);
        cancelButton = (FloatingActionButton) findViewById(R.id.cancelButton);
        lockButton = (FloatingActionButton) findViewById(R.id.lockButton);
        holdTextView = (TextView) findViewById(R.id.holdTextView);

        isRunning = false;
        isLocked = false;
        holdTextView.setVisibility(View.INVISIBLE);

        distanceTracker = new DistanceTracker(this, distanceTextView, speedTextView);
        timer = new Timer(timerTextView, handler, 0);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLocked){
                    return;
                }
                if (!isRunning) {
                    startRun();
                } else {
                    stopRun();
                }
            }
        });

        startButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(buttonHandler != null) return true;
                        buttonHandler = new Handler();
                        buttonHandler.postDelayed(action,2000);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(buttonHandler == null) return true;
                        buttonHandler.removeCallbacks(action);
                        buttonHandler = null;
                        break;
                }
                return false;
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLocked){
                    return;
                }
                onBackPressed();
            }
        });

        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLocked = true;
                cancelButton.setVisibility(View.INVISIBLE);
                lockButton.setVisibility(View.INVISIBLE);
                holdTextView.setVisibility(View.VISIBLE);
                completeView.setKeepScreenOn(true);
                startButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                startButton.setImageDrawable(ContextCompat.getDrawable(lockButton.getContext(), R.drawable.ic_lock_white_36dp));
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

    @Override
    public void onBackPressed() {
        dialog = new MyDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title","ARE YOU SURE?");
        bundle.putString("message","Are you sure you want end this run?");
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(),"dialog");
    }

    Runnable action = new Runnable() {
        @Override
        public void run() {
            if(!isLocked)
                return;
            cancelButton.setVisibility(View.VISIBLE);
            lockButton.setVisibility(View.VISIBLE);
            holdTextView.setVisibility(View.INVISIBLE);
            isLocked = false;
            completeView.setKeepScreenOn(false);
            if(!isRunning){
                startButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                startButton.setImageDrawable(ContextCompat.getDrawable(startButton.getContext(), R.drawable.ic_play_arrow_black_36dp));
            }else{
                startButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                startButton.setImageDrawable(ContextCompat.getDrawable(startButton.getContext(), R.drawable.ic_stop_black_36dp));
            }

        }
    };

}
