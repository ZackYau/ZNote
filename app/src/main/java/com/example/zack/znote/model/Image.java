package com.example.zack.znote.model;

/**
 * Created by Zack on 2016/9/17.
 */
public class Image {
    private long id;
    private long notesId;
    private String filename;

    public Image(long id, long notesId, String filename) {
        this.id = id;
        this.notesId = notesId;
        this.filename = filename;
    }

    public Image(long notesId, String filename) {
        this.notesId = notesId;
        this.filename = filename;
    }

    public long getId() {
        return id;
    }

    public long getNotesId() {
        return notesId;
    }

    public String getFilename() {
        return filename;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNotesId(long notesId) {
        this.notesId = notesId;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
