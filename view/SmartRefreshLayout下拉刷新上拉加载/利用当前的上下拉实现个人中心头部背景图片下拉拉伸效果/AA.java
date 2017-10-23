package com.shixiutv;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.shixiutv.utils.DensityUtils;
import com.shixiutv.utils.live_play.LiveUtils;

public class AA extends Activity {
    private int mOffset = 0;
    private int mScrollY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);

        final View parallax = findViewById(R.id.parallax);
        final NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        final RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);

        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                mOffset = offset / 2;
                parallax.setTranslationY(mOffset - mScrollY);
//                toolbar.setAlpha(1 - Math.min(percent, 1));
            }

            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                mOffset = offset / 2;
                parallax.setTranslationY(mOffset - mScrollY);
//                toolbar.setAlpha(1 - Math.min(percent, 1));
            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            private int lastScrollY = 0;
            private int h = DensityUtils.dp2px(AA.this, 200);

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    parallax.setTranslationY(mOffset - mScrollY);
                }
                lastScrollY = scrollY;
            }
        });

    }
}