package com.example.zack.znote.model;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

/**
 * Created by Zack on 2017/1/20.
 */
public class UncheckItem implements Parent<CheckItem> {
    private String name;
    private List<CheckItem> checkItems;

    public UncheckItem(String name, List<CheckItem> checkItems) {
        this.name = name;
        this.checkItems = checkItems;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<CheckItem> getChildList() {
        return checkItems;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public CheckItem getCheckItem(int position) {
        return checkItems.get(position);
    }
}
