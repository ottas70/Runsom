package ottas70.runningapp.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;
import ottas70.runningapp.Runsom;
import ottas70.runningapp.User;

public class SettingsFragment extends PreferenceFragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

    }

    @Override
    public void onStop() {
        User u = Runsom.getInstance().getUser();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        if(sharedPref.getString("gender","").equals("1")){
            u.setGender("Male");
        }else{
            u.setGender("Female");
        }
        u.setHeight(Integer.valueOf(sharedPref.getString("height","")));
        u.setWeight(Integer.valueOf(sharedPref.getString("weight","")));
        ServerRequest request = new ServerRequest(getActivity().getApplicationContext());
        request.updatePersonalInfoAsyncTask(false, new GetCallback() {
            @Override
            public void done(Object o) {

            }
        });
        super.onStop();
    }
}
