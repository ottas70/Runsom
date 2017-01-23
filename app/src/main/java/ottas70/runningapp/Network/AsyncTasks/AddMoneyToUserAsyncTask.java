package ottas70.runningapp.Network.AsyncTasks;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Runsom;
import ottas70.runningapp.Utils.HttpQueryUtils;

/**
 * Created by ottovodvarka on 23.01.17.
 */

public class AddMoneyToUserAsyncTask extends AsyncTask<Void, Void, Void> {

    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADRESS = "http://ottas70.com/Runsom/";

    private int money;
    private GetCallback getCallback;
    private ProgressDialog progressDialog;

    public AddMoneyToUserAsyncTask(int money, GetCallback getCallback, ProgressDialog progressDialog) {
        this.money = money;
        this.getCallback = getCallback;
        this.progressDialog = progressDialog;
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(SERVER_ADRESS + "AddMoneyToUser.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            writeStream(out);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
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
        values.put("money", money);

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
}
