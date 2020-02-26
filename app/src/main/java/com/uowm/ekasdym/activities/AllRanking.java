package com.uowm.ekasdym.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.HorizontalScrollView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.uowm.ekasdym.R;
import com.uowm.ekasdym.adapters.ClubAdapter;
import com.uowm.ekasdym.model.Club;
import com.uowm.ekasdym.utilities.FixedGridLayoutManager;
import com.uowm.ekasdym.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllRanking extends AppCompatActivity
{

    int scrollX = 0;
    List<Club> clubList = new ArrayList<>();
    RecyclerView rvClub;
    HorizontalScrollView headerScroll;
    SearchView searchView;
    ClubAdapter clubAdapter;
    private String url = "",title;
    private int categoryId,groupId;
    private static final String ARRAY_NAME = "Team_Details";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ranking);

        Intent i = getIntent();
        categoryId = i.getIntExtra("categoryId", 0);
        groupId = i.getIntExtra("groupId", 0);
        title = i.getStringExtra("title");

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>"+title+"</font>"));


        new JSONParse().execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // filter recycler view when query submitted
                clubAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query)
            {
                // filter recycler view when text is changed
                clubAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        // close search view on back button pressed
        if (!searchView.isIconified())
        {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void initViews()
    {
        rvClub = findViewById(R.id.rvClub);
        headerScroll = findViewById(R.id.headerScroll);
    }

    private void setUpRecyclerView()
    {
        clubAdapter = new ClubAdapter(AllRanking.this, clubList);
        FixedGridLayoutManager manager = new FixedGridLayoutManager();
        manager.setTotalColumnCount(1);
        rvClub.setLayoutManager(manager);
        rvClub.setAdapter(clubAdapter);
        rvClub.addItemDecoration(new DividerItemDecoration(AllRanking.this, DividerItemDecoration.VERTICAL));
    }

    public class JSONParse extends AsyncTask< String, String, String > {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllRanking.this);
            pDialog.setMessage(getString(R.string.waiting_screen));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String...args) {


            url = getString(R.string.server) + "getRankingPerCategory.php?cid=" +categoryId+"&gid="+groupId;

            JSONParser jParser = new JSONParser();

            String st = jParser.getJSONFromUrl(url);

            return st;
        }


        @Override
        protected void onPostExecute(String json) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pDialog.dismiss();
                }
            }, 500);

            int error_code = 0;
            JSONObject jobj = null;

            try {
                jobj = new JSONObject(json);
                JSONObject errorObject = jobj.getJSONObject("ERROR");
                error_code = errorObject.getInt("error_code");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (error_code == 200) {

                try {
                    JSONArray jsonArray = jobj.getJSONArray(ARRAY_NAME);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        int id = obj.getInt("id");
                        String name = obj.getString("name");
                        int wins = obj.getInt("wins");
                        int loses = obj.getInt("loses");
                        int points = obj.getInt("points");

                        Club club = new Club(id, name, wins, loses, points);
                        clubList.add(club);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                initViews();

                setUpRecyclerView();

                rvClub.addOnScrollListener(new RecyclerView.OnScrollListener()
                {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy)
                    {
                        super.onScrolled(recyclerView, dx, dy);

                        scrollX += dx;

                        headerScroll.scrollTo(scrollX, 0);
                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState)
                    {
                        super.onScrollStateChanged(recyclerView, newState);
                    }
                });

            } else if (error_code == 403) {
                Toast toast = Toast.makeText(AllRanking.this, getString(R.string.error_code_403), Toast.LENGTH_LONG);
                toast.show();
                AllRanking.this.finishAffinity();
            } else if (error_code == 204) {
                Toast toast = Toast.makeText(AllRanking.this, getString(R.string.error_code_204), Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(AllRanking.this, getString(R.string.error_code_0), Toast.LENGTH_LONG);
                toast.show();
                AllRanking.this.finishAffinity();
            }
        }
    }
}
