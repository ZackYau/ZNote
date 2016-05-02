package com.example.zack.znote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Zack on 2016/3/20.
 */
public class ZNoteSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_NOTES = "create table Notes(" +
            "id integer primary key autoincrement," +
            "date Long," +
            "title text," +
            "folder_id text" +
            "location_id text" +
            "tags text" +
            "archived integer)";

    public ZNoteSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
