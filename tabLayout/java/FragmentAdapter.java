package com.xal.texttablayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/27 0027.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> list;
    ArrayList<String> listTitle;

    public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> list, ArrayList<String> listTitle) {
        super(fm);
        this.list = list;
        this.listTitle = listTitle;
    }


    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle.get(position);
    }
}
