package com.example.hp.ajcegosafe;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AdminHome extends AppCompatActivity {
    public int layoutpressed = -1;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        setTitle(R.string.Admin_home);

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        final LinearLayout linearLayout4 = (LinearLayout) findViewById(R.id.Linear_layout4);
        final LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.Linear_layout3);
        final LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.Linear_layout2);
        final LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.Linear_layout);

        linearLayout1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction()==MotionEvent.ACTION_DOWN){
                    linearLayout1.setBackgroundResource(R.drawable.rounded_edges_pressed);
                    linearLayout1.setPadding(10, 10, 10, 10);
                    layoutpressed = arg0.getId();
                }
                else if (arg1.getAction()== MotionEvent.ACTION_UP){
                    linearLayout1.setBackgroundResource(R.drawable.rounded_edges_normal);
                    linearLayout1.setPadding(10, 10, 10, 10);
                    if(layoutpressed == arg0.getId()){
                        Intent intent = new Intent(AdminHome.this, AdminOutpass.class);
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
                        Intent intent = new Intent(AdminHome.this, AdminOutpassHistory.class);
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
                        Intent intent = new Intent(AdminHome.this, SearchStudent.class);
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
                        Intent intent = new Intent(AdminHome.this, AdminNotification.class);
                        startActivity(intent);
                    }
                }

                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_home, menu);
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
            Intent intent = new Intent(AdminHome.this, AdminSettings.class);
            startActivity(intent);
        }
        else if(id == R.id.action_logout){

            logoutUser();
        }

        return super.onOptionsItemSelected(item);
    }
    private void logoutUser() {
        session.setLogin(false);

//        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(AdminHome.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
