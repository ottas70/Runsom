package ottas70.runningapp.Network.AsyncTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

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

import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.Runsom;

/**
 * Created by ottovodvarka on 21.01.17.
 */

public class GetNominatimAddressAsyncTask extends AsyncTask<Void, Void, String> {

    private GetCallback getCallback;
    private ProgressDialog progressDialog;
    private LatLng latLng;

    public GetNominatimAddressAsyncTask(LatLng latLng, GetCallback getCallback, ProgressDialog progressDialog) {
        this.getCallback = getCallback;
        this.progressDialog = progressDialog;
        this.latLng = latLng;
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        String address = "";
        try {
            URL url = new URL("http://nominatim.openstreetmap.org/reverse?format=json" +
                    "&email=" + Runsom.getInstance().getUser().getEmail() +
                    "&lat=" + latLng.latitude +
                    "&lon=" + latLng.longitude);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(ServerRequest.CONNECTION_TIMEOUT);

            Log.e("Response Code", String.valueOf(urlConnection.getResponseCode()));

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            address = readStream(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return address;
    }

    @Override
    protected void onPostExecute(String address) {
        super.onPostExecute(address);
        progressDialog.dismiss();
        getCallback.done(address);
    }

    private String readStream(InputStream in) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder builder = new StringBuilder();
        String address = "";

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JSONObject jsonObject = new JSONObject(builder.toString());
            JSONObject addressObject = jsonObject.getJSONObject("address");

            if (addressObject.has("road")) {
                address = addressObject.getString("road");
            }
            if (addressObject.has("house_number") && !address.equals("")) {
                address += (" " + addressObject.getString("house_number"));
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return address;
    }

}
