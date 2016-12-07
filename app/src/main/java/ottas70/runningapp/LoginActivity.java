package ottas70.runningapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

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

        //authenticate(new User("yyy@y.y",generateHash("yyy")));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if(validate()) {
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    //System.out.println(generateHash(password));
                    User user = new User(email, generateHash(password));
                    authenticate(user);
                }
                break;
            case R.id.link_signup:
                Intent registerintent = new Intent(LoginActivity.this, RunningActivity.class);
                startActivity(registerintent);
                break;
        }
    }

    private void authenticate(User user) {
        dialog = new ProgressDialog(getApplicationContext());
        dialog.setCancelable(false);
        dialog.setTitle("Processing...");
        dialog.setMessage("Please wait...");
        //dialog.show();
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchUserDataAsyncTask(user, new GetCallback() {
            @Override
            public void done(Object o) {
                if (o == null) {
                    showErrorMessage();
                    dialog.dismiss();
                } else {
                    logUserIn((User) o);
                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        dialogBuilder.setMessage("Incorrect user data");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void logUserIn(final User user) {
        //LifeTasks.instance.setUser(user);
        Intent intent = new Intent(LoginActivity.this, RunningActivity.class);
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
