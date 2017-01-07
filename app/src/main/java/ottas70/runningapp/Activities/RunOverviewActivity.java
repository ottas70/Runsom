package ottas70.runningapp.Activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import ottas70.runningapp.Adapters.RunsAdapter;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Interfaces.MyLocationListener;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;
import ottas70.runningapp.Run;
import ottas70.runningapp.Runsom;
import ottas70.runningapp.Services.LocationProviderService;

public class RunOverviewActivity extends BaseActivity {

    private FloatingActionButton startRun;
    private MapView mapView;
    private ListView listView;

    private GoogleMap gMap;

    private RunsAdapter runsAdapter;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_overview);

        setTitleText("ACTIVITY");
        initiateListeners();

        startRun = (FloatingActionButton) findViewById(R.id.startRunButton);
        mapView = (MapView) findViewById(R.id.mapView);
        listView = (ListView) findViewById(R.id.runsListView);

        this.savedInstanceState = savedInstanceState;
        createMap();

        startRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RunOverviewActivity.this, RunningActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRuns();
    }

    private void getRuns() {
        ServerRequest request = new ServerRequest(this);
        request.getRuns(false, new GetCallback() {
            @Override
            public void done(Object o) {
                if (o == null) {
                    return;
                }
                Runsom.getInstance().getUser().setRuns((ArrayList<Run>) o);

                runsAdapter = new RunsAdapter(getApplicationContext(), (List<Run>) o);
                listView.setAdapter(runsAdapter);

            }
        });
    }

    private void createMap() {
        mapView.onCreate(savedInstanceState);
        mapView.setClickable(false);
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                gMap.getUiSettings().setMapToolbarEnabled(false);
                gMap.getUiSettings().setMyLocationButtonEnabled(false);
                gMap.setMyLocationEnabled(true);
                LocationProviderService lps = new LocationProviderService(getApplicationContext(),
                        new MyLocationListener() {
                            @Override
                            public void onLocationReceived(Location location) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                mapView.onResume();
                            }
                        });
            }
        });
    }
}
