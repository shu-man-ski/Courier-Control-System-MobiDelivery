package com.shm.dim.client.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shm.dim.client.Models.Order;

import java.util.ArrayList;

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

        db.execSQL("CREATE TABLE IF NOT EXISTS ORDERS ( "
                + "OrderCode       TEXT     NOT NULL, "
                + "ProductName     TEXT     NOT NULL, "
                + "Quantity        TEXT     NOT NULL, "
                + "Cost            TEXT     NOT NULL, "
                + "Name            TEXT     NOT NULL, "
                + "Address         TEXT     NOT NULL, "
                + "Date            TEXT     NOT NULL, "
                + "Time            TEXT     NOT NULL, "
                + "Status          TEXT     CHECK([Status] = 'Новый' OR "
                    + "Status = 'Комплектуется' OR "
                    + "Status = 'В доставке' OR "
                    + "Status = 'Получен клиентом' OR "
                    + "Status = 'Оплачен' OR "
                    + "Status = 'Отменен') DEFAULT 'Новый' NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE USER;");
        db.execSQL("DROP TABLE ORDERS;");
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

    public void addOrders(SQLiteDatabase db, ArrayList<Order> order) {
        db.delete("ORDERS", null, null);
        ContentValues cv = new ContentValues();
        for(int i = 0; i < order.size(); i++) {
            cv.put("OrderCode", order.get(i).getOrderCode());
            cv.put("ProductName", order.get(i).getProductName());
            cv.put("Quantity", order.get(i).getQuantity());
            cv.put("Cost", order.get(i).getCost());
            cv.put("Name", order.get(i).getName());
            cv.put("Address", order.get(i).getAddress());
            cv.put("Date", order.get(i).getDate());
            cv.put("Time", order.get(i).getTime());
            cv.put("Status", order.get(i).getStatus());
            db.insert("ORDERS", null, cv);
            cv.clear();
        }
    }

    public ArrayList<Order> getOrders(SQLiteDatabase db) {
        ArrayList<Order> orders = new ArrayList<>();
        String query = "select * from ORDERS";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int orderCodeIndex = cursor.getColumnIndex("OrderCode");
            int productNameIndex = cursor.getColumnIndex("ProductName");
            int quantityIndex = cursor.getColumnIndex("Quantity");
            int costIndex = cursor.getColumnIndex("Cost");
            int nameIndex = cursor.getColumnIndex("Name");
            int addressIndex = cursor.getColumnIndex("Address");
            int dateIndex = cursor.getColumnIndex("Date");
            int timeIndex = cursor.getColumnIndex("Time");
            int statusIndex = cursor.getColumnIndex("Status");
            do {
                orders.add(new Order (
                        cursor.getString(orderCodeIndex),
                        cursor.getString(productNameIndex),
                        cursor.getString(quantityIndex),
                        cursor.getString(costIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(addressIndex),
                        cursor.getString(dateIndex),
                        cursor.getString(timeIndex),
                        cursor.getString(statusIndex)));
            } while (cursor.moveToNext());
        }
        return orders;
    }
}