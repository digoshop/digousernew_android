package com.digoshop.app.module.home.huodongzx;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.home.couponactive.ActivityDetails;
import com.digoshop.app.module.home.model.ActivityDetailBean;
import com.digoshop.app.module.storedetail.Preferrential_GridViewAdapter;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class AtyListFragment extends Fragment {
    private Preferrential_GridViewAdapter flashSaleslistAdp;
    private ListView lv_citycouponlist;
    private ArrayList<ActivityDetailBean> couponInfos= new ArrayList<>();
    private ArrayList<ActivityDetailBean> couponInfosNew = new ArrayList<>();

    private PullToRefreshLayout ptrl;
    private int page = 1;
    private int page_length = 10;
    private RelativeLayout re_nolist;
    private String CityCodestr = "";
    private String latstr = "";
    private String logstr = "";
    private String operateTypestr = "";
    private String regionIdstr = "";
    private int screenWidth;


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.w("test", "www");
            if (getNetWifi()) {

                if (couponInfosNew != null) {
                    couponInfosNew.clear();
                }
                if (couponInfos != null) {
                    couponInfos.clear();
                }
                if("2".equals(sortstr)){//热门活动
                    atyinfo();
                }else{
                    getAtyList();
                }

            }

        } else {

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.atys_fragment, null);
        Bundle args = getArguments();
        sortstr = args != null ? args.getString("sort") : "";
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth  = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        re_nolist = (RelativeLayout) view.findViewById(R.id.re_nolist);
        CityCodestr = LocalSave.getValue(getActivity(), AppConfig.basefile, "CityCode", "");
        ptrl = ((PullToRefreshLayout) view.findViewById(R.id.refresh_store_view));
        lv_citycouponlist = (ListView) view.findViewById(R.id.lv_couponactive);

        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (couponInfosNew.size() > 0) {
                    couponInfosNew.clear();
                }
                CityCodestr = LocalSave.getValue(getActivity(), AppConfig.basefile, "CityCode", "");
                page = 1;
                page_length = 10;
                if("2".equals(sortstr)){//热门活动
                    atyinfo();
                }else{
                    getAtyList();
                }
                flashSaleslistAdp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

                if (couponInfos != null) {
                    if (couponInfos.size() > 0) {
                        couponInfos.clear();
                    }
                    page = page + 1;
                    if("2".equals(sortstr)){//热门活动
                        atyinfo();
                    }else{
                        getAtyList();
                    }
                    flashSaleslistAdp.appendData(couponInfos);
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
        lv_citycouponlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ActivityDetailBean homeactivityinfo = new ActivityDetailBean();
                Intent intentaty = new Intent();
                homeactivityinfo.setMnid(couponInfosNew.get(position).getMnid());
                intentaty.setClass(getActivity(), ActivityDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("atycontent", homeactivityinfo);
                intentaty.putExtras(bundle);
                startActivity(intentaty);


            }
        });
        if (couponInfosNew != null) {
            couponInfosNew.clear();
        }
        if (couponInfos != null) {
            couponInfos.clear();
        }
        flashSaleslistAdp = new Preferrential_GridViewAdapter(getActivity(), couponInfosNew,screenWidth, screenWidth * 3 / 7);
        lv_citycouponlist.setAdapter(flashSaleslistAdp);
        if (getNetWifi()) {
            if("2".equals(sortstr)){//热门活动
                atyinfo();
            }else{
                getAtyList();
            }
        } else {
//            getActivity().finish();
            App.getInstance().showToast("网络不给力，请检查网络设置");

        }


        return view;
    }
    private void getAtyList() {
        if (TextUtils.isEmpty(CityCodestr)) {
            App.getInstance().showToast("定位获取失败,默认北京"+CityCodestr);
            CityCodestr = "010";
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                queryNewsListapi();
            }
        }).start();
    }
    private String sortstr ="2";
    private void queryNewsListapi() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {

            couponInfos = api.queryNewsList(CityCodestr,sortstr, regionIdstr,
                    operateTypestr, latstr, logstr, page + "", page_length + "");
            if (couponInfos == null || "".equals(couponInfos)) {
                handler.sendEmptyMessage(2);
            } else {
                handler.sendEmptyMessage(1);
            }

        } catch (JSONException e) {
             Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {

            handler.sendEmptyMessage(2);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }

    }


    private void atyinfo() {
        if (TextUtils.isEmpty(CityCodestr)) {
            App.getInstance().showToast("定位获取失败,默认北京");
            CityCodestr = "010";
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                AtyinfoApi();
            }
        }).start();

    }
    private void AtyinfoApi() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            couponInfos = api.getLookSeekAty(page+"",CityCodestr, latstr, logstr, "HOME_ATY_HOTATY");
            if (couponInfos == null || "".equals(couponInfos)) {
                handler.sendEmptyMessage(2);
            } else {
                handler.sendEmptyMessage(1);
            }
        } catch (JSONException e) {
           Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {

            handler.sendEmptyMessage(2);
             Log.v("ceshi", "WSError");
            e.printStackTrace();
        }

    }

    boolean isview = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if(couponInfosNew!=null&couponInfos!=null){
                        if(couponInfosNew.size()==0&couponInfos.size()==0){
                            re_nolist.setVisibility(View.VISIBLE);
                        }else{
                            re_nolist.setVisibility(View.GONE);
                        }
                    }
                    couponInfosNew.addAll(couponInfos);
                    flashSaleslistAdp.notifyDataSetChanged();

                    break;
                case 2:
                    if(couponInfosNew!=null&couponInfos!=null){
                        if(couponInfosNew.size()>0&couponInfos.size()==0){
                            App.getInstance().showToast("已是最后一页");
                        }
                        if(couponInfosNew.size()==0&couponInfos.size()==0){
                            re_nolist.setVisibility(View.VISIBLE);
                            ptrl.setVisibility(View.GONE);
                        }else{
                            ptrl.setVisibility(View.VISIBLE);
                            re_nolist.setVisibility(View.GONE);
                        }
                    }
                    if(ptrl!=null){
                        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    //  App.getInstance().showToast("暂无数据!");
                break;
                case 3:
                    App.getInstance().showToast("解析异常!");
                    break;
            }
        }
    };
}
