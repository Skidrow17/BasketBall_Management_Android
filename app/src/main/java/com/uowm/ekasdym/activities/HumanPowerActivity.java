package com.uowm.ekasdym.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.uowm.ekasdym.R;
import com.uowm.ekasdym.adapters.HumanPowerListAdapter;
import com.uowm.ekasdym.database.DatabaseHelper;
import com.uowm.ekasdym.model.Contact;
import com.uowm.ekasdym.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class HumanPowerActivity extends AppCompatActivity {

    private String url = "",name = "";
    private int gameId;
    private static final String CONTACTS = "contacts";
    private HumanPowerListAdapter contactAdapter;
    public  ArrayList <Contact> contacts = new ArrayList <> ();
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        gameId = i.getIntExtra("gameId", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>"+getString(R.string.human_power)+"</font>"));
        new JSONParse().execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public class JSONParse extends AsyncTask < String, String, String > {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HumanPowerActivity.this);
            pDialog.setMessage(getString(R.string.waiting_screen));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String...args) {

            DatabaseHelper myDb = new DatabaseHelper(HumanPowerActivity.this);
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

            url = getString(R.string.server) + "getMatchRelatedContacts.php?safe_key=" + safe_key + "&id=" + user_id+"&gid="+gameId;

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
                        String surname = obj.getString("surname");
                        String profile_pic = obj.getString("profile_pic");
                        String profile = obj.getString("profession");
                        Contact referee = new Contact(id, fname, surname, getString(R.string.image_server) + profile_pic,profile);
                        contacts.add(referee);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listView = HumanPowerActivity.this.findViewById(android.R.id.list);
                contactAdapter = new HumanPowerListAdapter(HumanPowerActivity.this, contacts);
                listView.setAdapter(contactAdapter);

            } else if (error_code == 403) {
                Toast toast = Toast.makeText(HumanPowerActivity.this, getString(R.string.error_code_403), Toast.LENGTH_LONG);
                toast.show();
                HumanPowerActivity.this.finishAffinity();
            } else if (error_code == 204) {
                Toast toast = Toast.makeText(HumanPowerActivity.this, getString(R.string.error_code_204), Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(HumanPowerActivity.this, getString(R.string.error_code_0), Toast.LENGTH_LONG);
                toast.show();
                HumanPowerActivity.this.finishAffinity();
            }
        }
    }
}