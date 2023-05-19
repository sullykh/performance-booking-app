package com.example.project58;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


// this is list adapter for tickets list in my tickets screen
public class MyTicketsListAdapter extends ArrayAdapter<TicketsListItem> {

    public MyTicketsListAdapter(Context context, ArrayList<TicketsListItem> list){
        super(context, R.layout.ticket_list_item,list);
    }

    //inflate each ticket list item with their respective data
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TicketsListItem item = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ticket_list_item,parent,false);
        }

        TextView title = convertView.findViewById(R.id.ticket_name);
        TextView amount = convertView.findViewById(R.id.ticket_amount);

        title.setText(item.title);
        amount.setText(String.valueOf(item.amount));

        return convertView;
    }
}
