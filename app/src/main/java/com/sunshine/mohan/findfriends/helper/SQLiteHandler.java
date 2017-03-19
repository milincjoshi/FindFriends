package com.sunshine.mohan.findfriends.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by FEBIELGIVA on 6/7/2016.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

    public static final String LOG_TAG = SQLiteHandler.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "login.db";
    private static final String TABLE_NAME = "users";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";


    public SQLiteHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " +TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                KEY_ID + " INTEGER PRIMARY KEY ," +
                KEY_NAME + " TEXT NOT NULL, " +
                KEY_EMAIL + " TEXT UNIQUE, " +
                KEY_UID + "TEXT, "+
                KEY_CREATED_AT + " TEXT )";
        Log.d(LOG_TAG,"Database table created");

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void addUser(String name,String email, String uuid, String created_at){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,name);
        values.put(KEY_EMAIL,email);
        values.put(KEY_UID,uuid);
        values.put(KEY_CREATED_AT,created_at);
        long id = db.insert(TABLE_NAME,null,values);
        db.close();
        Log.d(LOG_TAG, "New User Was Added to SQLite   " + id);

    }

    //get user details
    public HashMap<String,String> getUserDetails(){
        HashMap<String,String> temp = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(selectQuery,null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            temp.put("name",cursor.getString(1));
            temp.put("email",cursor.getString(2));
            temp.put("uid",cursor.getString(3));
            temp.put("created_at",cursor.getString(4));

        }
        cursor.close();
        db.close();
        Log.d(LOG_TAG,"Fetching user infor from SQLite");
        return temp;
    }

// get the number of rows in table
    public  int getRowCount(){
        String countQuery = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery, null);
        int rowCount = c.getCount();
        db.close();
        c.close();
        return rowCount;
    }

    public void deleteUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();
        Log.d(LOG_TAG,"user has been deleted from SQLite");
    }


}























