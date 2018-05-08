package com.shm.dim.client.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDb.db";
    private static final int DATABASE_VERSION = 1;


    public LocalDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS USER ( "
                + "DeviceID        TEXT     PRIMARY KEY, "
                + "Surname         TEXT     NOT NULL, "
                + "Name            TEXT     NOT NULL, "
                + "Patronymic      TEXT     NOT NULL, "
                + "Birthdate       DATE     NOT NULL, "
                + "PhoneNumber     TEXT     NOT NULL, "
                + "Address         TEXT     NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE USER;");
        onCreate(db);
    }


    public void addUser(SQLiteDatabase db, String deviceID,
                        String surname, String name, String patronymic,
                        String birthdate, String phoneNumber, String address) {
        ContentValues cv = new ContentValues();
        cv.put("DeviceID", deviceID);
        cv.put("Surname", surname);
        cv.put("Name", name);
        cv.put("Patronymic", patronymic);
        cv.put("Birthdate", birthdate);
        cv.put("PhoneNumber", phoneNumber);
        cv.put("Address", address);
        db.insert("USER", null, cv);
        cv.clear();
    }
}