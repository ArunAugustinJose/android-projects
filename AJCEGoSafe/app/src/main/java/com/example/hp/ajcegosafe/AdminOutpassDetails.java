package com.example.hp.ajcegosafe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.*;
import android.view.Menu;
import android.view.MenuItem;
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

public class AdminOutpassDetails extends AppCompatActivity {

    private ProgressDialog pDialog;
    private SessionManager session;
    Activity context = this;
    private dbHelp db;
    private static final String TAG = "AdminHome";

    TextView t1,t2,t3,t4,t5,t6,t7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_outpass_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Outpass Details");

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        t1 = (TextView)findViewById(R.id.txtAdmno);
        t2 = (TextView) findViewById(R.id.txtName);
        t3 = (TextView) findViewById(R.id.txtTol);
        t4 = (TextView) findViewById(R.id.txtTor);
        t5 = (TextView) findViewById(R.id.txtPur);
        t6 = (TextView) findViewById(R.id.txtDol);
        t7 = (TextView) findViewById(R.id.txtDor);

        String oid = getIntent().getExtras().getString("oid");
        getStudentDetails(oid);


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

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * function to get student detail
     * */
    private void getStudentDetails(final String oid) {


        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Searching..");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADMIN_STUDENT_PROFILE, new Response.Listener<String>() {

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

                        JSONObject student = jObj.getJSONObject("student");
                        String fname = student.getString("fname");
                        String lname = student.getString("lname");
                        String admno = student.getString("admission_no");
                        String purpose = student.getString("purpose");
                        String dob = student.getString("dob");
                        String gender = student.getString("gender");
                        String mobno = student.getString("mobno");
                        String pmobno = student.getString("pmobno");
                        String email = student.getString("email");
                        String batch = student.getString("batch");
                        String stream_name = student.getString("stream_name");
                        String branch_name = student.getString("branch_name");

                        String date_of_leaving = student.getString("date_of_leaving");
                        String date_of_return = student.getString("date_of_return");
                        String time_of_leaving = student.getString("time_of_leaving");
                        String time_of_return = student.getString("time_of_return");

//                        Toast.makeText(getApplicationContext(),
//                                fname+"/"+lname+"/"+address+"/"+dob+"/"+gender+"/"+mobno+"/"+pmobno+"/"+email+"/"+batch+"/"
//                                +stream_id+"/"+branch_id, Toast.LENGTH_LONG).show();
                        t1.setText(admno);
                        t2.setText(fname+" "+lname);
                        t3.setText(time_of_leaving);
                        t4.setText(time_of_return);
                        t5.setText(purpose);
                        t6.setText(date_of_leaving);
                        t7.setText(date_of_return);



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
                params.put("oid", oid);

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
