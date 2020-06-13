package com.uowm.skidrow.eok.fragments;

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
import com.uowm.skidrow.eok.R;
import com.uowm.skidrow.eok.activities.MessageActivityActivity;
import com.uowm.skidrow.eok.activities.MessageShowActivityActivity;
import com.uowm.skidrow.eok.adapters.ChatInProgressAdapter;
import com.uowm.skidrow.eok.adapters.MessageReceiveAdapter;
import com.uowm.skidrow.eok.database.DatabaseHelper;
import com.uowm.skidrow.eok.model.Message;
import com.uowm.skidrow.eok.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatsInProgressFragment extends ListFragment {

    private String url = "";
    private static final String MESSAGE = "Open_Chats";
    private int global_position;
    private int global_id;
    ArrayList < Message > messages = new ArrayList<Message>();

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
            url = getString(R.string.server) + "getUserChats.php?id=" + user_id + "&safe_key=" + safe_key;
            JSONParser jParser = new JSONParser();
            String st = jParser.getJSONFromUrl(url);

            return st;
        }


        @Override
        protected void onPostExecute(String json) {

            messages.clear();
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
                        int id = obj.getInt("user_id");
                        String name = obj.getString("name");
                        String surname = obj.getString("surname");
                        String text_message = obj.getString("text_message");
                        String profile_pic = obj.getString("profile_pic");
                        String date_time = obj.getString("date_time");

                        Message message = new Message(id, name, surname, text_message, getString(R.string.image_server) + profile_pic, date_time);
                        messages.add(message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                final ListView listView = getActivity().findViewById(android.R.id.list);
                registerForContextMenu(listView);
                listView.setAdapter(new ChatInProgressAdapter(ChatsInProgressFragment.this.getActivity(), messages));

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
    public void onListItemClick(ListView l, View v, int position, long id) {

        global_position = position;
        Intent intent = new Intent(getActivity(), MessageActivityActivity.class);
        intent.putExtra("id", messages.get(position).getSender_id());
        intent.putExtra("name_surname", messages.get(position).getName() + " " + messages.get(position).getSurname());
        startActivity(intent);
    }
}