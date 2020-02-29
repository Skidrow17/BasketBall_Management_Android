package com.uowm.ekasdym.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
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
import com.uowm.ekasdym.activities.AnnouncementShow;
import com.uowm.ekasdym.activities.EditAnnouncement;
import com.uowm.ekasdym.adapters.AnnouncementListAdapter;
import com.uowm.ekasdym.database.DatabaseHelper;
import com.uowm.ekasdym.model.Announcement;
import com.uowm.ekasdym.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AnnouncementsFragment extends ListFragment {

    private String url = "";
    private static final String MESSAGE = "announcements";
    private int global_id;
    ArrayList < Announcement > announcements = new ArrayList<Announcement>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_item, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);


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

            url = getString(R.string.server) + "getAnnouncements.php?id=" + user_id + "&safe_key=" + safe_key;
            JSONParser jParser = new JSONParser();
            String st = jParser.getJSONFromUrl(url);

            return st;
        }




        @Override
        protected void onPostExecute(String json) {

            announcements.clear();
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

                    JSONArray jsonArray = jobj.getJSONArray(MESSAGE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String title = obj.getString("title");
                        int id = obj.getInt("id");
                        String text = obj.getString("text");
                        String date_time = obj.getString("date_time");
                        String name = obj.getString("name");
                        String surname = obj.getString("surname");


                        Announcement announcement = new Announcement(id, title, text, date_time, name + " " + surname);
                        announcements.add(announcement);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                final ListView listView = getActivity().findViewById(android.R.id.list);
                registerForContextMenu(listView);
                listView.setAdapter(new AnnouncementListAdapter(getActivity(), announcements));

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
    public void onListItemClick(ListView l, View v, final int position, long id) {

        Intent intent = new Intent(getActivity(), AnnouncementShow.class);
        intent.putExtra("title", announcements.get(position).getTitle());
        intent.putExtra("text", announcements.get(position).getText());
        startActivity(intent);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Μενού");
        menu.add(0, v.getId(), 0, getString(R.string.delete));
        menu.add(1, v.getId(), 1, getString(R.string.edit));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getGroupId() == 0) {
            global_id = announcements.get(info.position).getID();
            new Delete_Announcement().execute();
        } else {
            Intent intent = new Intent(getActivity(), EditAnnouncement.class);
            intent.putExtra("id", announcements.get(info.position).getID());
            intent.putExtra("title", announcements.get(info.position).getTitle());
            intent.putExtra("text", announcements.get(info.position).getText());
            startActivity(intent);
        }

        return true;
    }


    public class Delete_Announcement extends AsyncTask < String, String, String > {
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

            url = getString(R.string.server) + "deleteAnnouncement.php?user_id=" + user_id + "&safe_key=" + safe_key + "&announcement_id=" + global_id;
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
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_200), Toast.LENGTH_LONG);
                toast.show();
                for (int j = 0; j < announcements.size(); j++) {
                    if (announcements.get(j).getID() == global_id) {
                        announcements.remove(j);
                        break;
                    }
                }

                final ListView listView = getActivity().findViewById(android.R.id.list);
                listView.setAdapter(new AnnouncementListAdapter(getActivity(), announcements));

            } else if (error_code == 403) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_403), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();
            } else if (error_code == 204) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_204), Toast.LENGTH_LONG);
                toast.show();
            } else if (error_code == 201) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_201), Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_0), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();
            }
        }
    }
}