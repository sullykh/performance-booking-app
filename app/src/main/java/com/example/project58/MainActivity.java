package com.example.project58;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button signup,signin;

    @Override
    public void onStart() {
        super.onStart();

    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when user clicks signin button go to signin activity
                Intent i = new Intent(getApplicationContext(), SignIn.class);
                startActivity(i);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when user clicks signup button go to signup activity
                Intent i = new Intent(getApplicationContext(), SignUp.class);
                startActivity(i);
            }
        });

        //checking if user is already logged in then start Home screen directly
        SharedPreferences sharedPreferences = getSharedPreferences("logged_in_email",MODE_PRIVATE);
        String logged_in_user = sharedPreferences.getString("email","null");
        if (!logged_in_user.equals("null")){

            startActivity(new Intent(MainActivity.this,Home.class));
            finish();
        }
    }


}