package com.example.project58;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
public class PickSeat extends Home {
    Boolean[] btnSelected= new Boolean[40]; // array to store selected or not selected status of each seat
    int noOfSelectedSeats = 0;
    int totalSeats;
    int totalBookingCost;
    int seatCost = 50;


    String perfTitle;
    String perfDetails;
    ArrayList<String> selectedSeats = new ArrayList<>();

//    SQLiteDatabase wdb;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_seat);

        final ArrayList<Integer> selectedButtons=new ArrayList<Integer>();

        //getting intent data from previous activity
        Intent in = getIntent();
        totalSeats = in.getIntExtra("totalSeats",39);
        perfTitle = in.getStringExtra("perfTitle");
        perfDetails= in.getStringExtra("perfDetails");

        //getting seating information from database
        SharedPreferences sharedPreferences = getSharedPreferences("seats_remaining", Context.MODE_PRIVATE);

        Set<String> bookedSeats = sharedPreferences.getStringSet("selectedSeatsSet"+perfTitle,new HashSet<>());
        // if there are some seats already booked
        if (bookedSeats != null) {
            for (String i : bookedSeats) {
                Log.e("booked Seat",i);
                if(i=="") break;

                String buttonID = "btn"+i;
                Log.e("booked seat",i);

                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                Button btn = (Button) (findViewById(resID));
                btn.setBackground(getResources().getDrawable(R.drawable.booked));
                btn.setEnabled(false);
            }

        }
        // set btnselected flag as false for all buttons
        for(int i=0;i<40;i++){
            btnSelected[i]=false;
        }

        Button[] seatButtons = new Button[40];
        //
        for (int i = 1; i < seatButtons.length; i++) {

        int m = i;
        String buttonID = "btn" + m;

        int resID = getResources().getIdentifier(buttonID, "id", getPackageName());

        seatButtons[i] = ((Button) findViewById(resID));
        final int finalId = resID;
        final int buttonNum = i;

        // set listeners to all buttons
        if (seatButtons[i].isEnabled() && noOfSelectedSeats<=totalSeats) {

            seatButtons[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Button btn = (Button) findViewById(finalId);
                    if (!btnSelected[buttonNum]) {
                        //when seat is clicked
                        btn.setBackground(getResources().getDrawable(R.drawable.selected));
                        btnSelected[buttonNum] = true;
                        noOfSelectedSeats=noOfSelectedSeats+1;
                       selectedButtons.add(buttonNum);

                    } else {
                        //when seat is not selected
                        btn.setBackground(getResources().getDrawable(R.drawable.available));
                        btnSelected[buttonNum] = false;
                        noOfSelectedSeats=noOfSelectedSeats-1;

                        try {

                            selectedButtons.remove(selectedButtons.indexOf(buttonNum));
                        }
                        catch(Exception ex)
                        {
                            System.out.println(ex);
                        }
                    }

                }


            });

        }

    }

        //when user presses the confirm seats button
        Button btnProceed = (Button) findViewById(R.id.btnConfirmSeats);
        btnProceed.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
//

                    for (int i = 0; i < selectedButtons.size(); i++) {
                       System.out.println("Seat no "+selectedButtons.get(i));
                    }
                    // calculating total cost and adding selected seats to selectedSeats
                    for(int i : selectedButtons){
                        selectedSeats.add(String.valueOf(i));
                        totalBookingCost += seatCost;
                        Log.e("selected seat no", String.valueOf(i));
                    }

                    //if no seat is selected then don't allow user to go to ticket confirm screen
                    if(totalBookingCost == 0){
                        Toast.makeText(PickSeat.this, "Please select some seats first", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent myintent = new Intent(PickSeat.this, BookSeats.class);

                    myintent.putStringArrayListExtra("selectedSeats",  selectedSeats);
                    myintent.putExtra("totalBookingCost",totalBookingCost);
                    myintent.putExtra("perfTitle",perfTitle);
                    myintent.putExtra("perfDetails",perfDetails);
                    startActivity(myintent);
                    finish();
                Log.e("totalcost", String.valueOf(totalBookingCost));

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
