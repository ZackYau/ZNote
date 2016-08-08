package com.example.zack.znote.model;

import android.support.annotation.DrawableRes;

/**
 * Created by Zack on 2016/7/21.
 */
public class Item {

    private int drawableRes;
    private String title;

    public Item(@DrawableRes int drawable, String title) {
        drawableRes = drawable;
        this.title = title;
    }

    public int getDrawableResource() {
        return drawableRes;
    }

    public String getTitle() {
        return title;
    }
}
