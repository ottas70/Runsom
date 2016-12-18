package ottas70.runningapp;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.List;

/**
 * Created by ottovodvarka on 18.12.16.
 */

public class MapReadyCallback implements OnMapReadyCallback {

    private Run run;
    private MapView mapView;

    public MapReadyCallback(MapView mapView, Run run) {
        this.mapView = mapView;
        this.run = run;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        List<LatLng> path = PolyUtil.decode(run.getEncodedPath());
        Polyline line = googleMap.addPolyline(new PolylineOptions()
                .width(5)
                .color(Color.RED));
        line.setPoints(path);
        fixZoom(path, googleMap);
        mapView.onResume();
    }

    private void fixZoom(List<LatLng> latLngList, GoogleMap map) {
        LatLngBounds.Builder bc = new LatLngBounds.Builder();
        for (LatLng item : latLngList) {
            bc.include(item);
        }
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 30));
    }
}
