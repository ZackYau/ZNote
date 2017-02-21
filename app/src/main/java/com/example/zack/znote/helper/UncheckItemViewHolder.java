package com.example.zack.znote.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.example.zack.znote.R;
import com.example.zack.znote.model.UncheckItem;

/**
 * Created by Zack on 2017/1/21.
 */
public class UncheckItemViewHolder extends ParentViewHolder {
    private ImageView imageView;
    private TextView textView;

    public UncheckItemViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.iv_arrow);
        textView = (TextView) itemView.findViewById(R.id.tv_content);
    }

    public void bind(UncheckItem uncheckItem) {
        textView.setText(uncheckItem.getName());
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (expanded) {
            imageView.setImageResource(R.drawable.ic_expand_more_white_24dp);
        } else {
            imageView.setImageResource(R.drawable.ic_expand_less_white_24dp);
        }
    }
}
