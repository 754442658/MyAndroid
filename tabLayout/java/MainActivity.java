package com.xal.texttablayout;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager vp;

    FragmentAdapter adapter;
    ArrayList<Fragment> list = new ArrayList<>();
    ArrayList<String> listTitle = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        vp = (ViewPager) findViewById(R.id.vp);

        listTitle.add("时政要闻");
        listTitle.add("政策解读");
        listTitle.add("安环生活");
        listTitle.add("安环工具");
        listTitle.add("资料百科");
        listTitle.add("事故案例");
        listTitle.add("鑫保险");
        listTitle.add("阅读专题");

        for (int i = 0; i < listTitle.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(listTitle.get(i)));
            list.add(MyFragment.newInstence(listTitle.get(i)));
        }

        adapter = new FragmentAdapter(getSupportFragmentManager(), list, listTitle);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(vp);
    }
}
