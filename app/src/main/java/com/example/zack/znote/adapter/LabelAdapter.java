package com.example.zack.znote.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zack.znote.R;
import com.example.zack.znote.model.LabelItem;

import java.util.List;

/**
 * Created by Zack on 2016/10/23.
 */
public class LabelAdapter extends RecyclerView.Adapter<LabelAdapter.ViewHolder> {
    private List<LabelItem> items;
    private OnItemClickListener onItemClickListener;
    SparseBooleanArray checkStates=new SparseBooleanArray();

    public LabelAdapter(List<LabelItem> items, OnItemClickListener listener) {
        this.items = items;
        this.onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_label,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        public TextView textView;
        public CheckBox checkBox;
        public LabelItem labelItem;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.imageView = (ImageView) itemView.findViewById(R.id.iv_label);
            this.textView = (TextView) itemView.findViewById(R.id.tv_label);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.cb_label);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, labelItem);
            }
        }

        public void setData(LabelItem labelItem, int position) {
            this.labelItem = labelItem;
            this.textView.setText(labelItem.getTitle());
            this.checkBox.setTag(position);
            this.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    int pos = (int) compoundButton.getTag();
                    if (checkStates.get(pos, false) == isChecked)
                        return;
                    if (isChecked) {
                        checkStates.put(pos, true);
                    } else {
                        checkStates.delete(pos);
                    }
                }
            });
            this.checkBox.setChecked(checkStates.get(position, false));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, LabelItem item);
    }
}
