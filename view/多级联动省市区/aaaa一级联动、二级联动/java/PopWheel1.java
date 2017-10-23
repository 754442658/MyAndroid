package com.sxrecruit.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.module.base.BaseFunction;
import com.module.dialog.BasePopWindow;
import com.sxrecruit.R;
import com.sxrecruit.view.wheel.widget.OnWheelChangedListener;
import com.sxrecruit.view.wheel.widget.WheelView;
import com.sxrecruit.view.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * Created by ShiShow_xk on 2017/8/19.
 */

public class PopWheel1 extends BasePopWindow implements BaseFunction, View.OnClickListener, OnWheelChangedListener {
    private final String title;
    private final CallBack callBack;
    private final int size;
    WheelView wv1, wv2, wv3;
    TextView tvCancle, tvOk, tvTitle;
    View dismiss;

    // 数据源
    String[] list;
    // 当前选中的数据
    String curData;
    // 选中的id
    int curID;

    public interface CallBack {
        void onResult(String arg1, int position);
    }

    /**
     * @param context
     * @param list     数据源
     * @param title    中间标题
     * @param size     显示条目
     * @param callBack 当前选中的对象
     */
    public PopWheel1(Context context, String[] list, String title, int size, CallBack callBack) {
        this.context = context;
        this.title = title;
        this.callBack = callBack;
        this.list = list;
        this.size = size;
        width = ViewGroup.LayoutParams.MATCH_PARENT;
        height = ViewGroup.LayoutParams.MATCH_PARENT;
        dw = new ColorDrawable(0x80000000);
        init(R.layout.pop_wheel);
        initView();
        addListener();
    }

    @Override
    public void initView() {
        wv1 = view.findViewById(R.id.wv1);
        wv2 = view.findViewById(R.id.wv2);
        wv3 = view.findViewById(R.id.wv3);
        tvOk = view.findViewById(R.id.tvOk);
        tvCancle = view.findViewById(R.id.tvCancle);
        tvTitle = view.findViewById(R.id.tvTitle);
        dismiss = view.findViewById(R.id.dismiss);

        wv2.setVisibility(View.GONE);
        wv3.setVisibility(View.GONE);

        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<>(context, list);
        adapter.setItemResource(R.layout.item_wheel);
        adapter.setItemTextResource(R.id.tv);

        wv1.setDrawShadows(false);
        wv1.setViewAdapter(adapter);
        // 设置可见条目数量
        wv1.setVisibleItems(size);
        tvTitle.setText(title);

        curData = list[0];
        curID = 0;
    }


    @Override
    public void addListener() {
        tvOk.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
        dismiss.setOnClickListener(this);
        wv1.addChangingListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvOk:
                callBack.onResult(curData, curID);
                dismiss();
                break;
            case R.id.tvCancle:
                dismiss();
                break;
            case R.id.dismiss:
                dismiss();
                break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        curData = list[newValue];
        curID = newValue;
    }
}
