package com.example.project58;

import androidx.annotation.NonNull;

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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Ticket extends Home {

    TextView title,content;
    Button cancelTicket;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        title = findViewById(R.id.receiptTitle);
        content = findViewById(R.id.receiptContent);
        cancelTicket = findViewById(R.id.btnConfirmSeats);


    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        cancelTicket.setText("Cancel Ticket");

        //getting intent from this activity was started
        Intent i = getIntent();

        String perfTitle = i.getStringExtra("perfTitle");
        String[] selectedSeats = i.getStringArrayListExtra("selectedSeats").toArray(new String[0]);
        String title_text,selected_seats_text;
        int in;
        int colon_in = 0;
        int ticket_code_in = 0;

        //calculating performance title , selected seats and unique ticked code
        for( in = 0 ; in<perfTitle.length(); in++){
            if(perfTitle.charAt(in)==':'){
                colon_in = in;
            }
            if(perfTitle.charAt(in) == '('){
                ticket_code_in = in;
                break;
            }
        }

        //displaying data on the screen
        title_text = perfTitle.substring(0,colon_in-1);
        selected_seats_text = perfTitle.substring(colon_in+2,ticket_code_in-1);
        String ticket_code = perfTitle.substring(ticket_code_in,perfTitle.length());
        int totalCost = (selected_seats_text.length()/6) * 50;

        title.setText("Ticket");
        String receiptText=
                "    <b>Performance :</b>"+ " "+perfTitle+"<br /><br />"+
                        "    <b>Selected Seats :</b>"+" "+selected_seats_text+"<br /><br />" +
                        "    <b>Total Cost :</b>"+" "+"£"+totalCost+"<br /><br />"+
                        "    <b>Ticket Code :</b>"+" "+ticket_code+"<br /><br />";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            content.setText(Html.fromHtml(receiptText , Html.FROM_HTML_MODE_COMPACT));
        }else
            content.setText(Html.fromHtml(receiptText));


        cancelTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if cancel ticket button is clicked

                //getting logged in user data and seating data from database
                SharedPreferences emailPref = getSharedPreferences("logged_in_email",MODE_PRIVATE);
                String email = emailPref.getString("email","");
                SharedPreferences sharedPreferences = getSharedPreferences("seats_remaining", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //deleting the cancelled ticket data from database
                Set<String> oldSet = sharedPreferences.getStringSet("selectedSeatsSet"+title_text,new HashSet<>());
                Set<String> newSet = new HashSet<>(Arrays.asList(selectedSeats));
                oldSet.removeAll(newSet);
                editor.putStringSet("selectedSeatsSet"+title_text,oldSet);

                Set<String> tickets_ids = sharedPreferences.getStringSet("tickets_ids"+email,new HashSet<>());
                Log.e("removing tid",title_text+" : "+selected_seats_text);
                tickets_ids.remove(perfTitle);
                editor.putStringSet("tickets_ids"+email,tickets_ids);

                //updatin database
                Log.e("title",title_text);
                int totalSeats = sharedPreferences.getInt(title_text,39);
                editor.putInt(title_text, Math.min(totalSeats + (selected_seats_text.length()/6), 39));
                editor.apply();

                //starting UserTickets activity
                startActivity(new Intent(Ticket.this,UserTickets.class));
                finish();
            }
        });
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating options menu in this screen
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