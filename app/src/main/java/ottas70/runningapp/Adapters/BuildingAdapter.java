package ottas70.runningapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ottas70.runningapp.Activities.BuildingDetailAtivity;
import ottas70.runningapp.Building;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;

/**
 * Created by ottovodvarka on 31.01.17.
 */

public class BuildingAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<Building> buildingList;

    public BuildingAdapter(Context context, List<Building> buildingList) {
        this.context = context;
        this.buildingList = buildingList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return buildingList.size();
    }

    @Override
    public Object getItem(int i) {
        return buildingList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myView;
        if (view == null) {
            myView = layoutInflater.inflate(R.layout.building_list_row, null);
        } else {
            myView = view;
        }
        TextView address, price;
        ImageView romanNumeral;
        address = (TextView) myView.findViewById(R.id.usernameTextView);
        price = (TextView) myView.findViewById(R.id.priceTextView);
        romanNumeral = (ImageView) myView.findViewById(R.id.userImageView);

        final Building building = buildingList.get(i);
        address.setText(building.getAddress());
        price.setText(building.getPrice() + " $");
        setRomanNumeral(building, romanNumeral);

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerRequest request = new ServerRequest(context);
                request.getNominatimCoordinatesAsyncTask(building.getAddress(), false, new GetCallback() {
                    @Override
                    public void done(Object o) {
                        if(o == null)
                            return;
                        LatLng latLng = (LatLng) o;
                        Intent i = new Intent(context, BuildingDetailAtivity.class);
                        i.putExtra("building", building);
                        Bundle b = new Bundle();
                        b.putDouble("latitude", latLng.latitude);
                        b.putDouble("longitude", latLng.longitude);
                        i.putExtras(b);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                });
            }
        });

        return myView;
    }

    private void setRomanNumeral(Building building, ImageView imageView){
        switch (building.getBuildingType()) {
            case OUTSKIRTS:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.roman_one));
                break;
            case HOUSING_ESTATE:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.roman_two));
                break;
            case LUCRATIVE_AREA:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.roman_three));
                break;
            case CENTER:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.roman_four));
                break;
            case HISTORIC_CENTRE:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.roman_five));
                break;
        }
    }

}
