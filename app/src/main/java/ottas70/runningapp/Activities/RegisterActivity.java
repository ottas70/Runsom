package ottas70.runningapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ottas70.runningapp.Interfaces.GetCallback;
import ottas70.runningapp.R;
import ottas70.runningapp.Network.ServerRequest;
import ottas70.runningapp.Models.User;

public class RegisterActivity extends Activity implements View.OnClickListener{

    EditText etUsername,etEmail,etPassword;
    Button bRegister;
    TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = (EditText)findViewById(R.id.input_name);
        etEmail = (EditText)findViewById(R.id.input_email);
        etPassword = (EditText)findViewById(R.id.input_password);
        bRegister = (Button)findViewById(R.id.btn_signup);
        loginLink = (TextView)findViewById(R.id.link_login);

        bRegister.setOnClickListener(this);
        loginLink.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_signup:
                if(validate()) {
                    final String username = etUsername.getText().toString();
                    final String email = etEmail.getText().toString();
                    final String password = etPassword.getText().toString();


                    final ServerRequest request = new ServerRequest(this);
                    request.checkEmail(email, true, new GetCallback() {
                        @Override
                        public void done(Object o) {
                            Boolean emailExists = (Boolean) o;
                            if (emailExists.booleanValue()) {
                                showErrorMessage("Email is already used");
                            } else {
                                request.checkUsername(username, true, new GetCallback() {
                                    @Override
                                    public void done(Object o) {
                                        Boolean usernameExists = (Boolean) o;
                                        if (usernameExists.booleanValue()) {
                                            showErrorMessage("Username is already used");
                                        } else {
                                            User user = new User(generateHash(password), username, email);
                                            registerUser(user);
                                        }
                                    }
                                });

                            }
                        }
                    });
                }
                break;
            case R.id.link_login:
                Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
    }

    private void registerUser(User user){
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.registerUserAsyncTask(user,false, new GetCallback() {
            @Override
            public void done(Object o) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String name = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            etUsername.setError("at least 3 characters");
            valid = false;
        } else {
            etUsername.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("enter a valid email address");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            etPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    private void showErrorMessage(String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
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

}
