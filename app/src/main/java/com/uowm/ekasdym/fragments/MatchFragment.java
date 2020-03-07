package com.uowm.ekasdym.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uowm.ekasdym.R;
import com.uowm.ekasdym.activities.HumanPowerActivity;
import com.uowm.ekasdym.activities.ScoreEditActivityActivity;
import com.uowm.ekasdym.adapters.GameListAdapter;
import com.uowm.ekasdym.database.DatabaseHelper;
import com.uowm.ekasdym.model.Match;
import com.uowm.ekasdym.utilities.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MatchFragment extends ListFragment {

    private String url = "";
    private static final String MATCH = "Match_Details";
    ArrayList <Match> matches = new ArrayList< Match >();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_item, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        new JSONParse().execute();
        getActivity();
    }


    public class JSONParse extends AsyncTask < String, String, String > {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getString(R.string.waiting_screen));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String...args) {
            DatabaseHelper myDb = new DatabaseHelper(getActivity());
            Cursor res = myDb.getAllData();
            String user_id = "";
            String safe_key = "";

            if (res.getCount() != 0) {
                while (res.moveToNext()) {
                    user_id = res.getString(3);
                    safe_key = res.getString(5);
                }
            }
            res.close();

            url = getString(R.string.server) + "getMatch.php?id=" + user_id + "&safe_key=" + safe_key;

            JSONParser jParser = new JSONParser();

            String st = jParser.getJSONFromUrl(url);

            return st;
        }


        @Override
        protected void onPostExecute(String json) {


            final Handler handler = new Handler();
            handler.postDelayed(() -> pDialog.dismiss(), 500);

            int error_code = 0;
            JSONObject jobj = null;


            try {
                jobj = new JSONObject(json);
                JSONObject jobj4 = jobj.getJSONObject("ERROR");
                error_code = jobj4.getInt("error_code");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (error_code == 200) {
                try {

                    JSONArray jsonArray = jobj.getJSONArray(MATCH);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);


                        String team1 = obj.getString("team_id_1");
                        String team2 = obj.getString("team_id_2");
                        int team_1_score = obj.getInt("team_score_1");
                        int team_2_score = obj.getInt("team_score_2");
                        int id = obj.getInt("id");
                        String date_time = obj.getString("date_time");
                        String latitude = obj.getString("latitude");
                        String longitude = obj.getString("longitude");
                        int state = obj.getInt("state");


                        Match match = new Match(team1, team2, team_1_score, team_2_score, date_time, latitude, longitude, id,state);
                        matches.add(match);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final ListView listView = (ListView) getActivity().findViewById(android.R.id.list);
                registerForContextMenu(listView);
                listView.setAdapter(new GameListAdapter(MatchFragment.this.getActivity(), matches));
            } else if (error_code == 403) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_403), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();
            } else if (error_code == 204) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_204), Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_0), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(getString(R.string.menu));
        menu.add(0, v.getId(), 0, getString(R.string.location));
        menu.add(1, v.getId(), 1, getString(R.string.human_power));
        menu.add(2, v.getId(), 2, getString(R.string.score));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getGroupId() == 0) {
            String url = "http://maps.google.com/maps?daddr=" + ((Match) matches.get(info.position)).getLatitude() + "," + ((Match) matches.get(info.position)).getLongitude();
            Intent goZe = new Intent(Intent.ACTION_VIEW);
            goZe.setData(Uri.parse(url));
            startActivity(goZe);
        }else if(item.getGroupId() == 1){
            Intent i = new Intent(getActivity(), HumanPowerActivity.class);
            i.putExtra("gameId", matches.get(info.position).getMatch_id());
            startActivity(i);
        }
        else {
            Intent i = new Intent(getActivity(), ScoreEditActivityActivity.class);
            i.putExtra("team1", ((Match) matches.get(info.position)).getTeam1());
            i.putExtra("team2", ((Match) matches.get(info.position)).getTeam2());
            i.putExtra("team_score_1", ((Match) matches.get(info.position)).getTeam1_score());
            i.putExtra("team_score_2", ((Match) matches.get(info.position)).getTeam2_score());
            i.putExtra("match_id", ((Match) matches.get(info.position)).getMatch_id());
            i.putExtra("state", ((Match) matches.get(info.position)).getState());

            startActivity(i);
        }
        return true;
    }
}