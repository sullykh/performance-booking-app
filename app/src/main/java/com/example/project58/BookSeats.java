package com.example.project58;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BookSeats extends Home {



    TextView receiptContent;
    Button confirmBooking;


    @SuppressLint({"SuspiciousIndentation", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        receiptContent = findViewById(R.id.receiptContent);
        confirmBooking = findViewById(R.id.btnConfirmSeats);

        //getting intent data from previous activity
        Intent i = getIntent();
        String perfTitle = i.getStringExtra("perfTitle");
        int totalCost = i.getIntExtra("totalBookingCost",39);
        String[] selectedSeats = i.getStringArrayListExtra("selectedSeats").toArray(new String[0]);
        String selectedSeatsText = "";

        //calculating selected seats
        for(String s : selectedSeats){
            selectedSeatsText += "[S"+s+"] ";
        }

        //calculating text that will be shown in the ticket
        String receiptText=
                "    <b>Performance :</b>"+ " "+perfTitle+"<br /><br />"+
                        "    <b>Selected Seats :</b>"+" "+selectedSeatsText+"<br /><br />" +
                        "    <b>Total Cost :</b>"+" "+"£"+totalCost+"<br /><br />"+
                        "    <b>Ticket Code :</b>"+" "+(perfTitle+" : "+selectedSeatsText).hashCode()+"<br /><br />";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            receiptContent.setText(Html.fromHtml(receiptText , Html.FROM_HTML_MODE_COMPACT));
        }else
            receiptContent.setText(Html.fromHtml(receiptText));

        String finalSelectedSeatsText = selectedSeatsText;
        confirmBooking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //when confirm booking button is clicked

                //getting logged in user data and seating data from database
                SharedPreferences emailPref = getSharedPreferences("logged_in_email",MODE_PRIVATE);
                String email = emailPref.getString("email","");
                SharedPreferences sharedPreferences = getSharedPreferences("seats_remaining", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //Saving booking data to database
                Set<String> oldSet = sharedPreferences.getStringSet("selectedSeatsSet"+perfTitle,new HashSet<>());
                Set<String> newSet = new HashSet<>(Arrays.asList(selectedSeats));
                editor.putStringSet(perfTitle + " : "+ finalSelectedSeatsText,newSet);
                Set<String> tickets_ids = sharedPreferences.getStringSet("tickets_ids"+email,new HashSet<>());
                String title = perfTitle + " : "+finalSelectedSeatsText;
                tickets_ids.add(title+" ( "+title.hashCode()+" )");
                Log.e("adding tid",perfTitle+" : "+finalSelectedSeatsText);
                editor.putStringSet("tickets_ids"+email,tickets_ids);

                newSet.addAll(oldSet);
                editor.putStringSet("selectedSeatsSet"+perfTitle,newSet);

                int totalSeats = sharedPreferences.getInt(perfTitle,39);
                editor.putInt(perfTitle, Math.max(totalSeats - selectedSeats.length, 0));
                editor.apply();

                //starting Home activity
                Intent myintent=new Intent(BookSeats.this, Home.class);
                startActivity(myintent);
                finish();

            }

        });
        invalidateOptionsMenu();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating options menu in this activity
        boolean res = super.onCreateOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.nav_performances);
        menuItem.setVisible(true);
        MenuItem menuItem2 = menu.findItem(R.id.nav_myTickets);
        menuItem2.setVisible(true);
        return res;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

}
