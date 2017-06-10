package com.digoshop.app.module.shopdetailnew;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.home.couponactive.ActivityDetails;
import com.digoshop.app.module.home.model.ActivityDetailBean;
import com.digoshop.app.module.storedetail.Preferrential_GridViewAdapter;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * Created by lsqbeyond on 2016/11/10.
 */

public class ShopDetailAllAtyActivity extends BaseActivity {
    private ListView lv_atylist;
    private PullToRefreshLayout refresh_store_aty_view;
    private int page_length = 10;
    private int pageaty = 1;
    private Preferrential_GridViewAdapter atyadp;
    private ArrayList<ActivityDetailBean> activityDetailBeans;
    private ArrayList<ActivityDetailBean> activityDetailBeansNew = new ArrayList<>();
    private RelativeLayout re_nocouponlist;
    private String sid;
    private TextView tv_title_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopn_atylist);
        tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("店铺活动");
        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");
        if (TextUtils.isEmpty(sid)) {
            App.getInstance().showToast("店铺id为空!");
            finish();
        }
        lv_atylist = (ListView) findViewById(R.id.lv_atylist);
        re_nocouponlist = (RelativeLayout) findViewById(R.id.re_nocouponlist);
        refresh_store_aty_view = ((PullToRefreshLayout) findViewById(R.id.refresh_store_aty_view));
        refresh_store_aty_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (getNetWifi()) {
                    if (activityDetailBeansNew.size() > 0) {
                        activityDetailBeansNew.clear();
                    }
                    pageaty = 1;
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                    getAtyList();
                    atyadp.notifyDataSetChanged();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

                if (getNetWifi()) {
                    if (activityDetailBeans != null) {
                        if (activityDetailBeans.size() > 0) {
                            activityDetailBeans.clear();
                        }
                        pageaty = pageaty + 1;
                        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                        getAtyList();
                        atyadp.appendData(activityDetailBeans);
                    }
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
        setAtyItemClick();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        atyadp = new Preferrential_GridViewAdapter(ShopDetailAllAtyActivity.this, activityDetailBeansNew, screenWidth, screenWidth * 3 / 7);
        lv_atylist.setAdapter(atyadp);
        //StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        if (activityDetailBeansNew.size() > 0) {
            activityDetailBeansNew.clear();
        }
        if (getNetWifi()) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            getAtyList();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }

    }


    private void getAtyList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAtyListApi();
            }
        }).start();
    }

    private void setAtyItemClick() {

        lv_atylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                ActivityDetailBean homeactivityinfo = new ActivityDetailBean();
                Intent intentaty = new Intent();
                homeactivityinfo.setMnid(activityDetailBeansNew.get(arg2)
                        .getMnid());
                intentaty.setClass(ShopDetailAllAtyActivity.this, ActivityDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("atycontent", homeactivityinfo);
                intentaty.putExtras(bundle);
                startActivity(intentaty);
            }
        });
    }

    private void getAtyListApi() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            activityDetailBeans = api.getStoreorShopAtyList(
                    sid, "2", pageaty + "", page_length + "");
            if (activityDetailBeans != null) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }
        } catch (JSONException e) {
            handler.sendEmptyMessage(3);
            Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(2);
            Log.v("ceshi", "WSError" + e.getMessage());
            e.printStackTrace();
        }

    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    re_nocouponlist.setVisibility(View.GONE);
                    activityDetailBeansNew.addAll(activityDetailBeans);
                    atyadp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();


                    break;
                case 2:
                    if (activityDetailBeans != null) {
                        App.getInstance().showToast("没有数据了!");
                    } else {
                        if (activityDetailBeansNew.size() > 0) {
                            App.getInstance().showToast("没有数据了!");
                        } else {
                            re_nocouponlist.setVisibility(View.VISIBLE);
                        }

                    }

                    StyledDialog.dismissLoading();
                    refresh_store_aty_view.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
                case 4:
                    App.getInstance().showToast("请求异常");
                    StyledDialog.dismissLoading();

                    break;

            }
        }
    };

    @Override
    public boolean isNoTitle() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }
}
