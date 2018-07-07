package com.example.acer.teacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.acer.teacher.Home.Login;
import com.example.acer.teacher.ServerConnection.SessionManager;
import com.example.acer.teacher.Students.StudentDetailsMult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClassEntry extends AppCompatActivity {
    Button bt_save;
    EditText classname,subject,count;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    SessionManager session;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_entry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        classname = findViewById(R.id.etClass);
        subject = findViewById(R.id.etSubjet);
        count = findViewById(R.id.etNoStudents);

        // Session manager
        session = new SessionManager(getApplicationContext());

         //Retrieve a value from SharedPreference
        uid = session.getValue(ClassEntry.this,"uid");
        final DatabaseReference myRef = database.getReference(uid);
       // Toast.makeText(ClassEntry.this,"user:"+uid, Toast.LENGTH_LONG ).show();

        bt_save = (Button) findViewById(R.id.btnAddClass);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1 = classname.getText().toString();
                String subject1 = subject.getText().toString();
                String count1 = count.getText().toString();
                session.save(ClassEntry.this,"class_name", name1);
                //checking if the value is provided
                if (!TextUtils.isEmpty(name1)) {

                    //getting a unique id using push().getKey() method
                    //it will create a unique id and we will use it as the Primary Key for our Artist
                    String id = myRef.push().getKey();



                    //creating an Artist Object
                    User teacher = new User(uid,id,name1,count1);

                    //Saving the Artist
                    myRef.child(name1).setValue(teacher);
                    final DatabaseReference myRef2 = database.getReference(uid).child(name1).child("subjects");
                   User sub = new User(subject1);
                    String id2 = myRef2.push().getKey();
                    myRef2.child(id2).setValue(sub);

                    //setting edittext to blank again
                    classname.setText("");
                    subject.setText("");
                    count.setText("");

                    Intent in = new Intent(ClassEntry.this, StudentDetailsMult.class);

                    in.putExtra("cls",name1);
                    in.putExtra("uid",uid);
                    in.putExtra("count",count1);

                    startActivity(in);
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
}