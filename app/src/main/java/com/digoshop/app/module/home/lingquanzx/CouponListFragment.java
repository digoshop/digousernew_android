package com.digoshop.app.module.home.lingquanzx;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.digoshop.app.module.home.adp.CouponactiveAdp;
import com.digoshop.app.module.home.couponactive.CouponDetailActivity;
import com.digoshop.app.module.userCenter.model.Discountcoupons;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.R.id.refresh_store_view;
import static com.digoshop.app.utils.Tool.getNetWifi;

public class CouponListFragment extends Fragment {
    private String CityCodestr = "";
    private CouponactiveAdp flashSaleslistAdp;
    private ListView lv_citycouponlist;
    private ArrayList<Discountcoupons> couponInfos = new ArrayList<>();
    private ArrayList<Discountcoupons> couponInfosNew = new ArrayList<>();
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private int page_length = 10;
    private RelativeLayout re_nolist;
    private String nnstr;
    private int apitype = 0;//0是精选1是默认
    private String modidstr = "";//优惠券所属品类
    private String otype = "";//“”代表全部， 2服务类，1商品类，0位商场类


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
                page = 1;
                if (apitype == 0) {
                    getCouponListJiXuan();
                } else {
                    getCouponList();
                }

            }

        } else {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.cous_fragment, null);
        Bundle args = getArguments();
        modidstr = args != null ? args.getString("moid") : "";
        Log.v("ceshi", "moid*******" + modidstr);
        if ("null".equals(modidstr)) {
            modidstr = "";
        }
        page = 1;
        Log.v("ceshi", "moid22*******" + modidstr);
        nnstr = args != null ? args.getString("nn") : "";
        if ("精选".equals(nnstr)) {
            apitype = 0;
        } else {
            apitype = 1;
        }
        if ("商场".equals(nnstr)) {
            otype = "0";
        } else {
            otype = "";
        }
        re_nolist = (RelativeLayout) view.findViewById(R.id.re_nolist);
        CityCodestr = LocalSave.getValue(getActivity(), AppConfig.basefile, "CityCode", "");
        ptrl = ((PullToRefreshLayout) view.findViewById(refresh_store_view));
        lv_citycouponlist = (ListView) view.findViewById(R.id.lv_citycouponlist);

        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (couponInfosNew.size() > 0) {
                    couponInfosNew.clear();
                }
                CityCodestr = LocalSave.getValue(getActivity(), AppConfig.basefile, "CityCode", "");
                page = 1;
                page_length = 10;
                if (apitype == 0) {
                    getCouponListJiXuan();
                } else {
                    getCouponList();
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
                    if (apitype == 0) {
                        getCouponListJiXuan();
                    } else {
                        getCouponList();
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
                Intent intent = new Intent();
                intent.setClass(getActivity(), CouponDetailActivity.class);
                intent.putExtra("cdid", couponInfosNew.get(position).getCbid());
                startActivity(intent);

            }
        });
        if (couponInfosNew != null) {
            couponInfosNew.clear();
        }
        if (couponInfos != null) {
            couponInfos.clear();
        }
        flashSaleslistAdp = new CouponactiveAdp(getActivity(), couponInfosNew);
        lv_citycouponlist.setAdapter(flashSaleslistAdp);
        if (apitype == 0) {
            getCouponListJiXuan();
        } else {
            getCouponList();
        }
        return view;
    }


    private void getCouponList() {
        if (TextUtils.isEmpty(CityCodestr)) {
            App.getInstance().showToast("定位获取失败,默认北京");
            CityCodestr = "010";
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                getCouponApi();
            }
        }).start();
    }

    private void getCouponListJiXuan() {
        if (TextUtils.isEmpty(CityCodestr)) {
            App.getInstance().showToast("定位获取失败,默认北京");
            CityCodestr = "010";
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                getCouponJiXuanApi();
            }
        }).start();
    }

    private void getCouponJiXuanApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {

            couponInfos = api.getCouponListJiXuan(CityCodestr, page + "");
            if (couponInfos != null) {
                if (couponInfos.size() > 0) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }

            } else {
                handler.sendEmptyMessage(2);
            }

        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {

            couponInfos = null;
            handler.sendEmptyMessage(2);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }

    private void getCouponApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {

            couponInfos = api.getCouponList(CityCodestr, modidstr + "", "", otype, page + "",
                    page_length + "");
            if (couponInfos != null) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
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
                    if (couponInfosNew != null & couponInfos != null) {
                        if (couponInfosNew.size() == 0 & couponInfos.size() == 0) {
                            re_nolist.setVisibility(View.VISIBLE);
                        } else {
                            re_nolist.setVisibility(View.GONE);
                        }
                    }
                    couponInfosNew.addAll(couponInfos);

                    flashSaleslistAdp.notifyDataSetChanged();


                    break;
                case 2:
                    if (couponInfosNew != null & couponInfos != null) {
                        if (couponInfosNew.size() == 0 & couponInfos.size() == 0) {
                            re_nolist.setVisibility(View.VISIBLE);
                            ptrl.setVisibility(View.GONE);
                        } else {
                            ptrl.setVisibility(View.VISIBLE);
                            re_nolist.setVisibility(View.GONE);
                        }
                    if(couponInfosNew.size()>0& couponInfos.size()==0){
                        App.getInstance().showToast("已是最后一页");
                    }
                    }
                    //  App.getInstance().showToast("暂无数据!");
                    ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    break;
                case 3:
                    App.getInstance().showToast("解析异常!");
                    break;
            }
        }
    };
}
