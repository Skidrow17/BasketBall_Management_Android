package com.uowm.ekasdym.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.uowm.ekasdym.R;
import com.uowm.ekasdym.model.Restriction;

import java.util.ArrayList;

public class RestrictionListAdapter extends BaseAdapter {

    Context context;
    private ArrayList listData;
    private LayoutInflater layoutInflater;

    public RestrictionListAdapter(Context context, ArrayList listData) {
        this.listData = listData;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.my_restrictions, null);
            holder = new ViewHolder();
            holder.time_to = (TextView) convertView.findViewById(R.id.to);
            holder.time_from = (TextView) convertView.findViewById(R.id.from);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Restriction res = (Restriction) listData.get(position);
        holder.time_to.setText(res.getTime_to());
        holder.time_from.setText(res.getTime_from());
        holder.date.setText(res.getDate());

        return convertView;

    }

    static class ViewHolder {
        TextView time_to;
        TextView time_from;
        TextView date;
    }
}