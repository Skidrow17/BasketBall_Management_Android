package com.uowm.skidrow.eok.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uowm.skidrow.eok.R;
import com.uowm.skidrow.eok.model.Match;

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
            convertView = layoutInflater.inflate(R.layout.list_item_match, null);
            holder = new ViewHolder();
            holder.team1 = (TextView) convertView.findViewById(R.id.home_name);
            holder.team2 = (TextView) convertView.findViewById(R.id.away_name);
            holder.score = convertView.findViewById(R.id.score);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.state = (TextView) convertView.findViewById(R.id.status);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Match newsItem = (Match) listData.get(position);

        holder.team1.setText(newsItem.getTeam1());
        holder.team2.setText(newsItem.getTeam2());
        holder.date.setText(newsItem.getDateTime());
        holder.score.setText(newsItem.getTeam1_score()+"-"+newsItem.getTeam2_score());

        if(newsItem.getState() == 0){
            holder.state.setText(context.getString(R.string.game_not_started_yet));
        }else if(newsItem.getState() == 1){
            holder.state.setText(context.getString(R.string.p1));
        }else if(newsItem.getState() == 2){
            holder.state.setText(context.getString(R.string.p2));
        }else if(newsItem.getState() == 3){
            holder.state.setText(context.getString(R.string.p3));
        }else if(newsItem.getState() == 4){
            holder.state.setText(context.getString(R.string.p4));
        }else if(newsItem.getState() == 5){
            holder.state.setText(context.getString(R.string.finale));
        }


        return convertView;
    }

    static class ViewHolder {
        TextView team1;
        TextView team2;
        TextView date;
        TextView score;
        TextView state;
    }


}