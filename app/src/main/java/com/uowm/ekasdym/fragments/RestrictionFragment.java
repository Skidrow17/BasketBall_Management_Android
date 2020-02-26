package com.uowm.ekasdym.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uowm.ekasdym.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings("deprecation")
public class RestrictionFragment extends Fragment implements OnSelectDateListener {

    TextView tv;
    private String dates = "";
    private String Date = "";
    private String Time = "",Time2="";
    private String url = " ";
    DateFormat formatDateTime = DateFormat.getDateTimeInstance();
    Calendar dateTime = Calendar.getInstance();
    private TextView text;
    private Button btn_date;
    private Button btn_time;
    private Button btn_time_Till;
    private Button submit;
    AlertDialog.Builder builder;

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

    }

    @Override
    public void onSelect(List<Calendar> calendar) {
        Toast toast = Toast.makeText(getActivity(), "Επιλέξτε όλα τα πεδία", Toast.LENGTH_LONG);
        toast.show();
    }
}