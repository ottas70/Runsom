package ottas70.runningapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ottas70.runningapp.R;

/**
 * Created by ottovodvarka on 07.01.17.
 */

abstract class BaseActivity extends Activity {

    private TextView title;
    private ImageView feed;
    private ImageView run;
    private ImageView map;
    private ImageView stats;
    private ImageView account;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setTitleText(String text) {
        title = (TextView) findViewById(R.id.titleTextView);
        title.setText(text);
    }

    protected void setImage(int drawable, View.OnClickListener listener){
        image = (ImageView) findViewById(R.id.image);
        image.setImageDrawable(ContextCompat.getDrawable(this, drawable));
        image.setOnClickListener(listener);
    }

    protected void initiateListeners() {
        feed = (ImageView) findViewById(R.id.feedButton);
        run = (ImageView) findViewById(R.id.runOverviewButton);
        map = (ImageView) findViewById(R.id.economyMapButton);
        stats = (ImageView) findViewById(R.id.econonyOverviewButton);
        account = (ImageView) findViewById(R.id.accountButton);

        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BaseActivity.this, FeedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BaseActivity.this, RunOverviewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BaseActivity.this, EconomyMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BaseActivity.this, EconomyOverviewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BaseActivity.this, AccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
}
