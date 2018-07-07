package com.example.hp.ajcegosafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StudentProfile extends AppCompatActivity {


    private Context mContext=StudentProfile.this;

    private static final int REQUEST = 112;

    TextView t1,t2,t3,t4,t5,t6,t7,t8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        setTitle(R.string.Student_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        t1 = (TextView)findViewById(R.id.textView);
        t2 = (TextView)findViewById(R.id.textView3);
        t3 = (TextView)findViewById(R.id.textView4);
        t4 = (TextView)findViewById(R.id.textView10);
        t5 = (TextView)findViewById(R.id.textView11);
        t6 = (TextView)findViewById(R.id.textView12);
        t7 = (TextView)findViewById(R.id.textView6);
        t8 = (TextView)findViewById(R.id.textView7);

        // Fetch data from sqlite
        dbHelp db = new dbHelp(this);
            Cursor data = db.getStudentDetails();
            int numRows = data.getCount();
        if (numRows == 0){
            Toast.makeText(StudentProfile.this,"There is nothing to show..!",Toast.LENGTH_LONG).show();
        }
        else{
            data.moveToNext();
            String fname = data.getString(1);
            String lname = data.getString(2);
            String dob = data.getString(4);
            String gender = data.getString(5);
            String mob = data.getString(6);
            String pmob = data.getString(7);
            String email = data.getString(8);
            String batch = data.getString(9);
            String branch = data.getString(11);

            t1.setText(fname+" "+lname);
            t2.setText(branch);
            t3.setText(batch);
            t4.setText(dob);
            t5.setText(gender);
            t6.setText(email);
            t7.setText(mob);
            t8.setText(pmob);

        }

        Button b = (Button)findViewById(R.id.button2);
        Button b1 = (Button)findViewById(R.id.button3);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentProfile.this, StudentOutpassHistory.class);
                startActivity(intent);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentProfile.this, StudentSettingz.class);
                startActivity(intent);
            }
        });

        ImageView stcall = (ImageView) findViewById(R.id.imgStudCalling);

        stcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = t7.getText().toString();
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {android.Manifest.permission.CALL_PHONE};
                    if (!hasPermissions(mContext, PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
                    } else {
                        makeCall(number);
                    }
                } else {
                    makeCall(number);
                }

            }
        });

        ImageView ptcall = (ImageView) findViewById(R.id.imgParCalling);

        ptcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = t8.getText().toString();
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {android.Manifest.permission.CALL_PHONE};
                    if (!hasPermissions(mContext, PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
                    } else {
                        makeCall(number);
                    }
                } else {
                    makeCall(number);
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String number = t7.getText().toString();
                    makeCall(number);
                } else {
                    Toast.makeText(mContext, "The app was not allowed to call.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public void makeCall(String number)
    {
        Intent intent4=new Intent(Intent.ACTION_CALL);
        intent4.setData(Uri.parse("tel:"+number));
        startActivity(intent4);
    }


}
