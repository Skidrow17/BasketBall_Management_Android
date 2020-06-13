package com.uowm.skidrow.eok.fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import com.uowm.skidrow.eok.R;
import com.uowm.skidrow.eok.adapters.ChatAdapter;
import com.uowm.skidrow.eok.database.DatabaseHelper;
import com.uowm.skidrow.eok.model.ChatMessage;
import com.uowm.skidrow.eok.utilities.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessageHistoryFragment extends ListFragment {


    private ListView messagesContainer;
    private ChatAdapter adapter;
    private int buddy;
    private ArrayList<ChatMessage> chatHistory = new ArrayList<ChatMessage>();
    private static final String MESSAGE = "chat_message";
    private String url = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_chat, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buddy = getArguments().getInt("sender_id");
        new JSONParse().execute();

        initControls();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initControls() {
        messagesContainer = (ListView) getActivity().findViewById(android.R.id.list);
        RelativeLayout container = (RelativeLayout) getActivity().findViewById(R.id.container);

        loadDummyHistory();
    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadDummyHistory(){
        adapter = new ChatAdapter(getActivity(), new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        for(int i=0; i<chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }
    }


    public class JSONParse extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {

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

            url = getString(R.string.server)+"chatMessages.php?id="+user_id+"&safe_key="+safe_key+"&buddy="+buddy;
            JSONParser jParser = new JSONParser();
            String st =jParser.getJSONFromUrl(url);

            return st;
        }


        @Override
        protected void onPostExecute(String json) {

            chatHistory.clear();

            int error_code=0;
            JSONObject jobj = null;


            try {
                jobj = new JSONObject(json);
                JSONObject jobj4 = jobj.getJSONObject("ERROR");
                error_code=jobj4.getInt("error_code");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(error_code==200) {
                try {

                    JSONArray jsonArray = jobj.getJSONArray(MESSAGE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        int id = obj.getInt("id");
                        String set_me = obj.getString("set_me");
                        String text_message = obj.getString("text_message");
                        String date_time = obj.getString("date_time");

                        PrettyTime p = new PrettyTime();
                        p.setLocale(Locale.ENGLISH);
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date_time);

                        String hourInGreek = p.format(date).replace("ago",getString(R.string.ago));
                        hourInGreek = hourInGreek.replace("years",getString(R.string.years));
                        hourInGreek = hourInGreek.replace("days",getString(R.string.days));
                        hourInGreek = hourInGreek.replace("minutes",getString(R.string.minutes));
                        hourInGreek = hourInGreek.replace("hours",getString(R.string.hours));
                        hourInGreek = hourInGreek.replace("moments",getString(R.string.moments));
                        hourInGreek = hourInGreek.replace("months",getString(R.string.months));

                        hourInGreek = hourInGreek.replace("hour",getString(R.string.hour));
                        hourInGreek = hourInGreek.replace("month",getString(R.string.month));
                        hourInGreek = hourInGreek.replace("minute",getString(R.string.minute));
                        hourInGreek = hourInGreek.replace("year",getString(R.string.year));
                        hourInGreek = hourInGreek.replace("day",getString(R.string.day));

                        ChatMessage msg = new ChatMessage();
                        msg.setId(id);
                        msg.setMe(Boolean.parseBoolean(set_me));
                        msg.setMessage(text_message);
                        msg.setDate(hourInGreek);
                        chatHistory.add(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                initControls();

            }
            else if (error_code==403)
            {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_403), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();
            }
            else if (error_code==204)
            {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_204), Toast.LENGTH_LONG);
                toast.show();
            }
            else
            {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_0), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();
            }
        }
    }

}