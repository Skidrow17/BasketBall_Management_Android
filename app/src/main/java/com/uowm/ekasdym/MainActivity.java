package com.uowm.ekasdym;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.uowm.ekasdym.database.DatabaseHelper;
import com.uowm.ekasdym.utilities.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    public static DatabaseHelper myDb;
    public static int idi;
    public static String profile;
    public static String safe_key;
    private String username_filtered, password_filtered, device_name_filtered;
    String device_name;
    String name;
    String surname;
    String username;
    String password;
    String profile_pic;
    String url;
    String id,safety;
    boolean version_error;

    int number_of_messages, number_of_announcements;
    int active, polling_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);
        device_name = Build.MODEL;
        myDb = new DatabaseHelper(MainActivity.this);

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Cursor res = myDb.getAllData();
            if (res.getCount() == 0) {
                startActivity(new Intent(getApplicationContext(), GuestActivity.class));
                finish();
                overridePendingTransition(0, 0);
            } else {

                while (res.moveToNext()) {
                    username = res.getString(1);
                    password = res.getString(2);
                    id = res.getString(3);
                    safety = res.getString(5);
                }
                if (version_error == false)
                    new Login().execute();
            }
        }, 2000);


    }


    public class Login extends AsyncTask < String, String, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String...args) {

            username_filtered = username.replaceAll(" ", "%20");
            password_filtered = password.replaceAll(" ", "%20");
            device_name_filtered = device_name.replaceAll(" ", "%20");
            url = getString(R.string.server) + "login.php?username=" + username_filtered + "&password=" + password_filtered + "&device_name=" + device_name_filtered+"&safe_key="+safety+"&id="+id+"&mobile_token="+FirebaseInstanceId.getInstance().getToken();
            JSONParser jParser = new JSONParser();
            String st = jParser.getJSONFromUrl(url);
            return st;
        }


        @Override
        protected void onPostExecute(String json) {

            JSONObject jobj;
            String error_code = "";

            try {
                jobj = new JSONObject(json);
                JSONObject jobj4 = jobj.getJSONObject("ERROR");
                error_code = jobj4.getString("error_code");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (error_code.equals("401")) {
                Toast toast = Toast.makeText(MainActivity.this, getString(R.string.error_code_401), Toast.LENGTH_LONG);
                toast.show();
                Intent i = new Intent(MainActivity.this, GuestActivity.class);
                startActivity(i);
            } else if (error_code.equals("402")) {
                Toast toast = Toast.makeText(MainActivity.this, getString(R.string.error_code_402), Toast.LENGTH_LONG);
                toast.show();
                Intent i = new Intent(MainActivity.this, UserActivity.class);
                startActivity(i);
            } else if(error_code.equals("200")) {
                try {
                    jobj = new JSONObject(json);
                    JSONObject userInfo = jobj.getJSONObject("user");
                    JSONObject securityInfo = jobj.getJSONObject("safe_key");

                    safe_key = securityInfo.getString("key");
                    number_of_messages = userInfo.getInt("nom");
                    number_of_announcements = userInfo.getInt("noa");
                    idi = userInfo.getInt("id");
                    name = userInfo.getString("name");
                    username = userInfo.getString("username");
                    password = userInfo.getString("password");
                    surname = userInfo.getString("surname");
                    profile_pic = userInfo.getString("profile_pic");
                    profile = userInfo.getString("profession");
                    active = userInfo.getInt("active");
                    polling_time = userInfo.getInt("polling_time");

                    myDb = new DatabaseHelper(MainActivity.this);
                    Cursor res = myDb.getAllData();

                    if (res.getCount() == 0)
                        myDb.insertData("1", username, password, idi + "", number_of_messages, safe_key, number_of_announcements, polling_time);
                    else
                        myDb.updateData("1", username, password, idi + "", number_of_messages, safe_key, number_of_announcements, polling_time);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (active == 1) {
                    Toast toast = Toast.makeText(MainActivity.this, getString(R.string.inactive_account), Toast.LENGTH_LONG);
                    toast.show();
                } else {

                    Toast toast = Toast.makeText(MainActivity.this, getString(R.string.correct_user), Toast.LENGTH_LONG);
                    toast.show();
                    if (profile.equals("Admin")) {
                        Intent i = new Intent(MainActivity.this, AdminActivity.class);
                        i.putExtra("name", name + " " + surname);
                        i.putExtra("profile_pic", getString(R.string.image_server) + profile_pic);
                        i.putExtra("profile", profile);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(MainActivity.this, UserActivity.class);
                        i.putExtra("name", name + " " + surname);
                        i.putExtra("profile_pic", getString(R.string.image_server) + profile_pic);
                        i.putExtra("profile", profile);
                        startActivity(i);
                    }
                }
            }
        }
    }
}