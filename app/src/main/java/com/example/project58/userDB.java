package com.example.project58;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


//this class is for storing and getting data from sqlite database
public class userDB extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "userDB.db";

    public userDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    //this will execute when database is created for first time
    public void onCreate(SQLiteDatabase db) {
        Log.e("TABLE Creating","onCreate******************************************");

        //creating a new user
        db.execSQL(com.example.project58.databaseQueries.SQL_CREATE_USER);
        Log.e("TABLE CREATED","onCreate******************************************");

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //this method is required for SQLiteOpenHelper
        // but this is not needed in the app
    }

}
