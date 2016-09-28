package com.example.zack.znote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.zack.znote.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zack on 2016/9/17.
 */
public class ImageDB {
    private static final String NOTE_ID = "notes_id";
    private static final String FILENAME = "filename";
    private static final String IMAGE = "Images";
    private static ImageDB imageDB;
    private SQLiteDatabase db;

    public ImageDB(Context context) {
        ZNoteSQLiteOpenHelper zNoteSQLiteOpenHelper = new ZNoteSQLiteOpenHelper(context);
        db = zNoteSQLiteOpenHelper.getWritableDatabase();
    }

    /**
     * 获取ImageDB的实例
     */
    public synchronized static ImageDB getInstance(Context context) {
        if (imageDB == null) {
            imageDB = new ImageDB(context);
        }
        return imageDB;
    }

    public void insert(Image image) {
        if (image != null) {
            ContentValues values = new ContentValues();
            values.put(NOTE_ID, image.getNotesId());
            values.put(FILENAME, image.getFilename());
            long id = db.insert(IMAGE, null, values);
            image.setId(id);
        }
    }

    public void delete(long id) {
        db.delete(IMAGE, "id = ?", new String[] {String.valueOf(id)});
    }

    /**
     * 通过id查询图片
     */
    public Image query(long id) {
        Cursor cursor = db.query(IMAGE, null, "id=?", new String[] {String.valueOf(id)}, null, null, null);
        Image image = null;
        if (cursor.moveToNext()) {
            long notesId = cursor.getLong(cursor.getColumnIndex(NOTE_ID));
            String fileName = cursor.getString(cursor.getColumnIndex(FILENAME));
            image = new Image(id, notesId, fileName);
        }
        if (cursor != null) {
            cursor.close();
        }
        return image;
    }

    /**
     * 查询特定记事所有图片
     */
    public List<Image> queryAll(long notesId) {
        List<Image> list = new ArrayList<Image>();
        Cursor cursor = db.query(IMAGE, null, "notes_id=?", new String[] {String.valueOf(notesId)}, null, null, "id");
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                String fileName = cursor.getString(cursor.getColumnIndex(FILENAME));
                Image image = new Image(id, notesId, fileName);
                list.add(image);
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
