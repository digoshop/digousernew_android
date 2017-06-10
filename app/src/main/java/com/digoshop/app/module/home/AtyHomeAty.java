package com.digoshop.app.module.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.module.home.huodongzx.AtyListFragment;
import com.digoshop.app.module.home.huodongzx.AtysFragmentPagerAdapter;
import com.digoshop.app.utils.db.CityBean;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class AtyHomeAty extends FragmentActivity implements View.OnClickListener {
    private RadioButton text_homeaty_titlea, text_homeaty_titleb, text_homeaty_titlec;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ViewPager mViewPager;
    private ArrayList<CityBean> quyulistsNew = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_home_aty);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager_aty);
        text_homeaty_titlea = (RadioButton) findViewById(R.id.text_homeaty_titlea);
        text_homeaty_titleb = (RadioButton) findViewById(R.id.text_homeaty_titleb);
        text_homeaty_titlec = (RadioButton) findViewById(R.id.text_homeaty_titlec);
        text_homeaty_titlea.setOnClickListener(this);
        text_homeaty_titleb.setOnClickListener(this);
        text_homeaty_titlec.setOnClickListener(this);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("优惠活动");
        if(getNetWifi()){
            initFragment();
        }else{
            App.getInstance().showToast("网络不给力，请检查网络设置");
            finish();
        }

        text_homeaty_titlea.setChecked(true);
        text_homeaty_titleb.setChecked(false);
        text_homeaty_titlec.setChecked(false);
    }

    /**
     * 初始化Fragment
     */

    private void initFragment() {

        CityBean cb1 = new CityBean();
        cb1.setNn("2");
        quyulistsNew.add(cb1);
        CityBean cb2 = new CityBean();
        cb2.setNn("1");
        quyulistsNew.add(cb2);
        CityBean cb3 = new CityBean();
        cb3.setNn("0");
        quyulistsNew.add(cb3);
        int count = quyulistsNew.size();
        for (int i = 0; i < count; i++) {
            Bundle data = new Bundle();
            data.putString("sort", quyulistsNew.get(i).getNn());
            AtyListFragment newfragment = new AtyListFragment();
            newfragment.setArguments(data);
            fragments.add(newfragment);
        }
        AtysFragmentPagerAdapter mAdapetr = new AtysFragmentPagerAdapter(getSupportFragmentManager(), fragments);
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
            if ("0".equals(position+"")) {
                text_homeaty_titlea.setChecked(true);
                text_homeaty_titleb.setChecked(false);
                text_homeaty_titlec.setChecked(false);
            } else if ("1".equals(position+"")) {
                text_homeaty_titlea.setChecked(false);
                text_homeaty_titleb.setChecked(true);
                text_homeaty_titlec.setChecked(false);
            } else if ("2".equals(position+"")) {
                text_homeaty_titlea.setChecked(false);
                text_homeaty_titleb.setChecked(false);
                text_homeaty_titlec.setChecked(true);
            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_homeaty_titlea:
              //  StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                text_homeaty_titlea.setChecked(true);
                text_homeaty_titleb.setChecked(false);
                text_homeaty_titlec.setChecked(false);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.text_homeaty_titleb:
              //  StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                text_homeaty_titlea.setChecked(false);
                text_homeaty_titleb.setChecked(true);
                text_homeaty_titlec.setChecked(false);
                mViewPager.setCurrentItem(1);
                break;
            case R.id.text_homeaty_titlec:
                text_homeaty_titlea.setChecked(false);
                text_homeaty_titleb.setChecked(false);
                text_homeaty_titlec.setChecked(true);
                mViewPager.setCurrentItem(2);
                break;

            default:
                break;
        }
    }

    public void OnTitleReturnClick(View v) {
        finish();
    }


}
