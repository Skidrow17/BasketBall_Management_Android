package com.uowm.skidrow.eok.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uowm.skidrow.eok.R;
import com.uowm.skidrow.eok.activities.MessageActivityActivity;
import com.uowm.skidrow.eok.activities.MessageShowActivityActivity;
import com.uowm.skidrow.eok.adapters.MessageReceiveAdapter;
import com.uowm.skidrow.eok.database.DatabaseHelper;
import com.uowm.skidrow.eok.model.Message;
import com.uowm.skidrow.eok.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageHistoryFragment extends ListFragment {

    private String url = "";
    private static final String MESSAGE = "Received_Messages";
    private int global_position;
    private int global_id;
    ArrayList < Message > messages = new ArrayList<Message>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_item_tab, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}