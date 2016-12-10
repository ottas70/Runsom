package ottas70.runningapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import ottas70.runningapp.R;

public class RunOverviewActivity extends Activity {

    private FloatingActionButton startRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_overview);

        startRun = (FloatingActionButton) findViewById(R.id.startRunButton);

        startRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RunOverviewActivity.this,RunningActivity.class);
                startActivity(intent);
            }
        });

    }

}
