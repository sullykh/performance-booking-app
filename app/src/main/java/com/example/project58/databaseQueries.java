package com.example.project58;


public class databaseQueries {


//database query to create a new user
    public static final String SQL_CREATE_USER =
            "CREATE TABLE USER (Email text, Password text )";

//database query to delete a user from database
    public static final String SQL_DELETE_USERS =
            "DROP TABLE IF EXISTS  USER  ";

    }
