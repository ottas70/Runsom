package ottas70.runningapp.Network.AsyncTasks;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;

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

import ottas70.runningapp.Building;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.Utils.HttpQueryUtils;

/**
 * Created by ottovodvarka on 22.01.17.
 */

public class GetBuildingAsyncTask extends AsyncTask<Void, Void, Building> {

    private GetCallback getCallback;
    private ProgressDialog progressDialog;
    private String address;

    public GetBuildingAsyncTask(String address, GetCallback getCallback, ProgressDialog progressDialog) {
        this.getCallback = getCallback;
        this.progressDialog = progressDialog;
        this.address = address;
    }

    @Override
    protected Building doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        Building building = null;
        try {
            URL url = new URL(ServerRequest.SERVER_ADRESS + "GetBuilding.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setConnectTimeout(ServerRequest.CONNECTION_TIMEOUT);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            writeStream(out);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            building = readStream(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return building;
    }

    @Override
    protected void onPostExecute(Building building) {
        super.onPostExecute(building);
        progressDialog.dismiss();
        getCallback.done(building);
    }

    private void writeStream(OutputStream out) throws UnsupportedEncodingException {
        ContentValues values = new ContentValues();
        values.put("address", address);

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

    private Building readStream(InputStream in) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder builder = new StringBuilder();
        Building building = null;

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            if (!builder.toString().equals("[]")) {
                JSONObject jsonObject = new JSONObject(builder.toString());
                if (jsonObject.length() != 0) {
                    String ownersName = jsonObject.getString("ownersName");
                    String address = jsonObject.getString("address");
                    int type = jsonObject.getInt("type");
                    int price = jsonObject.getInt("price");

                    building = new Building(ownersName, address, type, price);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return building;
    }

}
