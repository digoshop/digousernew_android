package com.digoshop.app.module.home.couponactive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.base.BaseFragmentActivity;
import com.digoshop.app.module.home.adp.MyFragmentPagerAdapter;
import com.digoshop.app.module.home.fragment.GetCouponCenterFragment;
import com.digoshop.app.module.home.fragment.GoodAtyFragment;
import com.digoshop.app.module.home.view.ViewPagerCompat;

import java.util.ArrayList;

/**
 * TODO 首页--够优惠页面
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016年6月22日 下午8:57:02
 * @version: V1.0
 */
public class CouponActyActivity extends BaseFragmentActivity implements OnClickListener {
    private TextView tv_couponactive_active, tv_couponactive_coupon;
    private LinearLayout re_couponactive_type;
    private ViewPagerCompat mPager;
    private ArrayList<Fragment> fragmentList;
    private Fragment getcouponcenterfragment, goodatyfragment;
    private int pageint = 0;

    @Override
    public boolean isNoTitle() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        // 去掉窗口标题
        setContentView(R.layout.couponactactivity);
        mPager = (ViewPagerCompat) findViewById(R.id.viewpager);
        re_couponactive_type = (LinearLayout) findViewById(R.id.re_couponactive_type);
        tv_couponactive_active = (TextView) findViewById(R.id.tv_couponactive_active);
        tv_couponactive_coupon = (TextView) findViewById(R.id.tv_couponactive_coupon);
        tv_couponactive_active.setOnClickListener(this);
        tv_couponactive_coupon.setOnClickListener(this);
        Intent intent = getIntent();
        if (!TextUtils.isEmpty(intent.getStringExtra("pageint"))) {
            pageint = Integer.valueOf(intent.getStringExtra("pageint")).intValue();
        }

        InitViewPager();

    }

    private void InitViewPager() {

        mPager = (ViewPagerCompat) findViewById(R.id.viewpager);
        fragmentList = new ArrayList<Fragment>();
        getcouponcenterfragment = new GetCouponCenterFragment();
        goodatyfragment = new GoodAtyFragment();
        fragmentList.add(getcouponcenterfragment);
        fragmentList.add(goodatyfragment);
        // 给ViewPager设置适配器
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        Viewtag(pageint);
    }

    private void Viewtag(int type) {
        if (type == 0) {
            mPager.setCurrentItem(pageint);// 设置当前显示标签页为第一页
            tv_couponactive_active.setTextColor(getResources().getColor(R.color.white_text));
            tv_couponactive_active.setBackgroundResource(R.drawable.switch_button_right);
            tv_couponactive_coupon.setTextColor(getResources().getColor(R.color.black_text));
            tv_couponactive_coupon.setBackgroundResource(R.drawable.switch_button_left_checked);
//            re_couponactive_type.setBackgroundResource(R.drawable.gouyouhui_banner2);
        } else if (type == 1) {
            mPager.setCurrentItem(pageint);// 设置当前显示标签页为第一页
            tv_couponactive_active.setTextColor(getResources().getColor(R.color.black_text));
            tv_couponactive_active.setBackgroundResource(R.drawable.switch_button_right_checked);
            tv_couponactive_coupon.setTextColor(getResources().getColor(R.color.white_text));
            tv_couponactive_coupon.setBackgroundResource(R.drawable.switch_button_left);
//            re_couponactive_type.setBackgroundResource(R.drawable.gouyouhui_banner1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_couponactive_coupon:
                pageint = 0;
                Viewtag(pageint);
                break;
            case R.id.tv_couponactive_active:
                pageint = 1;
                Viewtag(pageint);
                break;
            default:
                break;
        }
    }

}
