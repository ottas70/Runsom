package ottas70.runningapp.Activities;

import android.os.Bundle;
import android.widget.TextView;

import ottas70.runningapp.R;

public class AccountActivity extends BaseActivity {

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        setTitleText("ACCOUNT");
        initiateListeners();
    }
}
