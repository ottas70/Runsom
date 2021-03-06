package ottas70.runningapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

import java.util.List;

import ottas70.runningapp.Activities.RunDetailsActivity;
import ottas70.runningapp.R;
import ottas70.runningapp.Models.Run;
import ottas70.runningapp.RunMapReadyCallback;
import ottas70.runningapp.Runsom;

/**
 * Created by ottovodvarka on 18.12.16.
 */

public class RunsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<Run> runsList;


    public RunsAdapter(Context context, List<Run> runsList) {
        this.context = context;
        this.runsList = runsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return runsList.size();
    }

    @Override
    public Object getItem(int i) {
        return runsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myView;
        if (view == null) {
            myView = inflater.inflate(R.layout.run_list_row, null);
        } else {
            myView = view;
        }
        TextView date, name, distance, speed, time;
        date = (TextView) myView.findViewById(R.id.dateTextView);
        name = (TextView) myView.findViewById(R.id.nameTextView);
        distance = (TextView) myView.findViewById(R.id.distanceTextView);
        speed = (TextView) myView.findViewById(R.id.speedTextView);
        time = (TextView) myView.findViewById(R.id.timeTextView);
        final MapView mapView = (MapView) myView.findViewById(R.id.mapView);

        final Run run = runsList.get(i);
        distance.setText(String.valueOf(run.getDistance()) + " km");
        date.setText(run.getDate());
        name.setText(run.getName());
        speed.setText(String.valueOf(run.getAverageSpeed()) + " km/h");
        time.setText(run.getDuration().toString());

        mapView.onCreate(null);
        mapView.setClickable(false);
        mapView.getMapAsync(new RunMapReadyCallback(mapView, run));

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, RunDetailsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putInt("listPosition", Runsom.getInstance().getUser().getRuns().indexOf(run));
                i.putExtras(b);
                context.startActivity(i);
            }
        });

        return myView;
    }

}
