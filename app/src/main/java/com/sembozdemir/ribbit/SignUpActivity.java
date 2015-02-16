package com.sembozdemir.ribbit;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUpActivity extends ActionBarActivity {

    protected EditText mUsername;
    protected EditText mPassword;
    protected EditText mEmail;
    protected Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init(); // initialize our View components

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, password, email;
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();
                email = mEmail.getText().toString();

                // girilen bilgilerde boşluk varsa sil
                username = username.trim();
                password = password.trim();
                email = email.trim();

                if(username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    // Kullanıcı username, password veya email sahalarından birini boş bıraktıysa dialogla onu uyar
                    AlertDialog.Builder errDialogbuilder = new AlertDialog.Builder(SignUpActivity.this);
                    errDialogbuilder.setMessage(R.string.sign_up_err_msg);
                    errDialogbuilder.setTitle(R.string.sign_up_err_title);
                    errDialogbuilder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog errDialog = errDialogbuilder.create();
                    errDialog.show();
                } else {
                    // Create the new user
                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(username);
                    newUser.setPassword(password);
                    newUser.setEmail(email);
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // Success !!
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {
                                // There is a ParseException !!
                                // Dialogla exceptionın ne olduğunu gosterelim.
                                AlertDialog.Builder errDialogbuilder = new AlertDialog.Builder(SignUpActivity.this);
                                errDialogbuilder.setMessage(e.getMessage()); // Exception mesajı dialog'da gosterilecek.
                                errDialogbuilder.setTitle(R.string.sign_up_err_title);
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
        mEmail = (EditText) findViewById(R.id.emailField);
        mSignUpButton = (Button) findViewById(R.id.buttonSignUp);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
