package com.example.hp.ajcegosafe;

/**
 * Created by hp on 07-09-2017.
 */

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    import android.app.Activity;
    import android.app.ProgressDialog;
    import android.content.Context;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.graphics.Typeface;
    import android.support.design.widget.Snackbar;
    import android.util.Log;
    import android.util.TypedValue;
    import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.ListView;
    import android.widget.TextView;
import android.widget.Toast;

    import com.android.volley.Request;
    import com.android.volley.Response;
    import com.android.volley.VolleyError;
    import com.android.volley.toolbox.StringRequest;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import static com.example.hp.ajcegosafe.R.drawable.view;

public class UserCustomAdapter extends ArrayAdapter<OutpassData> {

    int resource;
    String response;
    Context context;
    private List<OutpassData> items;
    private UserCustomAdapter adapter;
    private ProgressDialog pDialog;
    private SessionManager session;
    private dbHelp db;
    private static final String TAG = "AdminHome";


    public UserCustomAdapter(Context context, int resource, List<OutpassData> items) {
        super(context, resource, items);
        this.resource=resource;
        this.context = context;
        this.items=items;
        this.adapter = this;
        // Progress dialog
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout contactsView;
        final OutpassData contact = getItem(position);
        if(convertView==null) {
            contactsView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, contactsView, true);
        } else {
            contactsView = (LinearLayout) convertView;
        }
        final TextView sName =(TextView)contactsView.findViewById(R.id.txtName);
        final TextView purpose = (TextView)contactsView.findViewById(R.id.txtPurpose);
        final TextView oid = (TextView)contactsView.findViewById(R.id.txtHidden);
        final TextView pid = (TextView)contactsView.findViewById(R.id.hiddenPid);

        if (pid != null) {
            pid.setText((contact.getPid()));

            String gender = contact.getGender();

            if (gender.equals("male")){
                final ImageView img = (ImageView)contactsView.findViewById(R.id.imgPerson);
                img.setImageResource(R.drawable.person);
            }
            else {
                final ImageView img = (ImageView)contactsView.findViewById(R.id.imgPerson);
                img.setImageResource(R.drawable.women);
            }
        }
        if (sName != null) {
            sName.setText((contact.getName()));
            if (contact.getName() == "Nothing to show"){
                sName.setEnabled(false);
                Button approve = (Button) contactsView.findViewById(R.id.btnApprove);
                Button reject = (Button) contactsView.findViewById(R.id.btnReject);
                approve.setEnabled(false);
                reject.setEnabled(false);
            }
        }
        if (oid != null) {
            oid.setText(contact.getOid());
        }
        if (purpose != null) {
            purpose.setText(contact.getPurpose());
        }



        Button approve = (Button) contactsView.findViewById(R.id.btnApprove);
        Button reject = (Button) contactsView.findViewById(R.id.btnReject);
        approve.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String oid2 = oid.getText().toString();
                final String nm = sName.getText().toString();
                changeStatusToOne(oid2,nm);
                items.remove(contact); //Actually change your list of items here
                adapter.notifyDataSetChanged();
                Snackbar.make(v, nm+"'s Request is Approved.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

//                Toast.makeText(getContext(), "List View Clicked:" + oid2, Toast.LENGTH_LONG).show();
            }
        });

        reject.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String oid2 = oid.getText().toString();
                String nm = sName.getText().toString();

                changeStatusToTwo(oid2);
//                items.remove(contact); //Actually change your list of items here
//                adapter.notifyDataSetChanged();
                Snackbar.make(v, nm+"'s Request is Rejected.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

//                Toast.makeText(getContext(), "List View Clicked:" + oid2, Toast.LENGTH_LONG).show();
            }
        });

        sName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AdminProfileOfStudent.class);
                String oid2 = oid.getText().toString();
                String admno = pid.getText().toString();

                intent.putExtra("oid", oid2);
                intent.putExtra("admno", admno);
                context.startActivity(intent);
            }
        });
        return contactsView;
    }
    public void changeStatusToOne(final String oid, final String nm){

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Updating..");
        showDialog();

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

        pDialog.setMessage("Updating..");
        showDialog();

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
