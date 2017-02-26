package ottas70.runningapp.Network.AsyncTasks;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;

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

import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.Models.Run;
import ottas70.runningapp.Runsom;
import ottas70.runningapp.Utils.HttpQueryUtils;

/**
 * Created by Ottas on 10.12.2016.
 */

public class UploadRunAsyncTask extends AsyncTask<Void,Void,Void> {

    private Run run;
    private GetCallback getCallback;
    private ProgressDialog progressDialog;

    public UploadRunAsyncTask(Run run, GetCallback getCallback, ProgressDialog progressDialog) {
        this.run = run;
        this.getCallback = getCallback;
        this.progressDialog = progressDialog;
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(ServerRequest.SERVER_ADRESS + "UploadRun.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setConnectTimeout(ServerRequest.CONNECTION_TIMEOUT);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            writeStream(out);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            readStream(in);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        getCallback.done(null);
    }

    private void writeStream(OutputStream out) throws UnsupportedEncodingException {
        ContentValues values = new ContentValues();
        values.put("username", Runsom.getInstance().getUser().getUsername());
        values.put("date",run.getDate());
        values.put("distance",run.getDistance());
        values.put("averageSpeed",run.getAverageSpeed());
        values.put("moneyEarned",run.getMoneyEarned());
        values.put("duration",run.getDuration().toString());
        values.put("name",run.getName());
        values.put("encodedPath", run.getEncodedPath());

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

    private void readStream(InputStream in) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        StringBuilder builder = new StringBuilder();

        try {
            String line;
            while((line = reader.readLine()) != null){
                builder.append(line);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
