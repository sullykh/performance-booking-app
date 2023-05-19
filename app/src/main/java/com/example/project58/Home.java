package com.example.project58;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.ListView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Home extends AppCompatActivity implements SearchView.OnQueryTextListener  {

    ArrayList<performancesListItem> list = new ArrayList<>();
    List<Integer> images = Arrays.asList(
            R.drawable.perf1,
            R.drawable.perf2,
            R.drawable.perf3,
            R.drawable.perf4,
            R.drawable.perf5,
            R.drawable.perf6
            );

    List<String> titles = Arrays.asList(
            "A Midsummer Night's Dream by William Shakespeare",
            "Antigone by William Sophocles",
            "Hamlet by William Shakespeare",
            "The Merchant of Venice by William Shakespeare",
            "The Tempest by William Shakespeare",
            "Oedipus the King by Sophocles"
            );

    List<String> description = Arrays.asList(
            "19:00 Saturday 3 June 2023,\nDean's Park, York\n\n\t\t\u2022 Has wheelchair access.\n\n\t\t\u2022 Performance includes flashing lights.",
            "21:00 Thursday 20 September 2023,\nCrypt, York Minister, York\n\n\t\t\u2022 Must be able to climb 20 steps.",
            "18:00 19 May 2023,\nClifford's Tower, York\n\n\t\t\u2022 Must be able to climb 20 steps.\n\n\t\t\u2022 Performance includes flashing lights.",
            "20:00 Sunday April 23 2023,\nMerchant Adventure's Hall, York\n\n\t\t\u2022 Has wheelchair access.",
            "14:00 Saturday 19 August 2023,\nMillenneum Bridge, York\n\n\t\t\u2022 Has wheelchair access.",
            "20:00 Friday 28 July 2023,\nSt Mary's Abbey, Museum Gardens, York\n\n\t\t\u2022 Has wheelchair access."
    );

   List<String> tickets_remaining_details = Arrays.asList(
           "\n\n\t\t\u2022 On Stage\n\t\t\u2022 Grass",
           "\n\n\t\t\u2022 Inner Circle\n\t\t\u2022 Outer Circle\n\t\t\u2022 Standing",
           "\n\n\t\t\u2022 Seated\n\t\t\u2022 Standing",
           "\n\n\t\t\u2022 Seated",
           "\n\n\t\t\u2022 Boat A\n\t\t\u2022 Boat B\n\t\t\u2022 Riverbank",
           "\n\n\t\t\u2022 Seated\n\t\t\u2022 Standing"
           );

    ListView listView;

    SQLiteDatabase wdb;

    LinearLayout sortLayout;
    Button sortByNameAsc,sortByNameDsc,sortButton;

    SearchView searchView;


    ArrayList<Integer> total_seats = new ArrayList<>();

    boolean sortLayoutHidden = true;
    boolean is_sorted_desc = false;
    MyListAdapter listAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        wdb = databaseValidateUserClass.getWritable(this);

        sortByNameAsc = findViewById(R.id.sort_by_name_asc);
        sortByNameDsc = findViewById(R.id.sort_by_name_dsc);
        sortLayout = findViewById(R.id.sort_layout);
        sortButton = findViewById(R.id.sort_button);
        listView = findViewById(R.id.listview);
        listView.setClickable(true);
        searchView = findViewById(R.id.search_view);


        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //when sortButton is clicked
                if (sortLayoutHidden){
                    sortLayoutHidden = false;
                    sortLayout.setVisibility(View.VISIBLE);
                    sortButton.setText("Hide");
                }
                else{
                    sortLayoutHidden = true;
                    sortLayout.setVisibility(View.GONE);
                    sortButton.setText("Sort");
                }
            }
        });

        //getting seating information from database
        SharedPreferences sharedPreferences = getSharedPreferences("seats_remaining", Context.MODE_PRIVATE);

        for(int i=0; i<titles.size(); i++){//adding items to list
            performancesListItem item = new performancesListItem(titles.get(i), images.get(i));
            list.add(item);
            total_seats.add(sharedPreferences.getInt(titles.get(i),39));
        }


        //setting adapter to listview
        Home.this.listAdapter = new MyListAdapter(Home.this,list);
        listView.setAdapter(listAdapter);
        listView.setTextFilterEnabled(true);


        //when sort by name asc is clicked
        sortByNameAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_sorted_desc){
                    is_sorted_desc = false;
                    Collections.reverse(titles);
                    Collections.reverse(description);
                    Collections.reverse(images);
                    Collections.reverse(tickets_remaining_details);
                    Collections.reverse(total_seats);
                    Collections.reverse(list);
                    Home.this.listAdapter = new MyListAdapter(Home.this,list);
                    listView.setAdapter(listAdapter);
                }
            }
        });

        //when sort by name dsc is clicked
        sortByNameDsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!is_sorted_desc){
                    is_sorted_desc = true;
                    Collections.reverse(titles);
                    Collections.reverse(description);
                    Collections.reverse(images);
                    Collections.reverse(tickets_remaining_details);
                    Collections.reverse(total_seats);
                    Collections.reverse(list);
                    Home.this.listAdapter = new MyListAdapter(Home.this,list);
                    listView.setAdapter(listAdapter);
                }

            }
        });

        //adding searchview listener for searching text in list view
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(Home.this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Performance");


        //when any listitem is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Home.this,PerformanceDetails.class);
                i.putExtra("perfTitle", titles.get(position));
                i.putExtra("perfDetails", description.get(position));
                i.putExtra("ticketsRemainingDetails","Tickets Types (remaining "+sharedPreferences.getInt(titles.get(position),39)+" ) "+"(Cost £50)"+ tickets_remaining_details.get(position));
                i.putExtra("totalSeats",total_seats.get(position));
                startActivity(i);
            }
        });
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //when any options item is clicked
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_myTickets:
                Log.e("onOp","onOp");
                startActivity(new Intent(Home.this,UserTickets.class));
                finish();
                return true;
            case R.id.nav_performances:

                startActivity(new Intent(Home.this,Home.class));
                finish();
                return true;

            case R.id.nav_logOut:

                SharedPreferences sharedPreferences = getSharedPreferences("logged_in_email",MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email","null");
                editor.apply();

                Intent i = new Intent(Home.this, MainActivity.class);
                startActivity(i);
                finish();
                return true;
            case R.id.nav_quitApp:
                finishAffinity();
                System.exit(0);
                return true;
            default:
                return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.nav_performances);
        menuItem.setVisible(false);
        MenuItem menuItem2 = menu.findItem(R.id.nav_myTickets);
        menuItem2.setVisible(true);
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //when text is changed in search bar
        if (TextUtils.isEmpty(newText)) {
            listView.clearTextFilter();
        } else {
            listView.setFilterText(newText);
        }
        return true;
    }
}