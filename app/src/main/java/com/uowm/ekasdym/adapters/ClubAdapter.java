package com.uowm.ekasdym.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.uowm.ekasdym.R;
import com.uowm.ekasdym.model.Club;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ClubViewHolder> implements Filterable
{
    private static final int TYPE_ROW = 0;
    private static final int TYPE_ROW_COLORFUL = 1;

    private List<Club> clubList;
    private List<Club> filteredClubList;
    private Context context;

    public ClubAdapter(Context context, List<Club> clubList)
    {
        this.context = context;
        this.clubList = clubList;
        this.filteredClubList = clubList;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position % 2 == 0)
        {
            return TYPE_ROW_COLORFUL;
        }

        return TYPE_ROW;
    }

    @Override
    public ClubViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        if (viewType == TYPE_ROW)
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_club, viewGroup, false);
            return new ClubViewHolder(view);
        } else
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_club_colorful,
                    viewGroup, false);
            return new ClubViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ClubViewHolder holder, int position)
    {
        Club club = filteredClubList.get(position);
        holder.name.setText(String.valueOf(club.name));
        holder.points.setText(String.valueOf(club.points));
        holder.wins.setText(String.valueOf(club.wins));
        holder.loses.setText(String.valueOf(club.loses));
        holder.total_games.setText(String.valueOf(club.totalGames));
    }

    @Override
    public int getItemCount()
    {
        return filteredClubList.size();
    }

    public class ClubViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name, wins, loses, total_games, points;

        public ClubViewHolder(View view)
        {
            super(view);
            name = view.findViewById(R.id.name);
            points = view.findViewById(R.id.points);
            wins = view.findViewById(R.id.wins);
            loses = view.findViewById(R.id.loses);
            total_games = view.findViewById(R.id.total_games);
        }
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
                    filteredClubList = clubList;
                } else
                {
                    List<Club> filteredList = new ArrayList<>();
                    for (Club club : clubList)
                    {
                        if (club.name.toLowerCase().contains(charString.toLowerCase()) )
                        {
                            filteredList.add(club);
                        }
                    }

                    filteredClubList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredClubList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                filteredClubList = (ArrayList<Club>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}