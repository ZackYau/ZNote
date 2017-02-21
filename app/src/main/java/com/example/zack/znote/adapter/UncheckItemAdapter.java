package com.example.zack.znote.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.example.zack.znote.R;
import com.example.zack.znote.helper.CheckItemViewHolder;
import com.example.zack.znote.helper.UncheckItemViewHolder;
import com.example.zack.znote.model.CheckItem;
import com.example.zack.znote.model.UncheckItem;

import java.util.List;

/**
 * Created by Zack on 2017/1/31.
 */
public class UncheckItemAdapter extends ExpandableRecyclerAdapter<UncheckItem, CheckItem, UncheckItemViewHolder, CheckItemViewHolder> {
    private LayoutInflater layoutInflater;
    private List<UncheckItem> uncheckItemList;
    private OnItemClickListener onItemClickListener;

    public UncheckItemAdapter(Context context, List<UncheckItem> parentList) {
        super(parentList);
        uncheckItemList = parentList;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public UncheckItemViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_parent_checked_list, parentViewGroup, false);
        return new UncheckItemViewHolder(view);
    }

    @NonNull
    @Override
    public CheckItemViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_child_checked_list, childViewGroup, false);
        return new CheckItemViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull UncheckItemViewHolder parentViewHolder, int parentPosition, @NonNull UncheckItem parent) {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull final CheckItemViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull CheckItem child) {
        childViewHolder.bind(child);
        childViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (onItemClickListener != null && !b) {
                    onItemClickListener.onCheckItemCheck(childViewHolder.getAdapterPosition());
                }
            }
        });

        childViewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onCheckItemRemove(childViewHolder.getAdapterPosition());
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onCheckItemCheck(int position);
        void onCheckItemRemove(int position);
    }
}
