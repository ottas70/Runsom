package ottas70.runningapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.R;
import ottas70.runningapp.Runsom;
import ottas70.runningapp.User;

public class LoginActivity extends Activity implements View.OnClickListener {

    Button bLogin;
    EditText etEmail, etPassword;
    TextView registerLink;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bLogin = (Button) findViewById(R.id.btn_login);
        etEmail = (EditText) findViewById(R.id.input_email);
        etPassword = (EditText) findViewById(R.id.input_password);
        registerLink = (TextView) findViewById(R.id.link_signup);

        bLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);

        authenticate(new User("yyy@y.y", generateHash("yyyy")));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (validate()) {
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    User user = new User(email, generateHash(password));
                    authenticate(user);
                }
                break;
            case R.id.link_signup:
                Intent registerintent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerintent);
                break;
        }
    }

    private void authenticate(User user) {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchUserDataAsyncTask(user,true, new GetCallback() {
            @Override
            public void done(Object o) {
                if (o == null) {
                    showErrorMessage();
                } else {
                    User u = (User) o;
                    logUserIn(u);
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("gender",String.valueOf(u.getGenderInt()));
                    editor.putString("weight",String.valueOf(u.getWeight()));
                    editor.putString("height",String.valueOf(u.getHeight()));
                    editor.commit();
                    System.out.println();

                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        dialogBuilder.setMessage("Incorrect user data")
                .setPositiveButton("Ok", null)
                .show();
    }

    private void logUserIn(final User user) {
        Runsom.getInstance().setUser(user);
        Intent intent = new Intent(LoginActivity.this, RunOverviewActivity.class);
        startActivity(intent);

    }

    public boolean validate() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("enter a valid email address");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        return valid;
    }

    public String generateHash(String toHash) {
        MessageDigest md = null;
        byte[] hash = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            hash = md.digest(toHash.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return convertToHex(hash);
    }

    private String convertToHex(byte[] raw) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < raw.length; i++) {
            sb.append(Integer.toString((raw[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
