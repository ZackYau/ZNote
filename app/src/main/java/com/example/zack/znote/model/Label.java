package com.example.zack.znote.model;

/**
 * Created by Zack on 2016/9/17.
 */
public class Label {
    private long id;
    private String title;

    public Label(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Label(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
