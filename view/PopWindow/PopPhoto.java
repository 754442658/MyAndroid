package shixiu.mitao.window;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import shixiu.mitao.R;

/**
 * Created by ShiShow_xk on 2016/8/24.
 */
public class PopPhoto extends PopupWindow {

    private RelativeLayout btn_take_photo, btn_pick_photo;
    private LinearLayout btn_cancel, layout;
    private View mMenuView;

    public PopPhoto(Activity context, View.OnClickListener itemsOnClick) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.view_camero, null);
        btn_take_photo = (RelativeLayout) mMenuView
                .findViewById(R.id.view_camero_rl_takephoto);
        btn_pick_photo = (RelativeLayout) mMenuView
                .findViewById(R.id.view_camero_rl_selectphoto);
        btn_cancel = (LinearLayout) mMenuView
                .findViewById(R.id.view_camero_ll_cancel);
        // 取消按钮
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });
        layout = (LinearLayout) mMenuView.findViewById(R.id.pop_layout);
        layout.setOnClickListener(new View.OnClickListener() {

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
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
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
