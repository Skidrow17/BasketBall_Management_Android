package com.uowm.ekasdym.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.uowm.ekasdym.R;
import com.uowm.ekasdym.UserActivity;
import com.uowm.ekasdym.database.DatabaseHelper;
import com.uowm.ekasdym.utilities.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings( "deprecation" )
public class LoginFragment extends Fragment {

    private String profile_pic;
    private String name;
    private String surname;
    private String profile;
    private int active;
    private String username_filtered, password_filtered, device_name_filtered;
    private String device_name;
    private EditText uname, pword;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        super.onCreate(savedInstanceState);

        Button member = getActivity().findViewById(R.id.button);
        uname = getActivity().findViewById(R.id.editText);
        pword = getActivity().findViewById(R.id.editText2);
        device_name = android.os.Build.MODEL;



        member.setOnClickListener(v -> {
            String temp1 = uname.getText().toString();
            String temp2 = pword.getText().toString();
            username_filtered = temp1.replaceAll(" ", "%20");
            password_filtered = temp2.replaceAll(" ", "%20");
            device_name_filtered = device_name.replaceAll(" ", "%20");

            if(uname.getText().toString().equals("") && pword.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.complete_the_fields), Toast.LENGTH_LONG);
                toast.show();
            }else
                new LoginService().execute();
        });

    }

    public class LoginService extends AsyncTask< String, String, String > {
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
        protected String doInBackground(String... args) {

            String url = getString(R.string.server) + "login.php?username=" + username_filtered + "&password=" + password_filtered + "&device_name=" + device_name_filtered+"&mobile_token="+ FirebaseInstanceId.getInstance().getToken();
            JSONParser jParser = new JSONParser();
            String st = jParser.getJSONFromUrl(url);

            return st;
        }


        @Override
        protected void onPostExecute(String json) {

            pDialog.dismiss();
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
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_401), Toast.LENGTH_LONG);
                toast.show();
            } else if (error_code.equals("402")) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_402), Toast.LENGTH_LONG);
                toast.show();
            } else if (error_code.equals("200")) {
                try {
                    jobj = new JSONObject(json);
                    JSONObject userInfo = jobj.getJSONObject("user");
                    JSONObject securityInfo = jobj.getJSONObject("safe_key");

                    String safe_key = securityInfo.getString("key");
                    int number_of_messages = userInfo.getInt("nom");
                    int number_of_announcements = userInfo.getInt("noa");
                    int idi = userInfo.getInt("id");
                    name = userInfo.getString("name");
                    String username = userInfo.getString("username");
                    String password = userInfo.getString("password");
                    surname = userInfo.getString("surname");
                    profile_pic = userInfo.getString("profile_pic");
                    profile = userInfo.getString("profession");
                    active = userInfo.getInt("active");
                    int polling_time = userInfo.getInt("polling_time");

                    DatabaseHelper myDb = new DatabaseHelper(getActivity());
                    Cursor res = myDb.getAllData();

                    if (res.getCount() == 0)
                        myDb.insertData("1", username, password, idi + "", number_of_messages, safe_key, number_of_announcements, polling_time);
                    else
                        myDb.updateData("1", username, password, idi + "", number_of_messages, safe_key, number_of_announcements, polling_time);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (active == 1) {
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.inactive_account), Toast.LENGTH_LONG);
                    toast.show();
                } else {

                    Toast toast = Toast.makeText(getActivity(), getString(R.string.correct_user), Toast.LENGTH_LONG);
                    toast.show();

                    if (profile.equals("Admin")) {
                        Intent i = new Intent(getActivity(), UserActivity.class);
                        i.putExtra("name", name + " " + surname);
                        i.putExtra("profile_pic", getString(R.string.image_server) + profile_pic);
                        i.putExtra("profile", profile);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(getActivity(), UserActivity.class);
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



