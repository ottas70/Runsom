package ottas70.runningapp;

import android.widget.TextView;

/**
 * Created by Ottas on 27.11.2016.
 */

public class Timer implements Runnable {

    private long counter;
    private TextView timer;

    public Timer(TextView timer,long counter) {
        this.timer = timer;
        this.counter = counter;
    }

    @Override
    public void run() {
        counter++;
        long seconds = counter;
        long hours = seconds/3600;
        seconds -= hours*3600;
        long minutes = seconds/60;
        seconds -= minutes*60;
        timer.setText(String.valueOf(hours) + ":"+String.valueOf(minutes)+":"+String.valueOf(seconds));
    }

}
