package com.ershov.max.foodkeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Max on 24.04.2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "FoodKeeperDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("myLogs", "--- onCreate database ---");

        db.execSQL("create table foodkeeper ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "buyDate text,"
                + "expireDate text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
