package ottas70.runningapp.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

import ottas70.runningapp.Adapters.BuildingAdapter;
import ottas70.runningapp.Building;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Listeners.EndlessScrollListener;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;
import ottas70.runningapp.Views.SortDialog;

public class EconomyOverviewActivity extends BaseActivity {

    private ListView listView;
    private SortDialog sortDialog;

    private BuildingAdapter buildingAdapter;
    private ArrayList<Building> buildingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_economy_overview);

        setTitleText("ECONOMY");
        setImage(R.drawable.runner, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortDialog = new SortDialog();
                sortDialog.show(getFragmentManager(),"sortDialog");
            }
        });
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
        String query = "SELECT * FROM Buildings " +
                "JOIN Users ON Buildings.user_id = Users.user_id " +
                "LIMIT " + (pageNumber * 10) + ",10";
        return query;
    }

}
