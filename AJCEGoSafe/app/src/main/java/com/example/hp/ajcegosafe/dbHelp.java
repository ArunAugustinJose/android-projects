package com.example.hp.ajcegosafe;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hp on 18-08-2017.
 */

public class dbHelp extends SQLiteOpenHelper {

    /*data base*/
    public static final  String DB_NAME="Outpass1";
    public static final  int VERSION_NO=1;
    public static final String TABLE_STUDENT="StudentDetails";


/* Student profile table */

    public static final String SID="student_id";
    public static final String FNAME="student_fname";
    public static final String LNAME="student_lname";
    public static final String ADDRESS="address";
    public static final String DOB="Date_of_birth";
    public static final String GENDER="gender";
    public static final String MOBILE_NO="student_number";
    public static final String PMOBILE_NO="parent_id";
    public static final String EMAIL="email";
    public static final String BATCH="batch";
    public static final String STREAM="stream";
    public static final String BRANCH="branch";


    public dbHelp(Context context) {
        super(context, DB_NAME, null, VERSION_NO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    /* creating tables */


        String PROFILE_TABLE = "CREATE TABLE "+ TABLE_STUDENT +" ("+ SID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ FNAME +" TEXT NOT NULL, "+ LNAME +" TEXT NOT NULL, " +
                ""+ ADDRESS +" TEXT NOT NULL, "+ DOB +" TEXT NOT NULL, "+ GENDER +" TEXT NOT NULL, "+ MOBILE_NO +" INTEGER NOT NULL, "+ PMOBILE_NO +" INTEGER NOT NULL, "+ EMAIL +" TEXT NOT NULL, "+ BATCH +
                " TEXT NOT NULL, "+ STREAM +" TEXT NOT NULL, "+ BRANCH +" TEXT NOT NULL)";
        db.execSQL(PROFILE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // update stream and branch names
    public void updateStream(String stream_name){
        SQLiteDatabase db = this.getReadableDatabase();
        String update = "UPDATE "+TABLE_STUDENT+ " SET "+STREAM+" = '"+stream_name+"'";
        db.execSQL(update);
    }
    public void updateBranch(String branch_name){
        SQLiteDatabase db = this.getReadableDatabase();
        String update = "UPDATE "+TABLE_STUDENT+ " SET "+BRANCH+" = '"+branch_name+"'";
        db.execSQL(update);
    }

    /* display student details*/
    public Cursor getStudentDetails() {

        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data = db.rawQuery("select * from "+TABLE_STUDENT, null);
        return data;
    }

    public void deleteStudentTableContent(){
        SQLiteDatabase db = this.getReadableDatabase();
        String delete = "DELETE FROM "+TABLE_STUDENT;
        db.execSQL(delete);
    }
}
