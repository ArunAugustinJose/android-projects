package com.example.hp.ajcegosafe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private SessionManager session;
    Activity context = this;
    private dbHelp db;
    private static final String TAG = "AdminHome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.title_activity_login);

//        dbHelp db=new dbHelp(this);
//        db.insertRole();
//        db.admin();
//        db.insertBranch();// Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
//        db.insertBatch();
        final TextView error = (TextView) findViewById(R.id.textViewError);
        final EditText inputAdmno = (EditText) findViewById(R.id.editText);
        final EditText inputPassword = (EditText) findViewById(R.id.editText2);
        Button btnLogin = (Button) findViewById(R.id.button);



        // SQLite database handler
        db = new dbHelp(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {

            //Retrieve a value from SharedPreference
            String rid = session.getValue(context,"rollid");

            // User is already logged in. Take him to main activity
            if (rid.equals("1")){
                // Launch Admin main activity
                Intent intent = new Intent(LoginActivity.this,
                        AdminHome.class);
                startActivity(intent);
                finish();
            }
            else if (rid.equals("2")){
                // Launch Admin main activity
                Intent intent = new Intent(LoginActivity.this,
                        SecurityHome.class);
                startActivity(intent);
                finish();
            }
            else if (rid.equals("3")){
                // Launch Admin main activity
                Intent intent = new Intent(LoginActivity.this,
                        StudentHome.class);
                startActivity(intent);
                finish();
            }

        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String admno = inputAdmno.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!admno.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(admno, password);
                    getDetails(admno);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });
    }
    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String admno, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String login_id = jObj.getString("login_id");

                        JSONObject user = jObj.getJSONObject("user");
                        Integer role_id = Integer.parseInt(user.getString("role_id"));
                        String string_role_id = user.getString("role_id");
                        String admno = user.getString("admno");


                        // Save the text in SharedPreference
                        session.save(context,"rollid", string_role_id);
                        session.save(context,"admno", admno);

                        // Login based on the user
                        if (role_id == 1){
                            // Launch Admin main activity
                            Intent intent = new Intent(LoginActivity.this,
                                    AdminHome.class);
                            startActivity(intent);
                            finish();
                        }
                        else if (role_id == 2){
                            // Launch Admin main activity
                            Intent intent = new Intent(LoginActivity.this,
                                    SecurityHome.class);
                            startActivity(intent);
                            finish();
                        }
                        else if (role_id == 3){
                            // Launch Admin main activity
                            Intent intent = new Intent(LoginActivity.this,
                                    StudentHome.class);
                            startActivity(intent);
                            finish();
                        }


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
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
                params.put("admno", admno);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * function to get student detail
     * */
    private void getDetails(final String admno) {


        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_STUDINFO, new Response.Listener<String>() {

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
                        String address = student.getString("address");
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

                        /* insert student details in sqlite*/

                        SQLiteDatabase dbH = db.getWritableDatabase();
                        ContentValues value = new ContentValues();
                        value.put(dbHelp.FNAME, fname);
                        value.put(dbHelp.LNAME, lname);
                        value.put(dbHelp.ADDRESS, address);
                        value.put(dbHelp.DOB, dob);
                        value.put(dbHelp.GENDER, gender);
                        value.put(dbHelp.MOBILE_NO, mobno);
                        value.put(dbHelp.PMOBILE_NO, pmobno);
                        value.put(dbHelp.EMAIL, email);
                        value.put(dbHelp.BATCH, batch);
                        value.put(dbHelp.STREAM, stream_name);
                        value.put(dbHelp.BRANCH, branch_name);
                        dbH.insert(dbHelp.TABLE_STUDENT, null, value);
                        dbH.close();



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



