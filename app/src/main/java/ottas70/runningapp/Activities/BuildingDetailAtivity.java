package ottas70.runningapp.Activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
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
import com.google.android.gms.maps.model.MarkerOptions;

import ottas70.runningapp.Building;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Interfaces.MyDialogListener;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;
import ottas70.runningapp.Runsom;
import ottas70.runningapp.Views.InfoDialog;

public class BuildingDetailAtivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
        , MyDialogListener {

    private ImageView arrowBackButton;
    private ImageView buildingImageView;
    private TextView addressTextView;
    private TextView ownerTextView;
    private TextView priceTextView;
    private MapView mapView;
    private Button buyButton;

    private InfoDialog infoDialog;

    private GoogleApiClient googleApiClient;
    private Bundle bundle;
    private GoogleMap gMap;
    private double latitude;
    private double longtitude;
    private Building building;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);

        arrowBackButton = (ImageView) findViewById(R.id.arrowBackButton);
        buildingImageView = (ImageView) findViewById(R.id.buildingTypeImageView);
        addressTextView = (TextView) findViewById(R.id.addressTextView);
        ownerTextView = (TextView) findViewById(R.id.ownerTextView);
        priceTextView = (TextView) findViewById(R.id.priceTextView);
        mapView = (MapView) findViewById(R.id.mapView);
        buyButton = (Button) findViewById(R.id.buyButton);
        bundle = savedInstanceState;

        building = (Building) getIntent().getSerializableExtra("building");
        addressTextView.setText(building.getAddress());
        ownerTextView.setText(building.getOwnersName());
        priceTextView.setText(building.getPrice() + " $");
        latitude = getIntent().getExtras().getDouble("latitude");
        longtitude = getIntent().getExtras().getDouble("longitude");

        setImage();

        arrowBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Runsom.getInstance().getUser().getMoney() <= building.getPrice()) {
                    ServerRequest request = new ServerRequest(BuildingDetailAtivity.this);
                    request.buyBuildingAsyncTask(building, true, new GetCallback() {
                        @Override
                        public void done(Object o) {
                            if (o != null) {
                                Boolean b = (Boolean) o;
                                if (b.booleanValue() == true) {
                                    Runsom.getInstance().getUser().discountMoney(building.getPrice());
                                }
                            }
                        }
                    });
                } else {
                    notEnoughMoney();
                }
            }
        });

        createGoogleAPIClient();
    }

    private void notEnoughMoney() {
        infoDialog = new InfoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", " NOT ENOUGH MONEY");
        bundle.putString("message", " This building costs " + building.getPrice() + " $." +
                " You have only " + Runsom.getInstance().getUser().getMoney() + " $ on your account");
        infoDialog.setArguments(bundle);
        infoDialog.show(getFragmentManager(), "infoDialog");
    }

    private void setImage() {
        switch (building.getBuildingType()) {
            case FIRST_TYPE:
                buildingImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.building_type_1));
                break;
            case SECOND_TYPE:
                buildingImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.building_type_2));
                break;
            case THIRD_TYPE:
                buildingImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.building_type_3));
                break;

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
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                gMap.getUiSettings().setMapToolbarEnabled(false);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latitude, longtitude))
                        .zoom(15)
                        .build();
                gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                gMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longtitude)));

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

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
