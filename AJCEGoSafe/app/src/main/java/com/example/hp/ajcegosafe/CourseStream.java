package com.example.hp.ajcegosafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CourseStream extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_stream);
        setTitle(R.string.Course_streams);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button b1 = (Button)findViewById(R.id.button);
        Button b2 = (Button)findViewById(R.id.button2);
        Button b3 = (Button)findViewById(R.id.button3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stream = "BTech";
                Intent intent = new Intent(CourseStream.this, StudentRegistration.class);
                intent.putExtra("stream", stream);
                startActivity(intent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stream = "MTech";
                Intent intent = new Intent(CourseStream.this, StudentRegistration.class);
                intent.putExtra("stream", stream);
                startActivity(intent);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stream = "MCA";
                Intent intent = new Intent(CourseStream.this, StudentRegistration.class);
                intent.putExtra("stream", stream);
                startActivity(intent);
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
}
