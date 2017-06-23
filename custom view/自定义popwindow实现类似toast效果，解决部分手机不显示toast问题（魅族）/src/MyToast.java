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
		// ����SelectPicPopupWindow��View
		this.setContentView(mMenuView);
		// ����SelectPicPopupWindow��������Ŀ�
		this.setWidth(LayoutParams.WRAP_CONTENT);
		// ����SelectPicPopupWindow��������ĸ�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// ����SelectPicPopupWindow��������ɵ��
		this.setFocusable(false);
		// ����SelectPicPopupWindow�������嶯��Ч��
		this.setAnimationStyle(R.style.MyToast);
		// ʵ����һ��ColorDrawable��ɫΪ��͸��
		ColorDrawable dw = new ColorDrawable(0x00000000);
		// ����SelectPicPopupWindow��������ı���
		this.setBackgroundDrawable(dw);
	}

	/**
	 * ��ʾ2��
	 * 
	 */
	private void showShort(View view, String msg) {
		TextView tv = (TextView) mMenuView.findViewById(R.id.toast_tv);
		tv.setText(msg);
		showAtLocation(view, Gravity.BOTTOM, 0, 100);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				// 2���Ժ�����
				dismiss();
			}
		}, 2000);
	}

	/**
	 * ��ʾ3.5��
	 * 
	 */
	private void showLong(View view, String msg) {
		TextView tv = (TextView) mMenuView.findViewById(R.id.toast_tv);
		tv.setText(msg);
		showAtLocation(view, Gravity.BOTTOM, 0, 100);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				// 2���Ժ�����
				dismiss();
			}
		}, 3500);
	}
}
