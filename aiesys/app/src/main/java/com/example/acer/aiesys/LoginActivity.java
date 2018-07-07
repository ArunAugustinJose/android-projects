package com.example.acer.aiesys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.acer.aiesys.ContactList.ContactActivity;
import com.example.acer.aiesys.Database.dbHelp;
import com.example.acer.aiesys.Session.AppConfig;
import com.example.acer.aiesys.Session.AppController;
import com.example.acer.aiesys.Session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

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
        getSupportActionBar().hide();

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        final EditText inputUsername = findViewById(R.id.txtUsername);
        final EditText inputPassword = findViewById(R.id.txtPassword);
        Button btnLogin = findViewById(R.id.btnLogin);



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
//            else if (rid.equals("2")){
//                // Launch Admin main activity
//                Intent intent = new Intent(LoginActivity.this,
//                        SecurityHome.class);
//                startActivity(intent);
//                finish();
//            }

        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
//                dbHelp db = new dbHelp(context);
//                db.insertName();
                // Check for empty data in the form
                if (!username.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(username, password);
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
    private void checkLogin(final String username, final String password) {
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
                        String username = user.getString("username");


                        // Save the text in SharedPreference
                        session.save(context,"rollid", string_role_id);
                        session.save(context,"username", username);

                        // Login based on the user
                        if (role_id == 1){
                            // Launch Admin main activity
                            Intent intent = new Intent(LoginActivity.this,
                                    AdminHome.class);
                            startActivity(intent);
                            finish();
                        }
//                        else if (role_id == 2){
//                            // Launch Admin main activity
//                            Intent intent = new Intent(LoginActivity.this,
//                                    SecurityHome.class);
//                            startActivity(intent);
//                            finish();
//                        }

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
                params.put("username", username);
                params.put("password", password);

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
