package ottas70.runningapp;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ottovodvarka on 14.12.16.
 */

public class DisplayToast implements Runnable {

    private final Context context;
    private String text;

    public DisplayToast(Context context, String text) {
        this.context = context;
        this.text = text;
    }

    @Override
    public void run() {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

}
