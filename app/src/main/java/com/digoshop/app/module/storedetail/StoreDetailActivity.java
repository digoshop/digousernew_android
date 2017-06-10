package com.digoshop.app.module.storedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.base.BaseFragmentActivity;
import com.digoshop.app.module.storedetail.model.FloorBean;

import java.util.ArrayList;

/**
 * TODO<商场详情>
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016年7月16日 上午10:47:22
 * @version: V1.0
 */
public class StoreDetailActivity extends BaseFragmentActivity {
    private FragmentTabHost mTabHost;
    private LayoutInflater mLayoutInflater;
    private Class mFragmentArray[] = {Shopping_overviewFragment.class, Commercial_distributionFragment.class,
            Preferential_activityFragment.class};
    private String mTextArray[] = {"商场概况", "商业布局", "优惠活动"};
    public static String storeid = null;

    public static String tatolstr = null ;

    public static ArrayList<FloorBean> floorBeans;
    public static int istag = 0;
    public static int screenWidthstore;

    @Override
    public boolean isNoTitle() {
        return false;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_storedetial_home);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidthstore = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        Intent intent = getIntent();
        storeid = intent.getStringExtra("storeid");
        if (TextUtils.isEmpty(storeid)) {
            finish();
            App.getInstance().showToast("商场id为空");

        }
        initView();

    }

    /**
     * 初始化组件
     */
    private void initView() {
        mLayoutInflater = LayoutInflater.from(this);
        // 找到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        // 得到fragment的个数
        int count = mFragmentArray.length;
        for (int i = 0; i < count; i++) {
            // 给每个Tab按钮设置图标、文字和内容
            TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i]).setIndicator(getTabItemView(i));
            // 将Tab按钮添	加进Tab选项卡中
            mTabHost.addTab(tabSpec, mFragmentArray[i], null);
            // 设置Tab按钮的背景
            // mTabHost.getTabWidget().getChildAt(i)
            // .setBackgroundResource(R.drawable.selector_tab_background);
        }
    }

    /**
     * 给每个Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = mLayoutInflater.inflate(R.layout.activity_storedetail_tab_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_storedetail_tab);
        textView.setText(mTextArray[index]);
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        istag = 0;
    }
}