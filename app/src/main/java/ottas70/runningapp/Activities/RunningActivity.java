package ottas70.runningapp.Activities;

import android.app.Activity;
import android.app.DialogFragment;
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

import ottas70.runningapp.DistanceTracker;
import ottas70.runningapp.Duration;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Interfaces.MyDialogListener;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;
import ottas70.runningapp.Run;
import ottas70.runningapp.Timer;
import ottas70.runningapp.Utils.DateUtils;
import ottas70.runningapp.Views.MyDialog;

public class RunningActivity extends Activity implements MyDialogListener{

    private final int NOTIFICATION_ID = 001;
    private TextView distanceTextView;
    private TextView timerTextView;
    private TextView speedTextView;
    private TextView holdTextView;
    private TextView moneyTextView;
    private FloatingActionButton startButton;
    private FloatingActionButton cancelButton;
    private FloatingActionButton lockButton;
    private View completeView;
    private MyDialog dialog;
    private boolean isRunning;
    private boolean isLocked;
    Runnable action = new Runnable() {
        @Override
        public void run() {
            if (!isLocked)
                return;
            cancelButton.setVisibility(View.VISIBLE);
            lockButton.setVisibility(View.VISIBLE);
            holdTextView.setVisibility(View.INVISIBLE);
            isLocked = false;
            completeView.setKeepScreenOn(false);
            if (!isRunning) {
                startButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                startButton.setImageDrawable(ContextCompat.getDrawable(startButton.getContext(), R.drawable.ic_play_arrow_black_36dp));
            } else {
                startButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                startButton.setImageDrawable(ContextCompat.getDrawable(startButton.getContext(), R.drawable.ic_stop_black_36dp));
            }

        }
    };
    private Handler buttonHandler;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
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
        moneyTextView = (TextView) findViewById(R.id.moneyTextView);

        isRunning = false;
        isLocked = false;
        holdTextView.setVisibility(View.INVISIBLE);

        distanceTracker = new DistanceTracker(this, distanceTextView, speedTextView);
        timer = new Timer(timerTextView, handler);
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

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        finishRun();
        dialog.dismiss();
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    private void finishRun(){
        Duration duration = timer.getDuration();
        double distance = Math.round(distanceTracker.getDistance()) / 1000.0;
        double averageSpeed = Math.round(distanceTracker.getAverageSpeed() * 10) / 10.0;
        int money = distanceTracker.getMoney();
        String date = DateUtils.getCurrentDate();
        Run run = new Run(duration,distance,averageSpeed,money,date);
        ServerRequest request = new ServerRequest(this);
        request.uploadRun(run, false, new GetCallback() {
            @Override
            public void done(Object o) {
                //nothing
            }
        });
    }
}
