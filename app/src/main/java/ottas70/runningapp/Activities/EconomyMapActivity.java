package ottas70.runningapp.Activities;

import android.os.Bundle;

import ottas70.runningapp.R;

public class EconomyMapActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_economy_map);

        setTitleText("MAP");
        initiateListeners();
    }
}
