package com.example.acer.aiesys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.acer.aiesys.ContactList.ContactActivity;
import com.example.acer.aiesys.Session.SessionManager;

public class AdminHome extends AppCompatActivity {

    ImageView contacts;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        setTitle("Home");

        // session manager
        session = new SessionManager(getApplicationContext());

        //Contact button click
        contacts = findViewById(R.id.imgContacts);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this,
                        ContactActivity.class);
                startActivity(intent);
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

        if(id == R.id.action_logout){

            logoutUser();
        }

        return super.onOptionsItemSelected(item);
    }
    private void logoutUser() {
        session.setLogin(false);


        // Launching the login activity
        Intent intent = new Intent(AdminHome.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
