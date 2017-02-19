package ottas70.runningapp.Activities;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

import ottas70.runningapp.Adapters.BuildingAdapter;
import ottas70.runningapp.Building;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Interfaces.SortDialogListener;
import ottas70.runningapp.Listeners.EndlessScrollListener;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;
import ottas70.runningapp.SortInfo;
import ottas70.runningapp.SortInfoBuilder;
import ottas70.runningapp.Views.SortDialog;

public class EconomyOverviewActivity extends BaseActivity implements SortDialogListener{

    private ListView listView;
    private SortDialog sortDialog;

    private BuildingAdapter buildingAdapter;
    private ArrayList<Building> buildingList = new ArrayList<>();
    private String query;
    private SortInfo sortInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_economy_overview);

        setTitleText("BUILDING LIST");
        setImage(R.drawable.ic_sort_white_36dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortDialog = new SortDialog();
                Bundle args = new Bundle();
                args.putSerializable("sortInfo",sortInfo);
                sortDialog.setArguments(args);
                sortDialog.show(getFragmentManager(),"sortDialog");
            }
        });
        initiateListeners();

        listView = (ListView) findViewById(R.id.buildingList);

        initiateSort();
        createQueryWithoutLimit();
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
                listView.setOnScrollListener(new EndlessScrollListener(getApplicationContext(), buildingList,
                        buildingAdapter,query));
            }
        });
    }


    private String makeQuery(int pageNumber) {
        String query = this.query + " LIMIT " + (pageNumber * 10) + ",10";
        return query;
    }

    private void createQueryWithoutLimit(){
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < sortInfo.getTypes().size(); i++) {
            if(i != 0){
                builder.append(" UNION");
            }
            builder.append(" (SELECT * FROM Buildings " +
                    "JOIN Users ON Buildings.user_id = Users.user_id");
            builder.append(" WHERE type="+sortInfo.getTypes().get(i));
            if(sortInfo.getRunner() != null){
                builder.append(" AND username LIKE \"" + sortInfo.getRunner() + "%\"");
            }
            if(getIntent().hasExtra("username")){
                builder.append(" AND username=\"" + getIntent().getStringExtra("username") + "\"");
            }
            if(sortInfo.getMinPrice() != -1){
                builder.append(" AND price >= "+sortInfo.getMinPrice());
            }
            if(sortInfo.getMaxPrice() != -1){
                builder.append(" AND price <= "+sortInfo.getMaxPrice());
            }
            if(sortInfo.getAddress() != null){
                builder.append(" AND address LIKE \"" + sortInfo.getAddress() + "%\"");
            }

            builder.append(")");

        }

        if(sortInfo.isDesc()){
            builder.append(" ORDER BY price DESC");
        }

        if(sortInfo.isAsc()){
            builder.append(" ORDER BY price ASC");
        }

        query = builder.toString();

    }

    @Override
    public void onSortButtonClick(DialogFragment dialog, SortInfo sortInfo) {
         if(!sortInfo.getTypes().isEmpty()){
             this.sortInfo = sortInfo;
             createQueryWithoutLimit();
             listView.setAdapter(null);
             buildingList.clear();
             initiateList();
         }
        dialog.dismiss();
    }

    private void initiateSort(){
        ArrayList<Integer> types = new ArrayList<>();
        types.add(1);
        types.add(2);
        types.add(3);
        types.add(4);
        types.add(5);
        sortInfo = new SortInfoBuilder()
                .setTypes(types)
                .setEvery(true)
                .setAsc(false)
                .setDesc(false)
                .setMinPrice(-1)
                .setMaxPrice(-1)
                .createSortInfo();

    }
}
