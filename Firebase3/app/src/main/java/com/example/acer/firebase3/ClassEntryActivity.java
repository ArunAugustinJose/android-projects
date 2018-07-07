package com.example.acer.firebase3;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClassEntryActivity extends AppCompatActivity {


    EditText classname, subject, count;
    Button Classbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_entry);

        final TextView cname = findViewById(R.id.tvclass);
        final TextView sname = findViewById(R.id.tvSubject);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();
                    String email = firebaseUser.getEmail();
                    String name = firebaseUser.getDisplayName();
                    //and so on for other values

                    sname.setText(uid);
                    cname.setText(uid);

                    Toast.makeText(ClassEntryActivity.this, "You are signed in Firebase!"+uid, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ClassEntryActivity.this, "You are signed out from Firebase!", Toast.LENGTH_SHORT).show();
                }
            }
        };


        final DatabaseReference myRef = database.getReference("message");
        classname = findViewById(R.id.etClass);
        subject = findViewById(R.id.etSubjet);
        count = findViewById(R.id.etNoStudents);
        Classbtn= findViewById(R.id.btnAddClass);



        Classbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1 = classname.getText().toString();
                String subject1 = subject.getText().toString();
                String count1 = count.getText().toString();

                //checking if the value is provided
                if (!TextUtils.isEmpty(name1)) {

                    //getting a unique id using push().getKey() method
                    //it will create a unique id and we will use it as the Primary Key for our Artist
                    String id = myRef.push().getKey();

                    //creating an Artist Object
                    User artist = new User(id, name1, subject1, count1);

                    //Saving the Artist
                    myRef.child(id).setValue(artist);

                    //setting edittext to blank again
                    classname.setText("");
                    subject.setText("");
                    count.setText("");

                    //displaying a success toast
                    // Toast.makeText(this, "Artist added", Toast.LENGTH_LONG).show();
                }
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    User u = ds.getValue(User.class);

                  //  cname.setText(u.getName().toString());
                   // sname.setText(u.getSubject().toString());
                }



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}
