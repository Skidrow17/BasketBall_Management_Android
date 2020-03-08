package com.uowm.skidrow.eok.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uowm.skidrow.eok.R;
import com.uowm.skidrow.eok.model.Contact;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class HumanPowerListAdapter extends BaseAdapter implements Filterable {

    ArrayList<Contact> listData;
    LayoutInflater layoutInflater;
    ArrayList<Contact> filteredContactList;
    Context context;

    public HumanPowerListAdapter(Context context, ArrayList<Contact> listData) {
        this.listData = listData;
        this.filteredContactList = listData;
        this.context=context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return filteredContactList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredContactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        ViewHolder mainViewholder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.human_power_list, null);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.name);
            holder.surname =  convertView.findViewById(R.id.surname);
            holder.profile =  convertView.findViewById(R.id.profile);
            holder.profile_pic = convertView.findViewById(R.id.profile_pic);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Contact newsItem = filteredContactList.get(position);

        holder.name.setText(newsItem.getName());
        holder.surname.setText(newsItem.getSurname());
        holder.profile.setText(newsItem.getProfile());


        if (holder.profile_pic != null) {

            try {
                Glide.with(context)
                        .load(new URL(newsItem.getPic()))
                        .into(holder.profile_pic);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }

        return convertView;
    }

    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String charString = charSequence.toString();
                if (charString.isEmpty())
                {
                    filteredContactList = listData;
                } else {
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact contact : listData)
                    {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name
                        if (contact.name.toLowerCase().contains(charString.toLowerCase()) )
                        {
                            filteredList.add(contact);
                        }
                    }
                    filteredContactList = (ArrayList<Contact>) filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredContactList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                filteredContactList = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder {
        TextView name;
        TextView surname;
        TextView profile;
        ImageView profile_pic;
    }


}