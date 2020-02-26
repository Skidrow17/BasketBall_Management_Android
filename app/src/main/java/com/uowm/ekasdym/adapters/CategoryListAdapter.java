package com.uowm.ekasdym.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uowm.ekasdym.R;
import com.uowm.ekasdym.activities.AllMatches;
import com.uowm.ekasdym.model.TeamCategory;

import java.util.ArrayList;

public class CategoryListAdapter extends BaseAdapter {

    private ArrayList listData;
    private LayoutInflater layoutInflater;
    Context context;

    public CategoryListAdapter(Context context, ArrayList listData) {

        this.listData = listData;
        this.context=context;
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
            convertView = layoutInflater.inflate(R.layout.category_list, null);
            holder = new ViewHolder();
            holder.category = convertView.findViewById(R.id.category);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TeamCategory newsItem = (TeamCategory) listData.get(position);

        holder.category.setText(newsItem.getName());


        mainViewholder = (ViewHolder) convertView.getTag();

        mainViewholder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, AllMatches.class);
                intent.putExtra("id",((TeamCategory) listData.get(position)).getId());
                intent.putExtra("title",((TeamCategory) listData.get(position)).getName());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView category;
    }


}