package com.uowm.skidrow.eok.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.uowm.skidrow.eok.R;
import com.uowm.skidrow.eok.model.Message;

import java.util.ArrayList;

public class ChatInProgressAdapter extends BaseAdapter {

    Context context;
    private ArrayList listData;
    private LayoutInflater layoutInflater;

    public ChatInProgressAdapter(Context context, ArrayList listData) {
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
            convertView = layoutInflater.inflate(R.layout.chats_in_progress, null);
            holder = new ViewHolder();
            holder.headlineView = convertView.findViewById(R.id.password);
            holder.reporterNameView = convertView.findViewById(R.id.sender);
            holder.dateTime = convertView.findViewById(R.id.datetime);
            holder.imageView = convertView.findViewById(R.id.image_sender);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Message newsItem = (Message) listData.get(position);
        holder.headlineView.setText(newsItem.getMessage());
        holder.reporterNameView.setText(newsItem.getName() + " " + newsItem.getSurname());
        holder.dateTime.setText(newsItem.getDateTime());

        if (holder.imageView != null) {
            Glide.with(context).load(newsItem.getPic()).into(holder.imageView);
        }
        return convertView;

    }

    static class ViewHolder {
        TextView headlineView;
        TextView reporterNameView;
        TextView dateTime;
        ImageView imageView;
    }
}