package com.uowm.skidrow.eok.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.uowm.skidrow.eok.R;
import com.uowm.skidrow.eok.adapters.MessageAdapter;
import com.uowm.skidrow.eok.database.DatabaseHelper;
import com.uowm.skidrow.eok.model.ChatMessage;
import com.uowm.skidrow.eok.utilities.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageActivityActivity extends AppCompatActivity {

    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn;
    private MessageAdapter adapter;
    private ArrayList<ChatMessage> chatHistory = new ArrayList<ChatMessage>();
    private int buddy;
    private static final String MESSAGE = "chat_message";
    private String url = "";
    public String messageText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_on_activity);
        Intent intent = getIntent();
        buddy = intent.getIntExtra("id", 0);
        String name_surname = intent.getStringExtra("name_surname");
        new JSONParse().execute();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF' style=\"text-align:center;\">" + name_surname + "</font>"));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_load) {
            new JSONParse().execute();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        //companionLabel.setText("My Buddy");

        loadDummyHistory();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }
                new MessageSend().execute();

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(122);//dummy
                chatMessage.setMessage(messageText);
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMe(false);
                displayMessage(chatMessage);
            }
        });


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

        adapter = new MessageAdapter(MessageActivityActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        for(int i=0; i<chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public class JSONParse extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MessageActivityActivity.this);
            pDialog.setMessage(getString(R.string.waiting_screen));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {

            DatabaseHelper myDb = new DatabaseHelper(MessageActivityActivity.this);
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
                        String set_me = obj.getString("set_me");
                        String text_message = obj.getString("text_message");
                        String date_time = obj.getString("date_time");

                        ChatMessage msg = new ChatMessage();
                        msg.setId(id);
                        msg.setMe(Boolean.parseBoolean(set_me));
                        msg.setMessage(text_message);
                        msg.setDate(date_time);
                        chatHistory.add(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                initControls();

            }
            else if (error_code==403)
            {
                Toast toast = Toast.makeText(MessageActivityActivity.this, getString(R.string.error_code_403), Toast.LENGTH_LONG);
                toast.show();
                finishAffinity();
            }
            else if (error_code==204)
            {
                Toast toast = Toast.makeText(MessageActivityActivity.this, getString(R.string.error_code_204), Toast.LENGTH_LONG);
                toast.show();
                initControls();
            }
            else
            {
                Toast toast = Toast.makeText(MessageActivityActivity.this, getString(R.string.error_code_0), Toast.LENGTH_LONG);
                toast.show();
                finishAffinity();
            }
        }
    }

    public class MessageSend extends AsyncTask < String, String, String > {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MessageActivityActivity.this);
            pDialog.setMessage(getString(R.string.waiting_screen));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String...args) {

            DatabaseHelper myDb = new DatabaseHelper(MessageActivityActivity.this);
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

            url = getString(R.string.server) + "sentMessage.php?sender_id=" + user_id + "&receiver_id=" + buddy + "&text_message=" + messageText + "&safe_key=" + safe_key;
            JSONParser jParser = new JSONParser();
            String st = jParser.getJSONFromUrl(url);

            return st;
        }


        @Override
        protected void onPostExecute(String json) {
            final Handler handler = new Handler();
            handler.postDelayed(() -> pDialog.dismiss(), 500);
            messageET.setText("");
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
                Toast toast = Toast.makeText(MessageActivityActivity.this, getString(R.string.error_code_200), Toast.LENGTH_LONG);
                toast.show();
            } else if (error_code == 403) {
                Toast toast = Toast.makeText(MessageActivityActivity.this, getString(R.string.error_code_403), Toast.LENGTH_LONG);
                toast.show();
                MessageActivityActivity.this.finishAffinity();
            } else if (error_code == 201) {
                Toast toast = Toast.makeText(MessageActivityActivity.this, getString(R.string.error_code_201), Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(MessageActivityActivity.this, getString(R.string.error_code_0), Toast.LENGTH_LONG);
                toast.show();
                MessageActivityActivity.this.finishAffinity();
            }
        }
    }
}
