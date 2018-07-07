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
import android.widget.TextView;
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

public class AdminNotification extends AppCompatActivity {

    ArrayList<OutpassHistoryAdmin> outpassProfile = new ArrayList<OutpassHistoryAdmin>();

    private ProgressDialog pDialog;
    private SessionManager session;
    Activity context = this;
    private dbHelp db;
    private static final String TAG = "AdminHome";
    int JSON_SIZE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification);
        setTitle("Notification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        outpassHistory();



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
    private void outpassHistory() {


        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Loading..");
        showDialog();

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADMIN_NOTIFICATION, new Response.Listener<String>() {

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

                        OutpassHistoryAdmin history = new OutpassHistoryAdmin();
                        history.setDate(jObj.getString("date_of_leaving"));
                        history.setPurpose(jObj.getString("purpose"));
                        history.setStatus(jObj.getString("status"));
                        history.setAdmno(jObj.getString("admission_no"));
                        history.setOid(jObj.getString("outpass_id"));

                        outpassProfile.add(history);

                    }


                }
                catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                ListView listView=(ListView)findViewById(R.id.listView1);

                OutpassHistorypage_Admin_listAdapter adapter = new OutpassHistorypage_Admin_listAdapter(context,R.layout.admin_outpass_column_row,outpassProfile);
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

