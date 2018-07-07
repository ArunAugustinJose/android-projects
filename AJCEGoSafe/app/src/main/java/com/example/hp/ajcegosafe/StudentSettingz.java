package com.example.hp.ajcegosafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;

public class StudentSettingz extends AppCompatActivity {
    ListView list;
    String[] web = {
            "Profile",
            "Password",
            "Help",
            "About"
    } ;
    Integer[] imageId = {
            R.drawable.pro,
            R.drawable.password,
            R.drawable.help,
            R.drawable.about

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_settingz);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.Student_settings);

        CustomList adapter = new
                CustomList(StudentSettingz.this, web, imageId);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Toast.makeText(StudentSettingz.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                if (web[+position].equals("Profile")){
                    Intent intent = new Intent(StudentSettingz.this, SettingsProfile.class);
                    startActivity(intent);
                }
                if (web[+position].equals("Password")){
                    Intent intent = new Intent(StudentSettingz.this, SettingsPassword.class);
                    startActivity(intent);
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


}
