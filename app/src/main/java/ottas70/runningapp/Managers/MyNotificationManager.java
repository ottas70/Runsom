package ottas70.runningapp.Managers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import ottas70.runningapp.Activities.RunningActivity;
import ottas70.runningapp.R;

/**
 * Created by ottovodvarka on 13.12.16.
 */

public class MyNotificationManager {

    private final int NOTIFICATION_ID = 001;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    private Context context;

    public MyNotificationManager(Context context) {
        this.context = context;

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void createNotification() {
        notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Running App")
                .setContentText("Running App is tracking your run")
                .setOngoing(true);
        Intent resultIntent = new Intent(context, RunningActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

    }

    public void deleteNotification() {
        notificationManager.cancel(NOTIFICATION_ID);

    }

}
