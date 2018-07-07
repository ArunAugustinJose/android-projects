package com.example.hp.ajcegosafe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

import static com.example.hp.ajcegosafe.AppController.TAG;


public class SecurityReturnFragment extends Fragment{

    public SecurityReturnFragment() {
        // Required empty public constructor
    }

    ListView userList;

    private ProgressDialog pDialog;
    private SessionManager session;
    private dbHelp db;
    private static final String TAG = "AdminHome";
    int JSON_SIZE;
    ArrayList<SecurityOutpassReturn> securityArray = new ArrayList<SecurityOutpassReturn>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_security_home, container, false);
        userList = (ListView) view.findViewById(R.id.securityLstView);
        // Progress dialog
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setCancelable(false);
        returnStudent();
        return view;


    }

    /**
     * function to get student outpass detail
     * */
    private void returnStudent() {


        // Tag used to cancel the request
        String tag_string_req = "req_login";

//        pDialog.setMessage("Updating..");
//        showDialog();

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_STUDENT_RETURN, new Response.Listener<String>() {

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

                        SecurityOutpassReturn outpass = new SecurityOutpassReturn();

                        outpass.setOid(jObj.getString("outpass_id"));
                        outpass.setPid(jObj.getString("admission_no"));
                        String fname = jObj.getString("fname");
                        String lname = jObj.getString("lname");
                        outpass.setName(fname+" "+lname);
                        outpass.setGender(jObj.getString("gender"));

                        securityArray.add(outpass);

                    }


                }
                catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


                SecurityCustomAdapterReturn adapter = new SecurityCustomAdapterReturn(getContext(),R.layout.security_student_return_lstview, securityArray);
                userList.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                android.util.Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getContext(),
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
