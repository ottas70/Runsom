package ottas70.runningapp.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;
import ottas70.runningapp.Runsom;
import ottas70.runningapp.Models.User;

public class SettingsFragment extends PreferenceFragment {

    private String gender;
    private int weight;
    private int height;

    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        user = Runsom.getInstance().getUser();
        gender = user.getGender();
        weight = user.getWeight();
        height = user.getHeight();
    }

    @Override
    public void onStop() {
        ServerRequest request = new ServerRequest(getActivity().getApplicationContext());
        request.updatePersonalInfoAsyncTask(false, new GetCallback() {
            @Override
            public void done(Object o) {

            }
        });
        super.onStop();
    }

    @Override
    public void onPause() {
        updatePersonalInfo();
        super.onPause();
    }

    private void updatePersonalInfo(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        if(sharedPref.getString("gender","").equals("1")){
            user.setGender("Male");
        }else{
            user.setGender("Female");
        }
        user.setHeight(Integer.valueOf(sharedPref.getString("height","")));
        user.setWeight(Integer.valueOf(sharedPref.getString("weight","")));
    }


}
