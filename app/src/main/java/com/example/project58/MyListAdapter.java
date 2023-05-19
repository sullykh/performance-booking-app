package com.example.project58;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

//This class is list adapter for performances list in the Home screen

public class MyListAdapter extends BaseAdapter implements Filterable {

    public Context context;
    public ArrayList<performancesListItem> employeeArrayList;
    public ArrayList<performancesListItem> orig;

    public MyListAdapter(Context context, ArrayList<performancesListItem> employeeArrayList) {
        super();
        this.context = context;
        this.employeeArrayList = employeeArrayList;
    }


    public class MyListHolder
    {
        TextView name;
        ImageView image;

    }

    //method for filtering the list when user types in the search box
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<performancesListItem> results = new ArrayList<performancesListItem>();
                if (orig == null)
                    orig = employeeArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final performancesListItem g : orig) {
                            if (g.title.toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                employeeArrayList = (ArrayList<performancesListItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    //when data in search box is changed
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    //return the total size of list items
    @Override
    public int getCount() {
        return employeeArrayList.size();
    }

    //return a list item
    @Override
    public Object getItem(int position) {
        return employeeArrayList.get(position);
    }

    //returns a list item id
    @Override
    public long getItemId(int position) {
        return position;
    }

    //inflate each list item to the screen according to their respective data
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyListHolder holder;
        if(convertView==null)
        {
            convertView=LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            holder=new MyListHolder();
            holder.name=(TextView) convertView.findViewById(R.id.performance_name);
            holder.image=(ImageView) convertView.findViewById(R.id.performance_image);
            convertView.setTag(holder);
        }
        else
        {
            holder=(MyListHolder) convertView.getTag();
        }

        holder.name.setText(employeeArrayList.get(position).title);
        holder.image.setImageResource(employeeArrayList.get(position).image);

        return convertView;
    }

}