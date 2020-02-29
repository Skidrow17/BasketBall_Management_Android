package com.uowm.ekasdym.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.uowm.ekasdym.R;
import com.uowm.ekasdym.activities.MessageActivityActivity;
import com.uowm.ekasdym.model.Contact;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class ContactListAdapter extends BaseAdapter implements Filterable {

    ArrayList<Contact> listData;
    LayoutInflater layoutInflater;
    ArrayList<Contact> filteredContactList;
    Context context;

    public ContactListAdapter(Context context, ArrayList<Contact> listData) {
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
            convertView = layoutInflater.inflate(R.layout.contact_list, null);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.name);
            holder.surname =  convertView.findViewById(R.id.surname);
            holder.profile_pic = (ImageView)  convertView.findViewById(R.id.profile_pic);
            holder.message =  convertView.findViewById(R.id.password);
            holder.call = convertView.findViewById(R.id.call);
            holder.last_login = convertView.findViewById(R.id.last_login);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Contact newsItem = (Contact) filteredContactList.get(position);

        holder.name.setText(newsItem.getName());
        holder.surname.setText(newsItem.getSurname());

        if(!newsItem.getLast_login().equals("null")) {
            holder.last_login.setText(newsItem.getLast_login());
        }
        else
            holder.last_login.setText(context.getString(R.string.never));

        mainViewholder = (ViewHolder) convertView.getTag();

        mainViewholder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, MessageActivityActivity.class);
                intent.putExtra("id",((Contact) filteredContactList.get(position)).getId());
                intent.putExtra("name_surname",((Contact) filteredContactList.get(position)).getName()+" "+((Contact) filteredContactList.get(position)).getSurname());
                context.startActivity(intent);
            }
        });


        mainViewholder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", ((Contact) filteredContactList.get(position)).getPhone_number(), null));
                context.startActivity(intent);
            }
        });


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
        TextView last_login;
        ImageView profile_pic;
        ImageButton message;
        ImageButton call;
    }


}