package com.example.acer.aiesys.ContactList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.acer.aiesys.Database.dbHelp;
import com.example.acer.aiesys.R;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    ListView userList;
    ContactAdapter userAdapter;
    ArrayList<Contacts> userArray = new ArrayList<Contacts>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        setTitle("Contacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            dbHelp db = new dbHelp(this);
            userArray = db.getName();

            userAdapter = new ContactAdapter(ContactActivity.this, R.layout.contacts_listview,
                    userArray);
            userList = (ListView) findViewById(R.id.lstContacts);
            userList.setItemsCanFocus(false);
            userList.setAdapter(userAdapter);

            userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        final int position, long id) {
                    Log.i("List View Clicked", "**********");
                    Toast.makeText(ContactActivity.this,
                            "List View Clicked:" + position, Toast.LENGTH_LONG)
                            .show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(ContactActivity.this, "Nothing to show..!",
                    Toast.LENGTH_LONG).show();
        }

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
}
