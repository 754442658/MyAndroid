package com.anhuanjia.basemodel.utils;

/**
 * 给图片加水印
 * Created by lance on 2017/7/21.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Date;

public class WaterUtils {


    /*给图片添加水印*/
    public static boolean addWatermarkBitmap(Bitmap bitmap, String str, String path, ImageView imageView) {
        int destWidth = bitmap.getWidth();   //此处的bitmap已经限定好宽高
        int destHeight = bitmap.getHeight();
        Log.v("tag", "width = " + destWidth + " height = " + destHeight);

        Bitmap icon = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888); //定好宽高的全彩bitmap
        Canvas canvas = new Canvas(icon);//初始化画布绘制的图像到icon上

        Paint photoPaint = new Paint(); //建立画笔
        photoPaint.setDither(true); //获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);//过滤一些

        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());//创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, destWidth, destHeight);//创建一个指定的新矩形的坐标
        canvas.drawBitmap(bitmap, src, dst, photoPaint);//将photo 缩放或则扩大到 dst使用的填充区photoPaint

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔
        textPaint.setTextSize(destWidth / 30);//字体大小
        textPaint.setTextAlign(Align.CENTER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度
        //  textPaint.setAntiAlias(true);  //抗锯齿
        textPaint.setStrokeWidth(3);
        textPaint.setAlpha(15);
        textPaint.setStyle(Paint.Style.FILL); //空心
        textPaint.setColor(Color.WHITE);//采用的颜色
        textPaint.setShadowLayer(1f, 0f, 3f, Color.LTGRAY);

        canvas.drawText(str, destWidth / 2, destHeight - 45, textPaint);//绘制上去字，开始未知x,y采用那只笔绘制
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        bitmap.recycle();
        imageView.setImageBitmap(icon);
        imageView.setVisibility(View.VISIBLE);
        return BitmapUtils.saveBitmap(icon, String.valueOf(new Date().getTime())); //保存至文件

    }


    /*给图片添加水印*/
    public static boolean addWatermarkBitmap(Bitmap bitmap, String time, String address,String path) {



        int destWidth = bitmap.getWidth();   //此处的bitmap已经限定好宽高
        int destHeight = bitmap.getHeight();

        Bitmap icon = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888); //定好宽高的全彩bitmap
        Canvas canvas = new Canvas(icon);//初始化画布绘制的图像到icon上

        /*
        *建立画笔在画布上画一张图片
         */
        Paint photoPaint = new Paint(); //建立画笔
        photoPaint.setDither(true); //获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);//过滤一些
        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());//创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, destWidth, destHeight);//创建一个指定的新矩形的坐标
        canvas.drawBitmap(bitmap, src, dst, photoPaint);//将photo 缩放或则扩大到 dst使用的填充区photoPaint

        /*
        *建立画笔在画布上画一个矩形区域
         */
        Paint rect_Paint = new Paint();
        rect_Paint.setColor(Color.rgb(0, 0, 0));
        rect_Paint.setAlpha(80);
        rect_Paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, destHeight-80,destWidth, destHeight, rect_Paint);

        /*
        * 建立画笔在画布上画文字
        */
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔
        textPaint.setTextSize(destWidth / 30);//字体大小
        textPaint.setTextAlign(Align.LEFT);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度
        //  textPaint.setAntiAlias(true);  //抗锯齿
        textPaint.setStrokeWidth(3);
        textPaint.setAlpha(15);
        textPaint.setStyle(Paint.Style.FILL); //空心
        textPaint.setColor(Color.LTGRAY);//采用的颜色
        canvas.drawText(time, 0, destHeight - 45, textPaint);//绘制上去字，开始未知x,y采用那只笔绘制
        if (!TextUtils.isEmpty(address)){
            canvas.drawText(address, 0, destHeight - 15, textPaint);//绘制上去字，开始未知x,y采用那只笔绘制
        }


        /*
         保存释放状态
         */
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        bitmap.recycle();

        return BitmapUtils.saveBitmap(icon, path); //保存至文件

    }


}
