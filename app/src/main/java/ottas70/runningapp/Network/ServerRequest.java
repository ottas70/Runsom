package ottas70.runningapp.Network;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import ottas70.runningapp.Building;
import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.AsyncTasks.AddMoneyToUserAsyncTask;
import ottas70.runningapp.Network.AsyncTasks.BuyBuildingAsyncTask;
import ottas70.runningapp.Network.AsyncTasks.CheckEmailAsyncTask;
import ottas70.runningapp.Network.AsyncTasks.CheckUsernameAsyncTask;
import ottas70.runningapp.Network.AsyncTasks.FetchUserDataAsyncTask;
import ottas70.runningapp.Network.AsyncTasks.GetBuildingAsyncTask;
import ottas70.runningapp.Network.AsyncTasks.GetNominatimAddressAsyncTask;
import ottas70.runningapp.Network.AsyncTasks.GetRunsAsyncTask;
import ottas70.runningapp.Network.AsyncTasks.RegisterUserAsyncTask;
import ottas70.runningapp.Network.AsyncTasks.UploadRunAsyncTask;
import ottas70.runningapp.Run;
import ottas70.runningapp.User;

/**
 * Created by Ottas on 7.12.2016.
 */

public class ServerRequest {

    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADRESS = "http://ottas70.com/Runsom/";

    private ProgressDialog progressDialog;

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

    public void getRuns(boolean showDialog, GetCallback getCallback){
        if(showDialog){
            progressDialog.show();
        }
        new GetRunsAsyncTask(getCallback,progressDialog).execute();
    }

    public void getNominatimAddress(LatLng latLng, boolean showDialog, GetCallback getCallback) {
        if (showDialog) {
            progressDialog.show();
        }
        new GetNominatimAddressAsyncTask(latLng, getCallback, progressDialog).execute();
    }

    public void getBuildingAsyncTask(String address, boolean showDialog, GetCallback getCallback) {
        if (showDialog) {
            progressDialog.show();
        }
        new GetBuildingAsyncTask(address, getCallback, progressDialog).execute();
    }

    public void addMoneyToUserAsyncTask(int money, boolean showDialog, GetCallback getCallback) {
        if (showDialog) {
            progressDialog.show();
        }
        new AddMoneyToUserAsyncTask(money, getCallback, progressDialog).execute();
    }

    public void buyBuildingAsyncTask(Building building, boolean showDialog, GetCallback getCallback) {
        if (showDialog) {
            progressDialog.show();
        }
        new BuyBuildingAsyncTask(building, getCallback, progressDialog).execute();
    }

}
