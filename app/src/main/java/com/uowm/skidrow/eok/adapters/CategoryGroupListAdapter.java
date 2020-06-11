package com.uowm.skidrow.eok.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.uowm.skidrow.eok.R;
import com.uowm.skidrow.eok.activities.AllRankingActivity;
import com.uowm.skidrow.eok.model.TeamCategoryGroup;

import java.util.ArrayList;

public class CategoryGroupListAdapter extends BaseAdapter {

    private ArrayList listData;
    private LayoutInflater layoutInflater;
    Context context;

    public CategoryGroupListAdapter(Context context, ArrayList listData) {

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

        TeamCategoryGroup newsItem = (TeamCategoryGroup) listData.get(position);

        if(newsItem.getGroupId() != 0) {
            holder.category.setText(newsItem.getCategoryName() + " " + newsItem.getGroupName().split("-")[0]);
        }else{
            holder.category.setText(newsItem.getCategoryName());
        }
        mainViewholder = (ViewHolder) convertView.getTag();

        mainViewholder.category.setOnClickListener(v -> {
            Intent intent = new Intent(context, AllRankingActivity.class);
            intent.putExtra("categoryId",((TeamCategoryGroup) listData.get(position)).getCategoryId());
            intent.putExtra("groupId",((TeamCategoryGroup) listData.get(position)).getGroupId());

            if(((TeamCategoryGroup) listData.get(position)).getGroupId() != 0) {
                intent.putExtra("title",((TeamCategoryGroup) listData.get(position)).getCategoryName() + " " + ((TeamCategoryGroup) listData.get(position)).getGroupName().split("-")[0]);
            }else{
                intent.putExtra("title",((TeamCategoryGroup) listData.get(position)).getCategoryName());
            }
            context.startActivity(intent);
        });

        return convertView;
    }

    static class ViewHolder {
        TextView category;
    }


}