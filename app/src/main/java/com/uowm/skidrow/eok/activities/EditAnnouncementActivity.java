package com.uowm.skidrow.eok.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uowm.skidrow.eok.R;
import com.uowm.skidrow.eok.database.DatabaseHelper;
import com.uowm.skidrow.eok.utilities.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EditAnnouncementActivity extends AppCompatActivity {


    private String url = "";
    private int id;
    private String title_filtered;
    private String text_message;

    EditText text;
    EditText title;
    Button bt;
    DateFormat formatter;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_edit);


        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



        text = findViewById(R.id.text);
        title = findViewById(R.id.title);
        bt = findViewById(R.id.sent);


        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        String title1 = intent.getStringExtra("title");
        String text1 = intent.getStringExtra("text");

        title.setText(title1);
        text.setText(text1);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>"+getString(R.string.edit)+"</font>"));

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_message = text.getText().toString().replaceAll(" ", "%20");
                title_filtered = title.getText().toString().replaceAll(" ", "%20");
                new EditAnnouncementActivity.JSONParse().execute();
            }
        });


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
            pDialog = new ProgressDialog(EditAnnouncementActivity.this);
            pDialog.setMessage(getString(R.string.waiting_screen));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String...args) {

            DatabaseHelper myDb = new DatabaseHelper(EditAnnouncementActivity.this);
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
            url = getString(R.string.server) + "editAnnouncement.php?id=" + user_id + "&safe_key=" + safe_key + "&announcement_id=" + id + "&text=" + text_message + "&title=" + title_filtered;

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
                Toast toast = Toast.makeText(EditAnnouncementActivity.this, getString(R.string.error_code_200), Toast.LENGTH_LONG);
                toast.show();
            } else if (error_code == 403) {
                Toast toast = Toast.makeText(EditAnnouncementActivity.this, getString(R.string.error_code_403), Toast.LENGTH_LONG);
                toast.show();
                EditAnnouncementActivity.this.finishAffinity();
            } else if (error_code == 204) {
                Toast toast = Toast.makeText(EditAnnouncementActivity.this, getString(R.string.error_code_204), Toast.LENGTH_LONG);
                toast.show();
            } else if (error_code == 201) {
                Toast toast = Toast.makeText(EditAnnouncementActivity.this, getString(R.string.error_code_201), Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(EditAnnouncementActivity.this, getString(R.string.error_code_0), Toast.LENGTH_LONG);
                toast.show();
                EditAnnouncementActivity.this.finishAffinity();
            }
        }
    }
}