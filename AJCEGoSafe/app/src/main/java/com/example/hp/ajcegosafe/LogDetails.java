package com.example.hp.ajcegosafe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LogDetails extends AppCompatActivity {

    ArrayList<LogDetailsContent> logProfile = new ArrayList<LogDetailsContent>();
    LogDetailsContent log;
    ListView listView;

    private ProgressDialog pDialog;
    private SessionManager session;
    Activity context = this;
    private dbHelp db;
    private static final String TAG = "AdminHome";
    int JSON_SIZE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Log Details");


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        getDetails();



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
    private void getDetails() {


        // Tag used to cancel the request
        String tag_string_req = "req_login";

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ALL_STUDENT_LOGINS, new Response.Listener<String>() {

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

                        LogDetailsContent logc = new LogDetailsContent();
                        logc.setAdno(jObj.getString("admission_no"));
                        logc.setPassword(jObj.getString("password"));
                        String fname = jObj.getString("fname");
                        String lname = jObj.getString("lname");
                        logc.setName(fname+" "+lname);
                        logProfile.add(logc);

                    }


                }
                catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                listView=(ListView)findViewById(R.id.listView1);

                LogDetaild_listAdapter adapter = new LogDetaild_listAdapter(context,R.layout.colmn_row,logProfile);
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
