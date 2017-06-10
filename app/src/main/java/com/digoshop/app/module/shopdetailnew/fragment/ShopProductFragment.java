package com.digoshop.app.module.shopdetailnew.fragment;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.product.ProductDetailAty;
import com.digoshop.app.module.product.ProductExchangeAty;
import com.digoshop.app.module.shopdetailnew.adp.ShopAllProductAdp;
import com.digoshop.app.module.shopdetailnew.model.ShopDetailNData;
import com.digoshop.app.module.shopdetailnew.model.ShopProduct;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class ShopProductFragment extends Fragment implements View.OnClickListener {


    private String CityCodestr = "";
    private String latstr = "";
    private String logstr = "";
    private String operateTypestr = "";
    private String regionIdstr = "";
    private int screenWidth;
    private String sid;
    private GridViewWithHeaderAndFooter gv_shopn_allproduct;
    private ShopDetailNData detailNData;
    private String productType = "";
    private String priceMode = "";
    private int page = 1;
    private PullToRefreshLayout ptrl;
    private ArrayList<ShopProduct> couponInfos = new ArrayList<>();
    private ArrayList<ShopProduct> couponInfosNew = new ArrayList<>();
    private ShopAllProductAdp shopProductAdp;
    private RelativeLayout re_nolist;
    private TextView tv_shopn_zonghe, tv_shopn_xinpin, tv_shopn_cuxiao, tv_shopn_jiage;
    private ImageView iv_price_type;
    private LinearLayout lin_price_shop;
    private String moid = "";
    private int visibleLastIndex = 0; // 最后的可视项索引
    // View footerView;
    TextView text;
   boolean acceptapi = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.shopproduct_fragmentsn, null);
        Bundle args = getArguments();
        sid = args != null ? args.getString("sid") : "";
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        lin_price_shop = (LinearLayout) view.findViewById(R.id.lin_price_shop);
        lin_price_shop.setOnClickListener(this);
        iv_price_type = (ImageView) view.findViewById(R.id.iv_price_type);
        re_nolist = (RelativeLayout) view.findViewById(R.id.re_nocouponlist);
        tv_shopn_zonghe = (TextView) view.findViewById(R.id.tv_shopn_zonghe);
        tv_shopn_xinpin = (TextView) view.findViewById(R.id.tv_shopn_xinpin);
        tv_shopn_cuxiao = (TextView) view.findViewById(R.id.tv_shopn_cuxiao);
        tv_shopn_jiage = (TextView) view.findViewById(R.id.tv_shopn_jiage);
        tv_shopn_zonghe.setOnClickListener(this);
        tv_shopn_xinpin.setOnClickListener(this);
        tv_shopn_cuxiao.setOnClickListener(this);
        // tv_shopn_jiage.setOnClickListener(this);
        gv_shopn_allproduct = (GridViewWithHeaderAndFooter) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//        footerView = layoutInflater.inflate(R.layout.layout_footer, gv_shopn_allproduct, false);
//        text = (TextView) footerView.findViewById(R.id.text);
//        text.setText("下一页");
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 70);
//        footerView.setLayoutParams(lp);
//        gv_shopn_allproduct.addFooterView(footerView);
        gv_shopn_allproduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("pid", couponInfosNew.get(i).getPid());
                intent.putExtra("pt", couponInfosNew.get(i).getPt());
                if (!TextUtils.isEmpty(couponInfosNew.get(i).getPt())) {
                    if ("2".equals(couponInfosNew.get(i).getPt())) {
                        intent.setClass(getActivity(), ProductExchangeAty.class);
                        startActivity(intent);
                    } else if ("3".equals(couponInfosNew.get(i).getPt())) {
                        intent.setClass(getActivity(), ProductDetailAty.class);
                        startActivity(intent);
                    }

                }
            }
        });
        gv_shopn_allproduct.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int itemsLastIndex = shopProductAdp.getCount() - 1;

                System.out.println("-------itemsLastIndex>>>>" + itemsLastIndex);
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && visibleLastIndex >= itemsLastIndex) {
                    // 如果是自动加载,可以在这里放置异步加载数据的代码
                    if (couponInfos != null) {
                        if (couponInfos.size() > 0) {
                            couponInfos.clear();
                        }
                        page = page + 1;
                        if (getNetWifi()) {
                            if(acceptapi){
                                StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                                getShopDetailN();
                            }

                        } else {
                            App.getInstance().showToast("网络不给力，请检查网络设置");
                        }
                        shopProductAdp.appendData(couponInfos);
                    }
                    System.out.println("------------ok load more--");
//                    new Work().execute();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                System.out.println("--------firstVisibleItem:" + firstVisibleItem);
                System.out.println("--------visibleItemCount:" + visibleItemCount);
                visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
                System.out.println("-------visibleLastIndex>>>" + visibleLastIndex);


            }
        });
        CityCodestr = LocalSave.getValue(getActivity(), AppConfig.basefile, "CityCode", "");
        if (couponInfosNew != null) {
            couponInfosNew.clear();
        }
        if (couponInfos != null) {
            couponInfos.clear();
        }
        shopProductAdp = new ShopAllProductAdp(getActivity(), couponInfosNew);
        gv_shopn_allproduct.setAdapter(shopProductAdp);
        if (getNetWifi()) {
            tv_shopn_zonghe.setTextColor(getActivity().getResources().getColor(R.color.title_bar_bg));
            tv_shopn_xinpin.setTextColor(getActivity().getResources().getColor(R.color.hui));
            tv_shopn_cuxiao.setTextColor(getActivity().getResources().getColor(R.color.hui));
            tv_shopn_jiage.setTextColor(getActivity().getResources().getColor(R.color.hui));

            getShopDetailN();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }
        return view;
    }

    private void getShopDetailN() {
        acceptapi = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                getShopDetailNApi();
            }
        }).start();
    }

    private void getShopDetailNApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            detailNData = api.getShopnProdcuts(productType, sid, CityCodestr, priceMode, page + "", moid);

            if (detailNData != null) {
                couponInfos = detailNData.getShopProducts();
                if (couponInfos != null) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }
            } else {
                handler.sendEmptyMessage(2);
            }

        } catch (JSONException e) {
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            if ("A502".equals(e.getMessage())) {
                handler.sendEmptyMessage(502);
                return;
            }
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // ptrl.setVisibility(View.VISIBLE);
                    if (couponInfosNew != null & couponInfos != null) {
                        if (couponInfosNew.size() == 0 & couponInfos.size() == 0) {
                            re_nolist.setVisibility(View.VISIBLE);
                        } else {
                            re_nolist.setVisibility(View.GONE);
                        }

                    }
                    if (couponInfosNew == null) {
                        couponInfosNew = new ArrayList<>();
                    }
                    couponInfosNew.addAll(couponInfos);

                    if (couponInfosNew != null) {

                        if (couponInfosNew.size() <= 10) {
                            try {
                                //     gv_shopn_allproduct.removeFooterView(footerView);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    shopProductAdp.notifyDataSetChanged();

                    StyledDialog.dismissLoading();
                    acceptapi = true;
                    break;
                case 2:
                    StyledDialog.dismissLoading();
                    Log.v("ceshi", "2222222222");
                    Log.v("ceshi", "couponInfosNew11+" + couponInfosNew);
                    Log.v("ceshi", "couponInfos22" + couponInfos);
                    if (couponInfosNew != null) {
                        if (couponInfosNew.size() == 0 & couponInfos == null) {
                            re_nolist.setVisibility(View.VISIBLE);
                            //  ptrl.setVisibility(View.GONE);
                        }
                        if (couponInfosNew.size() > 0) {
                            if (couponInfos == null) {
                                acceptapi =false;
                                App.getInstance().showToast("已是最后一页");
                            }
                        }
                    }

                    if (couponInfosNew != null & couponInfos != null) {
                        Log.v("ceshi", "couponInfosNew.size()+" + couponInfosNew.size());
                        Log.v("ceshi", "couponInfos.size()" + couponInfos.size());
                        if (couponInfosNew.size() == 0 & couponInfos.size() == 0) {
                            //第一次进来的时候没一个
                            re_nolist.setVisibility(View.VISIBLE);
                            /// ptrl.setVisibility(View.GONE);
                        } else {
                            // ptrl.setVisibility(View.VISIBLE);
                            re_nolist.setVisibility(View.GONE);
                        }
                        if (couponInfosNew.size() > 0 & couponInfos.size() == 0) {
                            acceptapi =false;
                            App.getInstance().showToast("已是最后一页");
                        }
                        if (couponInfosNew.size() == 0) {
                            // ptrl.setVisibility(View.GONE);
                            re_nolist.setVisibility(View.VISIBLE);
                        }
                    }
                    //  App.getInstance().showToast("暂无数据!");
                    // ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    try {
                        //  gv_shopn_allproduct.removeFooterView(footerView);
                    } catch (Exception ignore) {
                    }
                    StyledDialog.dismissLoading();
                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    try {
                        //   gv_shopn_allproduct.removeFooterView(footerView);
                    } catch (Exception ignore) {
                    }
                    StyledDialog.dismissLoading();
                    acceptapi = true;
                    break;
                case 4:
                    App.getInstance().showToast("请求异常");
                    StyledDialog.dismissLoading();
                    acceptapi = true;
                    break;
                case 502:
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                    StyledDialog.dismissLoading();
                    acceptapi = true;
                    break;

            }

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_shopn_zonghe:
                //1综合 2促销 3 新品（必填）
                if (getNetWifi()) {
                    acceptapi = true;
                    iv_price_type.setBackgroundResource(R.drawable.more_arrow);
                    tv_shopn_zonghe.setTextColor(getActivity().getResources().getColor(R.color.title_bar_bg));
                    tv_shopn_xinpin.setTextColor(getActivity().getResources().getColor(R.color.hui));
                    tv_shopn_cuxiao.setTextColor(getActivity().getResources().getColor(R.color.hui));
                    tv_shopn_jiage.setTextColor(getActivity().getResources().getColor(R.color.hui));
                    if (couponInfosNew != null) {
                        couponInfosNew.clear();
                    }
                    if (couponInfos != null) {
                        couponInfos.clear();
                    }
                    priceMode = "";
                    productType = "";
                    page = 1;
                    if(acceptapi){
                        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                        getShopDetailN();
                    }
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }

                break;
            case R.id.tv_shopn_xinpin:
                if (getNetWifi()) {
                    acceptapi = true;
                    iv_price_type.setBackgroundResource(R.drawable.more_arrow);

                    tv_shopn_zonghe.setTextColor(getActivity().getResources().getColor(R.color.hui));
                    tv_shopn_xinpin.setTextColor(getActivity().getResources().getColor(R.color.title_bar_bg));
                    tv_shopn_cuxiao.setTextColor(getActivity().getResources().getColor(R.color.hui));
                    tv_shopn_jiage.setTextColor(getActivity().getResources().getColor(R.color.hui));
                    if (couponInfosNew != null) {
                        couponInfosNew.clear();
                    }
                    if (couponInfos != null) {
                        couponInfos.clear();
                    }
                    productType = "3";
                    priceMode = "";
                    page = 1;
                    if(acceptapi){
                        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                        getShopDetailN();
                    }
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
                break;
            case R.id.tv_shopn_cuxiao:
                if (getNetWifi()) {
                    acceptapi = true;
                    iv_price_type.setBackgroundResource(R.drawable.more_arrow);

                    tv_shopn_zonghe.setTextColor(getActivity().getResources().getColor(R.color.hui));
                    tv_shopn_xinpin.setTextColor(getActivity().getResources().getColor(R.color.hui));
                    tv_shopn_cuxiao.setTextColor(getActivity().getResources().getColor(R.color.title_bar_bg));
                    tv_shopn_jiage.setTextColor(getActivity().getResources().getColor(R.color.hui));
                    if (couponInfosNew != null) {
                        couponInfosNew.clear();
                    }
                    if (couponInfos != null) {
                        couponInfos.clear();
                    }
                    productType = "2";
                    priceMode = "";
                    page = 1;
                    if(acceptapi){
                        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                        getShopDetailN();
                    }
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
                break;
            case R.id.lin_price_shop:
                if (getNetWifi()) {
                    acceptapi = true;
                    //价格排序（desc,asc）
                    // desc,asc
                    if(TextUtils.isEmpty(priceMode)){
                        priceMode = "desc";
                    }
                    if ("desc".equals(priceMode)) {//高到底
                        priceMode = "asc";
                        iv_price_type.setBackgroundResource(R.drawable.a_shopn_top);
                    } else {
                        priceMode = "desc";
                        iv_price_type.setBackgroundResource(R.drawable.a_shopn_down);

                    }
                    tv_shopn_zonghe.setTextColor(getActivity().getResources().getColor(R.color.hui));
                    tv_shopn_xinpin.setTextColor(getActivity().getResources().getColor(R.color.hui));
                    tv_shopn_cuxiao.setTextColor(getActivity().getResources().getColor(R.color.hui));
                    tv_shopn_jiage.setTextColor(getActivity().getResources().getColor(R.color.title_bar_bg));
                    if (couponInfosNew != null) {
                        couponInfosNew.clear();
                    }
                    if (couponInfos != null) {
                        couponInfos.clear();
                    }

                    productType = "";
                    page = 1;
                    if(acceptapi){
                        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                        getShopDetailN();
                    }
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }

                break;


        }
    }

    public String getTitle() {
        return "全部商品";
    }
}
