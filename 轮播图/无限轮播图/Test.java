package shixiu.mitao.test;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import shixiu.mitao.R;

public class Test extends AppCompatActivity {
    private static final String TAG = "Test";
    ViewPager mpViewPager;
    LinearLayout ll;

    // 轮播图片地址集合
    private ArrayList<String> list;
    // viewpager底部的小圆点的集合
    private static ArrayList<View> viewList = new ArrayList<View>();
    // 轮播图片控件集合
    private static ArrayList<ImageView> imgList = new ArrayList<ImageView>();
    // 动态添加轮播图片底部圆点的布局属性
    private static LinearLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        initData();
        addListener();
        getNetData();
    }

    /**
     * 初始化圆点属性
     */
    private void initData() {
        params = new LinearLayout.LayoutParams(12, 12);
        params.leftMargin = 4;
        params.rightMargin = 4;
    }

    /**
     * 添加监听事件
     */
    private void addListener() {

        mpViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

                if (arg0 == 0) {
                    Log.e(TAG, "onPageScrollStateChanged: 当前页是" + mpViewPager.getCurrentItem());
                    for (int i = 0; i < viewList.size(); i++) {
                        int currentPage = (mpViewPager.getCurrentItem())%5;
                        if (i == currentPage) {
                            viewList.get(i).setBackgroundResource(
                                    R.mipmap.selected);
                        } else {
                            viewList.get(i).setBackgroundResource(
                                    R.mipmap.unselected);
                        }
                    }
                }
            }
        });
    }

    private void initView() {
        mpViewPager = (ViewPager) findViewById(R.id.vp);
        ll = (LinearLayout) findViewById(R.id.ll);
    }

    /**
     * 联网获取轮播图片路径
     */
    public void getNetData() {

        list = new ArrayList<>();
        list.add("http://icon.nipic.com/BannerPic/20160612/indexdesign/20160612162037.jpg");
        list.add("http://icon.nipic.com/BannerPic/20160612/indexdesign/20160612162055.jpg");
        list.add("http://icon.nipic.com/BannerPic/20160612/indexdesign/20160612162116.jpg");
        list.add("http://icon.nipic.com/BannerPic/20160612/indexdesign/20160612162131.jpg");
        list.add("http://icon.nipic.com/BannerPic/20160615/indexdesign/20160615100811.jpg");

        setView();
    }

    private void setView() {
        imgList.clear();
        viewList.clear();
        for (int i = 0; i < list.size(); i++) {
            final ImageView img = new ImageView(this);
            img.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));

            img.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this).load(list.get(i)).asBitmap().diskCacheStrategy(DiskCacheStrategy
                    .ALL).placeholder(R.mipmap.ic_launcher).into(img);

            img.setTag(list.get(i));
            img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Log.e("click",
                            ((String) img.getTag()));
                }
            });
            imgList.add(img);

            View view = new View(this);
            view.setBackgroundResource(R.mipmap.unselected);
            ll.addView(view, params);
            viewList.add(view);
        }

        PicturePagerAdapter adapter = new PicturePagerAdapter(imgList);
        mpViewPager.setAdapter(adapter);
        mpViewPager.setCurrentItem(0);
        viewList.get(0).setBackgroundResource(R.mipmap.selected);
    }
}
