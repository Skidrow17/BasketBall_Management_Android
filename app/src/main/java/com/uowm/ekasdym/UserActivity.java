package com.uowm.ekasdym;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.uowm.ekasdym.database.DatabaseHelper;
import com.uowm.ekasdym.utilities.JSONParser;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class UserActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView name, profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.add_restriction,R.id.weekly_matches,R.id.rank,R.id.rules,R.id.clarification,R.id.about_ekasdym,
                R.id.about_us,R.id.announcements,R.id.received_messages,R.id.sent_messages,R.id.my_restrictions,
                R.id.make_announcement,R.id.my_matches,R.id.password_change,R.id.image_change)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);

        name = headerView.findViewById(R.id.name);
        name.setText(getIntent().getStringExtra("name"));

        profile = headerView.findViewById(R.id.profile);
        profile.setText(getIntent().getStringExtra("profile"));

        ImageView iv = headerView.findViewById(R.id.profile_pic);

        Glide.with(getApplicationContext())
             .load(getIntent().getStringExtra("profile_pic"))
             .into(iv);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> navController.navigate(R.id.fab));
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.logout_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new PushNotificationDissable().execute();
                        DatabaseHelper myDb = new DatabaseHelper(UserActivity.this);
                        myDb.clearUserTable();
                        Intent i = new Intent(UserActivity.this, GuestActivity.class);
                        startActivity(i);
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            new PushNotificationDissable().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public class PushNotificationDissable extends AsyncTask< String, String, String > {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserActivity.this);
            pDialog.setMessage(getString(R.string.waiting_screen));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String...args) {

            DatabaseHelper myDb = new DatabaseHelper(UserActivity.this);
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
            String url = getString(R.string.server) + "deleteMobileToken.php?id=" + user_id + "&safe_key=" + safe_key;
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
                Toast toast = Toast.makeText(UserActivity.this, getString(R.string.error_code_200), Toast.LENGTH_LONG);
                toast.show();
                DatabaseHelper myDb = new DatabaseHelper(UserActivity.this);
                myDb.clearUserTable();
                Intent i = new Intent(UserActivity.this, GuestActivity.class);
                startActivity(i);
            } else if (error_code == 403) {
                Toast toast = Toast.makeText(UserActivity.this, getString(R.string.error_code_403), Toast.LENGTH_LONG);
                toast.show();
                UserActivity.this.finishAffinity();
            } else if (error_code == 201) {
                Toast toast = Toast.makeText(UserActivity.this, getString(R.string.error_code_201), Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(UserActivity.this, getString(R.string.error_code_0), Toast.LENGTH_LONG);
                toast.show();
                UserActivity.this.finishAffinity();
            }
        }
    }
}
