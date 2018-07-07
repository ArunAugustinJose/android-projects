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

public class SettingsProfile extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String TAG = "AdminHome";
    private dbHelp db;
    private SessionManager session;
    Activity context = this;

    EditText mob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Edit Profile");

        session = new SessionManager(getApplicationContext());

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        mob = (EditText) findViewById(R.id.editText4);
        Button save = (Button) findViewById(R.id.button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mob.getText().toString().trim().length()==0){
                    mob.setError("Enter Mobile number..");
                    mob.requestFocus();
                }
                else if(mob.getText().toString().trim().length() != 10){
                    mob.setError("Enter a valid Mobile number..");
                    mob.requestFocus();
                }
                else {

                    String mobno = mob.getText().toString();
                    String admno = session.getValue(context,"admno");
                    updateNumber(admno, mobno);
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
    private void updateNumber(final String admno, final String mobno) {


        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Updating..");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE_MOBILE, new Response.Listener<String>() {

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
                                "This no. is already Existing", Toast.LENGTH_LONG).show();


                    } else {
                        // Error in login. Get the error message
                        Toast.makeText(getApplicationContext(),
                                "Your number is updated..!", Toast.LENGTH_LONG).show();
                        clearText();
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
                params.put("mobno", mobno);
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
        mob.setText("");
        mob.requestFocus();
    }

}
