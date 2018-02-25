package com.xal.texttablayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/11/27 0027.
 */

public class MyFragment extends Fragment {
    View view;
    TextView tv;
    String text;

    public static MyFragment newInstence(String text) {
        Bundle bundle = new Bundle();
        bundle.putString("text", text);
        MyFragment fragment = new MyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        text = getArguments().getString("text");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment, null);
        tv = view.findViewById(R.id.tv);
        tv.setText(text);
        return view;

    }
}
