package com.example.zack.znote.helper;

import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.example.zack.znote.R;
import com.example.zack.znote.model.CheckItem;

/**
 * Created by Zack on 2017/1/20.
 */
public class CheckItemViewHolder extends ChildViewHolder {
    public CheckBox checkBox;
    public EditText editText;
    public ImageButton imageButton;

    public CheckItemViewHolder(View itemView) {
        super(itemView);
        checkBox = (CheckBox) itemView.findViewById(R.id.cb_select);
        editText = (EditText) itemView.findViewById(R.id.et_content);
        imageButton = (ImageButton) itemView.findViewById(R.id.ib_cancel);
    }

    public void bind(CheckItem checkItem) {
        checkBox.setChecked(true);
        editText.setText(checkItem.getContent());
        editText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if (editText.requestFocus()) {
            imageButton.setVisibility(View.VISIBLE);
        } else {
            imageButton.setVisibility(View.GONE);
        }

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    imageButton.setVisibility(View.VISIBLE);
                } else {
                    imageButton.setVisibility(View.GONE);
                }
            }
        });
    }
}
