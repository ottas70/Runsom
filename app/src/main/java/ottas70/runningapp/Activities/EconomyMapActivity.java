package ottas70.runningapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ottas70.runningapp.R;

public class EconomyMapActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

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
    public void onMapClick(LatLng latLng) {
        if (!bounds.contains(latLng)) {
            return;
        }
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String address = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1);

        Intent i = new Intent(this, BuildingDetailAtivity.class);
        Bundle b = new Bundle();
        b.putString("address", address);
        b.putDouble("latitude", latLng.latitude);
        b.putDouble("longitude", latLng.longitude);
        i.putExtras(b);
        startActivity(i);

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
}
