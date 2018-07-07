package com.example.hp.ajcegosafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.List;
import java.util.Map;

/**
 * Created by hp on 21-09-2017.
 */

public class SecurityCustomAdapterReturn extends ArrayAdapter<SecurityOutpassReturn> {

    int resource;
    String response;
    Context context;
    private List<SecurityOutpassReturn> items;
    private SecurityCustomAdapterReturn adapter;
    private ProgressDialog pDialog;
    private SessionManager session;
    private dbHelp db;
    private static final String TAG = "AdminHome";


    public SecurityCustomAdapterReturn(Context context, int resource, List<SecurityOutpassReturn> items) {
        super(context, resource, items);
        this.resource=resource;
        this.items=items;
        this.context=context;
        this.adapter = this;
        // Progress dialog
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout contactsView;
        final SecurityOutpassReturn contact = getItem(position);
        if(convertView==null) {
            contactsView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, contactsView, true);
        } else {
            contactsView = (LinearLayout) convertView;
        }
        final TextView R_Name =(TextView)contactsView.findViewById(R.id.SecTxtName2);
        final TextView R_oid = (TextView)contactsView.findViewById(R.id.HiddenOid2);
        final TextView pid = (TextView) contactsView.findViewById(R.id.HiddenPid2);

        if (pid != null) {
            pid.setText((contact.getPid()));
            String gender = contact.getGender();

                if (gender.equals("male")) {
                    final ImageView img1 = (ImageView) contactsView.findViewById(R.id.imgPerson1);
                    img1.setImageResource(R.drawable.person);
                } else {
                    final ImageView img1 = (ImageView) contactsView.findViewById(R.id.imgPerson1);
                    img1.setImageResource(R.drawable.women);
                }

        }
        if (R_Name != null) {
            R_Name.setText((contact.getName()));
            if (contact.getName() == "Nothing to show"){
                Button approve = (Button) contactsView.findViewById(R.id.btnApprove);
                approve.setEnabled(false);
            }
        }

        if (R_oid != null) {
            R_oid.setText(contact.getOid());
        }



        Button approve = (Button) contactsView.findViewById(R.id.btnApprove);
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                String formattedDate = df.format(c.getTime());

                Integer hour=Integer.parseInt(formattedDate.substring(0,2));
                if (hour >= 19) {

                    String oid2 = R_oid.getText().toString();

                    items.remove(contact); //Actually change your list of items here
                    adapter.notifyDataSetChanged();
                    Intent intent = new Intent(getContext(), StudentLateReturn.class);
                    intent.putExtra("oid", oid2);
                    context.startActivity(intent);


                }
                else {
                    String oid2 = R_oid.getText().toString();
                    String nam = R_Name.getText().toString();

                    changeStatusToFour(oid2, formattedDate);
                    items.remove(contact); //Actually change your list of items here
                    adapter.notifyDataSetChanged();

                    Snackbar.make(v, nam + " Return back.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
//                    Toast.makeText(getContext(), " List View Clicked:" + hour, Toast.LENGTH_LONG).show();
                }
            }
        });
        return contactsView;
    }

    public void changeStatusToFour(final String oid, final String ltime){

        // Tag used to cancel the request
        String tag_string_req = "req_login";

//        pDialog.setMessage("Updating..");
//        showDialog();

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
                hideDialog();
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
