package com.uowm.skidrow.eok.activities;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uowm.skidrow.eok.R;
import com.uowm.skidrow.eok.database.DatabaseHelper;
import com.uowm.skidrow.eok.utilities.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;


public class MessageShowActivityActivity extends AppCompatActivity {


    TextView text;
    int message_id;
    String url;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_show);
        text = findViewById(R.id.password);
        Button replyButton = findViewById(R.id.reply);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);


        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        String name_surname = intent.getStringExtra("name_surname");
        String class_name = intent.getStringExtra("class_name");
        message_id = intent.getIntExtra("id", 0);
        Integer sender_id = intent.getIntExtra("sender_id",0);

        text.setText(message);


        if (class_name.equals("Incoming_Message")) {
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>"+getString(R.string.from)+" : " + name_surname + "</font>"));
            new Message_Status_Change().execute();

        } else {
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>"+getString(R.string.to)+" : " + name_surname + "</font>"));
            replyButton.setVisibility(View.GONE);
        }

        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageShowActivityActivity.this, MessageActivityActivity.class);
                intent.putExtra("id", sender_id);
                intent.putExtra("name_surname", name_surname);
                startActivity(intent);
            }
        });


    }


    public class Message_Status_Change extends AsyncTask < String, String, String > {


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