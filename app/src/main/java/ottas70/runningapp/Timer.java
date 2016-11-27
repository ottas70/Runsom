package ottas70.runningapp;

import android.os.Handler;
import android.widget.TextView;

/**
 * Created by Ottas on 27.11.2016.
 */

public class Timer implements Runnable {

    private long counter;
    private TextView timer;
    private Handler handler;

    public Timer(TextView timer,Handler handler, long counter) {
        this.timer = timer;
        this.handler = handler;
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
        timer.setText(""+String.format("%02d",hours)+":"+String.format("%02d",minutes)+":"+String.format("%02d",seconds));
        handler.postDelayed(this,1000);
    }

}
