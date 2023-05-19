package com.example.project58;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
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



public class SignUp extends AppCompatActivity {

    Button signupButton;
    EditText name, email, passwd,rePasswd;

    TextView sigin;

    SQLiteDatabase wdb;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        signupButton = findViewById(R.id.signup_button);
        name = findViewById(R.id.editTextPersonName);
        email = findViewById(R.id.editTextEmail);
        passwd = findViewById(R.id.editTextPassword);
        rePasswd = findViewById(R.id.editTextRePass);
        sigin = findViewById(R.id.sign_in_button);


        wdb = databaseValidateUserClass.getWritable(this);

        sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, SignIn.class));
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailS, passwdS,rePasswdS,userS;
                emailS = String.valueOf(email.getText());
                passwdS = String.valueOf(passwd.getText());
                rePasswdS = String.valueOf(rePasswd.getText());
                userS = String.valueOf(name.getText());

                //if username is not entered
                if (TextUtils.isEmpty(userS)) {
                    Toast.makeText(SignUp.this, "please enter username !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //if email is not entered
                if (TextUtils.isEmpty(emailS)) {
                    Toast.makeText(SignUp.this, "please enter email !!!", Toast.LENGTH_SHORT).show();

                    return;
                }
                // if password is not entered
                if (TextUtils.isEmpty(passwdS)) {
                    Toast.makeText(SignUp.this, "please enter password !!!", Toast.LENGTH_SHORT).show();
                    return;
                }


                //if repassword is not entered
                if (TextUtils.isEmpty(rePasswdS)) {
                    Toast.makeText(SignUp.this, "please enter re password !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //if password dont match
                if(!passwdS.equals(rePasswdS)){
                    Toast.makeText(SignUp.this, "password don't match !!!", Toast.LENGTH_SHORT).show();

                    return;
                }


                final String selectQuery = "SELECT Email FROM USER ";

                boolean isValid = databaseValidateUserClass.isAlreadyRegistered(emailS, selectQuery);
                if(isValid)   // if username already exists in database
                {

                    email.setText(null);
                    email.setHint("Email");
                    passwd.setText(null);
                    passwd.setHint("Password");
                    Toast.makeText(SignUp.this, "This username is already registerd.Please Pick another or Log In!!", Toast.LENGTH_SHORT).show();

                }
                else{     // if username is not found in database , then register new user
                    ContentValues values = new ContentValues();
                    values.put("Email", emailS);
                    values.put("Password", passwdS);
                    long newRowId = wdb.insert("USER", null, values);

                    Toast.makeText(SignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("logged_in_email",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email",emailS);
                    editor.apply();
                    Intent i = new Intent(getApplicationContext(), Home.class);
                    startActivity(i);
                    finish();

                }
            }

        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //starting main activity when back button is pressed
        startActivity(new Intent(SignUp.this, MainActivity.class));
        finish();
    }
}