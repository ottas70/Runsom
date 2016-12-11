package ottas70.runningapp.Adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ottas70.runningapp.R;
import ottas70.runningapp.Run;

/**
 * Created by Ottas on 10.12.2016.
 */

public class RunListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Run> runs;
    private LayoutInflater inflater = null;

    public RunListAdapter(Context context, ArrayList<Run> runs) {
        this.context = context;
        this.runs = runs;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return runs.size();
    }

    @Override
    public Object getItem(int i) {
        return runs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myView;
        if(view == null){
            myView = inflater.inflate(R.layout.list_row,null);
        }else{
            myView = view;
        }
        Run run = runs.get(i);

        TextView date = (TextView) myView.findViewById(R.id.dateTextView);
        date.setText(run.getDate());

        TextView name = (TextView) myView.findViewById(R.id.nameTextView);
        name.setText(run.getName());

        TextView distance = (TextView) myView.findViewById(R.id.distanceTextView);
        distance.setText(String.valueOf(run.getDistance()));

        TextView speed = (TextView) myView.findViewById(R.id.speedTextView);
        speed.setText(String.valueOf(run.getAverageSpeed()));

        TextView time = (TextView) myView.findViewById(R.id.timeTextView);
        time.setText(run.getDuration().toString());

        FragmentManager manager = ((Activity)context).getFragmentManager();
        MapFragment mapFragment = (MapFragment) manager.findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(0, 0))
                        .title("Marker"));
            }
        });

        return myView;
    }


}
