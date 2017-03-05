package com.example.zack.znote.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.zack.znote.R;
import com.example.zack.znote.helper.ItemTouchHelperAdapter;
import com.example.zack.znote.helper.ItemTouchHelperViewHolder;
import com.example.zack.znote.helper.OnStartDragListener;

import java.util.Collections;
import java.util.List;

/**
 * Created by Zack on 2016/12/19.
 */
public class CheckboxesAdapter extends RecyclerView.Adapter<CheckboxesAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private List<String> items;
    private OnStartDragListener onStartDragListener;
    private OnItemClickListener onItemClickListener;
    private Context context;
    private int backgroundColor;
    public CheckboxesAdapter(List<String> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkboxes_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view, new ContentTextWatch());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.imageView.setImageResource(R.drawable.ic_list_white_24dp);
        holder.checkBox.setChecked(false);
        holder.contentTextWatch.updatePosition(position);
        //holder.editText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.editText.setText(items.get(position));
        if (holder.editText.requestFocus()) {
            holder.imageButton.setVisibility(View.VISIBLE);
        } else {
            holder.imageButton.setVisibility(View.GONE);
        }
        holder.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (onStartDragListener != null) {
                    onStartDragListener.onStartDrag(holder);
                }
                return true;
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (onItemClickListener != null && b) {
                    onItemClickListener.onItemCheck(holder.getAdapterPosition());
                }
            }
        });

        holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    holder.imageButton.setVisibility(View.VISIBLE);
                } else {
                    holder.imageButton.setVisibility(View.GONE);
                }
            }
        });

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemRemove(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(items, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

//    @Override
//    public void onItemDismiss(int position) {
//        items.remove(position);
//        notifyItemRemoved(position);
//    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnStartDragListener(OnStartDragListener listener) {
        this.onStartDragListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public ImageView imageView;
        public CheckBox checkBox;
        public EditText editText;
        public ImageButton imageButton;
        public ContentTextWatch contentTextWatch;

        public ViewHolder(View itemView, ContentTextWatch contentTextWatch) {
            super(itemView);
            if (itemView != null) {
                imageView = (ImageView) itemView.findViewById(R.id.iv_touch_move);
                checkBox = (CheckBox) itemView.findViewById(R.id.cb_select);
                editText = (EditText) itemView.findViewById(R.id.et_content);
                imageButton = (ImageButton) itemView.findViewById(R.id.ib_cancel);
                this.contentTextWatch = contentTextWatch;
                editText.addTextChangedListener(contentTextWatch);
            }
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(backgroundColor);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    private class ContentTextWatch implements TextWatcher {
        private int position;
        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            items.set(position, s.toString());
            if(onItemClickListener != null){
                onItemClickListener.onItemTextChanged(position);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    /**
     * 设置背景颜色
     * @param backgroundColor 颜色
     */
    public void setBackgroundColor(int backgroundColor){
        this.backgroundColor = backgroundColor;
    }

    public interface OnItemClickListener {
        void onItemCheck(int position);
        void onItemRemove(int position);
        /**
         * 获取当前项目的Text发生改变
         */
        void onItemTextChanged(int position);
    }
}
