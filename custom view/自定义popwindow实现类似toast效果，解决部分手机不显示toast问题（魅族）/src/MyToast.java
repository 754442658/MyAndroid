package com.shixiutv.view;

import com.shixiutv.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MyToast extends PopupWindow {
	private View mMenuView;

	public static void showShort(Context context, View view, String msg) {
		new MyToast(context).showShort(view, msg);
	}

	public static void showLong(Context context, View view, String msg) {
		new MyToast(context).showLong(view, msg);
	}

	private MyToast(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.toast, null);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(false);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.MyToast);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	}

	/**
	 * 显示2秒
	 * 
	 */
	private void showShort(View view, String msg) {
		TextView tv = (TextView) mMenuView.findViewById(R.id.toast_tv);
		tv.setText(msg);
		showAtLocation(view, Gravity.BOTTOM, 0, 100);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				// 2秒以后销毁
				dismiss();
			}
		}, 2000);
	}

	/**
	 * 显示3.5秒
	 * 
	 */
	private void showLong(View view, String msg) {
		TextView tv = (TextView) mMenuView.findViewById(R.id.toast_tv);
		tv.setText(msg);
		showAtLocation(view, Gravity.BOTTOM, 0, 100);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				// 2秒以后销毁
				dismiss();
			}
		}, 3500);
	}
}
