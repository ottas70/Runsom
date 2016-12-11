package ottas70.runningapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import ottas70.runningapp.Adapters.RunListAdapter;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;
import ottas70.runningapp.Run;

public class RunOverviewActivity extends Activity {

    private FloatingActionButton startRun;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_overview);

        startRun = (FloatingActionButton) findViewById(R.id.startRunButton);
        list = (ListView) findViewById(R.id.runList);

        startRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RunOverviewActivity.this,RunningActivity.class);
                startActivity(intent);
            }
        });

        ServerRequest request = new ServerRequest(this);
        request.getRuns(false, new GetCallback() {
            @Override
            public void done(Object o) {
                if(o == null){
                    return;
                }
                list.setAdapter(new RunListAdapter(list.getContext(), (ArrayList<Run>) o));
            }
        });

    }

}
