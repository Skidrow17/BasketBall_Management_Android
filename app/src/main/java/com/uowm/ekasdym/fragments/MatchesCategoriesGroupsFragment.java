package com.uowm.ekasdym.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.uowm.ekasdym.R;
import com.uowm.ekasdym.adapters.CategoryGroupListAdapter;
import com.uowm.ekasdym.model.TeamCategoryGroup;
import com.uowm.ekasdym.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MatchesCategoriesGroupsFragment extends ListFragment {

    private String url = "";
    private static final String ARRAY_NAME = "Category_Group";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_item, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        new JSONParse().execute();

    }


    public class JSONParse extends AsyncTask<String, String, String> {
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


            url = getString(R.string.server)+"getCategoryGroups.php";

            JSONParser jParser = new JSONParser();

            String st =jParser.getJSONFromUrl(url);

            return st;
        }


        @Override
        protected void onPostExecute(String json) {

            ArrayList<TeamCategoryGroup> categories_group = new ArrayList<>();


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pDialog.dismiss();
                }
            }, 500);

            int error_code = 0;
            JSONObject jobj = null;

            try {
                jobj = new JSONObject(json);
                JSONObject errorObject = jobj.getJSONObject("ERROR");
                error_code = errorObject.getInt("error_code");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (error_code == 200) {

                try {
                    JSONArray jsonArray = jobj.getJSONArray(ARRAY_NAME);
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject obj = jsonArray.getJSONObject(i);
                        int categoryId = obj.getInt("categoryId");
                        String categoryName = obj.getString("categoryName");
                        int groupId = obj.getInt("groupId");
                        String groupName = obj.getString("groupName");

                        TeamCategoryGroup category_group = new  TeamCategoryGroup(categoryId,categoryName,groupId,groupName);
                        categories_group.add(category_group);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final ListView listView =  getActivity().findViewById(android.R.id.list);
                listView.setAdapter(new CategoryGroupListAdapter(getActivity(), categories_group));
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