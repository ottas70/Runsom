package ottas70.runningapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

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

        MapView mapView = (MapView) myView.findViewById(R.id.mapView);
        buildMap(mapView);

        return myView;
    }

    private  void buildMap(MapView mapView){
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                
            }
        });
    }

}
