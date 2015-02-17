package com.sembozdemir.ribbit;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginActivity extends ActionBarActivity {

    protected EditText mUsername;
    protected EditText mPassword;
    protected Button mLoginButton;

    protected TextView mSignUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); // Progressbar için yazıldı
        setContentView(R.layout.activity_login);

        init(); // initialize our View components

        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intentSignUp);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, password, email;
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();

                // girilen bilgilerde boşluk varsa sil
                username = username.trim();
                password = password.trim();

                if(username.isEmpty() || password.isEmpty()) {
                    // Kullanıcı username, password veya email sahalarından birini boş bıraktıysa dialogla onu uyar
                    AlertDialog.Builder errDialogbuilder = new AlertDialog.Builder(LoginActivity.this);
                    errDialogbuilder.setMessage(R.string.login_err_msg);
                    errDialogbuilder.setTitle(R.string.login_err_title);
                    errDialogbuilder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog errDialog = errDialogbuilder.create();
                    errDialog.show();
                } else {
                    // Login
                    setSupportProgressBarIndeterminateVisibility(true);
                    //setProgressBarIndeterminateVisibility(true); // Progressbar göster
                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            setSupportProgressBarIndeterminateVisibility(false);
                            //setProgressBarIndeterminateVisibility(false); // Progressbar gizle
                            if (e == null) {
                                // Success !!
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                // There is a ParseException !!
                                // Dialogla exceptionın ne olduğunu gosterelim.
                                AlertDialog.Builder errDialogbuilder = new AlertDialog.Builder(LoginActivity.this);
                                errDialogbuilder.setMessage(e.getMessage()); // Exception mesajı dialog'da gosterilecek.
                                errDialogbuilder.setTitle(R.string.login_err_title);
                                errDialogbuilder.setPositiveButton(android.R.string.ok, null);
                                AlertDialog errDialog = errDialogbuilder.create();
                                errDialog.show();
                            }
                        }
                    });
                }
            }
        });

    }

    private void init() {
        mUsername = (EditText) findViewById(R.id.usernameField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mLoginButton = (Button) findViewById(R.id.buttonSignIn);
        mSignUpTextView = (TextView) findViewById(R.id.textSignUp);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
