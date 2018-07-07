package com.example.hp.ajcegosafe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.*;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StudentOutpassHistory extends AppCompatActivity {

    private ProgressDialog pDialog;
    private SessionManager session;
    Activity context = this;
    private dbHelp db;
    private static final String TAG = "AdminHome";
    ArrayList<OutpassHistory> outpassProfile = new ArrayList<OutpassHistory>();
    int JSON_SIZE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_outpass_history);
        setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView=(ListView)findViewById(R.id.listView1);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        String admno = session.getValue(context,"admno");
        getDetails(admno);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                int pos=position+1;
                Toast.makeText(StudentOutpassHistory.this, Integer.toString(pos)+" Clicked", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * function to get student outpass detail
     * */
    private void getDetails(final String admno) {


        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Loading..");
        showDialog();

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_FETCH_OUTPASS_HISTORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                android.util.Log.d(TAG, "Login Response: " + response1.toString());
                hideDialog();

                try {
                    JSONArray jarray = new JSONArray(response1);
                    JSON_SIZE = jarray.length();
                }
                catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }


                try {


                for (int i = 0; i < JSON_SIZE; i++) {
                    JSONArray jarray = new JSONArray(response1);
                    JSONObject jObj = jarray.getJSONObject(i);

                            OutpassHistory outpass = new OutpassHistory();
                            outpass.setDate(jObj.getString("date_of_leaving"));
                            outpass.setPurpose(jObj.getString("purpose"));
                            outpass.setStatus(jObj.getString("status"));

                            outpassProfile.add(outpass);

                        }


                }
                catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                ListView listView=(ListView)findViewById(R.id.listView1);

                OutpassHistorypage_listAdapter adapter = new OutpassHistorypage_listAdapter(context,R.layout.colmn_row,outpassProfile);
                listView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                android.util.Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("admno", admno);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
