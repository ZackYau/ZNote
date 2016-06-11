package com.example.zack.znote.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.zack.znote.MainActivity;
import com.example.zack.znote.R;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by Zack on 2016/5/15.
 */
public class BitmapWorkerTask extends AsyncTask<Object, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private String path;
    private Context context;

    public BitmapWorkerTask(ImageView imageView, Context ct) {
        imageViewReference = new WeakReference<ImageView>(imageView);
        context = ct;
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        path = String.valueOf(params[0]);
        int reqWidth = (int) params[1];
        int reqHeight = (int) params[2];
        File file = new File(path);
        Bitmap bitmap = null;
        if (!file.exists()) {
            return BitmapFactory.decodeResource(context.getResources(),
                    android.R.drawable.ic_menu_report_image);
        }
        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) context;
            bitmap = activity.getBitmapFromDiskCache(path);
        }
        if (bitmap == null) {
            bitmap = BitmapUtil.decodeSampledBitmapFromFile(path, reqWidth, reqHeight);
        }
        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) context;
            activity.addBitmapToMemoryCache(path, bitmap);
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (isCancelled()) {
            bitmap = null;
        }
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask  = getBitmapWorkerTask(imageView);
            if (this == bitmapWorkerTask  && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * 检索AsyncTask是否已经被分配到指定的ImageView:
     */
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapLoaderTask();
            }
        }
        return null;
    }

    /**
     * 取消潜在的任务
     */
    public static boolean cancelPotentialWork(String data, ImageView imageView) {
        // 通过imageView找到task
        final BitmapWorkerTask bitmapWorkerTask  = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask  != null) {
            //如果找到的task不为null,并且task的url和给定的url相同，那么取消任务
            final String bitmapData = bitmapWorkerTask.path;
            if ("".equals(bitmapData) || !bitmapData.equals(data)) {
                bitmapWorkerTask .cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 解决并发问题
     */
    public static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public final BitmapWorkerTask getBitmapLoaderTask() {
            return (BitmapWorkerTask) bitmapWorkerTaskReference.get();
        }
    }
}
