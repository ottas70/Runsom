package ottas70.runningapp.Activities;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

import ottas70.runningapp.Adapters.BuildingAdapter;
import ottas70.runningapp.Building;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Listeners.EndlessScrollListener;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;

public class EconomyOverviewActivity extends BaseActivity {

    private ListView listView;

    private BuildingAdapter buildingAdapter;
    private ArrayList<Building> buildingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_economy_overview);

        setTitleText("ECONOMY");
        initiateListeners();

        listView = (ListView) findViewById(R.id.buildingList);

        initiateList();

    }

    private void initiateList() {
        ServerRequest request = new ServerRequest(this);
        request.loadBuildingsAsyncTask(makeQuery(0), false, new GetCallback() {
            @Override
            public void done(Object o) {
                if (o == null)
                    return;
                buildingList.addAll((Collection<? extends Building>) o);
                buildingAdapter = new BuildingAdapter(getApplicationContext(), buildingList);
                listView.setAdapter(buildingAdapter);
                listView.setOnScrollListener(new EndlessScrollListener(getApplicationContext(), buildingList, buildingAdapter));
            }
        });
    }


    private String makeQuery(int pageNumber) {
        String query = "SELECT * FROM Buildings LIMIT " + (pageNumber * 10) + ",10";
        return query;
    }

}
