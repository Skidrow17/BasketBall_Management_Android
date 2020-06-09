package com.uowm.skidrow.eok.activities;



import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.uowm.skidrow.eok.R;
import com.uowm.skidrow.eok.adapters.TabAdapter;
import com.uowm.skidrow.eok.database.DatabaseHelper;
import com.uowm.skidrow.eok.fragments.MessageViewFragment;
import com.uowm.skidrow.eok.utilities.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageShowActivityActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    int message_id;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_tab);


        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        String name_surname = intent.getStringExtra("name_surname");
        String class_name = intent.getStringExtra("class_name");
        message_id = intent.getIntExtra("id", 0);
        Integer sender_id = intent.getIntExtra("sender_id",0);

        Bundle bundle = new Bundle();
        bundle.putString("name_surname", name_surname);
        bundle.putString("message", message);
        bundle.putInt("id",message_id);
        bundle.putInt("sender_id", sender_id);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);

        if (class_name.equals("Incoming_Message")) {
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>"+getString(R.string.from)+" : " + name_surname + "</font>"));
            new Message_Status_Change().execute();
        } else {
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>"+getString(R.string.to)+" : " + name_surname + "</font>"));
        }


        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Μήνυμα"));
        tabLayout.addTab(tabLayout.newTab().setText("Αποστολή"));
        tabLayout.addTab(tabLayout.newTab().setText("Ιστορικό"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final TabAdapter adapter = new TabAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount(),bundle);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public class Message_Status_Change extends AsyncTask< String, String, String > {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String...args) {

            DatabaseHelper myDb = new DatabaseHelper(MessageShowActivityActivity.this);
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
            url = getString(R.string.server) + "setReadMessage.php?id=" + user_id + "&safe_key=" + safe_key + "&message_id=" + message_id;
            JSONParser jParser = new JSONParser();
            String st = jParser.getJSONFromUrl(url);
            return st;
        }


        @Override
        protected void onPostExecute(String json) {

            int error_code = 0;
            JSONObject jobj;

            try {
                jobj = new JSONObject(json);
                JSONObject jobj4 = jobj.getJSONObject("ERROR");
                error_code = jobj4.getInt("error_code");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (error_code == 403) {
                Toast toast = Toast.makeText(MessageShowActivityActivity.this, getString(R.string.error_code_403), Toast.LENGTH_LONG);
                toast.show();
                finishAffinity();
            }

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}