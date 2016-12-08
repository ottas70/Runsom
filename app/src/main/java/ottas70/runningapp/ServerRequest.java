package ottas70.runningapp;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import ottas70.runningapp.AsyncTasks.CheckEmailAsyncTask;
import ottas70.runningapp.AsyncTasks.CheckUsernameAsyncTask;
import ottas70.runningapp.AsyncTasks.FetchUserDataAsyncTask;
import ottas70.runningapp.AsyncTasks.RegisterUserAsyncTask;

/**
 * Created by Ottas on 7.12.2016.
 */

public class ServerRequest {

    private ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADRESS = "http://ottas70.com/Runsom/";

    public ServerRequest(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    public static String getQuery(ContentValues values) throws UnsupportedEncodingException {
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

    public void fetchUserDataAsyncTask(User user,boolean showDialog, GetCallback getCallback) {
        if (showDialog) progressDialog.show();
        new FetchUserDataAsyncTask(user, getCallback, progressDialog).execute();
    }

    public void registerUserAsyncTask(User user, boolean showDialog, GetCallback getCallback) {
        if (showDialog) progressDialog.show();
        new RegisterUserAsyncTask(user, getCallback, progressDialog).execute();
    }

    public void checkEmail(String email,boolean showDialog,GetCallback callback){
        if(showDialog){
            progressDialog.show();
        }
        new CheckEmailAsyncTask(email,callback,progressDialog).execute();
    }

    public void checkUsername(String username, boolean showDialog, GetCallback callback){
        if(showDialog){
            progressDialog.show();
        }
        new CheckUsernameAsyncTask(username,callback,progressDialog).execute();
    }

}
