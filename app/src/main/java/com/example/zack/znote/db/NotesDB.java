package com.example.zack.znote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.zack.znote.model.Notes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zack on 2016/9/21.
 */
public class NotesDB {
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String TEXT = "text";
    private static final String COLOR_ID = "color_id";
    private static final String TIME = "time";
    private static final String LABELS = "labels";
    private static final String ARCHIVED = "archived";
    private static final String NOTES = "Notes";
    private static final String DATE_DESC = "date desc";
    private static NotesDB notesDB;
    private SQLiteDatabase db;

    public NotesDB(Context context) {
        ZNoteSQLiteOpenHelper zNoteSQLiteOpenHelper = new ZNoteSQLiteOpenHelper(context);
        db = zNoteSQLiteOpenHelper.getWritableDatabase();
    }

    /**
     * 获取ImageDB的实例
     */
    public synchronized static NotesDB getInstance(Context context) {
        if (notesDB == null) {
            notesDB = new NotesDB(context);
        }
        return notesDB;
    }

    public void insert(Notes notes) {
        if (notes != null) {
            ContentValues values = new ContentValues();
            values.put(TITLE, notes.getTitle());
            values.put(TEXT, notes.getText());
            values.put(COLOR_ID, notes.getColorId());
            values.put(TIME, notes.getTime());
            values.put(LABELS, notes.getLabels());
            values.put(ARCHIVED, notes.getArchived());
            long id = db.insert(NOTES, null, values);
            notes.setId(id);
        }
    }

    public void delete(long id) {
        db.delete(NOTES, "id=?", new String[] {String.valueOf(id)});
    }

    public void update(Notes notes) {
        if (notes != null) {
            ContentValues values = new ContentValues();
            values.put(TITLE, notes.getTitle());
            values.put(TEXT, notes.getText());
            values.put(COLOR_ID, notes.getColorId());
            values.put(TIME, notes.getTime());
            values.put(LABELS, notes.getLabels());
            values.put(ARCHIVED, notes.getArchived());
            db.update(NOTES, values, "id=?", new String[] {String.valueOf(notes.getId())});
        }
    }

    /**
     * 通过id查询记事
     */
    public Notes query(long id) {
        Cursor cursor = db.query(NOTES, null, "id=?", new String[] {String.valueOf(id)}, null, null, null);
        Notes notes = null;
        if (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(TITLE));
            String text = cursor.getString(cursor.getColumnIndex(TEXT));
            int colorId = cursor.getInt(cursor.getColumnIndex(COLOR_ID));
            long time = cursor.getLong(cursor.getColumnIndex(TIME));
            String labels = cursor.getString(cursor.getColumnIndex(LABELS));
            int archived = cursor.getInt(cursor.getColumnIndex(ARCHIVED));
            notes = new Notes(id, title, text, colorId, time, labels, archived);
        }
        if (cursor != null) {
            cursor.close();
        }
        return notes;
    }

    /**
     * 查询所有记事(未归档)
     */
    public List<Notes> queryAll() {
        List<Notes> list = new ArrayList<Notes>();
        Cursor cursor = db.query(NOTES, null, "archived=?", new String[] {"0"}, null, null, "time desc");
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(ID));
                String title = cursor.getString(cursor.getColumnIndex(TITLE));
                String text = cursor.getString(cursor.getColumnIndex(TEXT));
                int colorId = cursor.getInt(cursor.getColumnIndex(COLOR_ID));
                long time = cursor.getLong(cursor.getColumnIndex(TIME));
                String labels = cursor.getString(cursor.getColumnIndex(LABELS));
                int archived = cursor.getInt(cursor.getColumnIndex(ARCHIVED));
                Notes notes = new Notes(id, title, text, colorId, time, labels, archived);
                list.add(notes);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 查询归档记事
     */
    public List<Notes> queryArchived() {
        List<Notes> list = new ArrayList<Notes>();
        Cursor cursor = db.query(NOTES, null, "archived=?", new String[] {"1"}, null, null, "time desc");
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(ID));
                String title = cursor.getString(cursor.getColumnIndex(TITLE));
                String text = cursor.getString(cursor.getColumnIndex(TEXT));
                int colorId = cursor.getInt(cursor.getColumnIndex(COLOR_ID));
                long time = cursor.getLong(cursor.getColumnIndex(TIME));
                String labels = cursor.getString(cursor.getColumnIndex(LABELS));
                int archived = cursor.getInt(cursor.getColumnIndex(ARCHIVED));
                Notes notes = new Notes(id, title, text, colorId, time, labels, archived);
                list.add(notes);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 根据标签查询记事
     */
    public List<Notes> queryNotesByLabel(String label) {
        List<Notes> list = new ArrayList<Notes>();
        Cursor cursor = db.query(NOTES, null, "labels LIKE ? and archived=?", new String[] {"%" + label + "%", "0"}, null, null, "time desc");
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(ID));
                String title = cursor.getString(cursor.getColumnIndex(TITLE));
                String text = cursor.getString(cursor.getColumnIndex(TEXT));
                int colorId = cursor.getInt(cursor.getColumnIndex(COLOR_ID));
                long time = cursor.getLong(cursor.getColumnIndex(TIME));
                String labels = cursor.getString(cursor.getColumnIndex(LABELS));
                int archived = cursor.getInt(cursor.getColumnIndex(ARCHIVED));
                Notes notes = new Notes(id, title, text, colorId, time, labels, archived);
                list.add(notes);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 修改记事中的标签
     * @param oldlabel 旧标签
     * @param newLabel 新标签
     */
    public void changeLabel(String oldlabel, String newLabel) {
        Cursor cursor = db.query(NOTES, null, "labels LIKE ? COLLATE NOCASE", new String[] {"%" + oldlabel + "%"}, null, null, "time desc");
        if (cursor.moveToFirst()) {
            do {
                String str = cursor.getString(cursor.getColumnIndex(LABELS));
                str = "," + str + ",";
                if (str.contains("," + oldlabel + ",")) {
                    String labels = str.replace("," + oldlabel + ",", "," + newLabel + ",");
                    Notes notes = query(cursor.getLong(cursor.getColumnIndex(ID)));
                    notes.setLabels(labels.substring(1, labels.length() - 1));
                    update(notes);
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * 删除记事中的标签
     */
    public void deleteLabel(String label) {
        Cursor cursor = db.query(NOTES, null, "labels LIKE ? COLLATE NOCASE", new String[] {"%" + label + "%"}, null, null, "time desc");
        if (cursor.moveToFirst()) {
            do {
                String str = cursor.getString(cursor.getColumnIndex(LABELS));
                str = "," + str + ",";
                if (str.contains("," + label + ",")) {
                    String labels = str.replace("," + label + ",", ",");
                    Notes notes = query(cursor.getLong(cursor.getColumnIndex(ID)));
                    String newlabels = "";
                    if (!labels.equals(",")) {
                        newlabels = labels.substring(1, labels.length() - 1);
                    }
                    notes.setLabels(newlabels);
                    update(notes);
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    public void close() {
        if (db.isOpen()) {
            db.close();
        }
    }
}
