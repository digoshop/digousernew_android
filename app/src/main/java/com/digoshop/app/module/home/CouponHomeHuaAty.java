package com.digoshop.app.module.home;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.arrountshops.adp.ArountShop_textgridAdp;
import com.digoshop.app.module.arrountshops.htextview.TextsHorizonScrollView;
import com.digoshop.app.module.home.lingquanzx.CouponListFragment;
import com.digoshop.app.module.home.lingquanzx.CouponsFragmentPagerAdapter;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.DensityUtil;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.db.CityBean;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class CouponHomeHuaAty extends FragmentActivity implements View.OnClickListener {
    private String CityCodestr = "";
    float fheingt;
    private ImageView iv_coupontype_all;


    private ArrayList<CityBean> quyulistsNew = new ArrayList<>();
    private ArrayList<CityBean> quyulistsNewpop = new ArrayList<>();
    private TextsHorizonScrollView aty_textviewsc;
    private RadioButton   text_coupon_all;
    private LinearLayout lin_coupon_type_all;
    private int typeapi = 0;  //api==0为精选接口，  1为按类型搜索接口。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_home_atyhua);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager_coupon);
        iv_coupontype_all = (ImageView) findViewById(R.id.iv_coupontype_all);
       // iv_coupontype_all.setOnClickListener(this);
        lin_coupon_type_all = (LinearLayout) findViewById(R.id.lin_coupon_type_all);
        lin_coupon_type_all.setOnClickListener(this);
        text_coupon_all = (RadioButton) findViewById(R.id.text_coupon_all);
        text_coupon_all.setOnClickListener(this);
        text_coupon_all.setOnClickListener(this);
        aty_textviewsc = (TextsHorizonScrollView) findViewById(R.id.aty_textviewsc);

        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("领券中心");
        fheingt = App.getInstance().getCouponHeight();
        CityCodestr = LocalSave.getValue(this, AppConfig.basefile, "CityCode", "");

        typeapi = 0;
        text_coupon_all.setChecked(false);
        if (getNetWifi()) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            getCouponTypeList();
        } else {
            App.getInstance()   .showToast("网络不给力，请检查网络设置");
            finish();
        }
    }



    private void getCouponTypeList() {
        if (TextUtils.isEmpty(CityCodestr)) {
            App.getInstance().showToast("定位获取失败,默认北京");
            CityCodestr = "010";
        }
        CityBean cb0 = new CityBean();
        cb0.setNn("全部分类");
        quyulistsNew.add(cb0);
        CityBean cb1 = new CityBean();
        cb1.setNn("精选");
        quyulistsNew.add(cb1);
        CityBean cb2 = new CityBean();
        cb2.setNn("商场");
        quyulistsNew.add(cb2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCouponTypeListApi();
            }
        }).start();
    }

    private void getCouponTypeListApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {

            ArrayList<CityBean>       quyulistsNewlin = api.getCouponTypeList(CityCodestr);
            if(quyulistsNewlin!=null){
                if(quyulistsNewlin.size()>0){
                    quyulistsNew.addAll(quyulistsNewlin);
                }
            }

//            if (quyulistsNew != null) {
              handler.sendEmptyMessage(23);
//            } else {
//                handler.sendEmptyMessage(24);
//            }

        } catch (JSONException e) {
            handler.sendEmptyMessage(24);
            e.printStackTrace();
        } catch (WSError e) {
            if ("A502".equals(e.getMessage())) {
                handler.sendEmptyMessage(502);
                return;
            }
            handler.sendEmptyMessage(24);
            e.printStackTrace();
        }
    }


    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    StyledDialog.dismissLoading();

                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
                case 4:
                    App.getInstance().showToast("请求异常");
                    StyledDialog.dismissLoading();

                    break;
                case 502:
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                    StyledDialog.dismissLoading();
                    finish();
                    break;
                case 23:
                    aty_textviewsc.setListData(quyulistsNew, mListener, 0);
                    aty_textviewsc.smoothScrollBy(0, 0);
                    initFragment();
                    StyledDialog.dismissLoading();
                    break;
                case 24:
                    App.getInstance().showToast("请求失败，请稍后再试");

                    StyledDialog.dismissLoading();
                    finish();
                    break;
                case 25:
                    if (null != popupWindowzonghe) {
                        popupWindowzonghe.dismiss();
                        popupWindowzonghe = null;
                    }
                    break;
            }

        }
    };
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ViewPager mViewPager;

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        int count = quyulistsNew.size();
        for (int i = 0; i < count; i++) {
            Bundle data = new Bundle();
            //"2016-09-04 12:00"
            data.putString("nn", quyulistsNew.get(i).getNn());
            data.putString("moid", quyulistsNew.get(i).getMoid()+"");
            CouponListFragment newfragment = new CouponListFragment();
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
            aty_textviewsc.setListData(quyulistsNew, mListener, position  );
            popitempostion  =position;
        }
    };


    private PopupWindow popupWindowzonghe;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_coupon_type_all:
                iv_coupontype_all.setBackgroundResource(R.drawable.xx_jiant2);
                getPopupWindowzonghe();
                // 这里是位置显示方式,在屏幕的下方
                popupWindowzonghe.showAsDropDown(v);
                break;
//            case R.id.iv_coupontype_all:
//
//                break;

        }
    }


    ArrayList<CityBean> quyulists;

    /***
     * 获取综合搜索pop
     */
    private void getPopupWindowzonghe() {
        if (null != popupWindowzonghe) {
            popupWindowzonghe.dismiss();
            return;
        } else {
            initPopuptWindowzonghe();
        }
    }


    ArountShop_textgridAdp arountShop_textAdp;
    private int popitempostion;

    /**
     * 创建商品类别pop
     */
    protected void initPopuptWindowzonghe() {

        if (quyulistsNewpop != null) {
            quyulistsNewpop.clear();
        }
//        CityBean cb1 = new CityBean();
//        cb1.setNn("精选");
//        quyulistsNewpop.add(cb1);
//        CityBean cb2 = new CityBean();
//        cb2.setNn("商场");
//        quyulistsNewpop.add(cb2);
        quyulistsNewpop.addAll(quyulistsNew);
        arountShop_textAdp = new ArountShop_textgridAdp(this, quyulistsNewpop);
        View popupWindow_view = getLayoutInflater().inflate(
                R.layout.fragment_arountshop_gridview, null, false);
        final GridView gv_coupin_typepop = (GridView) popupWindow_view.findViewById(R.id.gv_coupin_typepop);
        TextView tv_coupontypenum = (TextView) popupWindow_view.findViewById(R.id.tv_coupontypenum);
        tv_coupontypenum.setText("全部" + quyulistsNewpop.size() + "个分类");
        gv_coupin_typepop.setAdapter(arountShop_textAdp);
        arountShop_textAdp.setChoicePos(popitempostion);
        arountShop_textAdp.notifyDataSetChanged();
        gv_coupin_typepop.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
                handler.sendMessageDelayed(handler.obtainMessage(25), 50);
                iv_coupontype_all.setBackgroundResource(R.drawable.xx_jiant);
                popitempostion = postion;
                arountShop_textAdp.setChoicePos(postion);
                arountShop_textAdp.notifyDataSetChanged();
                text_coupon_all.setChecked(false);
                typeapi = 1;
                int offest = (postion - 1) * getResources().getDimensionPixelSize(R.dimen.base_dimen_60);
                aty_textviewsc.smoothScrollTo((postion - 1) * getResources().getDimensionPixelSize(R.dimen.base_dimen_100) + offest, 0);
                aty_textviewsc.setListData(quyulistsNew, mListener, postion);
               mViewPager.setCurrentItem(postion);

            }
        });
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        float sizeh = 0;
        if (quyulistsNewpop.size() % 3 == 0) {
            sizeh = (quyulistsNewpop.size() / 3) * fheingt;
        } else {
            sizeh = (quyulistsNewpop.size() / 3 + 1) * fheingt;
        }
        popupWindowzonghe = new PopupWindow(popupWindow_view,
                ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.floatToInt(sizeh), true);
        popupWindowzonghe.setFocusable(true);
        popupWindowzonghe.setBackgroundDrawable(new BitmapDrawable());
        popupWindowzonghe.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv_coupontype_all.setBackgroundResource(R.drawable.xx_jiant);
            }
        });
        // 设置动画效果
        popupWindowzonghe.setAnimationStyle(android.R.style.Animation_Toast);
        // popupWindow.setAnimationStyle(R.style.AnimationFade);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindowzonghe != null && popupWindowzonghe.isShowing()) {
                    popupWindowzonghe.dismiss();
                    popupWindowzonghe = null;
                }
                iv_coupontype_all.setBackgroundResource(R.drawable.xx_jiant);

                return false;
            }
        });
    }



    /**
     * 实现类，响应按钮点击事件
     */
    private TextsHorizonScrollView.TextTypeClickListener mListener = new TextsHorizonScrollView.TextTypeClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            text_coupon_all.setChecked(false);
            typeapi = 1;
            popitempostion = position + 1;
            int offest = (position - 1) * getResources().getDimensionPixelSize(R.dimen.base_dimen_60);
            aty_textviewsc.smoothScrollTo((position - 1) * getResources().getDimensionPixelSize(R.dimen.base_dimen_100) + offest, 0);
            aty_textviewsc.setListData(quyulistsNew, mListener, position);
            mViewPager.setCurrentItem(position);

        }
    };

    public void OnTitleReturnClick(View v){
        finish();
    }
}
