package ottas70.runningapp.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import ottas70.runningapp.R;

public class BuildingDetailAtivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ImageView arrowBackButton;
    private ImageView buildingImageView;
    private TextView addressTextView;
    private TextView ownerTextView;
    private MapView mapView;
    private Button buyButton;

    private GoogleApiClient googleApiClient;
    private Bundle bundle;
    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);

        arrowBackButton = (ImageView) findViewById(R.id.arrowBackButton);
        buildingImageView = (ImageView) findViewById(R.id.buildingTypeImageView);
        addressTextView = (TextView) findViewById(R.id.addressTextView);
        ownerTextView = (TextView) findViewById(R.id.ownerTextView);
        mapView = (MapView) findViewById(R.id.mapView);
        buyButton = (Button) findViewById(R.id.buyButton);
        bundle = savedInstanceState;

        createGoogleAPIClient();
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
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                gMap.getUiSettings().setMapToolbarEnabled(false);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(50.081002, 14.427984))
                        .zoom(15)
                        .build();
                gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                mapView.onResume();
            }
        });
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
