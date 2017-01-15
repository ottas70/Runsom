package ottas70.runningapp.Activities;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolygonOptions;

import ottas70.runningapp.R;

public class EconomyMapActivity extends BaseActivity {

    private final LatLng NORTHEAST_LATLNG = new LatLng(50.113836, 14.474836);
    private final LatLng SOUTHWEST_LATLNG = new LatLng(50.047974, 14.362646);

    private MapView mapView;

    private GoogleMap gMap;
    private LatLngBounds bounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_economy_map);

        setTitleText("MAP");
        initiateListeners();

        mapView = (MapView) findViewById(R.id.economyMap);
        mapView.onCreate(savedInstanceState);

        createMap();
    }

    private void createMap() {
        mapView.setClickable(true);
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                gMap.getUiSettings().setMapToolbarEnabled(false);
                gMap.getUiSettings().setMyLocationButtonEnabled(false);

                bounds = new LatLngBounds(SOUTHWEST_LATLNG, NORTHEAST_LATLNG);
                gMap.setLatLngBoundsForCameraTarget(bounds);
                gMap.setMinZoomPreference(11.5f);


                createBorder();
            }
        });
    }

    private void createBorder() {
        PolygonOptions options = new PolygonOptions();

        LatLng northwest = new LatLng(bounds.southwest.latitude, bounds.northeast.longitude);
        LatLng southeast = new LatLng(bounds.northeast.latitude, bounds.southwest.longitude);

        options.add(bounds.northeast, northwest);
        options.add(northwest, bounds.southwest);
        options.add(bounds.southwest, southeast);
        options.add(southeast, bounds.northeast);

        options.strokeColor(Color.RED);
        gMap.addPolygon(options);
    }
}
