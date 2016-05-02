package com.example.zack.znote.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zack.znote.R;
import com.example.zack.znote.model.NotesCard;
import com.example.zack.znote.util.DensityUtil;

import java.util.List;

/**
 * Created by Zack on 2016/3/24.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.NotesViewHolder> {
    private List<NotesCard> mDataSet;
    private Context mContext;
    // 添加图片的最小高度
    private final int mPicHeight;
    public RecyclerViewAdapter(List<NotesCard> mDataSet, Context context) {
        this.mDataSet = mDataSet;
        this.mContext = context;
        this.mPicHeight = getImageHeight();
    }

    /**
     * 自定义ViewHolder类
     */
    public static class NotesViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public ImageView notesPhoto;
        public TextView notesTitle;
        public TextView notesText;
        public LinearLayout notesLabel;

        public NotesViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            notesPhoto = (ImageView)itemView.findViewById(R.id.notes_photo);
            notesTitle = (TextView)itemView.findViewById(R.id.notes_title);
            notesText = (TextView)itemView.findViewById(R.id.notes_text);
            notesLabel = (LinearLayout)itemView.findViewById(R.id.notes_label);
        }
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_notes,parent,false);
        NotesViewHolder notesViewHolder = new NotesViewHolder(view);
        return notesViewHolder;
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
       // final int position = position;
        NotesCard card = mDataSet.get(position);
        String title = card.getTitle();
        String text = card.getText();
        int color = card.getColor();
        final long id = card.getId();
        final List<String> names = card.getNames();
        bindImageView(holder,names);
        holder.notesTitle.setText(title);
        if(!TextUtils.isEmpty(title)){
            holder.notesTitle.setVisibility(View.VISIBLE);
        } else {
            holder.notesTitle.setVisibility(View.GONE);
        }
        holder.notesText.setText(text);
        if(!TextUtils.isEmpty(text)){
            holder.notesText.setVisibility(View.VISIBLE);
        } else {
            holder.notesText.setVisibility(View.GONE);
        }
        holder.cardView.setCardBackgroundColor(color);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * 根据手机屏幕设置图片区域的最小高度
     * 用于适配图片的高度（1x,1.5x,2x）
     */
    private int getImageHeight() {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int span = DensityUtil.dip2px(mContext, 8);
        int cardWidth;

//        if (HomeActivity.mSpanCount == 2) {
            cardWidth = (screenWidth - span * 3) / 2;
 //       } else {
 //           cardWidth = (screenWidth - span * 4) / 3;
//        }
        return cardWidth / 3;
    }

    /**
     * CardView绑定图片的数据
     * 动态设置尺寸（根据 mPicHeight）和图片数量
     */
    private void bindImageView(NotesViewHolder holder, List<String> names) {
        int w, h;
        ViewGroup.LayoutParams notesPhotoLayoutParams = holder.notesPhoto.getLayoutParams();
        switch (names.size()) {
            case 0:
                break;
            case 1:
                w = mPicHeight * 3;
                h = mPicHeight * 2;
                notesPhotoLayoutParams.height = h;

                holder.notesPhoto.setTag(names.get(0));
                holder.notesPhoto.setImageResource(R.drawable.xxy1);
 //               loadBitmap(holder.notesPhoto, names.get(0), w, h);
                break;
            default:
                break;
        }
    }

    public void loadBitmap(ImageView imageView, String name, int w, int h) {

    }
}
