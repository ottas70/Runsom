package ottas70.runningapp.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapView;

import ottas70.runningapp.MapReadyCallback;
import ottas70.runningapp.R;
import ottas70.runningapp.Run;
import ottas70.runningapp.Runsom;

public class RunDetailsActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView dateTextView;
    private TextView distanceTextView;
    private TextView durationTextView;
    private TextView speedTextView;
    private TextView moneyTextView;
    private TextView caloriesTextView;
    private MapView mapView;
    private ImageView backButton;

    private Run run;

    private Bundle bundle;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_details);

        dateTextView = (TextView) findViewById(R.id.dateTextView);
        distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        durationTextView = (TextView) findViewById(R.id.durationTextView);
        speedTextView = (TextView) findViewById(R.id.speedTextView);
        moneyTextView = (TextView) findViewById(R.id.moneyTextView);
        caloriesTextView = (TextView) findViewById(R.id.caloriesTextView);
        mapView = (MapView) findViewById(R.id.mapView);
        backButton = (ImageView) findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bundle = savedInstanceState;
        int listPosition = getIntent().getExtras().getInt("listPosition");
        run = Runsom.getInstance().getUser().getRuns().get(listPosition);

        initiateValues();
        createGoogleAPIClient();
    }

    private void initiateValues() {
        dateTextView.setText(run.getDate());
        distanceTextView.setText(String.valueOf(run.getDistance()) + " km");
        durationTextView.setText(run.getDuration().toString());
        speedTextView.setText(String.valueOf(run.getAverageSpeed()) + " km/h");
        moneyTextView.setText(run.getMoneyEarned() + " $");
        if (Runsom.getInstance().getUser().getWeight() != -1) {
            caloriesTextView.setText(run.getCalories());
        }

    }

    private void createGoogleAPIClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addApi(ActivityRecognition.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        googleApiClient.connect();
    }

    private void initiateMap() {
        mapView.onCreate(bundle);
        mapView.setClickable(false);
        mapView.getMapAsync(new MapReadyCallback(mapView, run));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initiateMap();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
