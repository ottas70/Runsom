package ottas70.runningapp;

import android.os.Handler;
import android.widget.TextView;

/**
 * Created by Ottas on 27.11.2016.
 */

public class Timer implements Runnable {

    private TextView timerTextView;
    private Handler handler;
    private Duration duration;

     public Timer(TextView timerTextView,Handler handler) {
        this.timerTextView = timerTextView;
        this.handler = handler;
        duration = new Duration(0,0,0);
    }

    @Override
    public void run() {
        duration.addSecond();
        timerTextView.setText(duration.toString());
        handler.postDelayed(this,1000);
    }

    public Duration getDuration() {
        return duration;
    }
}
