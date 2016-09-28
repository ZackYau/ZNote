package com.example.zack.znote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.zack.znote.model.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zack on 2016/9/20.
 */
public class LabelDB {
    private static final String TITLE = "title";
    private static final String LABELS = "Labels";
    private static LabelDB labelDB;
    private SQLiteDatabase db;

    public LabelDB(Context context) {
        ZNoteSQLiteOpenHelper zNoteSQLiteOpenHelper = new ZNoteSQLiteOpenHelper(context);
        db = zNoteSQLiteOpenHelper.getWritableDatabase();
    }

    /**
     * 获取ImageDB的实例
     */
    public synchronized static LabelDB getInstance(Context context) {
        if (labelDB == null) {
            labelDB = new LabelDB(context);
        }
        return labelDB;
    }

    public void insert(Label label) {
        if (label != null) {
            ContentValues values = new ContentValues();
            values.put(TITLE, label.getTitle());
            db.insert(LABELS, null, values);
        }
    }

    public void delete(long id) {
        db.delete(LABELS, "id = ?", new String[] {String.valueOf(id)});
    }

    public void update(Label label) {
        if (label != null) {
            ContentValues values = new ContentValues();
            values.put(TITLE, label.getTitle());
            db.update(LABELS, values, "id = ?", new String[]{String.valueOf(label.getId())});
        }
    }

    /**
     * 查询所有标签
     */
    public List<Label> queryAll() {
        List<Label> list = new ArrayList<Label>();
        Cursor cursor = db.query(LABELS, null, null, null, null, null, TITLE);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex(TITLE));
                Label label = new Label(id, title);
                list.add(label);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 查询标签名称查询ID
     */
    public long getIdByTitle(String title) {
        Cursor cursor = db.query(LABELS, null, "title=? COLLATE NOCASE", new String[] {title}, null, null, null);
        long id = 0;
        if (cursor.moveToNext()) {
            id = cursor.getLong(cursor.getColumnIndex("id"));
        }
        if (cursor != null) {
            cursor.close();
        }
        return id;
    }

    /**
     * 模糊查询标签
     */
    public List<Label> queryAllByKeyword(String keyword) {
        Cursor cursor = db.query(LABELS, null, "title LIKE ?", new String[] {"%" + keyword + "%"}, null, null, null);
        List<Label> list = new ArrayList<Label>();
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex(TITLE));
                Label label = new Label(id, title);
                list.add(label);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public void close() {
        if (db.isOpen()) {
            db.close();
        }
    }
}
