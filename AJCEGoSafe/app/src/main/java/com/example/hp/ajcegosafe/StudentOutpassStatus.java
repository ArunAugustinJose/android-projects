package com.example.hp.ajcegosafe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.*;
import android.view.MenuItem;
import android.widget.AbsoluteLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StudentOutpassStatus extends AppCompatActivity {

    private ProgressDialog pDialog;
    private SessionManager session;
    Activity context = this;
    private dbHelp db;
    private static final String TAG = "AdminHome";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_outpass_status);
        setTitle("Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        String admno = session.getValue(context,"admno");

        getOutpassStatus(admno);

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
     * function to get status of last outpass request
     * */
    private void getOutpassStatus(final String admno) {


        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_OUTPASS_STATUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                android.util.Log.d(TAG, "Login Response: " + response1.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response1);
                    boolean error = jObj.  getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        // Now store the user in SQLite

                        JSONObject outpass_status = jObj.getJSONObject("outpass_status");
                        String st = outpass_status.getString("status");

                        TextView t1 = (TextView) findViewById(R.id.txtStatus);

                        Integer status = Integer.parseInt(st);
                        if (status == 0){
                            t1.setText("Requested");
                            t1.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        else if (status == 1){
                            t1.setText("Approved");
                        }
                        else if (status == 2){
                            t1.setText("Rejected");
                        }
                        else if (status == 3){
                            t1.setText("Went Out");
                        }
                        else if (status == 4){
                            t1.setText("Returned");
                        }
                        else if (status == 5){
                            t1.setText("Late");
                            t1.setTextColor(Color.parseColor("#FFFFFF"));
                        }



                    } else {
                        // Error in login. Get the error message
                        String errorMsg1 = jObj.getString("error_msg1");
                        Toast.makeText(getApplicationContext(),
                                errorMsg1, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

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
