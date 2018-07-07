package com.example.hp.ajcegosafe;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class StudentOutpass extends AppCompatActivity {

    CalendarView calendarView;
    EditText etPurpose, etDate;
    private ProgressDialog pDialog;
    private static final String TAG = "AdminHome";
    private dbHelp db;
    private SessionManager session;
    Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_outpass);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.Student_outpass);

        session = new SessionManager(getApplicationContext());

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        etPurpose = (EditText) findViewById(R.id.etPurpose);
        etDate = (EditText) findViewById(R.id.etDate);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setShowWeekNumber(false); // set true value for showing the week numbers.

        long now = System.currentTimeMillis() - 1000;
        calendarView.setMinDate(now);
        calendarView.setMaxDate(now+(1000*60*60*24*21)); //After 7 Days from Now


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                Integer day = i2;
                Integer month = i1+1;
                Integer year = i;
                    if (month < 10 && day < 10) {
                        String days = "0" + day;
                        String months = "0" + month;
                        etDate.setText(days + "-" + months + "-" + year);
                    }
                    else if (month < 10) {

                        String months = "0" + month;
                        etDate.setText(day + "-" + months + "-" + year);
                    }
                    else if (day < 10) {
                        String days = "0" + day;
                        etDate.setText(days + "-" + month + "-" + year);
                    }
                    else {
                        etDate.setText(day + "-" + month + "-" + year);
                    }

                }
        });

        etDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable text) {
                // TODO Auto-generated method stub
                if (text.length() == 2 ) {
                    text.append('-');
                }
                if (text.length() == 5 ) {
                    text.append('-');
                }
            }
        });

        Button btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* validation */

                // Get current date
                Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
                int currentDay = localCalendar.get(Calendar.DATE);
                int currentMonth = localCalendar.get(Calendar.MONTH) + 1;
                int currentYear = localCalendar.get(Calendar.YEAR);


                // Get typed date
                CharSequence foo = etDate.getText();
                String bar = foo.toString();
                Integer day = Integer.parseInt(bar.substring(0,2));
                Integer month = Integer.parseInt(bar.substring(3,5));
                Integer year = Integer.parseInt(bar.substring(6,10));

                if(etDate.getText().toString().trim().length()==0){
                    etDate.setError("Select a date from Culedar");
                    etDate.requestFocus();
                }
                else if( bar.length() != 10){
                    etDate.setError("enter a valid date");
                    etDate.requestFocus();
                }
                else if(day == 00 || day >= 31 || day < currentDay){
                    etDate.setError("Enter a valid date..!");
                    etDate.requestFocus();
                }
                else if(month == 00 || month > 12 || month < currentMonth || month >currentMonth+1){
                    etDate.setError("select a valid month..!");
                    etDate.requestFocus();
                }
                else if(year == 0000 || year > currentYear+1 || year < currentYear){
                    etDate.setError("select a valid Year..!");
                    etDate.requestFocus();
                }
                else if(etPurpose.getText().toString().trim().length()==0){
                    etPurpose.setError("Enter purpose");
                    etPurpose.requestFocus();
                }
                else {

                    String purpose = etPurpose.getText().toString();
                    String date = etDate.getText().toString();
                    String admno = session.getValue(context,"admno");

                    // Calling functions
                    sendRequest(purpose, date, admno);
                    clearText();
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
     * function to send output request
     * */
    private void sendRequest(final String pur, final String dt, final String admno) {


        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Sending request..");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_OUTPASS_REQUEST, new Response.Listener<String>() {

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
                                "Today you are already applied for an outpass..!", Toast.LENGTH_LONG).show();


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
                params.put("purpose", pur);
                params.put("date", dt);
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

    public void clearText(){
        etPurpose.setText("");
        etPurpose.requestFocus();
        etDate.setText("");
    }

    public void showMessege(String title,String message) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}
