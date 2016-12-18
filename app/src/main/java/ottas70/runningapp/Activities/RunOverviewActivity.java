package ottas70.runningapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ottas70.runningapp.Adapters.RunsAdapter;
import ottas70.runningapp.Adapters.RunsListAdapter;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;
import ottas70.runningapp.Run;
import ottas70.runningapp.Runsom;

public class RunOverviewActivity extends Activity {

    private FloatingActionButton startRun;
    private RecyclerView recyclerView;

    private ListView listView;

    private RunsAdapter runsAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_overview);

        startRun = (FloatingActionButton) findViewById(R.id.startRunButton);
        //recyclerView = (RecyclerView) findViewById(R.id.runsRecyclerView);

        listView = (ListView) findViewById(R.id.runsListView);

        startRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RunOverviewActivity.this,RunningActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRuns();
    }

    private void getRuns() {
        ServerRequest request = new ServerRequest(this);
        request.getRuns(false, new GetCallback() {
            @Override
            public void done(Object o) {
                if (o == null) {
                    return;
                }
                Runsom.getInstance().getUser().setRuns((ArrayList<Run>) o);
                //initializeRecyclerView((List<Run>) o);

                listView.setAdapter(new RunsListAdapter(getApplicationContext(), (List<Run>) o));

            }
        });
    }

    private void initializeRecyclerView(List<Run> runs) {
        runsAdapter = new RunsAdapter(runs, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.horizontal_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(runsAdapter);
    }
}
