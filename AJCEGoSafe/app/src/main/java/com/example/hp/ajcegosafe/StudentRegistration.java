package com.example.hp.ajcegosafe;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.R.attr.id;

public class StudentRegistration extends AppCompatActivity {
    private String[] branchSpinner;

    Button b;
    EditText t1,t2,t3,t4,t5;
    TextView tv,tv1,tv2;
    Spinner spinner,s;
    RadioGroup gender;
    RadioButton radio;
    private int sYear;
    private int sMonth;
    private int sDay;
    static final int DATE_DIALOG_ID = 0;
    private String[] yearSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);
        setTitle(R.string.Student_registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    /*Radio button */
        gender = (RadioGroup)findViewById(R.id.radioSex);

        /* Year spinner */

        s = (Spinner) findViewById(R.id.spinner);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String year = parent.getItemAtPosition(position).toString();
                tv1.setText(year);
                if (year.equals("Year")){
                    View v1 = s.getSelectedView();
                    ((TextView)v1).setTextColor(Color.GRAY);
                }
                else {
                    View v1 = s.getSelectedView();
                    ((TextView)v1).setTextColor(Color.BLACK);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        loadYearSpinnerData();

        /* Branch Spinner click listener */
        spinner = (Spinner) findViewById(R.id.spinner4);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String batch = parent.getItemAtPosition(position).toString();
                tv.setText(batch);
                if (batch.equals("Branch")){
                    View v = spinner.getSelectedView();
                    ((TextView)v).setTextColor(Color.GRAY);
                }
                else {
                    View v = spinner.getSelectedView();
                    ((TextView)v).setTextColor(Color.BLACK);
                }

                // Showing selected spinner item
               // Toast.makeText(parent.getContext(), "You selected: " + batch,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        // Loading spinner data from database
        loadSpinnerData();

        t1 = (EditText)findViewById(R.id.editText1);
        t2 = (EditText) findViewById(R.id.editText2);
        t3 = (EditText) findViewById(R.id.editText7);
        t4 = (EditText) findViewById(R.id.editText3);
        t5 = (EditText) findViewById(R.id.editText6);
        tv2 = (TextView) findViewById(R.id.textView5);
        tv = (TextView) findViewById(R.id.textView16);
        tv1 = (TextView) findViewById(R.id.textView19);
        b = (Button)findViewById(R.id.button3);

        t3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                if (t3.length() == 2 ) {
//                    t3.append("-");}
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
        /* Culender */
        final ImageView ib = (ImageView)findViewById(R.id.imageView);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
                fromupdateDisplay();

            }
        });

        /* Button click event */

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        /* Gender section */
                // get selected radio button from radioGroup
                int selectedId = gender.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radio = (RadioButton) findViewById(selectedId);

                /* Validation */


                if(t1.getText().toString().trim().length()==0){
                    t1.setError("Enter Admission No.");
                    t1.requestFocus();
                }
                else if(t2.getText().toString().trim().length()==0){
                    t2.setError("Enter Name");
                    t2.requestFocus();
                }

                else if(t3.getText().toString().trim().length()==0){
                    t3.setError("Select Date of Birth");
                    t3.requestFocus();
                }
                else if (gender.getCheckedRadioButtonId() == -1)
                {
                    if (gender.getCheckedRadioButtonId() == -1) {
                        tv2.setError("Select gender");
                        gender.requestFocus();
                    }
                    else {
                        tv2.setError(null);
                    }

                }
                else if(t4.getText().toString().trim().length()==0){
                    t4.setError("Enter Mobile No.");
                    t4.requestFocus();
                }
                else if(t4.getText().toString().trim().length() < 10 || t4.getText().toString().trim().length() > 12){
                    t4.setError("Enter a valid Mobile No.");
                    t4.requestFocus();
                }
                else if(t5.getText().toString().trim().length()==0){
                    t5.setError("Enter Mobile No.");
                    t5.requestFocus();
                }
                else if(t5.getText().toString().trim().length() < 10 || t5.getText().toString().trim().length() >12){
                    t5.setError("Enter a valid Mobile No.");
                    t5.requestFocus();
                }
                else if(tv.getText().toString().equals("Branch") && tv1.getText().toString().equals("Year") ){
                    Toast.makeText(StudentRegistration.this,"Select Batch and Year", Toast.LENGTH_LONG).show();
                    spinner.requestFocus();
                }
                else if(tv.getText().toString().equals("Branch")){
                    Toast.makeText(StudentRegistration.this,"Select Batch", Toast.LENGTH_LONG).show();
                    spinner.requestFocus();
                }
                else if(tv1.getText().toString().equals("Year")){
                    Toast.makeText(StudentRegistration.this,"Select Year", Toast.LENGTH_LONG).show();
                    s.requestFocus();
                }
                else {
                    String admno = t1.getText().toString();
                    String name = t2.getText().toString();
                    String dob = t3.getText().toString();
                    String mob = t4.getText().toString();
                    String pmob = t5.getText().toString();
                    String password = "0000";
                    String roleid = "2";
                    String gender = radio.getText().toString();

//                    try {
//                        dbHelp dbH5 = new dbHelp(getApplicationContext());
//                        SQLiteDatabase db5 = dbH5.getWritableDatabase();
//                        String q5 = "select * from " + dbHelp.TABLE_NAME3 + " WHERE " + dbHelp.ADNO + "='" + admno + "'";
//                        Cursor c5 = db5.rawQuery(q5, null);
//                        c5.moveToNext();
//                        db5.close();
//                        if (c5.getCount() == 0) {
//
//                            dbHelp dbH = new dbHelp(getApplicationContext());
//                            SQLiteDatabase db = dbH.getWritableDatabase();
//
//                            ContentValues value2 = new ContentValues();
//                            value2.put(dbHelp.RID2, roleid);
//                            value2.put(dbHelp.ADNO, admno);
//                            value2.put(dbHelp.PASS, password);
//
//                            db.insert(dbHelp.TABLE_NAME3, null, value2);
//                            db.close();
//
//                    //Toast.makeText(StudentRegistration.this,"Inserted in login", Toast.LENGTH_LONG).show();
//
////                dbHelp dbH1=new dbHelp(getApplicationContext());
////                SQLiteDatabase db1=dbH1.getWritableDatabase();
////                Cursor c1= db1.rawQuery("SELECT * from "+dbHelp.TABLE_NAME,null);
////
////                StringBuffer buffer=new StringBuffer();
////        while(c1.moveToNext())
////        {
////            buffer.append(" bid:"+c1.getString(c1.getColumnIndex(dbHelp.ID))+"\n");
////            buffer.append("sid:"+c1.getString(c1.getColumnIndex(dbHelp.ADNO2))+"\n");
////            buffer.append("Adno:"+c1.getString(c1.getColumnIndex(dbHelp.NAME))+"\n\n\n");
////            buffer.append("pass:"+c1.getString(c1.getColumnIndex(dbHelp.PASS))+"\n");
////        }
////        showMessege("Student details",buffer.toString());
////                db1.close();
//
//
//           /*Get latest login id from login table */
//                    dbHelp dbH2 = new dbHelp(getApplicationContext());
//                    SQLiteDatabase db2 = dbH2.getWritableDatabase();
//                    String query = "SELECT " + dbHelp.LID + " from " + dbHelp.TABLE_NAME3 + " order by " + dbHelp.LID + " DESC limit 1";
//                    Cursor c = db2.rawQuery(query, null);
//                    c.moveToFirst();
//                    String logid = c.getString(0).toString();
//                    db2.close();//The 0 is the column index, we only have 1 column, so the index is 0
//
//           /* Get branch code from branch table according to spinner branch name  */
//                    dbHelp dbH3 = new dbHelp(getApplicationContext());
//                    SQLiteDatabase db3 = dbH3.getWritableDatabase();
//                    String bname = tv.getText().toString();
//                    String query1 = "SELECT " + dbHelp.BID + " from " + dbHelp.TABLE_BATCH + " WHERE " + dbHelp.BNAME + " = '" + bname + "'";
//                    Cursor c2 = db3.rawQuery(query1, null);
//                    c2.moveToFirst();
//                    String batch_id = c2.getString(0).toString();
//                    //Toast.makeText(StudentRegistration.this,batch_id, Toast.LENGTH_LONG).show();
//                    db3.close();
//
//         /* Generate year duration */
//                    String year = "";
//                    String yr = tv1.getText().toString();
//                    String stream = getIntent().getExtras().getString("stream");
//                    if (stream.equals("MCA") && batch_id.equals("4") || stream.equals("MTech")) {
//                        String y = String.valueOf(Integer.parseInt(yr) + 2);
//                        year = yr + "-" + y;
//                    } else if (stream.equals("MCA") && batch_id.equals("5")) {
//                        String y = String.valueOf(Integer.parseInt(yr) + 3);
//                        year = yr + "-" + y;
//                    } else if (stream.equals("MCA") && batch_id.equals("6")) {
//                        String y = String.valueOf(Integer.parseInt(yr) + 5);
//                        year = yr + "-" + y;
//                    } else if (stream.equals("BTech")) {
//                        String y = String.valueOf(Integer.parseInt(yr) + 4);
//                        year = yr + "-" + y;
//                    } else
//                        year = yr;
//
//                    dbHelp dbH4 = new dbHelp(getApplicationContext());
//                    SQLiteDatabase db4 = dbH4.getWritableDatabase();
//                    ContentValues value = new ContentValues();
//                    value.put(dbHelp.LOGID, logid);
//                    value.put(dbHelp.BID2, batch_id);
//                    value.put(dbHelp.NAME, name);
//                    value.put(dbHelp.DOB, dob);
//                    value.put(dbHelp.ADNO2, admno);
//                    value.put(dbHelp.YEAR, year);
//                    value.put(dbHelp.GEN, gender);
//                    value.put(dbHelp.STNO, mob);
//                    value.put(dbHelp.PTNO, pmob);
//                    db4.insert(dbHelp.TABLE_NAME, null, value);
//                    db4.close();
//                    Intent intent = new Intent(StudentRegistration.this, CourseStream.class);
//                    startActivity(intent);
//                    // Toast.makeText(StudentRegistration.this,"Inserted in profile", Toast.LENGTH_LONG).show();
//                    clearText();
//                    }else {
//                            t1.setError("Admission No. already existing");
//                            t1.requestFocus();
//                        }
//                    }
//                    catch (Exception e){
//                        Toast.makeText(StudentRegistration.this,"Admission No. already existing..!", Toast.LENGTH_LONG).show();
//                    }

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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                DatePickerDialog dialog = new DatePickerDialog(this, from_dateListener, sYear, sMonth, sDay);
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                Calendar c = Calendar.getInstance();
                c.set(1985, 0, 1);//Year,Mounth -1,Day
                dialog.getDatePicker().setMinDate(c.getTimeInMillis());
                return dialog;
        }
        return null;
    }

    //update month day year
    private void fromupdateDisplay() {
        EditText t3 = (EditText)findViewById(R.id.editText7);
        t3.setText(//this is the edit text where you want to show the selected date
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(sDay).append("-")
                        .append(sMonth + 1).append("-")
                        .append(sYear).append(""));
    }
    // the call back received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener from_dateListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    sYear = year;
                    sMonth = monthOfYear;
                    sDay = dayOfMonth;
                    fromupdateDisplay();
                }
            };

    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData() {
        // database handler
        dbHelp db = new dbHelp(getApplicationContext());

        String stream = getIntent().getExtras().getString("stream");
       // Toast.makeText(this,"id:"+stream, Toast.LENGTH_LONG).show();
        // Spinner Drop down elements
//        List<String> batches = db.getAllBatches(stream);

        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, batches){
//            @Override
//            public boolean isEnabled(int position){
//                if(position == 0)
//                {
//                    // Disable the first item from Spinner
//                    // First item will be use for hint
//                    return false;
//                }
//                else
//                {
//                    return true;
//                }
//            }
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view;
//                if(position == 0){
//                    // Set the hint text color gray
//                    tv.setTextColor(Color.GRAY);
//                }
//                else {
//                    tv.setTextColor(Color.BLACK);
//                }
//                return view;
//            }
//        };

//        // Drop down layout style - list view with radio button
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        spinner.setAdapter(dataAdapter);
    }

    /**
     * Function to load the Year spinner data
     * */
    private void loadYearSpinnerData() {

        String stream = getIntent().getExtras().getString("stream");


        // Creating adapter for spinner
        this.yearSpinner = new String[] {
                "Year","2014","2015","2016","2017", "2018", "2019", "2020"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, yearSpinner){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv1 = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv1.setTextColor(Color.GRAY);
                }
                else {
                    tv1.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        // Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        s.setAdapter(adapter);
    }



    public void showMessege(String title,String message) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText(){
        t1.setText("");
        t3.setText("");
        t2.setText("");
        t4.setText("");
        t5.setText("");
        t1.requestFocus();
    }
}
