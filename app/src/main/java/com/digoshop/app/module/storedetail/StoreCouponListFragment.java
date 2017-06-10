package com.digoshop.app.module.storedetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import com.digoshop.app.module.home.adp.CouponactiveAdp;
import com.digoshop.app.module.home.couponactive.CouponDetailActivity;
import com.digoshop.app.module.userCenter.model.Discountcoupons;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * Created by lsqbeyond on 2016/11/10.
 */

public class StoreCouponListFragment extends BaseFragment   {
    View view;
    private int page = 1;
    private int page_length = 10;
    private ArrayList<Discountcoupons> discountcoupons;
    private ArrayList<Discountcoupons> discountcouponsNew = new ArrayList<>();
    private CouponactiveAdp couadp;
    private PullToRefreshLayout refresh_store_view;
    private ListView lv_couponlist;
    private RelativeLayout re_nocouponlist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.store_couponlist, container, false);
        lv_couponlist = (ListView) view
                .findViewById(R.id.lv_couponlist);
        re_nocouponlist = (RelativeLayout) view
                .findViewById(R.id.re_nocouponlist);
        refresh_store_view = ((PullToRefreshLayout) view.findViewById(R.id.refresh_store_view));
        refresh_store_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (discountcouponsNew.size() > 0) {
                    discountcouponsNew.clear();
                }
                page = 1;
                getCouponList();
                couadp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if(discountcoupons!=null){
                    if (discountcoupons.size() > 0) {
                        discountcoupons.clear();
                    }
                    page = page + 1;
                    getCouponList();
                    couadp.appendData(discountcoupons);
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
        setCouponItemClick();
        couadp = new CouponactiveAdp(getActivity(), discountcouponsNew);
        lv_couponlist.setAdapter(couadp);
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        if (discountcouponsNew.size() > 0) {
            discountcouponsNew.clear();
        }
        getCouponList();
        Log.v("ceshi", "99999999999999992222");
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.w("lsq", "2222www");

        if (isVisibleToUser) {
            Log.w("lsq", "1111111111111www");
            //相当于Fragment的onResume
        } else {
            //相当于Fragment的onPause
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.w("lsq", "www");
            if (getNetWifi()) {

                if (discountcouponsNew != null) {
                    discountcouponsNew.clear();
                }
                if (discountcoupons != null) {
                    discountcoupons.clear();
                }
                getCouponList();
            }

        } else {

        }
    }


    private void getCouponList() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                getCouponListApi();
            }
        }).start();
    }

    private void setCouponItemClick() {
        // 添加消息处理
        lv_couponlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("cdid", discountcouponsNew.get(position).getCbid());
                intent.setClass(getActivity(), CouponDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getCouponListApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {

            discountcoupons = api.getStoreorShopCouponList(
                    StoreDetailActivity.storeid, "1", page + "", page_length + "");
            if (discountcoupons != null) {
                handler.sendEmptyMessage(5);
            } else {
                handler.sendEmptyMessage(6);
            }
        } catch (JSONException e) {
            handler.sendEmptyMessage(3);
            Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(6);
        Log.v("ceshi", "WSError" + e.getMessage());
            e.printStackTrace();
        }

    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
                case 4:
                    App.getInstance().showToast("请求异常");
                    StyledDialog.dismissLoading();

                    break;
                case 5:
                    re_nocouponlist.setVisibility(View.GONE);
                    discountcouponsNew.addAll(discountcoupons);
                    couadp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();
                    break;
                case 6:
                    if(discountcoupons!=null){
                        App.getInstance().showToast("没有数据了!");
                    }else{
                        if(discountcouponsNew.size()>0){
                            App.getInstance().showToast("没有数据了!");
                        }else{
                            re_nocouponlist.setVisibility(View.VISIBLE);
                        }

                    }
                    if(refresh_store_view!=null){
                        refresh_store_view.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    StyledDialog.dismissLoading();
                    break;
            }
        }
    };



}
