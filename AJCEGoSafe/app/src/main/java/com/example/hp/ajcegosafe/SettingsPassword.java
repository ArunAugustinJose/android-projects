package com.example.hp.ajcegosafe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SettingsPassword extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String TAG = "AdminHome";
    private dbHelp db;
    private SessionManager session;
    Activity context = this;

    EditText pass,npass,rpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Change Password");

        session = new SessionManager(getApplicationContext());

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        pass = (EditText) findViewById(R.id.edtCurrentPass);
        npass = (EditText) findViewById(R.id.edtNewPass);
        rpass = (EditText) findViewById(R.id.edtRepass);
        TextView updated = (TextView) findViewById(R.id.textView24);
        Button update = (Button) findViewById(R.id.button5);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newpass = npass.getText().toString();
                String confirmpass = rpass.getText().toString();
                if (pass.getText().toString().trim().length() == 0) {
                    pass.setError("Enter Current password.");
                    pass.requestFocus();
                } else if (npass.getText().toString().trim().length() == 0) {
                    npass.setError("Enter new password..");
                    npass.requestFocus();
                } else if (rpass.getText().toString().trim().length() == 0) {
                    rpass.setError("Re-Enter new password..");
                    rpass.requestFocus();
                }
                else if (!newpass.equals(confirmpass))  {
                    rpass.setError("Password do not match..!");
                    rpass.requestFocus();
                }else {
                    String Cpass = pass.getText().toString();
                    String Npass = npass.getText().toString();

                    String admno = session.getValue(context,"admno");
                    updatePassword(admno, Cpass, Npass);


                }
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

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * function to update mobile number
     * */
    private void updatePassword(final String admno, final String Cpass, final String Npass) {


        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Updating..");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                android.util.Log.d(TAG, "Login Response: " + response1.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response1);
                    boolean error = jObj.  getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        Toast.makeText(getApplicationContext(),
                                "Password Changed Successfully..", Toast.LENGTH_LONG).show();
                        clearText();

                    } else {
                        // Error in login. Get the error message
//                        Toast.makeText(getApplicationContext(),
//                                "Your password is wrong..!", Toast.LENGTH_LONG).show();
                        pass.setError("Password is wrong..!");
                        pass.setText("");
                        pass.requestFocus();

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
                params.put("Cpass", Cpass);
                params.put("Npass", Npass);
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
    public void clearText(){
        pass.setText("");
        pass.requestFocus();
        npass.setText("");
        rpass.setText("");
    }

}
