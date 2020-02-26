package com.uowm.ekasdym.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.uowm.ekasdym.R;
import com.uowm.ekasdym.model.Match;

import java.util.ArrayList;

public class AllMatchesListAdapter extends BaseAdapter {

    private ArrayList listData;
    private LayoutInflater layoutInflater;
    Context context;

    public AllMatchesListAdapter(Context context, ArrayList listData) {

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

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        ViewHolder mainViewholder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.all_game_list, null);
            holder = new ViewHolder();
            holder.team1 = (TextView) convertView.findViewById(R.id.textView2);
            holder.team2 = (TextView) convertView.findViewById(R.id.textView3);
            holder.date = (TextView) convertView.findViewById(R.id.textView4);
            holder.location = (Button) convertView.findViewById(R.id.button5);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Match newsItem = (Match) listData.get(position);

        holder.team1.setText(newsItem.getTeam1());
        holder.team2.setText(newsItem.getTeam2());
        holder.date.setText(newsItem.getDateTime());



        mainViewholder = (ViewHolder) convertView.getTag();

        mainViewholder.location.setOnClickListener(v -> {
            String url = "http://maps.google.com/maps?daddr=" + ((Match) listData.get(position)).getLatitude() + "," + ((Match) listData.get(position)).getLongitude();
            Intent goZe = new Intent(Intent.ACTION_VIEW);
            goZe.setData(Uri.parse(url));
            context.startActivity(goZe);
        });


        return convertView;
    }

    static class ViewHolder {
        TextView team1;
        TextView team2;
        TextView date;
        Button location;
    }


}