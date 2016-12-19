package ottas70.runningapp.Activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ottas70.runningapp.Interfaces.MyDialogListener;
import ottas70.runningapp.Managers.RunManager;
import ottas70.runningapp.R;
import ottas70.runningapp.Views.FinishRunDialog;
import ottas70.runningapp.Views.MyDialog;

public class RunningActivity extends Activity implements MyDialogListener{

    private TextView holdTextView;
    private TextView moneyTextView;
    private FloatingActionButton startButton;
    private FloatingActionButton cancelButton;
    private FloatingActionButton lockButton;
    private View completeView;
    private MyDialog myDialog;
    private FinishRunDialog finishRunDialog;

    private boolean isLocked;
    private boolean finish;

    private Handler buttonHandler;
    private RunManager runManager;
    private HoldButtonRunnable holdButtonRunnable;
    private MessageReceiver messageReceiver;

    private List<Integer> detectedActivityList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        completeView = findViewById(android.R.id.content);
        startButton = (FloatingActionButton) findViewById(R.id.startButton);
        cancelButton = (FloatingActionButton) findViewById(R.id.cancelButton);
        lockButton = (FloatingActionButton) findViewById(R.id.lockButton);
        holdTextView = (TextView) findViewById(R.id.holdTextView);
        moneyTextView = (TextView) findViewById(R.id.moneyTextView);

        isLocked = false;
        finish = false;
        holdTextView.setVisibility(View.INVISIBLE);

        runManager = new RunManager(this);
        holdButtonRunnable = new HoldButtonRunnable();
        messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(messageReceiver, new IntentFilter("ActivityRecognitionUpdates"));

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLocked){
                    return;
                }
                if (!runManager.isRunning()) {
                    runManager.startRun();
                    startButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    startButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_stop_black_36dp));
                } else {
                    runManager.stopRun();
                    detectedActivityList.clear();
                    startButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    startButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_play_arrow_black_36dp));
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
                        buttonHandler.postDelayed(holdButtonRunnable, 2000);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(buttonHandler == null) return true;
                        buttonHandler.removeCallbacks(holdButtonRunnable);
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
    }

    @Override
    public void onBackPressed() {
        myDialog = new MyDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title","ARE YOU SURE?");
        bundle.putString("message","Are you sure you want end this run?");
        myDialog.setArguments(bundle);
        myDialog.show(getFragmentManager(), "myDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        runManager.finishRun();
        dialog.dismiss();
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        if (finish) {
            finish();
        }
        dialog.dismiss();
    }

    private class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("Status");
            if (isFinishing()) {
                return;
            }

            if (isDefinetlyNotRunnning(Integer.parseInt(message))) {
                finish = true;
                runManager.stopRun();
                startButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                startButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_play_arrow_black_36dp));

                finishRunDialog = new FinishRunDialog();
                Bundle bundle = new Bundle();
                if (message == "1") {
                    bundle.putString("title", " VEHICLE USAGE IS NOT ALLOWED");
                } else {
                    bundle.putString("title", " BICYCLE USAGE IS NOT ALLOWED");
                }
                bundle.putString("message", "Only activity allowed here is running. This run will be terminated.");
                finishRunDialog.setArguments(bundle);
                finishRunDialog.show(getFragmentManager(), "finishRunDialog");
            }
        }

        private boolean isDefinetlyNotRunnning(int currentActivity) {
            detectedActivityList.add(currentActivity);
            int counter = 0;
            for (int activity : detectedActivityList) {
                if (activity == currentActivity) {
                    counter++;
                } else {
                    detectedActivityList.clear();
                    detectedActivityList.add(currentActivity);
                    return false;
                }
            }
            if (counter == 5) {
                return true;
            }
            return false;
        }
    }

    private class HoldButtonRunnable implements Runnable {

        @Override
        public void run() {
            if (!isLocked)
                return;
            cancelButton.setVisibility(View.VISIBLE);
            lockButton.setVisibility(View.VISIBLE);
            holdTextView.setVisibility(View.INVISIBLE);
            isLocked = false;
            completeView.setKeepScreenOn(false);
            if (runManager.isRunning()) {
                startButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                startButton.setImageDrawable(ContextCompat.getDrawable(startButton.getContext(), R.drawable.ic_play_arrow_black_36dp));
            } else {
                startButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                startButton.setImageDrawable(ContextCompat.getDrawable(startButton.getContext(), R.drawable.ic_stop_black_36dp));
            }
        }
    }

}
