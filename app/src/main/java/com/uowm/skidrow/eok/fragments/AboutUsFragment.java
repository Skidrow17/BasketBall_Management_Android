package com.uowm.skidrow.eok.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uowm.skidrow.eok.R;

import java.util.Calendar;
import java.util.List;

@SuppressWarnings("deprecation")
public class AboutUsFragment extends Fragment implements OnSelectDateListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
    }

    @Override
    public void onSelect(List<Calendar> calendar) {

    }
}