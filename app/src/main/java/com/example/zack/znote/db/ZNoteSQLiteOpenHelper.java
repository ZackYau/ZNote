package com.example.zack.znote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Zack on 2016/3/20.
 */
public class ZNoteSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "znote.db";
    private static final int DB_VERSION = 1;

    public static final String CREATE_NOTES = "create table Notes(" +
            "id integer primary key autoincrement," +
            "title text," +
            "text text," +
            "color_id integer," +
            "time integer," +
            "labels text," +
            "archived integer)";

    public static final String CREATE_LABELS = "CREATE TABLE Labels (" +
            "id integer primary key autoincrement," +
            "title text DEFAULT '' NOT NULL)";

    public static final String CREATE_IMAGES = "CREATE TABLE Images (" +
            "id integer primary key autoincrement," +
            "notes_id integer DEFAULT 1 NOT NULL," +
            "filename text DEFAULT '' NOT NULL)";

    public ZNoteSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTES);
        db.execSQL(CREATE_LABELS);
        db.execSQL(CREATE_IMAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
