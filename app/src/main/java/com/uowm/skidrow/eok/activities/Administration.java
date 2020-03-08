package com.uowm.skidrow.eok.activities;


import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.uowm.skidrow.eok.R;
import com.uowm.skidrow.eok.database.DatabaseHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.uowm.skidrow.eok.MainActivity.myDb;


public class Administration extends AppCompatActivity {

    public WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.webview);
        mWebView = findViewById(R.id.webview);
        String username = "";
        String password = "";
        String safety = "";
        myDb = new DatabaseHelper(Administration.this);
        Cursor res = myDb.getAllData();
        if (res.getCount() != 0) {

            while (res.moveToNext()) {
                username = res.getString(1);
                password = res.getString(2);
                safety = res.getString(5);
            }
        }
        String url = getString(R.string.image_server) + "/php/webview_login.php";
        String postData = "";
        try {
            postData = "username=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&safe_key=" + URLEncoder.encode(safety, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mWebView.postUrl(url, postData.getBytes());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}