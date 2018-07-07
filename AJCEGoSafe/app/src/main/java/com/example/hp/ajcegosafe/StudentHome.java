package com.example.hp.ajcegosafe;

/**
 * Created by hp on 15-08-2017.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StudentHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public int layoutpressed = -1;
    private SessionManager session;
    private dbHelp db;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Home");
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.Linear_layout);
        final LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.Linear_layout2);
        final LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.Linear_layout3);
        final LinearLayout linearLayout4 = (LinearLayout) findViewById(R.id.Linear_layout4);

        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction()==MotionEvent.ACTION_DOWN){
                    linearLayout.setBackgroundResource(R.drawable.rounded_edges_pressed);
                    linearLayout.setPadding(10, 10, 10, 10);
                    layoutpressed = arg0.getId();
                }
                else if (arg1.getAction()== MotionEvent.ACTION_UP){
                    linearLayout.setBackgroundResource(R.drawable.rounded_edges_normal);
                    linearLayout.setPadding(10, 10, 10, 10);
                    if(layoutpressed == arg0.getId()){

                        Intent intent = new Intent(StudentHome.this, StudentOutpass.class);
                        startActivity(intent);
                    }
                }

                return true;
            }
        });

        linearLayout2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction()==MotionEvent.ACTION_DOWN){
                    linearLayout2.setBackgroundResource(R.drawable.rounded_edges_pressed);
                    linearLayout2.setPadding(10, 10, 10, 10);
                    layoutpressed = arg0.getId();
                }
                else if (arg1.getAction()== MotionEvent.ACTION_UP){
                    linearLayout2.setBackgroundResource(R.drawable.rounded_edges_normal);
                    linearLayout2.setPadding(10, 10, 10, 10);
                    if(layoutpressed == arg0.getId()){

                        Intent intent = new Intent(StudentHome.this, StudentOutpassStatus.class);
                        startActivity(intent);
                    }
                }

                return true;
            }
        });

        linearLayout3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction()==MotionEvent.ACTION_DOWN){
                    linearLayout3.setBackgroundResource(R.drawable.rounded_edges_pressed);
                    linearLayout3.setPadding(10, 10, 10, 10);
                    layoutpressed = arg0.getId();
                }
                else if (arg1.getAction()== MotionEvent.ACTION_UP){
                    linearLayout3.setBackgroundResource(R.drawable.rounded_edges_normal);
                    linearLayout3.setPadding(10, 10, 10, 10);
                    if(layoutpressed == arg0.getId()){

                        Intent intent = new Intent(StudentHome.this, StudentOutpassHistory.class);
                        startActivity(intent);
                    }
                }

                return true;
            }
        });

        linearLayout4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction()==MotionEvent.ACTION_DOWN){
                    linearLayout4.setBackgroundResource(R.drawable.rounded_edges_pressed);
                    linearLayout4.setPadding(10, 10, 10, 10);
                    layoutpressed = arg0.getId();
                }
                else if (arg1.getAction()== MotionEvent.ACTION_UP){
                    linearLayout4.setBackgroundResource(R.drawable.rounded_edges_normal);
                    linearLayout4.setPadding(10, 10, 10, 10);
                    if(layoutpressed == arg0.getId()){
                        Toast.makeText(getApplicationContext(), "Reminder",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView sname = (TextView) headerView.findViewById(R.id.txtSName);
        TextView branch = (TextView) headerView.findViewById(R.id.txtBranch);

        // Set name and branch on navigationView
        dbHelp db = new dbHelp(this);
        Cursor data = db.getStudentDetails();
        int numRows = data.getCount();
        if (numRows == 0){
            Toast.makeText(StudentHome.this,"There is nothing to show..!",Toast.LENGTH_LONG).show();
        }
        else {
            data.moveToNext();
            String fname = data.getString(1);
            String lname = data.getString(2);
            String branch_name = data.getString(11);
            sname.setText(fname+" "+lname);
            branch.setText(branch_name);
        }
        navigationView.setNavigationItemSelectedListener(this);

//        dbHelp dbH1=new dbHelp(getApplicationContext());
//        SQLiteDatabase db1=dbH1.getWritableDatabase();
//        Cursor c1= db1.rawQuery("SELECT * from "+dbHelp.TABLE_STUDENT,null);
//
//        StringBuffer buffer=new StringBuffer();
//        while(c1.moveToNext())
//        {
//            buffer.append(" fname:"+c1.getString(c1.getColumnIndex(dbHelp.FNAME))+"\n");
//            buffer.append("lname:"+c1.getString(c1.getColumnIndex(dbHelp.LNAME))+"\n");
//            buffer.append("mob:"+c1.getString(c1.getColumnIndex(dbHelp.MOBILE_NO))+"\n");
//            buffer.append("email:"+c1.getString(c1.getColumnIndex(dbHelp.BRANCH))+"\n");
//        }
//        showMessege("Student details",buffer.toString());
//        db1.close();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_home, menu);
//        Toast.makeText(this, "Hello World", Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(StudentHome.this, StudentSettingz.class);
            startActivity(intent);
        }
        else if(id == R.id.action_logout){
            logoutUser();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(StudentHome.this, StudentProfile.class);
            startActivity(intent);
        } else if (id == R.id.nav_dates) {

        } else if (id == R.id.nav_notifications) {

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(StudentHome.this, StudentSettingz.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_help) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser() {
        session.setLogin(false);

        dbHelp db=new dbHelp(this);
        db.deleteStudentTableContent();

        // Launching the login activity
        Intent intent = new Intent(StudentHome.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void showMessege(String title,String message) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}

