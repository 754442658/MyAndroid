package com.module.dialog;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.module.R;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

public class PopSelectPhoto extends PopupWindow {

	private AutoRelativeLayout btn_take_photo, btn_pick_photo;
	private AutoLinearLayout btn_cancel, layout;
	private View mMenuView;

	public PopSelectPhoto(Activity context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.view_camero, null);
		btn_take_photo =  mMenuView
				.findViewById(R.id.view_camero_rl_takephoto);
		btn_pick_photo =  mMenuView
				.findViewById(R.id.view_camero_rl_selectphoto);
		btn_cancel =  mMenuView
				.findViewById(R.id.view_camero_ll_cancel);
		// 取消按钮
		btn_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 销毁弹出框
				dismiss();
			}
		});
		layout =  mMenuView.findViewById(R.id.pop_layout);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 销毁弹出框
				dismiss();

			}
		});
		// 设置按钮监听
		btn_pick_photo.setOnClickListener(itemsOnClick);
		btn_take_photo.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}
}
