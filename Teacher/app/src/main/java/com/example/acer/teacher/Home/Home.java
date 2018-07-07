package com.example.acer.teacher.Home;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.teacher.ClassEntry;
import com.example.acer.teacher.R;
import com.example.acer.teacher.ServerConnection.SessionManager;
import com.example.acer.teacher.Students.StudentDetailsMult;
import com.example.acer.teacher.Students.StudentsHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Home extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    Calendar myCalendar;
    TextView txt_date;
    Spinner sp_day;
    FirebaseDatabase database;
    ListView lst;
    private ArrayList<String> arrayList=new ArrayList<>();
    private ArrayAdapter<String> adapter;

    SessionManager session;
    String uid,class_sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManager(getApplicationContext());
        class_sv = session.getValue(Home.this,"class_name");
        uid=session.getValue(Home.this,"uid");

        adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        lst=(ListView) findViewById(R.id.lvClass);

        database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 =database.getReference(uid);
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //SDetail u = dataSnapshot.getValue(SDetail.class);
                // Toast.makeText(StudentDetailsMult.this,u.getPhone().toString(),Toast.LENGTH_LONG).show();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    final String cls = ds.child("name").getValue(String.class);

                                    final DatabaseReference myRef =database.getReference(uid).child(cls).child("subjects");
                                    myRef1.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            //SDetail u = dataSnapshot.getValue(SDetail.class);
                                            // Toast.makeText(StudentDetailsMult.this,u.getPhone().toString(),Toast.LENGTH_LONG).show();
                                            for(DataSnapshot ds: dataSnapshot.getChildren())
                                            {
                                                String sub = ds.child("subject").getValue(String.class);
                                                Toast.makeText(Home.this,sub,Toast.LENGTH_SHORT).show();
                                               arrayList.add(cls+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+sub);
                                               }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                            // Failed to read value
                                            Log.w("Failed to read value.", error.toException());
                                        }
                                    });
                }
                lst.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });


        Intent in= getIntent();

        final String classname=in.getStringExtra("cls");
        final String uid=in.getStringExtra("uid");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd / MM / yyyy ");
        String strDate =  mdformat.format(calendar.getTime());


        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String weekday = new DateFormatSymbols().getWeekdays()[dayOfWeek];

        ImageView bt_date=(ImageView) findViewById(R.id.bt_date);
        txt_date=(TextView) findViewById(R.id.txt_date);
        sp_day=(Spinner) findViewById(R.id.sp_day);



       myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        bt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Home.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//                Intent in=new Intent(Home.this, ClassEntry.class);
//                startActivity(in);
//            }
//        });
        FloatingActionButton subject = findViewById(R.id.fabAddSubject);
        FloatingActionButton  student = findViewById(R.id.fabAddStudent);

        subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Home.this,ClassEntry.class);

                startActivity(in);
            }
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String count1="1";
                Intent in=new Intent(Home.this,StudentDetailsMult.class);
                in.putExtra("cls",classname);
                in.putExtra("uid",uid);
                in.putExtra("count",count1);
                startActivity(in);
            }
        });


    }
    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        int dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK)-1;
        //String weekday = new DateFormatSymbols().getWeekdays()[dayOfWeek];
        sp_day.setSelection(dayOfWeek);
        txt_date.setText(sdf.format(myCalendar.getTime()));

    }

}
