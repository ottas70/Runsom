package ottas70.runningapp.Network.AsyncTasks;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

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
import ottas70.runningapp.Utils.HttpQueryUtils;

/**
 * Created by Ottas on 8.12.2016.
 */

public class CheckUsernameAsyncTask extends AsyncTask<Void,Void,Boolean> {

    private String username;
    private GetCallback getCallback;
    private ProgressDialog progressDialog;

    public CheckUsernameAsyncTask(String username, GetCallback getCallback, ProgressDialog progressDialog) {
        this.username = username;
        this.getCallback = getCallback;
        this.progressDialog = progressDialog;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        Boolean usernameExists = new Boolean(false);
        try {
            URL url = new URL(ServerRequest.SERVER_ADRESS + "CheckUsername.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setConnectTimeout(ServerRequest.CONNECTION_TIMEOUT);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            writeStream(out);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            usernameExists = readStream(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return usernameExists;
    }

    @Override
    protected void onPostExecute(Boolean emailExists) {
        super.onPostExecute(emailExists);
        progressDialog.dismiss();
        getCallback.done(emailExists);
    }

    private void writeStream(OutputStream out) throws UnsupportedEncodingException {
        ContentValues values = new ContentValues();
        values.put("username", username);

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

    private Boolean readStream(InputStream in) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        StringBuilder builder = new StringBuilder();
        Boolean usernameExists = new Boolean(false);

        try {
            String line;
            while((line = reader.readLine()) != null){
                builder.append(line);
            }

            JSONArray jsonArray = new JSONArray(builder.toString());

            if (jsonArray.length() > 0){
                usernameExists = new Boolean(true);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return usernameExists;
    }

}
