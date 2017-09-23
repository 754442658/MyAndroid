package com.module.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.module.R;

/**
 * Created by ShiShow_xk on 2017/7/11.
 */
public class BasePopWindow extends PopupWindow {
    // 用法如下  f1是要显示的界面的最外层布局ID
    // new BasePopWindow().showAtLocation(findViewById(R.id.fl),Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置


    public View view;
    public Context context;
    // 弹出 关闭动画  R.style.AnimBottom
    public int anim = 0;
    // 弹框宽  ScreenUtils.getScreenHeight(context) / 3    LayoutParams.FILL_PARENT
    public int width;
    // 弹框高  ScreenUtils.getScreenHeight(context) / 3
    public int height;
    public ColorDrawable dw;

    public void init(int resId) {
        try {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resId, null);
            // 设置SelectPicPopupWindow的View
            this.setContentView(view);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(width);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(height);
            // 设置SelectPicPopupWindow弹出窗体可点击
            this.setFocusable(true);
            // 设置SelectPicPopupWindow弹出窗体动画效果 可有可无,有一个默认的
            if (anim != 0) {
                this.setAnimationStyle(anim);
            } else {
                // 默认弹出动画
                this.setAnimationStyle(R.style.AnimBottom);
            }
            // 实例化一个ColorDrawable颜色为半透明
            if (dw == null)
                dw = new ColorDrawable(0x00000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            // 触摸外面销毁
            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        dismiss();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
