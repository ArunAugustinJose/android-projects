package com.example.acer.aiesys.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.acer.aiesys.ContactList.Contacts;

import java.util.ArrayList;

public class dbHelp extends SQLiteOpenHelper {

    /*data base*/
    public static final String DB_NAME = "name";
    public static final int VERSION_NO = 1;
    public static final String TABLE_NAME = "names";


    /* name table */

    public static final String ID = "id";
    public static final String NAME = "fname";

    public dbHelp(Context context) {
        super(context, DB_NAME, null, VERSION_NO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        /* creating tables */


        String PROFILE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL)";
        db.execSQL(PROFILE_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void insertName(){
        SQLiteDatabase db = this.getReadableDatabase();
        String insert = "INSERT INTO "+TABLE_NAME+ " VALUES( 1,'Arun Jose')";
        db.execSQL(insert);
        String insert1 = "INSERT INTO "+TABLE_NAME+ " VALUES( 2,'Boby J Jacob')";
        db.execSQL(insert1);

    }

    public ArrayList<Contacts> getName() {
        SQLiteDatabase db=this.getWritableDatabase();
        Integer status = 0;
        Cursor cur = db.rawQuery("select * from "+TABLE_NAME, null);
        ArrayList<Contacts> olist = new ArrayList<Contacts>();

        if(cur.getCount()!=0 ) {
            cur.moveToFirst();
            int c = cur.getCount();
            for (int i = 0; i < c; i++) {
                Contacts out = new Contacts();
                String name = cur.getString(cur.getColumnIndex(NAME));
                out.setName(name);
                cur.moveToNext();
                olist.add(out);
            }
            return olist;
        }

        return null;
    }
}