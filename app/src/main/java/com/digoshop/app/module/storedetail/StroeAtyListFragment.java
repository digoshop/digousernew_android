package com.digoshop.app.module.storedetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseFragment;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.home.couponactive.ActivityDetails;
import com.digoshop.app.module.home.model.ActivityDetailBean;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by lsqbeyond on 2016/11/10.
 */

public class StroeAtyListFragment extends BaseFragment {
    View view;
    private ListView lv_atylist;
    private PullToRefreshLayout refresh_store_aty_view;
    private int page_length = 10;
    private int pageaty = 1;
    private Preferrential_GridViewAdapter atyadp;
    private ArrayList<ActivityDetailBean> activityDetailBeans;
    private ArrayList<ActivityDetailBean> activityDetailBeansNew = new ArrayList<>();
    private RelativeLayout re_nocouponlist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.store_atylist, container, false);
        lv_atylist = (ListView) view
                .findViewById(R.id.lv_atylist);
        re_nocouponlist = (RelativeLayout) view
                .findViewById(R.id.re_nocouponlist);
        refresh_store_aty_view = ((PullToRefreshLayout) view.findViewById(R.id.refresh_store_aty_view));
        refresh_store_aty_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (activityDetailBeansNew.size() > 0) {
                    activityDetailBeansNew.clear();
                }
                pageaty = 1;
                getAtyList();
                atyadp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
               if(activityDetailBeans!=null){
                   if (activityDetailBeans.size() > 0) {
                       activityDetailBeans.clear();
                   }
                   pageaty = pageaty + 1;
                   getAtyList();
                   atyadp.appendData(activityDetailBeans);
               }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
        setAtyItemClick();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        atyadp = new Preferrential_GridViewAdapter(getActivity(), activityDetailBeansNew,screenWidth,screenWidth*3/7);
        lv_atylist.setAdapter(atyadp);
       // StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        if (activityDetailBeansNew.size() > 0) {
            activityDetailBeansNew.clear();
        }
        getAtyList();
        return view;
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
                intentaty.setClass(getActivity(), ActivityDetails.class);
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
                    StoreDetailActivity.storeid, "1", pageaty+"", page_length+"");
            if (activityDetailBeans != null) {
                Log.v("ceshi", "activityDetailBeans+"+activityDetailBeans.size());
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
                    if(activityDetailBeans!=null){
                        App.getInstance().showToast("没有数据了!");
                    }else{
                        if(activityDetailBeansNew.size()>0){
                            App.getInstance().showToast("没有数据了!");
                        }else{
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

}
