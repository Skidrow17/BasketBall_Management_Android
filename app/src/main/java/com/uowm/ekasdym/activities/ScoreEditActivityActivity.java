package com.uowm.ekasdym.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uowm.ekasdym.R;
import com.uowm.ekasdym.database.DatabaseHelper;
import com.uowm.ekasdym.utilities.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

public class ScoreEditActivityActivity extends AppCompatActivity {


    private TextView view1, view2;
    private EditText score1, score2;
    private Button submit;
    private String url, team1, team2;
    private int team_score_1, team_score_2, match_id, state;
    private CheckBox p1,p2,p3,p4,finale;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_edit);
        //text=findViewById(R.id.text);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>" + getString(R.string.score) + "</font>"));


        Intent intent = getIntent();
        team1 = intent.getStringExtra("team1");
        team2 = intent.getStringExtra("team2");
        team_score_1 = intent.getIntExtra("team_score_1", 0);
        team_score_2 = intent.getIntExtra("team_score_2", 0);
        match_id = intent.getIntExtra("match_id", 0);
        state = intent.getIntExtra("state", 0);




        view1 = findViewById(R.id.textView6);
        view2 = findViewById(R.id.textView7);
        score1 = findViewById(R.id.editText3);
        score2 = findViewById(R.id.editText4);
        submit = findViewById(R.id.button3);
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);
        finale = findViewById(R.id.finale);

        view1.setText(team1);
        view2.setText(team2);
        score1.setText(team_score_1 + "", TextView.BufferType.EDITABLE);
        score2.setText(team_score_2 + "", TextView.BufferType.EDITABLE);

        team_score_1 = Integer.parseInt(score1.getText().toString());
        team_score_2 = Integer.parseInt(score2.getText().toString());

        if(state == 1){
            p1.setChecked(true);
        }else if(state == 2){
            p2.setChecked(true);
        }else if(state == 3){
            p3.setChecked(true);
        }else if(state == 4){
            p4.setChecked(true);
        }else if(state == 5){
            finale.setChecked(true);
        }

        p1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!p1.isChecked()){
                    p1.setChecked(false);
                }else {
                    p1.setChecked(true);
                }
                state = 1;
                p2.setChecked(false);
                p3.setChecked(false);
                p4.setChecked(false);
                finale.setChecked(false);
            }
        });

        p2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                p1.setChecked(false);
                if(!p2.isChecked()){
                    p2.setChecked(false);
                }else {
                    p2.setChecked(true);
                }
                state = 2;
                p3.setChecked(false);
                p4.setChecked(false);
                finale.setChecked(false);
            }
        });

        p3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                p1.setChecked(false);
                p2.setChecked(false);
                if(!p3.isChecked()){
                    p3.setChecked(false);
                }else {
                    p3.setChecked(true);
                }
                p4.setChecked(false);
                state = 3;
                finale.setChecked(false);
            }
        });

        p4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                p1.setChecked(false);
                p2.setChecked(false);
                p3.setChecked(false);
                if(!p4.isChecked()){
                    p4.setChecked(false);
                }else {
                    p4.setChecked(true);
                }
                state = 4;
                finale.setChecked(false);
            }
        });

        finale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                p1.setChecked(false);
                p2.setChecked(false);
                p3.setChecked(false);
                p4.setChecked(false);
                if(!finale.isChecked()){
                    finale.setChecked(false);
                }else {
                    finale.setChecked(true);
                }
                state = 5;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!score1.getText().toString().equals("") && !score2.getText().toString().equals(""))
                    new Sent_Data().execute();
                else {
                    Toast toast = Toast.makeText(ScoreEditActivityActivity.this, getString(R.string.empty_score), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

    }


    public class Sent_Data extends AsyncTask < String, String, String > {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ScoreEditActivityActivity.this);
            pDialog.setMessage(getString(R.string.waiting_screen));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String...args) {


            DatabaseHelper myDb = new DatabaseHelper(ScoreEditActivityActivity.this);
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

            url = getString(R.string.server) + "setScore.php?match_id=" + match_id + "&team_score_1=" + score1.getText().toString() + "&team_score_2=" + score2.getText().toString() + "&user_id=" + user_id + "&safe_key=" + safe_key+"&state="+state;

            JSONParser jParser = new JSONParser();

            String st = jParser.getJSONFromUrl(url);

            return st;
        }


        @Override
        protected void onPostExecute(String json) {
            String message = "";
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
                Toast toast = Toast.makeText(ScoreEditActivityActivity.this, getString(R.string.error_code_200), Toast.LENGTH_LONG);
                toast.show();
            } else if (error_code == 201) {
                Toast toast = Toast.makeText(ScoreEditActivityActivity.this, getString(R.string.error_code_201), Toast.LENGTH_LONG);
                toast.show();
            } else if (error_code == 403) {
                Toast toast = Toast.makeText(ScoreEditActivityActivity.this, getString(R.string.error_code_403), Toast.LENGTH_LONG);
                toast.show();
                ScoreEditActivityActivity.this.finishAffinity();
            } else {
                Toast toast = Toast.makeText(ScoreEditActivityActivity.this, getString(R.string.error_code_0), Toast.LENGTH_LONG);
                toast.show();
                ScoreEditActivityActivity.this.finishAffinity();

            }
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}