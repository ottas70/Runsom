package ottas70.runningapp.Activities;

import android.os.Bundle;
import android.widget.TextView;

import ottas70.runningapp.R;
import ottas70.runningapp.Run;
import ottas70.runningapp.Runsom;
import ottas70.runningapp.User;

public class AccountActivity extends BaseActivity {

    private TextView title;

    private TextView money;
    private TextView runs;
    private TextView distance;

    private TextView gender;
    private TextView height;
    private TextView weight;

    private User user = Runsom.getInstance().getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        setTitleText("ACCOUNT");
        initiateListeners();

        money = (TextView) findViewById(R.id.moneyTextView);
        runs = (TextView) findViewById(R.id.runsTextView);
        distance = (TextView) findViewById(R.id.distanceTextView);
        gender = (TextView) findViewById(R.id.genderTextView);
        height = (TextView) findViewById(R.id.heightTextView);
        weight = (TextView) findViewById(R.id.weightTextView);

        initiateValues();
    }

    private void initiateValues(){
        money.setText(String.valueOf(user.getMoney()));
        runs.setText(String.valueOf(user.getRuns().size()));
        double km = 0;
        for ( Run r : user.getRuns()){
            km += r.getDistance();
        }
        distance.setText(km + " km");
        gender.setText(user.getGender());
        height.setText(String.valueOf(user.getHeight()) + " cm");
        weight.setText(String.valueOf(user.getWeight()) + " kg");
    }

}
