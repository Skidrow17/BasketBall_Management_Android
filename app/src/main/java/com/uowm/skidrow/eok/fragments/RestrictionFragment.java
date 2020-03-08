package com.uowm.skidrow.eok.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uowm.skidrow.eok.R;
import com.uowm.skidrow.eok.database.DatabaseHelper;
import com.uowm.skidrow.eok.utilities.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings("deprecation")
public class RestrictionFragment extends Fragment implements OnSelectDateListener {


    private String dates = "";
    private String Date = "";
    private String Time = "",Time2="";
    private String url = " ";
    Calendar dateTime = Calendar.getInstance();
    private TextView text;
    private Button btn_date;
    private Button btn_time;
    private Button btn_time_Till;
    private Button submit;
    private String text_comment;
    private TextView comment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_restriction, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        getActivity();

        text = (TextView) getActivity().findViewById(R.id.txt_TextDateTime);
        btn_date = (Button) getActivity().findViewById(R.id.btn_datePicker);
        btn_time_Till =(Button) getActivity().findViewById(R.id.btn_till_timePicker);
        btn_time =(Button) getActivity().findViewById(R.id.btn_timePicker);
        submit = (Button) getActivity().findViewById(R.id.submit);
        comment = (TextView) getActivity().findViewById(R.id.comment);


        DatePickerBuilder manyDaysBuilder = new DatePickerBuilder(getActivity(), this)
                .pickerType(CalendarView.MANY_DAYS_PICKER)
                .headerColor(R.color.colorPrimary)
                .selectionColor(R.color.colorPrimary)
                .todayLabelColor(R.color.colorPrimary)
                .dialogButtonsColor(R.color.colorPrimary);

        Button openManyDaysPickerDialogButton = getActivity().findViewById(R.id.openManyDaysPickerDialogButton);

        openManyDaysPickerDialogButton.setOnClickListener((View v) -> {
            DatePicker datePicker = manyDaysBuilder.build();
            datePicker.show();
        });


        btn_date.setOnClickListener(v -> updateDate());

        btn_time.setOnClickListener(v -> updateTime());

        btn_time_Till.setOnClickListener(v -> updateTime2());

        submit.setOnClickListener(v -> {
            if (!Date.equals("") && !Time.equals("") && !Time2.equals("")) {
                text_comment = comment.getText().toString().replaceAll(" ", "%20");
                new Sent_Data().execute();
            }
            else {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.complete_the_fields), Toast.LENGTH_LONG);
                toast.show();
            }
        });



    }

    private void updateDate() {
        new DatePickerDialog(getActivity(), d, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateTime() {
        new TimePickerDialog(getActivity(), t, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), true).show();
    }

    private void updateTime2() {
        new TimePickerDialog(getActivity(), t2, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), true).show();
    }


    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(android.widget.DatePicker datePicker, int i, int i1, int i2) {
            dateTime.set(Calendar.YEAR, i);
            dateTime.set(Calendar.MONTH, i1);
            dateTime.set(Calendar.DAY_OF_MONTH, i2);
            Date = new SimpleDateFormat("yyyy-MM-dd").format(dateTime.getTime());
            text.setText("Απο :" + Time + "\nΜέχρι :" + Time2 + "\nΗμερομηνία :" + Date);
        }

    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateTime.set(Calendar.MINUTE, minute);
            Time = new SimpleDateFormat("HH:mm:ss").format(dateTime.getTime());
            text.setText(getString(R.string.from) + " :" + Time + "\n" +
                    getString(R.string.to) + " :" + Time2 +
                    "\n" +
                    getString(R.string.date) + " :" + Date);
        }
    };


    TimePickerDialog.OnTimeSetListener t2 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateTime.set(Calendar.MINUTE, minute);
            Time2 = new SimpleDateFormat("HH:mm:ss").format(dateTime.getTime());
            text.setText(getString(R.string.from) + " :" + Time + "\n" +
                    getString(R.string.to) + " :" + Time2 +
                    "\n" +
                    getString(R.string.date) + " :" + Date);

        }
    };



    @Override
    public void onSelect(List < Calendar > calendars) {


        for (int i = 0; i < calendars.size(); i++) {
            String s = new SimpleDateFormat("yyyy-MM-dd").format(calendars.get(i).getTime());

            dates = dates + "/" + s;
        }


        new Sent_Multiple_Dates().execute();
    }



    public class Sent_Data extends AsyncTask < String, String, String > {
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
            url = getString(R.string.server) + "setRestriction.php?date=" + Date + "&time=" + Time + "&time2=" + Time2 + "&user_id=" + user_id + "&safe_key=" + safe_key+"&comment="+text_comment;

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
            } else if (error_code == 403) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_403), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();
            } else if(error_code == 202){
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_202), Toast.LENGTH_LONG);
                toast.show();
            }else {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_0), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();
            }

            Time = "";
            Time2 = "";
            Date = "";
            text.setText("");


        }
    }



    public class Sent_Multiple_Dates extends AsyncTask< String, String, String > {
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
            url = getString(R.string.server) + "setMultipleRestriction.php?date='" + dates + "'&user_id=" + user_id + "&safe_key=" + safe_key+"&comment="+comment.getText().toString();

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
            } else if (error_code == 403) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_403), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();
            } else if(error_code == 202){
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_202), Toast.LENGTH_LONG);
                toast.show();
            }else {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_0), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();

            }
        }
    }
}