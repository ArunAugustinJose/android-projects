package com.example.hp.ajcegosafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.*;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class StudentLateReturn extends AppCompatActivity {

    TextView t1,t2;
    RadioGroup permission;
    RadioButton radio;

    private ProgressDialog pDialog;
    private static final String TAG = "AdminHome";
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_late_return);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Late Note");

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        t1 = (TextView) findViewById(R.id.txtLateAdno);
        t2 = (TextView) findViewById(R.id.txtLateName);
        Button b = (Button) findViewById(R.id.btnLateSubmit);
        permission = (RadioGroup)findViewById(R.id.radioPermission);

        final String oid = getIntent().getExtras().getString("oid");

        getStudentDetails(oid);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = permission.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radio = (RadioButton) findViewById(selectedId);

                if (permission.getCheckedRadioButtonId() == -1)
                {
                    if (permission.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(StudentLateReturn.this, " Please choose one item", Toast.LENGTH_SHORT).show();
                        permission.requestFocus();
                    }
                }

                String permission = radio.getText().toString();

                if (permission.equals("Have Permission")){

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                    String formattedDate = df.format(c.getTime());

                    changeStatusToFour(oid, formattedDate);

//                    Snackbar.make(v, name + " Return back.", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    Toast.makeText(StudentLateReturn.this, " have permssion", Toast.LENGTH_SHORT).show();
                }

                else{
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                    String formattedDate = df.format(c.getTime());

                    changeStatusToFive(oid, formattedDate);
//                    Snackbar.make(v, name + " Return back.", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                    Toast.makeText(StudentLateReturn.this, " Don't have permission", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(StudentLateReturn.this, AdminOutpassDetails.class);
                startActivity(intent);

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
    private void getStudentDetails(final String oid) {


        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Updating..");
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
                        String purpose = student.getString("purpose");
                        String admno = student.getString("admission_no");
                        String gender = student.getString("gender");
                        String mobno = student.getString("mobno");
                        String pmobno = student.getString("pmobno");
                        String email = student.getString("email");
                        String batch = student.getString("batch");
                        String stream_name = student.getString("stream_name");
                        String branch_name = student.getString("branch_name");
//                        Toast.makeText(getApplicationContext(),
//                                fname+"/"+lname+"/"+address+"/"+dob+"/"+gender+"/"+mobno+"/"+pmobno+"/"+email+"/"+batch+"/"
//                                +stream_id+"/"+branch_id, Toast.LENGTH_LONG).show();
                        t1.setText(admno);
                        t2.setText(fname+" "+lname);


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



    public void changeStatusToFour(final String oid, final String ltime){

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_STATUS_TO_FOUR, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                android.util.Log.d(TAG, "Login Response: " + response1.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response1);
                    boolean error = jObj.  getBoolean("error");

                    // Check for error node in json
//                    if (!error) {
//                        Snackbar snackbar = Snackbar
//                                .make(contactsVIew, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);
//
//                        snackbar.show();
//
//
//
//                    }
// else {
//                        // Error in login. Get the error message
//                        String errorMsg1 = jObj.getString("error_msg1");
//                        Toast.makeText(context,
//                                "Student Returned", Toast.LENGTH_LONG).show();
//                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(context, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                android.util.Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("oid", oid);
                params.put("ltime", ltime);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    public void changeStatusToFive(final String oid, final String ltime){

        // Tag used to cancel the request
        String tag_string_req = "req_login";
        pDialog.setMessage("Updating..");
        showDialog();

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_STATUS_TO_FIVE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                android.util.Log.d(TAG, "Login Response: " + response1.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response1);
                    boolean error = jObj.  getBoolean("error");

                    // Check for error node in json
//                    if (!error) {
//                        Snackbar snackbar = Snackbar
//                                .make(contactsVIew, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);
//
//                        snackbar.show();
//
//
//
//                    }
// else {
//                        // Error in login. Get the error message
//                        String errorMsg1 = jObj.getString("error_msg1");
//                        Toast.makeText(context,
//                                "Student Returned", Toast.LENGTH_LONG).show();
//                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(context, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                android.util.Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("oid", oid);
                params.put("ltime", ltime);
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
