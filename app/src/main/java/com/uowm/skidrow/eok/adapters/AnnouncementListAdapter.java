package com.uowm.skidrow.eok.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.uowm.skidrow.eok.R;
import com.uowm.skidrow.eok.model.Announcement;
import java.util.ArrayList;

public class AnnouncementListAdapter extends BaseAdapter {

    Context context;
    private ArrayList listData;
    private LayoutInflater layoutInflater;

    public AnnouncementListAdapter(Context context, ArrayList listData) {
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
            convertView = layoutInflater.inflate(R.layout.announcement_list, null);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.security_key);
            holder.text = convertView.findViewById(R.id.password);
            holder.dateTime = convertView.findViewById(R.id.datetime);
            holder.editor = convertView.findViewById(R.id.editor);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Announcement newsItem = (Announcement) listData.get(position);

        holder.title.setText(newsItem.getTitle());
        holder.text.setText(newsItem.getText());
        holder.dateTime.setText(newsItem.getDateTime());
        holder.editor.setText(newsItem.getEditor());
        return convertView;
    }


    static class ViewHolder {
        TextView title;
        TextView dateTime;
        TextView text;
        TextView editor;
    }
}