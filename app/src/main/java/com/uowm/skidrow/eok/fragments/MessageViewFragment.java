package com.uowm.skidrow.eok.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.uowm.skidrow.eok.R;

public class MessageViewFragment extends Fragment {

    TextView text;

    public MessageViewFragment() {
        // Required empty public constructor  
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.read_message, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        text = getActivity().findViewById(R.id.read_text);
        text.setText(getArguments().getString("message"));
    }

} 