package ottas70.runningapp.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

import ottas70.runningapp.DisplayToast;

/**
 * Created by ottovodvarka on 13.12.16.
 */

public class ActivityRecognitionService extends IntentService {

    private Handler toastHandler;

    public ActivityRecognitionService() {
        super("ActivityRecognitionService");
        toastHandler = new Handler();
    }

    public ActivityRecognitionService(String name) {
        super(name);
        toastHandler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("SERVICE", "HANDLE USE");
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities(result.getProbableActivities());
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        for (DetectedActivity activity : probableActivities) {
            switch (activity.getType()) {
                case DetectedActivity.IN_VEHICLE: {
                    if (activity.getConfidence() > 75) {
                        toastHandler.post(new DisplayToast(this, "Vehicle usage is not allowed"));
                        sendMessageToActivity("1");
                    }
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    if (activity.getConfidence() > 75) {
                        toastHandler.post(new DisplayToast(this, "Bicycle usage is not allowed"));
                        sendMessageToActivity("2");
                    }
                    break;
                }
            }
        }
    }

    private void sendMessageToActivity(String msg) {
        Intent intent = new Intent("ActivityRecognitionUpdates");
        intent.putExtra("Status", msg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("SERVICE", "DISCONNECTED");
    }
}
