package com.mycalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycalendar.view.MonthDateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.imgLeft)
    ImageView imgLeft;
    @InjectView(R.id.tvToday)
    TextView tvToday;
    @InjectView(R.id.tvDate)
    TextView tvDate;
    @InjectView(R.id.tvWeek)
    TextView tvWeek;
    @InjectView(R.id.imgRight)
    ImageView imgRight;
    @InjectView(R.id.monthDateView)
    MonthDateView monthDateView;

    List<Integer> list = new ArrayList<Integer>();
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            ButterKnife.inject(this);
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        list.add(10);

        monthDateView.setTextView(tvDate, tvWeek);
        monthDateView.setDaysHasThingList(list);
        monthDateView.setDateClick(new MonthDateView.DateClick() {
            @Override
            public void onClickOnDate() {
                Log.e(TAG, "点击了" + monthDateView.getmSelYear() + "年" + (monthDateView
                        .getmSelMonth() + 1) + "月" + monthDateView.getmSelDay() + "日");
            }
        });
    }

    @OnClick({R.id.imgLeft, R.id.tvToday, R.id.imgRight, R.id.btAdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgLeft:
                monthDateView.onLeftClick();
                break;
            case R.id.tvToday:
                monthDateView.setTodayToView();
                break;
            case R.id.imgRight:
                monthDateView.onRightClick();
                break;
            case R.id.btAdd:
                if (list.size() != 0) {
                    list.clear();
                } else {
                    list.add(10);
                }
                monthDateView.setDaysHasThingList(list);
                break;
        }
    }
}
