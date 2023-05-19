package com.example.project58;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PerformanceDetails extends Home {

    TextView title;
    TextView perfDetails;
    TextView ticketsRemaining;

    Button book_now;

    int totalSeats;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_details);

        title = findViewById(R.id.perf_title);
        perfDetails = findViewById(R.id.perf_details);
        ticketsRemaining = findViewById(R.id.tickets_remaining);
        book_now = findViewById(R.id.book_now);

        //getting intent data from previous activity
        Intent i = getIntent();
        title.setText(i.getStringExtra("perfTitle"));
        perfDetails.setText(i.getStringExtra("perfDetails"));
        ticketsRemaining.setText(i.getStringExtra("ticketsRemainingDetails"));
        totalSeats = i.getIntExtra("totalSeats",39);

        if(totalSeats==0){
            book_now.setEnabled(false);
            book_now.setVisibility(View.GONE);
        }

        //when book_now is clicked
        book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PerformanceDetails.this,PickSeat.class);
                i.putExtra("perfTitle",title.getText().toString());
                i.putExtra("perfDetails",perfDetails.getText().toString());
                i.putExtra("totalSeats",totalSeats);
                startActivity(i);
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