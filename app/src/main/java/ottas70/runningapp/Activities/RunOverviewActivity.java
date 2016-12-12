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

import java.util.List;

import ottas70.runningapp.Adapters.RunsAdapter;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;
import ottas70.runningapp.Run;

public class RunOverviewActivity extends Activity {

    private FloatingActionButton startRun;
    private RecyclerView recyclerView;
    private Bundle savedInstanceState;

    private RunsAdapter runsAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_overview);

        this.savedInstanceState = savedInstanceState;
        startRun = (FloatingActionButton) findViewById(R.id.startRunButton);
        recyclerView = (RecyclerView) findViewById(R.id.runsRecyclerView);

        startRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RunOverviewActivity.this,RunningActivity.class);
                startActivity(intent);
            }
        });
        getRuns();
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
                runsAdapter = new RunsAdapter((List<Run>) o, savedInstanceState, getApplicationContext());
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
        });
    }
}
