package com.example.hp.ajcegosafe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class AdminProfileOfStudent extends AppCompatActivity {


    private Context mContext=AdminProfileOfStudent.this;

    private static final int REQUEST = 112;

    private ProgressDialog pDialog;
    private SessionManager session;
    Activity context = this;
    private dbHelp db;
    private static final String TAG = "AdminHome";
    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile_of_student);
        setTitle(R.string.Student_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String oid = getIntent().getExtras().getString("oid");
        String admno = getIntent().getExtras().getString("admno");

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        getStudentDetails(oid);
        //Toast.makeText(this,"id:"+admno, Toast.LENGTH_LONG).show();

        t1 = (TextView)findViewById(R.id.textView);
        t2 = (TextView)findViewById(R.id.textView3);
        t3 = (TextView)findViewById(R.id.textView4);
        t4 = (TextView)findViewById(R.id.textView10);
        t5 = (TextView)findViewById(R.id.textView11);
        t6 = (TextView)findViewById(R.id.textView12);
        t7 = (TextView)findViewById(R.id.textView6);
        t8 = (TextView)findViewById(R.id.textView7);
        t9 = (TextView)findViewById(R.id.txtPurpose);

        t6.setText(admno);



//
//

        ImageView stcall = (ImageView) findViewById(R.id.imgStudCalling);

        stcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = t7.getText().toString();
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {android.Manifest.permission.CALL_PHONE};
                    if (!hasPermissions(mContext, PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
                    } else {
                        makeCall(number);
                    }
                } else {
                    makeCall(number);
                }

            }
        });

        ImageView ptcall = (ImageView) findViewById(R.id.imgParCalling);

        ptcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = t8.getText().toString();
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {android.Manifest.permission.CALL_PHONE};
                    if (!hasPermissions(mContext, PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
                    } else {
                        makeCall(number);
                    }
                } else {
                    makeCall(number);
                }
            }
        });

        final Button approve = (Button)findViewById(R.id.buttonApprove);
        final Button reject = (Button) findViewById(R.id.buttonReject);

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oid = getIntent().getExtras().getString("oid");

                changeStatusToOne(oid);
                String nm = t1.getText().toString();
                Snackbar.make(v, nm+"'s Request approved.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                approve.setVisibility(View.INVISIBLE);
                reject.setVisibility(View.INVISIBLE);
                t9.setVisibility(View.INVISIBLE);

            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oid = getIntent().getExtras().getString("oid");

              changeStatusToTwo(oid);

              String nm = t1.getText().toString();
                Snackbar.make(v, nm+"'s Request Rejected.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                approve.setVisibility(View.INVISIBLE);
                reject.setVisibility(View.INVISIBLE);
                t9.setVisibility(View.INVISIBLE);


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                final Button approve = (Button)findViewById(R.id.buttonApprove);
                Button reject = (Button) findViewById(R.id.buttonReject);
                approve.setVisibility(View.VISIBLE);
                reject.setVisibility(View.VISIBLE);
                t9.setVisibility(View.VISIBLE);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String number = t7.getText().toString();
                    makeCall(number);
                } else {
                    Toast.makeText(mContext, "The app was not allowed to call.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public void makeCall(String number)
    {
        Intent intent4=new Intent(Intent.ACTION_CALL);
        intent4.setData(Uri.parse("tel:"+number));
        startActivity(intent4);
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
                Log.d(TAG, "Login Response: " + response1.toString());
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
                        String dob = student.getString("dob");
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
                        t1.setText(fname+" "+lname);
                        t2.setText(branch_name);
                        t3.setText(batch);
                        t4.setText(dob);
                        t5.setText(gender);
                        t7.setText(mobno);
                        t8.setText(pmobno);
                        t9.setText(purpose);


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
                Log.e(TAG, "Login Error: " + error.getMessage());
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

    public void changeStatusToOne(final String oid){

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_STATUS_TO_ONE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                android.util.Log.d(TAG, "Login Response: " + response1.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response1);
                    boolean error = jObj.  getBoolean("error");

                    // Check for error node in json
//                    if (!error) {
//                        Snackbar snackbar = Snackbar
//                                .make(coordinatorLayout, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);
//
//                        snackbar.show();



//                    } else {
//                        // Error in login. Get the error message
//                        String errorMsg1 = jObj.getString("error_msg1");
//                        Toast.makeText(context,
//                                errorMsg1, Toast.LENGTH_LONG).show();
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

    public void changeStatusToTwo(final String oid2){

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_STATUS_TO_TWO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                android.util.Log.d(TAG, "Login Response: " + response1.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response1);
                    boolean error = jObj.  getBoolean("error");

                    // Check for error node in json
//                    if (!error) {
////                Snackbar.make(, nm+"'s Request is Approved.", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//
//
//
//
//                    } else {
//                        // Error in login. Get the error message
//                        String errorMsg1 = jObj.getString("error_msg1");
//                        Toast.makeText(context,
//                                errorMsg1, Toast.LENGTH_LONG).show();
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
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("oid", oid2);
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
