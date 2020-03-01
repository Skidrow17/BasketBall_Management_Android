package com.uowm.ekasdym.fragments;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uowm.ekasdym.R;
import com.uowm.ekasdym.database.DatabaseHelper;
import com.uowm.ekasdym.utilities.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;


public class MakeAnnouncementFragment extends Fragment {

    private String url;
    private Button submit;
    private String textFilter = "", titleFilter = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.announcement_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textFilter = "";
        titleFilter = "";

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        getActivity();

        submit = getActivity().findViewById(R.id.sent);
        TextView title = getActivity().findViewById(R.id.title);
        TextView text = getActivity().findViewById(R.id.text);

        submit.setOnClickListener(v -> {
            if (text.getText().toString().equals("") && title.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.complete_the_fields), Toast.LENGTH_LONG);
                toast.show();
            } else {
                textFilter = text.getText().toString().replaceAll(" ", "%20");
                titleFilter = title.getText().toString().replaceAll(" ", "%20");

                new Sent_Announcement().execute();
            }
        });

    }

    public class Sent_Announcement extends AsyncTask < String, String, String > {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getString(R.string.waiting_screen));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
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

            url = getString(R.string.server) + "setAnnouncement.php?user_id=" + user_id + "&safe_key=" + safe_key + "&text=" + textFilter + "&title=" + titleFilter;
            JSONParser jParser = new JSONParser();
            String st = jParser.getJSONFromUrl(url);

            return st;
        }


        @Override
        protected void onPostExecute(String json) {
            pDialog.dismiss();

            int error_code = 0;
            JSONObject jobj;
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
            } else if (error_code == 201) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_201), Toast.LENGTH_LONG);
                toast.show();
            } else if (error_code == 403) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_403), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();
            } else {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_0), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();

            }

        }

    }
}