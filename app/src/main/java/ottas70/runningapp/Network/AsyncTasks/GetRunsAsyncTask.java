package ottas70.runningapp.Network.AsyncTasks;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;

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

import ottas70.runningapp.Duration;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Run;
import ottas70.runningapp.Runsom;
import ottas70.runningapp.Utils.HttpQueryUtils;

/**
 * Created by Ottas on 11.12.2016.
 */

public class GetRunsAsyncTask extends AsyncTask<Void,Void,ArrayList<Run>> {

    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADRESS = "http://ottas70.com/Runsom/";

    private GetCallback getCallback;
    private ProgressDialog progressDialog;

    public GetRunsAsyncTask(GetCallback getCallback, ProgressDialog progressDialog) {
        this.getCallback = getCallback;
        this.progressDialog = progressDialog;
    }

    @Override
    protected ArrayList<Run> doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        ArrayList<Run> runs = null;
        try {
            URL url = new URL(SERVER_ADRESS + "GetRuns.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            writeStream(out);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            runs =  readStream(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return runs;
    }

    @Override
    protected void onPostExecute(ArrayList<Run> runs) {
        super.onPostExecute(runs);
        progressDialog.dismiss();
        getCallback.done(runs);
    }

    private void writeStream(OutputStream out) throws UnsupportedEncodingException {
        ContentValues values = new ContentValues();
        values.put("username", Runsom.getInstance().getUser().getUsername());

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
        try {
            writer.write(HttpQueryUtils.getQuery(values));
            writer.flush();
            writer.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Run> readStream(InputStream in) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        StringBuilder builder = new StringBuilder();
        ArrayList<Run> runs = null;

        try {
            String line;
            while((line = reader.readLine()) != null){
                builder.append(line);
            }

            JSONArray jArray = new JSONArray(builder.toString());

            if (jArray.length() != 0){
                runs = new ArrayList<>();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject object = jArray.getJSONObject(i);
                    String durationString = object.getString("duration");
                    Duration duration = Duration.parseDuration(durationString);
                    double distance = object.getDouble("distance");
                    double averageSpeed = object.getDouble("averageSpeed");
                    int moneyEarned = object.getInt("moneyEarned");
                    String date = object.getString("date");
                    String name = object.getString("name");
                    String encodedPath = object.getString("encodedPath");

                    Run r = new Run(duration, distance, averageSpeed, moneyEarned, date, name, encodedPath);
                    runs.add(r);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return runs;
    }

}
