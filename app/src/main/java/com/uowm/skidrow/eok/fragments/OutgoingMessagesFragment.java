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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uowm.skidrow.eok.R;
import com.uowm.skidrow.eok.activities.MessageShowActivityActivity;
import com.uowm.skidrow.eok.adapters.MessageReceiveAdapter;
import com.uowm.skidrow.eok.adapters.MessageSentAdapter;
import com.uowm.skidrow.eok.database.DatabaseHelper;
import com.uowm.skidrow.eok.model.Message;
import com.uowm.skidrow.eok.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OutgoingMessagesFragment extends ListFragment
{

    private String url = "";
    private TextView tv;
    private int global_id;
    private static final String MESSAGE = "Sent_Messages";
    ArrayList<Message> messages = new ArrayList<Message>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_item,container,false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        new JSONParse().execute();
        getActivity();

    }


    public class JSONParse extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getString(R.string.waiting_screen));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

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

            url = getString(R.string.server)+"getSentMessages.php?id="+user_id+"&safe_key="+safe_key;
            JSONParser jParser = new JSONParser();
            String st =jParser.getJSONFromUrl(url);

            return st;
        }


        @Override
        protected void onPostExecute(String json) {

            messages.clear();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pDialog.dismiss();
                }
            }, 500);

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
                    String name = obj.getString("name");
                    String surname=obj.getString("surname");
                    String text_message = obj.getString("text_message");
                    String profile_pic = obj.getString("profile_pic");
                    String date_time = obj.getString("date_time");
                    int receiver_id = obj.getInt("receiver_id");
                    int message_status = obj.getInt("message_read");
                    String st = "";
                    if(message_status==1)
                        st = getString(R.string.read);
                    else
                        st = getString(R.string.unread);

                    Message message = new Message(id,name,surname,text_message,getString(R.string.image_server)+profile_pic,date_time,st,receiver_id);
                    messages.add(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
                final ListView listView = getActivity().findViewById(android.R.id.list);
                registerForContextMenu(listView);
                listView.setAdapter(new MessageSentAdapter(OutgoingMessagesFragment.this.getActivity(), messages));

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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Intent intent =new Intent(getActivity(), MessageShowActivityActivity.class);
        intent.putExtra("name_surname",messages.get(position).getName()+" "+messages.get(position).getSurname());
        intent.putExtra("message",messages.get(position).getMessage());
        intent.putExtra("sender_id", messages.get(position).getSender_id());
        intent.putExtra("class_name","OutgoingMessagesFragment");
        startActivity(intent);



    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(getString(R.string.menu));
        menu.add(0, v.getId(), 0, getString(R.string.delete));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        global_id = messages.get(info.position).getId();
        new Delete_Message().execute();
        return true;
    }


    public class Delete_Message extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getString(R.string.waiting_screen));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

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

            url = getString(R.string.server) + "delete_message.php?user_id=" + user_id + "&safe_key=" + safe_key+"&message_id="+global_id+"&type=outgoing";

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
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_200), Toast.LENGTH_LONG);
                toast.show();
                for(int j = 0; j < messages.size(); j++)
                {
                    if(messages.get(j).getId()==global_id){
                        messages.remove(j);
                        break;
                    }
                }

                final ListView listView = getActivity().findViewById(android.R.id.list);
                listView.setAdapter(new MessageReceiveAdapter(OutgoingMessagesFragment.this.getActivity(), messages));

            } else if (error_code == 403) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_403), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();
            } else if (error_code == 204) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_204), Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.error_code_0), Toast.LENGTH_LONG);
                toast.show();
                getActivity().finishAffinity();
            }
        }
    }

}
