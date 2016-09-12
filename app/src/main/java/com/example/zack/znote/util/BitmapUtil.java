package com.example.zack.znote.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

/**
 * Created by Zack on 2016/5/4.
 */
public class BitmapUtil {
    /**
     * 计算图片的缩放比例
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight, int rotate) {
        // 原始图片的宽高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        //如果旋转的角度是90的奇数倍,则输出的宽和高和原始宽高调换
        if((rotate / 90) % 2 != 0){
            reqWidth = reqWidth ^ reqHeight;
            reqHeight = reqHeight ^ reqWidth;
            reqWidth = reqWidth ^ reqHeight;
        }
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // 在保证解析出的bitmap宽高分别大于目标尺寸宽高的前提下，取可能的inSampleSize的最大值
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 根据图片文件路径加载任意大小的 Bitmap
     */
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight, int rotate) {
        // 首先设置 inJustDecodeBounds=true 来获取图片尺寸
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // 计算 inSampleSize 的值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight, rotate);

        // 根据计算出的 inSampleSize 来解码图片生成Bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 读取照片exif信息中的旋转角度
     * @param path 照片路径
     * @return 旋转的角度
     */
    public static int readImageDegree(String path) {
        int degree = 0;
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (exifInterface == null)
            return degree;
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
        }
        return degree;
    }

    /**
     * 通过bitmap得到旋转rotate角度后的bitmap
     */
    public static Bitmap rotateImage(Bitmap bitmap, int rotate){
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        return bitmap;
    }
}
