package ottas70.runningapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ottas70.runningapp.Building;
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
        TextView address;
        address = (TextView) myView.findViewById(R.id.addressTextView);

        Building building = buildingList.get(i);
        address.setText(building.getAddress());

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return myView;
    }
}
