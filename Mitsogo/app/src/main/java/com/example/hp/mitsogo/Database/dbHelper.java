package com.example.hp.mitsogo.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hp on 21-03-2018.
 */

public class dbHelper extends SQLiteOpenHelper {

    /*data base*/
    public static final String DB_NAME = "mitsogo";
    public static final int VERSION_NO = 1;
    public static final String TABLE_PROPERTY = "table_property";
    public static final String TABLE_BATTERY = "table_battery";
    public static final String TABLE_STORAGE = "table_storage";
    public static final String TABLE_NETWORK = "table_network";
    public static final String TABLE_WEATHER = "table_weather";


/* Student profile table */

    public static final String ID = "property_id";
    public static final String PROPERTY = "property_name";

    public static final String BATTERY_ID = "bid";
    public static final String ID_FOR_BATTERY = "id";
    public static final String CHARGE = "charge";
    public static final String BDATE = "date";
    public static final String BTIME = "time";
    public static final String BATTERY_STATUS = "status";

    public static final String NETWORK_ID = "nid";
    public static final String ID_FOR_NETWORK = "id";
    public static final String INTERNAL = "internal";
    public static final String EXTERNAL = "external";
    public static final String NDATE = "date";
    public static final String NTIME = "time";
    public static final String NETWORK_STATUS = "status";

    public static final String STORAGE_ID = "sid";
    public static final String ID_FOR_STORAGE = "id";
    public static final String NETWORK_TYPE = "type";
    public static final String PROVIDER = "provider";
    public static final String SDATE = "date";
    public static final String STIME = "time";
    public static final String STORAGE_STATUS = "status";

    public static final String WEATHER_ID = "wid";
    public static final String ID_FOR_WEATHER = "id";
    public static final String PLACE = "place";
    public static final String TEMPERATURE = "temperature";
    public static final String WDATE = "date";
    public static final String WTIME = "time";
    public static final String WEATHER_STATUS = "status";

    public dbHelper(Context context) {
        super(context, DB_NAME, null, VERSION_NO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    /* creating tables */


        String PROPERTY_TABLE = "CREATE TABLE "+ TABLE_PROPERTY +" ("+ ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ PROPERTY +" TEXT NOT NULL";
        db.execSQL(PROPERTY_TABLE);

        String BATTERY_TABLE = "CREATE TABLE "+ TABLE_BATTERY +" ("+ BATTERY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ID_FOR_BATTERY+"INTEGER"+ CHARGE +" TEXT NOT NULL "+BDATE+" TEXT NOT NULL "
                +BTIME+" TEXT NOT NULL "+BATTERY_STATUS+" INTEGER , FOREIGN KEY ("+ID_FOR_BATTERY+") REFERENCES "+TABLE_PROPERTY+"("+ID+")";
        db.execSQL(BATTERY_TABLE);

        String STORAGE_TABLE = "CREATE TABLE "+ TABLE_STORAGE +" ("+ STORAGE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ID_FOR_STORAGE+"INTEGER"+ INTERNAL +" TEXT NOT NULL "+ EXTERNAL +" TEXT NOT NULL "+SDATE+" TEXT NOT NULL "
                +STIME+" TEXT NOT NULL "+STORAGE_STATUS+" INTEGER , FOREIGN KEY ("+ID_FOR_STORAGE+") REFERENCES "+TABLE_PROPERTY+"("+ID+")";
        db.execSQL(STORAGE_TABLE);

        String NETWORK_TABLE = "CREATE TABLE "+ TABLE_NETWORK +" ("+ NETWORK_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ID_FOR_NETWORK+"INTEGER"+ NETWORK_TYPE +" TEXT NOT NULL "+ PROVIDER +" TEXT NOT NULL "+NDATE+" TEXT NOT NULL "
                +NTIME+" TEXT NOT NULL "+NETWORK_STATUS+" INTEGER , FOREIGN KEY ("+ID_FOR_NETWORK+") REFERENCES "+TABLE_PROPERTY+"("+ID+")";
        db.execSQL(NETWORK_TABLE);

        String WEATHER_TABLE = "CREATE TABLE "+ TABLE_WEATHER +" ("+ WEATHER_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ID_FOR_WEATHER+"INTEGER"+ PLACE +" TEXT NOT NULL "+ TEMPERATURE +" TEXT NOT NULL "+WDATE+" TEXT NOT NULL "
                +WTIME+" TEXT NOT NULL "+WEATHER_STATUS+" INTEGER , FOREIGN KEY ("+ID_FOR_WEATHER+") REFERENCES "+TABLE_PROPERTY+"("+ID+")";
        db.execSQL(NETWORK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
