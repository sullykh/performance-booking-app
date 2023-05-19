package com.example.project58;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

public class databaseValidateUserClass {
//this class check whether user is signed up already, same user exists, etc
    private static SQLiteDatabase wdb = null;
    private static userDB db = null;
    public static SQLiteDatabase getWritable(Context context) {
        if (db == null){
            db = new userDB(context);
        }


        if (wdb == null)
            wdb = db.getWritableDatabase();

        return wdb;

    }

    public static boolean checkUser(String email,String password,String selectQuery) {

        //checking whether user entered correct email password combination saved in the database
        //if it does not exists then do not allow to login
        Cursor cursor = wdb.rawQuery(selectQuery, null);

        boolean res = false;
        if (cursor.moveToFirst()) {
            do {
                String s1 = cursor.getString(0);
                Log.e(TAG, "checkUser col 1: "+s1);
                String s2 = cursor.getString(1);
                Log.e(TAG, "checkUser col 2: "+s1);
                if (s1.equals(email) && s2.equals(password))  // check if username and email already exist in DB
                {
                    res = true;
                    break;
                }
            } while (cursor.moveToNext());
        }
        return res;
    }


    public static boolean isAlreadyRegistered (String email,String selectQuery) {
        //checking whether user with this email already exists or not
        //if it does not exists then creating a new user in the database
        Cursor cursor = wdb.rawQuery(selectQuery, null);
        boolean res = false;
        if (cursor.moveToFirst()) {
            do {
                String s1 = cursor.getString(0);
                Log.e("columns", "checkUser1 col1: " + s1);
                if (s1.equals(email))
                {
                    res = true;
                    break;
                }
            } while (cursor.moveToNext());
        }
    return res;
    }


// close database connection

    public static void close() {
        if (db != null)
            db.close();
    }

}


