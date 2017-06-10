package com.digoshop.app.module.looksales.poitsale;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.arrountshops.htextview.TextsHorizonScrollView;
import com.digoshop.app.module.arrountshops.model.ArrountitemBean;
import com.digoshop.app.module.arrountshops.model.MyArrayListCityBean;
import com.digoshop.app.module.home.jifenhg.SaleListFragment;
import com.digoshop.app.module.home.jifenhg.SalesFragmentPagerAdapter;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.db.CityBean;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * TODO<首页--看促销页面--积分换购>
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016年7月26日 上午12:04:37
 * @version: V1.0
 */
public class SaleActivity extends FragmentActivity {
    private String CityCodestr = "";
    private ArrayList<CityBean> quyulistsNew = new ArrayList<>();
    TextsHorizonScrollView sale_textviewsc;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager_sale);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("积分兑换");
        CityCodestr = LocalSave.getValue(SaleActivity.this, AppConfig.basefile, "CityCode", "");
        sale_textviewsc = (TextsHorizonScrollView) findViewById(R.id.sale_textviewsc);
        if (getNetWifi()) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            getAroutType();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
            finish();
        }

    }

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

    private ArrayList<MyArrayListCityBean> myArrayListCityBeens;
    private void getAroutTypeapi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            myArrayListCityBeens = api.getSaleTypeList(CityCodestr);
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
            if( myArrayListCityBeens.get(i).getArrountitemBeens()!=null){
                if( myArrayListCityBeens.get(i).getArrountitemBeens().size()>0){
                    data.putSerializable("list", myArrayListCityBeens.get(i).getArrountitemBeens());

                }else{
                    ArrayList<ArrountitemBean> arrountitemBeenS = new ArrayList<>();
                    ArrountitemBean arrountitemBean = new ArrountitemBean();
                    arrountitemBean.setMoid(myArrayListCityBeens.get(i).getMoid());
                    arrountitemBeenS.add(arrountitemBean);
                    data.putSerializable("list", arrountitemBeenS);

                }
            }else{
                ArrayList<ArrountitemBean> arrountitemBeenS = new ArrayList<>();
                ArrountitemBean arrountitemBean = new ArrountitemBean();
                arrountitemBean.setMoid(myArrayListCityBeens.get(i).getMoid());
                arrountitemBeenS.add(arrountitemBean);
                data.putSerializable("list", arrountitemBeenS);
            }

            //"2016-09-04 12:00"
            SaleListFragment newfragment = new SaleListFragment();
            newfragment.setArguments(data);
            fragments.add(newfragment);
        }
        SalesFragmentPagerAdapter mAdapetr = new SalesFragmentPagerAdapter(getSupportFragmentManager(), fragments);
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
            sale_textviewsc.smoothScrollTo((position-1 ) * getResources().getDimensionPixelSize(R.dimen.base_dimen_100) + offest, 0);
            sale_textviewsc.setListData(quyulistsNew, mListener, position  );
        }
    };



    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;

                case 23:
                    for (int i = 0; i < myArrayListCityBeens.size(); i++) {
                        CityBean cityBean = new CityBean();
                        cityBean.setNn(myArrayListCityBeens.get(i).getTagName());
                        quyulistsNew.add(cityBean);
                    }
                    initFragment();
                    sale_textviewsc.setListData(quyulistsNew, mListener, 0);
                    sale_textviewsc.smoothScrollBy(0, 0);
                    StyledDialog.dismissLoading();
                    break;
                case 24:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
            }
        }
    };
    /**
     * 实现类，响应按钮点击事件
     */
    private TextsHorizonScrollView.TextTypeClickListener mListener = new TextsHorizonScrollView.TextTypeClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            // StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            if (getNetWifi()) {

                if (myArrayListCityBeens.size() > 0) {
                    mViewPager.setCurrentItem(position);
                }
                int offest = (position - 2) * getResources().getDimensionPixelSize(R.dimen.base_dimen_60);
                sale_textviewsc.smoothScrollTo((position - 2) * getResources().getDimensionPixelSize(R.dimen.base_dimen_100) + offest, 0);
                sale_textviewsc.setListData(quyulistsNew, mListener, position);
            } else {
                App.getInstance().showToast("网络不给力，请检查网络设置");
            }

        }
    };
    public void OnTitleReturnClick(View v){
        finish();
    }
}
