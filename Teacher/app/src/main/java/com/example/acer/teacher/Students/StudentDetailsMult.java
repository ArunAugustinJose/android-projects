package com.example.acer.teacher.Students;



import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.teacher.ClassEntry;
import com.example.acer.teacher.Home.Home;
import com.example.acer.teacher.Home.Login;
import com.example.acer.teacher.R;
import com.example.acer.teacher.SDetail;
import com.example.acer.teacher.ServerConnection.SessionManager;
import com.example.acer.teacher.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentDetailsMult extends AppCompatActivity {
    String classname,count1;
    String uid;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Integer count;
    EditText name,phone;
    Button next;
    String id,class_sv;
    SessionManager session;
    ListView lst;
    private ArrayList<String> arrayList=new ArrayList<>();
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details_mult);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManager(getApplicationContext());
        class_sv = session.getValue(StudentDetailsMult.this,"class_name");
        uid=session.getValue(StudentDetailsMult.this,"uid");
        Toast.makeText(StudentDetailsMult.this,"user:"+class_sv, Toast.LENGTH_LONG ).show();
        //getting values from Classentry
        Intent in= getIntent();

           final String classname=in.getStringExtra("cls");
         //  final String uid=in.getStringExtra("uid");
           String count1=in.getStringExtra("count");



        adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        lst=(ListView) findViewById(R.id.lvStud);

       // Toast.makeText(StudentDetailsMult.this,"dd"+uid+"name"+classname,Toast.LENGTH_SHORT).show();
        //final DatabaseReference myRef = database.getReference(uid).child(classname);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 =database.getReference(uid).child(class_sv).child("students");
        name=(EditText) findViewById(R.id.etName);
        phone=(EditText) findViewById(R.id.etPhone);
        next=(Button) findViewById(R.id.btNext);
      // count=Integer.parseInt(count1);
        count=3;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                if(count!=0)
                {
                    id = myRef1.push().getKey();
                    SDetail st=new SDetail(name.getText().toString(),phone.getText().toString());
                    myRef1.child(id).setValue(st);

                   // Bundle bundle=new Bundle();
                  //  bundle.putString("cls",classname);
                  //  bundle.putString("uid",uid);
                  //  bundle.putString("count",count1);
                  //  in.putExtras(bundle);

                    name.setText("");
                    phone.setText("");
                    name.setHint("Enter another Student name");
                    phone.setHint("Phone");

                }
                else
                {
                    id = myRef1.push().getKey();
                    SDetail st=new SDetail(name.getText().toString(),phone.getText().toString());
                    myRef1.child(id).setValue(st);

                    Intent in = new Intent(StudentDetailsMult.this, Home.class);
                    in.putExtra("cls",classname);
                    in.putExtra("uid",uid);
                    startActivity(in);
                }


                }
        });

        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

             //SDetail u = dataSnapshot.getValue(SDetail.class);
            // Toast.makeText(StudentDetailsMult.this,u.getPhone().toString(),Toast.LENGTH_LONG).show();
           for(DataSnapshot ds: dataSnapshot.getChildren())
            {


                    String name2 = ds.child("stud_name").getValue(String.class);
                    String phone = ds.child("phone").getValue(String.class);

                    arrayList.add(name2+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+phone);

               }
                lst.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
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