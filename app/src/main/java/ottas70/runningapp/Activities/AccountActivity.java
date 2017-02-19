package ottas70.runningapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ottas70.runningapp.Adapters.BuildingAdapter;
import ottas70.runningapp.Building;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;
import ottas70.runningapp.Run;
import ottas70.runningapp.Runsom;
import ottas70.runningapp.User;

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

    private TextView loadMore;

    private User user = Runsom.getInstance().getUser();
    private ListView mBuildings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        setTitleText("ACCOUNT");
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
        ServerRequest request = new ServerRequest(this);
        request.loadBuildingsAsyncTask(QUERY, false, new GetCallback() {
            @Override
            public void done(Object o) {
                if (o == null)
                    return;
                mBuildings.setAdapter(new BuildingAdapter(getApplicationContext(), (List<Building>) o));
            }
        });
    }

}
