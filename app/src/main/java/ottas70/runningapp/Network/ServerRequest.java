package ottas70.runningapp.Network;

import android.app.ProgressDialog;
import android.content.Context;

import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.AsyncTasks.CheckEmailAsyncTask;
import ottas70.runningapp.Network.AsyncTasks.CheckUsernameAsyncTask;
import ottas70.runningapp.Network.AsyncTasks.FetchUserDataAsyncTask;
import ottas70.runningapp.Network.AsyncTasks.RegisterUserAsyncTask;
import ottas70.runningapp.Network.AsyncTasks.UploadRunAsyncTask;
import ottas70.runningapp.Run;
import ottas70.runningapp.User;

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

    public void fetchUserDataAsyncTask(User user, boolean showDialog, GetCallback getCallback) {
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

    public void uploadRun(Run run, boolean showDialog, GetCallback getCallback){
        if(showDialog){
            progressDialog.show();
        }
        new UploadRunAsyncTask(run,getCallback,progressDialog).execute();
    }

}
