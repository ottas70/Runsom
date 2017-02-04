package ottas70.runningapp.Network.AsyncTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.Runsom;

/**
 * Created by ottovodvarka on 03.02.17.
 */

public class GetNominatimCoordinatesAsyncTask extends AsyncTask<Void, Void, LatLng> {

    private GetCallback getCallback;
    private ProgressDialog progressDialog;
    private String address;

    public GetNominatimCoordinatesAsyncTask(String address, GetCallback getCallback, ProgressDialog progressDialog) {
        this.getCallback = getCallback;
        this.progressDialog = progressDialog;
        this.address = address;
    }

    @Override
    protected LatLng doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        LatLng latLng = null;
        try {
            String s = address + "," + "Praha";
            URL url = new URL("http://nominatim.openstreetmap.org/search?format=json" +
                    "&email=" + Runsom.getInstance().getUser().getEmail() +
                    "&q=" + URLEncoder.encode(s,"UTF-8"));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(ServerRequest.CONNECTION_TIMEOUT);

            Log.e("Response Code", String.valueOf(urlConnection.getResponseCode()));

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            latLng = readStream(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return latLng;
    }

    @Override
    protected void onPostExecute(LatLng latLng) {
        super.onPostExecute(latLng);
        progressDialog.dismiss();
        getCallback.done(latLng);
    }

    private LatLng readStream(InputStream in) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder builder = new StringBuilder();
        LatLng latLng = null;

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JSONArray array = new JSONArray(builder.toString());
            JSONObject jsonObject = array.getJSONObject(0);
            double lat = jsonObject.getDouble("lat");
            double lon = jsonObject.getDouble("lon");

            latLng = new LatLng(lat,lon);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return latLng;
    }
}
