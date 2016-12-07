package ottas70.runningapp;

import android.app.ProgressDialog;
import android.content.Context;

import ottas70.runningapp.AsyncTasks.FetchUserDataAsyncTask;

/**
 * Created by Ottas on 7.12.2016.
 */

public class ServerRequest {

    private ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADRESS = "http://ottas70.com/";

    public ServerRequest(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    public void fetchUserDataAsyncTask(User user, GetCallback getCallback) {
        //progressDialog.show();
        new FetchUserDataAsyncTask(user, getCallback,progressDialog).execute();
    }

}
