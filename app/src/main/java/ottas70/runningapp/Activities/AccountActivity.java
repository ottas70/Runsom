package ottas70.runningapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ottas70.runningapp.Adapters.BuildingAdapter;
import ottas70.runningapp.Models.Building;
import ottas70.runningapp.BuildingType;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;
import ottas70.runningapp.Models.Run;
import ottas70.runningapp.Runsom;
import ottas70.runningapp.Models.User;

public class AccountActivity extends BaseActivity {

    private String QUERY = "SELECT * FROM Buildings JOIN Users ON Buildings.user_id = Users.user_id " +
            "WHERE username=\"" + Runsom.getInstance().getUser().getUsername()+"\" LIMIT 0,3";
    private TextView title;

    private TextView username;
    private TextView email;
    private TextView money;
    private TextView runs;
    private TextView distance;

    private TextView gender;
    private TextView height;
    private TextView weight;

    private TextView total;
    private TextView type1;
    private TextView type2;
    private TextView type3;
    private TextView type4;
    private TextView type5;

    private TextView loadMore;

    private User user = Runsom.getInstance().getUser();
    private ListView mBuildings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        setTitleText("ACCOUNT");
        setRightImage(R.drawable.ic_settings_white_36dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        initiateListeners();

        username = (TextView) findViewById(R.id.usernameTextView);
        email = (TextView) findViewById(R.id.emailTextView);
        money = (TextView) findViewById(R.id.moneyTextView);
        runs = (TextView) findViewById(R.id.runsTextView);
        distance = (TextView) findViewById(R.id.distanceTextView);
        gender = (TextView) findViewById(R.id.genderTextView);
        height = (TextView) findViewById(R.id.heightTextView);
        weight = (TextView) findViewById(R.id.weightTextView);
        mBuildings = (ListView) findViewById(R.id.myBuildingsList);
        loadMore = (TextView) findViewById(R.id.loadMore);
        total = (TextView) findViewById(R.id.totalTextView);
        type1 = (TextView) findViewById(R.id.type1TextView);
        type2 = (TextView) findViewById(R.id.type2TextView);
        type3 = (TextView) findViewById(R.id.type3TextView);
        type4 = (TextView) findViewById(R.id.type4TextView);
        type5 = (TextView) findViewById(R.id.type5TextView);

        initiateValues();

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, EconomyOverviewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("username",user.getUsername());
                getApplicationContext().startActivity(intent);
            }
        });
    }

    private void initiateValues() {
        username.setText(user.getUsername());
        email.setText(user.getEmail());
        money.setText(String.valueOf(user.getMoney()));
        runs.setText(String.valueOf(user.getRuns().size()));
        double km = 0;
        for (Run r : user.getRuns()) {
            km += r.getDistance();
        }
        distance.setText(km + " km");
        gender.setText(user.getGender());
        height.setText(String.valueOf(user.getHeight()) + " cm");
        weight.setText(String.valueOf(user.getWeight()) + " kg");
        total.setText(String.valueOf(user.getBuildings().size()));
        type1.setText(String.valueOf(user.getBuildingsCount(BuildingType.OUTSKIRTS)));
        type2.setText(String.valueOf(user.getBuildingsCount(BuildingType.HOUSING_ESTATE)));
        type3.setText(String.valueOf(user.getBuildingsCount(BuildingType.LUCRATIVE_AREA)));
        type4.setText(String.valueOf(user.getBuildingsCount(BuildingType.CENTER)));
        type5.setText(String.valueOf(user.getBuildingsCount(BuildingType.HISTORIC_CENTRE)));
        ServerRequest request = new ServerRequest(this);
        request.loadBuildingsAsyncTask(QUERY, null, new GetCallback() {
            @Override
            public void done(Object o) {
                if (o == null)
                    return;
                mBuildings.setAdapter(new BuildingAdapter(getApplicationContext(), (List<Building>) o));
            }
        });
    }

    @Override
    protected void onResume() {
        gender.setText(user.getGender());
        height.setText(String.valueOf(user.getHeight()) + " cm");
        weight.setText(String.valueOf(user.getWeight()) + " kg");
        super.onResume();
}
}
