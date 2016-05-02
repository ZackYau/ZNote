package com.example.zack.znote.util;

import android.content.Context;

/**
 * Created by Zack on 2016/4/6.
 */
public class DensityUtil {
    public DensityUtil() {
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
