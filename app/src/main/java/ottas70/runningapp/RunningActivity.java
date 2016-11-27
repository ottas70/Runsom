package ottas70.runningapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DecimalFormat;

public class RunningActivity extends Activity {

    private TextView distanceTextView;
    private TextView timerTextView;
    private Button start;

    private boolean isRunning;

    private DistanceTracker distanceTracker;
    private Handler handler = new Handler();
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        distanceTextView = (TextView) findViewById(R.id.distanceEditText);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        start = (Button) findViewById(R.id.StartButton);

        distanceTracker = new DistanceTracker(this,distanceTextView);
        timer = new Timer(timerTextView,handler,0);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isRunning){
                    start.setText("Stop");
                    handler.postDelayed(timer,1000);
                    distanceTracker.startLocationUpdates();
                    isRunning = true;
                }else{
                    start.setText("Start");
                    handler.removeCallbacks(timer);
                    isRunning = false;
                    distanceTracker.stopLocationUpdates();
                }
            }
        });


    }

    protected void onStart() {
        super.onStart();
        distanceTracker.getClient().connect();
    }

    protected void onStop() {
        super.onStop();
        distanceTracker.getClient().disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(distanceTracker.getClient().isConnected()){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }else{
                distanceTracker.startLocationUpdates();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(distanceTracker.getClient().isConnected()){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }else{
                distanceTracker.setCurrentLocation(null);
                distanceTracker.startLocationUpdates();
            }
        }
    }

}
