package com.mycalendar.view.pop;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mycalendar.R;
import com.mycalendar.view.wheel.model.CityModel;
import com.mycalendar.view.wheel.model.DistrictModel;
import com.mycalendar.view.wheel.model.ProvinceModel;
import com.mycalendar.view.wheel.service.XmlParserHandler;
import com.mycalendar.view.wheel.widget.OnWheelChangedListener;
import com.mycalendar.view.wheel.widget.WheelView;
import com.mycalendar.view.wheel.widget.adapters.ArrayWheelAdapter;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by ShiShow_xk on 2016/8/10.
 */
public class PopTest extends PopupWindow implements View.OnClickListener, OnWheelChangedListener {

    private View mMenuView;
    private Context context;

    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";

    // 确定，取消
    private TextView tv_ok, tv_cacle;
    // 省市区
    private WheelView wv_province, wv_city, wv_area;

    public interface CallBack {
        void onResult(String province, String city, String area);
    }

    public PopTest(Context context) {
        super(context);
        try {
            this.context = context;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mMenuView = inflater.inflate(R.layout.pop_test, null);
            // 设置SelectPicPopupWindow的View
            this.setContentView(mMenuView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体可点击
            this.setFocusable(true);
            // 设置SelectPicPopupWindow弹出窗体动画效果
            this.setAnimationStyle(R.style.AnimBottom);
            // 实例化一个ColorDrawable颜色为白色
            ColorDrawable dw = new ColorDrawable(0xffffff);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setOutsideTouchable(true);
//            mMenuView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View view, boolean b) {
//                    if (!b) {
//                        dismiss();
//                    }
//                }
//            });
            findView();
            addListener();
            initView();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void findView() {
        tv_ok = (TextView) mMenuView.findViewById(R.id.btn_ok);
        tv_cacle = (TextView) mMenuView.findViewById(R.id.btn_cacle);

        wv_province = (WheelView) mMenuView.findViewById(R.id.id_province);
        wv_city = (WheelView) mMenuView.findViewById(R.id.id_city);
        wv_area = (WheelView) mMenuView.findViewById(R.id.id_district);
    }

    private void initView() {
        initProvince();
    }

    private void addListener() {
        tv_ok.setOnClickListener(this);
        tv_cacle.setOnClickListener(this);

        wv_province.addChangingListener(this);
        wv_city.addChangingListener(this);
        wv_area.addChangingListener(this);
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btn_ok:
                    // 完成
                    if (mCurrentDistrictName.equals("昌平区")) {
                        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
                    }
                    final String ss = mCurrentProviceName + mCurrentCityName + mCurrentDistrictName;

                    Log.e("TAG", "onClick: " + ss);
                    break;
                case R.id.btn_cacle:
                    // 关闭
                    dismiss();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化省
     */
    private void initProvince() {
        initProvinceDatas();
        wv_province.setViewAdapter(new ArrayWheelAdapter<String>(context, mProvinceDatas));
        // 设置可见条目数量
        wv_province.setVisibleItems(7);
        wv_city.setVisibleItems(7);
        wv_area.setVisibleItems(7);
        initCity();
    }

    /**
     * 初始化市
     */
    private void initCity() {
        int pCurrent = wv_province.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        wv_city.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
        wv_city.setCurrentItem(0);
        initArea();
    }

    /**
     * 初始化区
     */
    private void initArea() {
        int pCurrent = wv_city.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        wv_area.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
        wv_area.setCurrentItem(0);
    }

    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }


    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == wv_province) {
            initCity();
        } else if (wheel == wv_city) {
            initArea();
        } else if (wheel == wv_area) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }
}
