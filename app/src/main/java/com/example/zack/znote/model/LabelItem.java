package com.example.zack.znote.model;

/**
 * Created by Zack on 2016/10/23.
 */
public class LabelItem {
    String title;
    boolean checked;

    public LabelItem(String title, boolean checked) {
        this.title = title;
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
