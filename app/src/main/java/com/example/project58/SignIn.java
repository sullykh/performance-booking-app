package com.example.project58;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SignIn extends AppCompatActivity {

    Button signInButton;
    EditText  email, passwd;

    SQLiteDatabase wdb;

    TextView signup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        signInButton = findViewById(R.id.signin_button);

        email = findViewById(R.id.editTextEmail);
        passwd = findViewById(R.id.editTextPassword);
        wdb = databaseValidateUserClass.getWritable(this);
        signup = findViewById(R.id.sign_up_button);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
                finish();
            }
        });


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailS, passwdS;
                emailS = String.valueOf(email.getText());
                passwdS = String.valueOf(passwd.getText());

                //if user has not entered email
                if (TextUtils.isEmpty(emailS)) {
                    Toast.makeText(SignIn.this, "please enter email !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //if user has not entered password
                if (TextUtils.isEmpty(passwdS)) {
                    Toast.makeText(SignIn.this, "please enter password !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //select query for getting user from database
                final String selectQuery = "SELECT  Email,Password  FROM USER ";

                boolean isValid = databaseValidateUserClass.checkUser(emailS, passwdS, selectQuery);

                if (isValid) {  // if username and email are valid , go to PickMovie Activity
                    SharedPreferences sharedPreferences = getSharedPreferences("logged_in_email",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email",emailS);
                    editor.apply();
                    Intent i = new Intent(getApplicationContext(), Home.class);
                    startActivity(i);
                    finish();

                } else {        // if for a registered user ,email id is not correct
                    Toast.makeText(SignIn.this, "Please enter Correct email and password !!!", Toast.LENGTH_SHORT).show();

                    email.setText(null);
                    email.setHint("Email");
                    passwd.setText(null);
                    passwd.setHint("Password");

                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //start mainactivity on back pressed
        startActivity(new Intent(SignIn.this, MainActivity.class));
        finish();
    }
}