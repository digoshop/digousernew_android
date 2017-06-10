package com.digoshop.app.module.arrountshops;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.arrountshops.htextview.TextsHorizonScrollView;
import com.digoshop.app.module.arrountshops.model.ArrountitemBean;
import com.digoshop.app.module.arrountshops.model.MyArrayListCityBean;
import com.digoshop.app.module.home.lingquanzx.CouponsFragmentPagerAdapter;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.db.CityBean;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class ArountActivity extends FragmentActivity {
    private String CityCodestr = "";
    private String latstr = "";
    private String logstr = "";
    private RelativeLayout re_nolist;
    float fheingt;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ViewPager mViewPager;
    private TextsHorizonScrollView aty_textviewsc;
    private ArrayList<CityBean> quyulistsNew = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shops);
        fheingt = App.getInstance().getSelectHeight();
        mViewPager = (ViewPager) findViewById(R.id.mViewPager_arount);
        aty_textviewsc = (TextsHorizonScrollView) findViewById(R.id.aty_textviewsc);
        CityCodestr = LocalSave.getValue(ArountActivity.this, AppConfig.basefile, "CityCode", "");
        Log.v("ceshi","**********************CityCodestr"+CityCodestr);
        latstr = LocalSave.getValue(ArountActivity.this, AppConfig.basefile, "lat", "");

        logstr = LocalSave.getValue(ArountActivity.this, AppConfig.basefile, "lon", "");
        re_nolist = (RelativeLayout) findViewById(R.id.re_nolist);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("游周边");

        if (getNetWifi()) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

            getAroutType();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }
    }


    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    if (re_nolist != null) {
                        re_nolist.setVisibility(View.GONE);
                    }
                    StyledDialog.dismissLoading();

                    break;
                case 2:

                    StyledDialog.dismissLoading();

                    break;
                case 3:
                    re_nolist.setVisibility(View.VISIBLE);
                    //  App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();
                    break;
                case 4:
                    //re_nolist.setVisibility(View.VISIBLE);
                    StyledDialog.dismissLoading();
                    break;
                case 24:
                    App.getInstance().showToast("暂无数据!");
                    break;
                case 23:
                    MyArrayListCityBean myArrayListCityBean = new MyArrayListCityBean();
                    myArrayListCityBean.setTagName("全部");
                    myArrayListCityBeens.add(myArrayListCityBean);
                    if(quyulistsNew!=null){
                        quyulistsNew.clear();
                    }
                    for(int i =0;i<myArrayListCityBeens.size();i++){
                        CityBean cityBean = new CityBean();
                        cityBean.setNn(myArrayListCityBeens.get(i).getTagName());
                        quyulistsNew.add(cityBean);
                    }
                    aty_textviewsc.setListData(quyulistsNew, mListener, 0,1);
                    aty_textviewsc.smoothScrollBy(0, 0);

                    initFragment();
                    break;


            }

        }
    };



    private ArrayList<MyArrayListCityBean> myArrayListCityBeens;

    private ArrayList<ArrountitemBean> arrountitemBeens;

    private void getAroutType() {
        if (TextUtils.isEmpty(CityCodestr)) {
            App.getInstance().showToast("定位获取失败,默认北京");
            CityCodestr = "010";
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                getAroutTypeapi();
            }
        }).start();

    }

    private void getAroutTypeapi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            myArrayListCityBeens = api.getArroutTypeList(CityCodestr);
            if (myArrayListCityBeens != null) {
                handler.sendEmptyMessage(23);
            } else {
                handler.sendEmptyMessage(24);
            }

        } catch (JSONException e) {
            handler.sendEmptyMessage(24);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(24);
            e.printStackTrace();
        }
    }
    /**
     * 初始化Fragment
     */
    private void initFragment() {
        int count = myArrayListCityBeens.size();
        for (int i = 0; i < count; i++) {
            Bundle data = new Bundle();
            //"2016-09-04 12:00"
            data.putSerializable("list", myArrayListCityBeens.get(i).getArrountitemBeens());
            ArountListFragment newfragment = new ArountListFragment();
            newfragment.setArguments(data);
            fragments.add(newfragment);
        }
        CouponsFragmentPagerAdapter mAdapetr = new CouponsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);
    }

    /**
     * ViewPager切换监听方法
     */
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            mViewPager.setCurrentItem(position);
            //移动tag位置
            int offest = (position-1 ) * getResources().getDimensionPixelSize(R.dimen.base_dimen_60);
            aty_textviewsc.smoothScrollTo((position-1 ) * getResources().getDimensionPixelSize(R.dimen.base_dimen_100) + offest, 0);
            aty_textviewsc.setListData(quyulistsNew, mListener, position ,1 );
        }
    };


    /**
     * 实现类，响应按钮点击事件
     */
    private TextsHorizonScrollView.TextTypeClickListener mListener = new TextsHorizonScrollView.TextTypeClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            int offest = (position - 1) * getResources().getDimensionPixelSize(R.dimen.base_dimen_60);
            aty_textviewsc.smoothScrollTo((position - 1) * getResources().getDimensionPixelSize(R.dimen.base_dimen_100) + offest, 0);
            aty_textviewsc.setListData(quyulistsNew, mListener, position,1);
            mViewPager.setCurrentItem(position);

        }
    };
    public void OnTitleReturnClick(View v){
        finish();
    }
}
