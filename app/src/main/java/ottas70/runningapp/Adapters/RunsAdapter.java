package ottas70.runningapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

import java.util.List;

import ottas70.runningapp.MapReadyCallback;
import ottas70.runningapp.R;
import ottas70.runningapp.Run;

/**
 * Created by ottovodvarka on 12.12.16.
 */

public class RunsAdapter extends RecyclerView.Adapter<RunsAdapter.MyViewHolder> {

    private List<Run> runsList;
    private Context context;

    public RunsAdapter(List<Run> runList, Context context) {
        this.runsList = runList;
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

        holder.mapView.getMapAsync(new MapReadyCallback(holder.mapView, run));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //...
            }
        });
    }

    @Override
    public int getItemCount() {
        return runsList.size();
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

            mapView.onCreate(null);
            mapView.setClickable(false);
            mapView.onResume();

        }
    }


}
