package ottas70.runningapp.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ottas70.runningapp.Fragments.SettingsFragment;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;

public class SettingsActivity extends Activity {

    private TextView header;
    private ImageView leftImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        header = (TextView) findViewById(R.id.header).findViewById(R.id.titleTextView);
        leftImage = (ImageView) findViewById(R.id.header).findViewById(R.id.left_image);

        header.setText("SETTINGS");
        leftImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_36dp));
        leftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

}
