package com.example.acer.teacher.Subjects;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.acer.teacher.R;
import com.example.acer.teacher.ServerConnection.AppConfig;
import com.example.acer.teacher.ServerConnection.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubjectsHome extends AppCompatActivity {

    ArrayList<Subjects> userArray = new ArrayList<Subjects>();
    private ProgressDialog pDialog;
    Activity context = this;
//    private dbHelp db;
    private static final String TAG = "AdminHome";
    int JSON_SIZE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Class");

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        getDetails();


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.admin_outpass_refresh_menu, menu);
//        Toast.makeText(this, "Hello World", Toast.LENGTH_LONG).show();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    /**
     * function to get student outpass detail
     * */
    private void getDetails() {


        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Searching..");
        showDialog();

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

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

                        Subjects subject = new Subjects();
                        subject.setClass_name(jObj.getString("date_of_leaving"));
                        subject.setSubject(jObj.getString("purpose"));

                        userArray.add(subject);

                    }


                }
                catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                ListView userList= findViewById(R.id.lstClass);

                UserCustomAdapter adapter = new UserCustomAdapter(context,R.layout.subject_list, userArray);
                userList.setAdapter(adapter);
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
