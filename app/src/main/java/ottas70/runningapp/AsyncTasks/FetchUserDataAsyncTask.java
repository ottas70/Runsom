package ottas70.runningapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

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
import java.net.URLEncoder;
import java.util.Map;


import ottas70.runningapp.GetCallback;
import ottas70.runningapp.User;

/**
 * Created by Ottas on 7.12.2016.
 */

public class FetchUserDataAsyncTask extends AsyncTask<Void,Void,User> {

    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADRESS = "http://ottas70.com/Runsom/";
    User user;
    GetCallback getCallback;
    ProgressDialog progressDialog;

    public FetchUserDataAsyncTask(User user, GetCallback getCallback, ProgressDialog progressDialog) {
        this.user = user;
        this.getCallback = getCallback;
        this.progressDialog = progressDialog;
    }

    @Override
    protected User doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        User returnedUser = null;
        try {
            URL url = new URL(SERVER_ADRESS + "FetchUserData.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            writeStream(out);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            returnedUser = readStream(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return returnedUser;
    }

    @Override
    protected void onPostExecute(User returnedUser) {
        super.onPostExecute(returnedUser);
        progressDialog.dismiss();
        getCallback.done(returnedUser);
    }

    private void writeStream(OutputStream out) throws UnsupportedEncodingException {
        ContentValues values = new ContentValues();
        values.put("email",user.getEmail());
        values.put("password",user.getPassword());

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
        try {
            writer.write(getQuery(values));
            writer.flush();
            writer.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private User readStream(InputStream in) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        StringBuilder builder = new StringBuilder();
        User returnedUser = null;

        try {
            String line;
            while((line = reader.readLine()) != null){
               builder.append(line);
            }

            JSONObject jsonObject = new JSONObject(builder.toString());

            if (jsonObject.length() != 0){
                int id = jsonObject.getInt("id");
                String username = jsonObject.getString("username");

                returnedUser = new User(id,username, user.getEmail(),user.getPassword());
            }

            return returnedUser;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedUser;
    }

    private String getQuery(ContentValues values) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String,Object> entry : values.valueSet())
        {
            String key = entry.getKey();
            String value = entry.getValue().toString();

            if (first) {
                first = false;
            }else {
                result.append("&");
            }
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value, "UTF-8"));
        }

        return result.toString();
    }

}
