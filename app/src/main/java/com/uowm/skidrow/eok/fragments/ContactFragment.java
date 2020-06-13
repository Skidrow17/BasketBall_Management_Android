package com.uowm.skidrow.eok.fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.ListFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uowm.skidrow.eok.R;
import com.uowm.skidrow.eok.adapters.ContactListAdapter;
import com.uowm.skidrow.eok.database.DatabaseHelper;
import com.uowm.skidrow.eok.model.Contact;
import com.uowm.skidrow.eok.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ContactFragment extends ListFragment {

    private String url = "";
    private static final String CONTACTS = "contacts";
    private ContactListAdapter contactAdapter;
    public  ArrayList <Contact> contacts = new ArrayList <> ();
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.list_item, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_search).setVisible(true);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    contactAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
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

            url = getString(R.string.server) + "getContacts.php?safe_key=" + safe_key + "&id=" + user_id;

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
                    JSONArray jsonArray = jobj.getJSONArray(CONTACTS);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String fname = obj.getString("name");
                        int id = obj.getInt("id");
                        String phone_number = obj.getString("phone");
                        String surname = obj.getString("surname");
                        String profile_pic = obj.getString("profile_pic");
                        String last_login = obj.getString("last_login");
                        String profile = obj.getString("profession");

                        PrettyTime p = new PrettyTime();
                        String hourInGreek;
                        p.setLocale(Locale.ENGLISH);
                        if(!isNullOrEmpty(last_login)) {
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(last_login);
                            hourInGreek = p.format(date).replace("ago", getString(R.string.ago));
                            hourInGreek = hourInGreek.replace("years",getString(R.string.years));
                            hourInGreek = hourInGreek.replace("days",getString(R.string.days));
                            hourInGreek = hourInGreek.replace("minutes",getString(R.string.minutes));
                            hourInGreek = hourInGreek.replace("hours",getString(R.string.hours));
                            hourInGreek = hourInGreek.replace("moments",getString(R.string.moments));
                            hourInGreek = hourInGreek.replace("months",getString(R.string.months));
                            hourInGreek = hourInGreek.replace("weeks",getString(R.string.weeks));
                            hourInGreek = hourInGreek.replace("hour",getString(R.string.hour));
                            hourInGreek = hourInGreek.replace("month",getString(R.string.month));
                            hourInGreek = hourInGreek.replace("minute",getString(R.string.minute));
                            hourInGreek = hourInGreek.replace("year",getString(R.string.year));
                            hourInGreek = hourInGreek.replace("day",getString(R.string.day));
                            hourInGreek = hourInGreek.replace("week",getString(R.string.week));

                        }else{
                            hourInGreek = "null";
                        }
                        Contact referee = new Contact(id, fname, surname, getString(R.string.image_server) + profile_pic, phone_number, hourInGreek, profile);
                        contacts.add(referee);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                listView = getActivity().findViewById(android.R.id.list);
                contactAdapter = new ContactListAdapter(ContactFragment.this.getActivity(), contacts);
                listView.setAdapter(contactAdapter);

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

    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty() && !str.equals("null"))
            return false;
        return true;
    }
}