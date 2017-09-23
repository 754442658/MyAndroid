package com.sxrecruit.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.module.base.BaseFunction;
import com.module.dialog.BasePopWindow;
import com.module.utils.L;
import com.module.utils.StatusBarUtils;
import com.sxrecruit.R;
import com.sxrecruit.view.wheel.widget.OnWheelChangedListener;
import com.sxrecruit.view.wheel.widget.WheelView;
import com.sxrecruit.view.wheel.widget.adapters.ArrayWheelAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ShiShow_xk on 2017/8/19.
 */

public class PopWheel2 extends BasePopWindow implements BaseFunction, View.OnClickListener, OnWheelChangedListener {
    WheelView wv1, wv2, wv3;
    TextView tvCancle, tvOk, tvTitle;
    String title;
    CallBack callBack;
    int size, curPosition1;
    View dismiss;

    String[] data1, data2;
    Map<String, String[]> map = new HashMap<String, String[]>();

    String curData1, curData2;

    public interface CallBack {
        void onResult(String arg1);
    }

    /**
     *
     * @param context   上下文对象
     * @param data1     一级菜单的数据源
     * @param map       二级菜单和一级菜单对应的map
     * @param title     菜单标题
     * @param size      菜单显示最大条数
     * @param curPosition1  当前选中的条目下标
     * @param callBack      选中后的回调
     */
    public PopWheel2(Context context, String[] data1, Map<String, String[]> map, String title, int size, int curPosition1, PopWheel2.CallBack callBack) {
        this.context = context;
        this.title = title;
        this.data1 = data1;
        this.map = map;
        this.size = size;
        this.curPosition1 = curPosition1;
        this.callBack = callBack;
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
        dismiss = view.findViewById(R.id.dismiss);
        tvCancle = view.findViewById(R.id.tvCancle);
        tvTitle = view.findViewById(R.id.tvTitle);
        wv3.setVisibility(View.GONE);

        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<>(context, data1);
        adapter.setItemResource(R.layout.item_wheel);
        adapter.setItemTextResource(R.id.tv);

        wv1.setDrawShadows(false);
        wv1.setViewAdapter(adapter);
        // 设置可见条目数量
        wv1.setVisibleItems(size);
        wv1.setCurrentItem(curPosition1);
        tvTitle.setText(title);
        initData2();
        curData1 = data1[curPosition1];
    }

    private void initData2() {
        int pCurrent = wv1.getCurrentItem();
        curData1 = data1[pCurrent];
        String[] cities = map.get(curData1);
        if (cities == null) {
            cities = new String[]{""};
        }
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<>(context, cities);
        adapter.setItemResource(R.layout.item_wheel);
        adapter.setItemTextResource(R.id.tv);


        wv2.setDrawShadows(false);
        wv2.setViewAdapter(adapter);
        // 设置可见条目数量
        wv2.setVisibleItems(size);
        wv2.setCurrentItem(0);


        if (map.get(curData1) == null)
            curData2 = "";
        else
            curData2 = map.get(curData1)[0];
    }

    @Override
    public void addListener() {
        tvOk.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
        wv1.addChangingListener(this);
        wv2.addChangingListener(this);
        dismiss.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvOk:
                callBack.onResult(curData1 + curData2);
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
        if (wheel == wv1) {
            initData2();
        } else if (wheel == wv2) {
            if (map.get(curData1) == null)
                curData2 = "";
            else
                curData2 = map.get(curData1)[newValue];
        }
    }
}
