package com.uowm.ekasdym.fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.uowm.ekasdym.R;
import com.uowm.ekasdym.database.DatabaseHelper;
import com.uowm.ekasdym.utilities.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordChangeFragment extends Fragment {

    private String url;
    private Button submit;
    private EditText password, repeat, security_key;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.password_image_change, container, false);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        getActivity();
        new Password_Change_Request().execute();
        submit = getActivity().findViewById(R.id.sent);
        password = getActivity().findViewById(R.id.password);
        repeat = getActivity().findViewById(R.id.repeat_password);
        security_key = getActivity().findViewById(R.id.security_key);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(repeat.getText().toString()) && !security_key.getText().toString().equals("") && !password.getText().toString().equals("")){
                    new  Password_Change_Submit().execute();
                }else{
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.password_doesnt_match), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

    }


    public class Password_Change_Request extends AsyncTask < String, String, String > {
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
        protected String doInBackground(String...args) {

            DatabaseHelper myDb = new DatabaseHelper(getActivity());
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

            url =  getString(R.string.server)+ "passwordChangeRequest.php?id=" + user_id + "&safe_key=" + safe_key;
            JSONParser jParser = new JSONParser();
            String st = jParser.getJSONFromUrl(url);

            return st;
        }


        @Override
        protected void onPostExecute(String json) {
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
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_200), Toast.LENGTH_LONG);
                toast.show();
            } else if (error_code == 201) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_201), Toast.LENGTH_LONG);
                toast.show();
            } else if (error_code == 206) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_206), Toast.LENGTH_LONG);
                toast.show();
            }else if (error_code == 403) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_403), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();
            } else {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_0), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();
            }
        }
    }

    public class Password_Change_Submit extends AsyncTask < String, String, String > {
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
        protected String doInBackground(String...args) {

            DatabaseHelper myDb = new DatabaseHelper(getActivity());
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
            url = getString(R.string.server) + "passwordChange.php?id=" + user_id + "&safe_key=" + safe_key + "&password="+password.getText().toString()+"&recovery_code="+security_key.getText().toString();

            JSONParser jParser = new JSONParser();

            String st = jParser.getJSONFromUrl(url);

            return st;
        }


        @Override
        protected void onPostExecute(String json) {
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
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_200), Toast.LENGTH_LONG);
                toast.show();
            } else if (error_code == 201) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_201), Toast.LENGTH_LONG);
                toast.show();
            } else if (error_code == 406) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_406), Toast.LENGTH_LONG);
                toast.show();
            }else if (error_code == 403) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_403), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();
            } else {
                Toast toast = Toast.makeText(getActivity(), error_code+" "+getString(R.string.error_code_0), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();
            }
        }
    }
}