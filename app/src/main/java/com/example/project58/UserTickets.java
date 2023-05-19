package com.example.project58;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UserTickets extends Home {

    ArrayList<TicketsListItem> list = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tickets);

        //getting logged in user data and seating data from database
        SharedPreferences emailPref = getSharedPreferences("logged_in_email",MODE_PRIVATE);
        String email = emailPref.getString("email","");
        SharedPreferences sharedPreferences = getSharedPreferences("seats_remaining", Context.MODE_PRIVATE);
        String[] titles = sharedPreferences.getStringSet("tickets_ids"+email, new HashSet<>()).toArray(new String[0]);
        Log.e("titles size", String.valueOf(titles.length));

        //calculating tickets which were booked by current user
        for(int i=0; i<titles.length; i++){
            TicketsListItem item = new TicketsListItem(titles[i],titles.length*50);
            list.add(item);
        }

        //showing list int he screen
        ListAdapter listAdapter = new MyTicketsListAdapter(UserTickets.this,list);
        listView = findViewById(R.id.listview);
        listView.setAdapter(listAdapter);
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //when any list item is clicked

                Intent i = new Intent(UserTickets.this,Ticket.class);

                //putting extra data for Ticket activity like selected seats,cost etc
                i.putExtra("perfTitle",titles[position]);
                ArrayList<String> selectedSeats = new ArrayList<>();
                Set<String> selectedButtons =  sharedPreferences.getStringSet(titles[position],new HashSet<>());
                for(String x : selectedButtons) {
                    selectedSeats.add(x);
                }

                //starting Ticket activity
                i.putStringArrayListExtra("selectedSeats",selectedSeats );
                startActivity(i);
//                finish();
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
        menuItem2.setVisible(false);
        return res;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        startActivity(new Intent(UserTickets.this, Home.class));
        finish();
    }
}