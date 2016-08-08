package com.example.zack.znote.model;

/**
 * Created by Zack on 2016/8/7.
 */
public class Notes {
    private long id;
    private String title;
    private String text;
    private int colorId;
    private long time;
    private String tags;
    private int archived;

    public Notes(long id, String title, String text, int colorId, long time, String tags, int archived) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.colorId = colorId;
        this.time = time;
        this.tags = tags;
        this.archived = archived;
    }

    public Notes(String title, String text, int colorId, long time, String tags, int archived) {
        this.title = title;
        this.text = text;
        this.colorId = colorId;
        this.time = time;
        this.tags = tags;
        this.archived = archived;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public int getColorId() {
        return colorId;
    }

    public long getTime() {
        return time;
    }

    public String getTags() {
        return tags;
    }

    public int getArchived() {
        return archived;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setArchived(int archived) {
        this.archived = archived;
    }
}
