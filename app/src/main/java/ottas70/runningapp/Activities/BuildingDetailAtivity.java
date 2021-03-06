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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import ottas70.runningapp.Models.Building;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Interfaces.MyDialogListener;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;
import ottas70.runningapp.Runsom;
import ottas70.runningapp.Models.User;
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

    private TextView total;
    private TextView type1;
    private TextView type2;
    private TextView type3;
    private TextView type4;
    private TextView type5;
    private LinearLayout requirements;

    private InfoDialog infoDialog;

    private GoogleApiClient googleApiClient;
    private Bundle bundle;
    private GoogleMap gMap;
    private double latitude;
    private double longtitude;
    private Building building;
    private User user = Runsom.getInstance().getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);

        arrowBackButton = (ImageView) findViewById(R.id.arrowBackButton);
        buildingImageView = (ImageView) findViewById(R.id.userImageView);
        addressTextView = (TextView) findViewById(R.id.usernameTextView);
        ownerTextView = (TextView) findViewById(R.id.ownerTextView);
        priceTextView = (TextView) findViewById(R.id.priceTextView);
        mapView = (MapView) findViewById(R.id.mapView);
        buyButton = (Button) findViewById(R.id.buyButton);
        bundle = savedInstanceState;
        requirements = (LinearLayout) findViewById(R.id.reqirementsLayout);
        total = (TextView) findViewById(R.id.totalTextView);
        type1 = (TextView) findViewById(R.id.type1TextView);
        type2 = (TextView) findViewById(R.id.type2TextView);
        type3 = (TextView) findViewById(R.id.type3TextView);
        type4 = (TextView) findViewById(R.id.type4TextView);
        type5 = (TextView) findViewById(R.id.type5TextView);

        building = (Building) getIntent().getSerializableExtra("building");
        addressTextView.setText(building.getAddress());
        ownerTextView.setText(building.getOwnersName());
        priceTextView.setText(building.getPrice() + " $");
        latitude = getIntent().getExtras().getDouble("latitude");
        longtitude = getIntent().getExtras().getDouble("longitude");

        setImage();
        if (Runsom.getInstance().getUser().getUsername().equals(building.getOwnersName())) {
            requirements.setVisibility(View.GONE);
            changeButton();
        }

        arrowBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBuyButtonClicked();
            }
        });

        setRequirements();
        createGoogleAPIClient();
    }

    private void buyBuilding() {
        if (!user.isMeetingRequirements(building.getBuildingType())) {
            notEnoughBuildings();
            return;
        }
        if (Runsom.getInstance().getUser().getMoney() >= building.getPrice()) {
            ServerRequest request = new ServerRequest(BuildingDetailAtivity.this);
            request.buyBuildingAsyncTask(building, true, new GetCallback() {
                @Override
                public void done(Object o) {
                    if (o != null) {
                        Boolean b = (Boolean) o;
                        if (b.booleanValue() == true) {
                            Runsom.getInstance().getUser().discountMoney(building.getPrice());
                            building.setOwnersName(Runsom.getInstance().getUser().getUsername());
                            ownerTextView.setText(building.getOwnersName());
                            user.getBuildings().add(building);
                            changeButton();
                            transactionSuccessful();
                        } else {
                            error();
                        }
                    }
                }
            });
        } else {
            notEnoughMoney();
        }
    }

    private void upgrade() {

    }

    private void onBuyButtonClicked() {
        if (!building.getOwnersName().equals(Runsom.getInstance().getUser().getUsername())) {
            buyBuilding();
        } else {
            upgrade();
        }
    }

    private void changeButton() {
        buyButton.setBackgroundColor(ContextCompat.getColor(this, R.color.wallet_holo_blue_light));
        buyButton.setText("UPGRADE");
    }

    private void transactionSuccessful() {
        infoDialog = new InfoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", " PURCHASE SUCCESSFUL");
        bundle.putString("message", " You have succesfully purchased building on address " + building.getAddress());
        infoDialog.setArguments(bundle);
        infoDialog.show(getFragmentManager(), "infoDialog");
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

    private void notEnoughBuildings() {
        infoDialog = new InfoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", " YOU HAVE NOT MET THE REQUIREMENTS");
        bundle.putString("message", " You have to purchase more buildings in order to buy this one");
        infoDialog.setArguments(bundle);
        infoDialog.show(getFragmentManager(), "infoDialog");
    }

    private void error() {
        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
    }

    private void setImage() {
        switch (building.getBuildingType()) {
            case OUTSKIRTS:
                buildingImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.building_type_1));
                break;
            case HOUSING_ESTATE:
                buildingImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.building_type_2));
                break;
            case LUCRATIVE_AREA:
                buildingImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.building_type_3));
                break;
            case CENTER:
                buildingImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.building_type_4));
                break;
            case HISTORIC_CENTRE:
                buildingImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.building_type_5));
                break;

        }
    }

    private void setRequirements() {
        switch (building.getBuildingType()) {
            case OUTSKIRTS:
                requirements.setVisibility(View.GONE);
                break;
            case HOUSING_ESTATE:
                type1.setText("8");
                total.setText("8");
                break;
            case LUCRATIVE_AREA:
                type1.setText("8");
                type2.setText("4");
                total.setText("12");
                break;
            case CENTER:
                type1.setText("8");
                type2.setText("4");
                type3.setText("3");
                total.setText("15");
                break;
            case HISTORIC_CENTRE:
                type1.setText("8");
                type2.setText("4");
                type3.setText("3");
                type4.setText("2");
                total.setText("17");
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
