package ottas70.runningapp.Network.AsyncTasks;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ottas70.runningapp.Models.Building;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.Utils.HttpQueryUtils;

/**
 * Created by ottovodvarka on 31.01.17.
 */

public class LoadBuildingsAsyncTask extends AsyncTask<Void, Void, ArrayList<Building>> {

    private GetCallback getCallback;
    private ProgressBar progressBar;
    private String query;

    public LoadBuildingsAsyncTask(String query, GetCallback getCallback, ProgressBar progressBar) {
        this.query = query;
        this.getCallback = getCallback;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        if(progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected ArrayList<Building> doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        ArrayList<Building> buildings = null;
        try {
            URL url = new URL(ServerRequest.SERVER_ADRESS + "LoadBuildings.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setConnectTimeout(ServerRequest.CONNECTION_TIMEOUT);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            writeStream(out);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            buildings = readStream(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return buildings;
    }

    @Override
    protected void onPostExecute(ArrayList<Building> buildings) {
        super.onPostExecute(buildings);
        if(progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        getCallback.done(buildings);
    }

    private void writeStream(OutputStream out) throws UnsupportedEncodingException {
        ContentValues values = new ContentValues();
        values.put("query", query);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
        try {
            writer.write(HttpQueryUtils.getQuery(values));
            writer.flush();
            writer.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Building> readStream(InputStream in) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder builder = new StringBuilder();
        ArrayList<Building> buildings = null;

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JSONArray jArray = new JSONArray(builder.toString());

            if (jArray.length() != 0) {
                buildings = new ArrayList<>();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject object = jArray.getJSONObject(i);
                    String ownersName = object.getString("ownersName");
                    String address = object.getString("address");
                    int type = object.getInt("type");
                    int price = object.getInt("price");

                    Building b = new Building(ownersName, address, type, price);
                    buildings.add(b);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return buildings;
    }

}
