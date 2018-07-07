package com.example.hp.ajcegosafe;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StudentDelete extends AppCompatActivity {

    TextView tv1,tv2,tv3,tv4;
    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_delete);
        setTitle(R.string.Student_remove);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et = (EditText) findViewById(R.id.editText8);
        tv1 = (TextView) findViewById(R.id.textView17);
        tv2 = (TextView) findViewById(R.id.textView1);
        tv3 = (TextView) findViewById(R.id.textView18);
        tv4 = (TextView) findViewById(R.id.textView20);

//        try {
            String s_name = getIntent().getExtras().getString("s_name");

//                et.setText(adno);
//                tv1.setText(name);
//                tv2.setText(batch);
//                tv3.setText(yr);
//            }
//            else {
//                tv4.setText("Can't identify student. Search with admission no.");
//                et.requestFocus();
//            }
//        }
//        catch (Exception e){
//            Toast.makeText(StudentDelete.this,"Can't identify student.Search with admission no.", Toast.LENGTH_LONG).show();
//            et.requestFocus();
//        }

        ImageButton im = (ImageButton) findViewById(R.id.imageButton);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv4.setText("");
                String adno = et.getText().toString();
                Integer admno = Integer.parseInt(adno);
//                dbHelp dbH3= new dbHelp(getApplicationContext());
//                SQLiteDatabase db3 = dbH3.getWritableDatabase();
//                String query2 = "SELECT * from " + dbHelp.TABLE_NAME + " WHERE " + dbHelp.ADNO2 + "=" +admno ;
//                Cursor c2 = db3.rawQuery(query2, null);
//                if (c2.getCount()==1) {
//                    c2.moveToFirst();
//                    String name = c2.getString(c2.getColumnIndex(dbHelp.NAME));
//                    Integer bid = c2.getInt(c2.getColumnIndex(dbHelp.BID2));
//                    String yr = c2.getString(c2.getColumnIndex(dbHelp.YEAR));
//                    db3.close();
//
//                    dbHelp dbH4 = new dbHelp(getApplicationContext());
//                    SQLiteDatabase db4 = dbH4.getWritableDatabase();
//                    String query3 = "SELECT * from " + dbHelp.TABLE_BATCH + " WHERE " + dbHelp.BID + "=" + bid;
//                    Cursor c3 = db4.rawQuery(query3, null);
//                    c3.moveToFirst();
//                    String batch = c3.getString(c3.getColumnIndex(dbHelp.BNAME));
//                    db4.close();
//
//
//                    et.setText(adno);
//                    tv1.setText(name);
//                    tv2.setText(batch);
//                    tv3.setText(yr);
//                }
//                else if (c2.getCount()==0){
//                    tv4.setText("");
//                    tv1.setText("");
//                    tv2.setText("");
//                    tv3.setText("");
//                    et.setError("Invalid admission No.");
//                    et.requestFocus();
//                }
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
