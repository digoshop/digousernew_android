package com.digoshop.app.module.storedetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO<商场详情-优惠活动>
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016年7月21日 下午11:31:01
 * @version: V1.0
 */
public class Preferential_activityFragment extends Fragment implements
        OnClickListener {
    private ImageView img_return;

    private View rootView;
    private RelativeLayout re_couponactive_type;
    private TextView tv_couponactive_active, tv_shopnomsg, tv_couponactive_coupon;
    private ViewPager mPaper;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = View.inflate(getActivity(),
                    R.layout.fragment_preferential_activity, null);
            img_return = (ImageView) rootView.findViewById(R.id.img_return);
           img_return.setOnClickListener(this);


            re_couponactive_type = (RelativeLayout) rootView
                    .findViewById(R.id.re_couponactive_type);
            tv_couponactive_active = (TextView) rootView
                    .findViewById(R.id.tv_couponactive_active);
            tv_couponactive_coupon = (TextView) rootView
                    .findViewById(R.id.tv_couponactive_coupon);

            tv_couponactive_active.setOnClickListener(this);
            tv_couponactive_coupon.setOnClickListener(this);
            mPaper = (ViewPager) rootView.findViewById(R.id.store_couponaty_view_pager);
            StoreCouponListFragment f1 = new StoreCouponListFragment();
            StroeAtyListFragment f2 = new StroeAtyListFragment();
            mFragments.add(f1);
            mFragments.add(f2);
            mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {

                @Override
                public int getCount() {
                    return mFragments.size();
                }

                @Override
                public Fragment getItem(int arg0) {
                    return mFragments.get(arg0);
                }
            };
            mPaper.setAdapter(mAdapter);
            mPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                private int currentIndex;

                @Override
                public void onPageSelected(int position) {
                    switch (position) {
                        case 0:
                            tv_couponactive_active.setTextColor(getResources().getColor(R.color.white_text));
                            tv_couponactive_coupon.setTextColor(getResources().getColor(R.color.black_text));
                            re_couponactive_type.setBackgroundResource(R.drawable.gouyouhui_banner2);
                            break;
                        case 1:

                            tv_couponactive_active.setTextColor(getResources().getColor(R.color.black_text));
                            tv_couponactive_coupon.setTextColor(getResources().getColor(R.color.white_text));
                            re_couponactive_type.setBackgroundResource(R.drawable.gouyouhui_banner1);
                            break;

                    }
                    currentIndex = position;
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {

                }
            });

        }
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_return:
                getActivity().finish();
                break;
            case R.id.tv_couponactive_coupon://优惠券列表
                mPaper.setCurrentItem(0);
                tv_couponactive_active.setTextColor(getResources().getColor(R.color.white_text));
                tv_couponactive_coupon.setTextColor(getResources().getColor(R.color.black_text));
                re_couponactive_type.setBackgroundResource(R.drawable.gouyouhui_banner2);
                break;
            case R.id.tv_couponactive_active://活动列表
                mPaper.setCurrentItem(1);
                tv_couponactive_active.setTextColor(getResources().getColor(R.color.black_text));
                tv_couponactive_coupon.setTextColor(getResources().getColor(R.color.white_text));
                re_couponactive_type.setBackgroundResource(R.drawable.gouyouhui_banner1);
                break;
            default:
                break;
        }
    }

}
