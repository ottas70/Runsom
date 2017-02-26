package ottas70.runningapp.Listeners;

import android.content.Context;
import android.widget.AbsListView;

import java.util.Collection;
import java.util.List;

import ottas70.runningapp.Adapters.BuildingAdapter;
import ottas70.runningapp.Models.Building;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;

/**
 * Created by ottovodvarka on 31.01.17.
 */

public class EndlessScrollListener implements AbsListView.OnScrollListener {

    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;

    private Context context;
    private List<Building> buildingList;
    private BuildingAdapter adapter;
    private String query;

    public EndlessScrollListener(Context context, List<Building> buildingList, BuildingAdapter adapter, String query) {
        this.context = context;
        this.buildingList = buildingList;
        this.adapter = adapter;
        this.query = query;
    }

    public EndlessScrollListener(Context context, int visibleThreshold, List<Building> buildingList, BuildingAdapter adapter, String query) {
        this.context = context;
        this.visibleThreshold = visibleThreshold;
        this.buildingList = buildingList;
        this.adapter = adapter;
        this.query = query;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            ServerRequest request = new ServerRequest(context);
            request.loadBuildingsAsyncTask(makeQuery(currentPage + 1), null, new GetCallback() {
                @Override
                public void done(Object o) {
                    if (o == null)
                        return;
                    buildingList.addAll((Collection<? extends Building>) o);
                    adapter.notifyDataSetChanged();
                }
            });
            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    private String makeQuery(int pageNumber) {
        String s =  query + " LIMIT " + (pageNumber * 10) + ",10";
        return s;
    }

}
