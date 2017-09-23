package com.sxrecruit.adapter.elite;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.module.utils.GlideUtils;
import com.sxrecruit.entity.BinnerData;

import java.util.ArrayList;

/**
 * Created by ShiShow_xk on 2017/8/19.
 */

public class AdapterEliteVp extends StaticPagerAdapter {
    private ArrayList<BinnerData.binnerData> list;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.listener = mOnItemClickLitener;
    }

    public AdapterEliteVp(ArrayList<BinnerData.binnerData> list) {
        this.list = list;
    }

    @Override
    public View getView(ViewGroup container, final int position) {
        ImageView view = new ImageView(container.getContext());
        GlideUtils.showPic(container.getContext(), list.get(position).getImage(), view);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (listener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(view, position);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(view, position);
                    return true;
                }
            });
        }
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
