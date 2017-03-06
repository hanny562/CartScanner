package com.a202sgi.hans.cartscanner;

/**
 * Created by Hanny on 5/3/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Date;
import java.text.SimpleDateFormat;


public class SQLController {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "goods_name";
    public static final String KEY_PRICE = "price";
    public static final String KEY_TIMESTAMP = "timeStamp";

    private static final String DB_NAME = "Cart_db.db";
    private static final String DB_TABLE = "CartTable";
    private static final int DB_VERSION = 1;

    private DatabaseHelper myHelper;
    private Context myContext;
    private SQLiteDatabase myDatabase;


    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String LOGCAT = null;

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("CREATE TABLE " + DB_TABLE + " ("
                    + KEY_ROWID  + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_NAME + " TEXT NOT NULL,"
                    + KEY_PRICE + " TEXT NOT NULL,"
                    + KEY_TIMESTAMP +" INTEGER);");
            Log.d(LOGCAT, "Cart Table Created");
        }

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            // TODO Auto-generated constructor stub
            Log.d(LOGCAT, "Created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }

    }


    public SQLController(Context c) {
        myContext = c;
    }

    public SQLController open() throws SQLException {
        myHelper = new DatabaseHelper(myContext);
        myDatabase = myHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        myHelper.close();
    }

    public long createEntry(String stringName, String stringPrice, String stringDate)
            throws SQLException {
        // TODO Auto-generated method stub
        ContentValues contentvalue = new ContentValues();

        contentvalue.put(KEY_NAME, stringName);
        contentvalue.put(KEY_PRICE, stringPrice);
        contentvalue.put(KEY_TIMESTAMP, stringDate);

        //String selectQuery = "SELECT * from DB_TABLE";
        // Cursor cursor = myDatabase.rawQuery(selectQuery, null);

        // use insertOrThrow for exception
        return myDatabase.insertOrThrow(DB_TABLE, null, contentvalue);

    }

    public String getData() throws SQLException {
        // TODO Auto-generated method stub
        String[] dbColumns = new String[] { KEY_ROWID, KEY_NAME, KEY_PRICE, KEY_TIMESTAMP };
        Cursor cursor = myDatabase.query(DB_TABLE, dbColumns, null, null, null,
                null, null);
        String dbResult = "";
        int Row = cursor.getColumnIndex(KEY_ROWID);
        //int GoodsID = cursor.getColumnIndex(KEY_GOODSID);
        int GoodsName = cursor.getColumnIndex(KEY_NAME);
        int Price = cursor.getColumnIndex(KEY_PRICE);
        int timeStamp = cursor.getColumnIndex(KEY_TIMESTAMP);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            dbResult = dbResult + "\n"
                    + cursor.getString(Row) + "\t\t\t"
                    + cursor.getString(GoodsName) + "\t\t RM "
                    + cursor.getString(Price) + "\t\t"
                    + cursor.getString(timeStamp);
        }

        return dbResult;
    }

    public void deleteEntry(long longrow) throws SQLException {
        // TODO Auto-generated method stub
        myDatabase.delete(DB_TABLE, KEY_ROWID + "=" + longrow, null);
    }

    public void deleteAll() {
        // TODO Auto-generated method stub
        myDatabase.execSQL("DELETE FROM " + DB_TABLE);
        myDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                + DB_TABLE + "'"); // reset autoincrement to 1
    }

    public Cursor QueryData(String query)
    {
        return myDatabase.rawQuery(query, null);
    }



}
