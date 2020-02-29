package com.uowm.ekasdym.activities;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.uowm.ekasdym.R;


public class AnnouncementShowActivity extends AppCompatActivity {

    TextView text;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_show);
        text = findViewById(R.id.password);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);


        Intent intent = getIntent();
        String announcement = intent.getStringExtra("text");
        String title = intent.getStringExtra("title");
        text.setText(announcement);


        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>" + title + "</font>"));

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}