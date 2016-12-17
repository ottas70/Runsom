package ottas70.runningapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.List;

import ottas70.runningapp.R;
import ottas70.runningapp.Run;

/**
 * Created by ottovodvarka on 12.12.16.
 */

public class RunsAdapter extends RecyclerView.Adapter<RunsAdapter.MyViewHolder> {

    private List<Run> runsList;
    private Bundle bundle;
    private Context context;

    public RunsAdapter(List<Run> runList, Bundle bundle, Context context) {
        this.runsList = runList;
        this.bundle = bundle;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Run run = runsList.get(position);
        holder.distance.setText(String.valueOf(run.getDistance()) + " km");
        holder.date.setText(run.getDate());
        holder.name.setText(run.getName());
        holder.speed.setText(String.valueOf(run.getAverageSpeed()) + " km/h");
        holder.time.setText(run.getDuration().toString());

        holder.mapView.onCreate(bundle);
        holder.mapView.setClickable(false);
        holder.mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                try {
                    MapsInitializer.initialize(context);
                    List<LatLng> path = PolyUtil.decode(run.getEncodedPath());
                    Polyline line = googleMap.addPolyline(new PolylineOptions()
                            .width(5)
                            .color(Color.RED));
                    line.setPoints(path);
                    fixZoom(path, googleMap);
                    holder.mapView.onResume();

                } catch (Exception e) {

                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //...
            }
        });
    }

    @Override
    public void onViewRecycled(MyViewHolder holder) {
        holder.mapView.onResume();
    }

    @Override
    public int getItemCount() {
        return runsList.size();
    }

    private void fixZoom(List<LatLng> latLngList, GoogleMap map) {
        LatLngBounds.Builder bc = new LatLngBounds.Builder();
        for (LatLng item : latLngList) {
            bc.include(item);
        }
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 30));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, name, distance, speed, time;
        public MapView mapView;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.dateTextView);
            name = (TextView) view.findViewById(R.id.nameTextView);
            distance = (TextView) view.findViewById(R.id.distanceTextView);
            speed = (TextView) view.findViewById(R.id.speedTextView);
            time = (TextView) view.findViewById(R.id.timeTextView);
            mapView = (MapView) view.findViewById(R.id.mapView);
        }
    }


}
