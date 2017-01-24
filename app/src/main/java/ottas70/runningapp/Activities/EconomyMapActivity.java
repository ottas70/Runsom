package ottas70.runningapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolygonOptions;

import ottas70.runningapp.Building;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;

public class EconomyMapActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private final LatLng NORTHEAST_LATLNG = new LatLng(50.1138, 14.4748);
    private final LatLng SOUTHWEST_LATLNG = new LatLng(50.0479, 14.3626);

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
        mapView.getMapAsync(this);
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

    @Override
    public void onMapClick(final LatLng latLng) {
        if (!bounds.contains(latLng)) {
            return;
        }

        final ServerRequest request = new ServerRequest(this);
        request.getNominatimAddress(latLng, false, new GetCallback() {
            @Override
            public void done(Object o) {
                if (o != null && !o.equals("")) {
                    final String address = (String) o;
                    Log.e("NOMINATIM ADDRESS", address);
                    request.getBuildingAsyncTask(address, true, new GetCallback() {
                        @Override
                        public void done(Object o) {
                            if (o != null) {
                                Building building = (Building) o;
                                Intent i = new Intent(getApplicationContext(), BuildingDetailAtivity.class);
                                i.putExtra("building", building);
                                Bundle b = new Bundle();
                                b.putDouble("latitude", latLng.latitude);
                                b.putDouble("longitude", latLng.longitude);
                                i.putExtras(b);
                                startActivity(i);
                            } else {
                                writeToast();
                            }
                        }
                    });
                } else {
                    writeToast();
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setMapToolbarEnabled(false);
        gMap.getUiSettings().setMyLocationButtonEnabled(false);

        bounds = new LatLngBounds(SOUTHWEST_LATLNG, NORTHEAST_LATLNG);
        gMap.setLatLngBoundsForCameraTarget(bounds);
        gMap.setMinZoomPreference(11.5f);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(50.081002, 14.427984))
                .zoom(11.5f)
                .build();
        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        gMap.setOnMapClickListener(this);

        createBorder();
    }

    private void writeToast() {
        Toast.makeText(this, "Building is not for sale", Toast.LENGTH_SHORT).show();
    }
}
