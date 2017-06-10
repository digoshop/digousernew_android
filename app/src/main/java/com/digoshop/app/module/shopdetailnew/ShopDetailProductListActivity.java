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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
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

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * Created by lsqbeyond on 2017/3/22.
 */

public class ShopDetailProductListActivity extends BaseActivity implements View.OnClickListener {

    private String CityCodestr = "";
    private String latstr = "";
    private String logstr = "";
    private String operateTypestr = "";
    private String regionIdstr = "";
    private int screenWidth;
    private String sid;
    private GridView gv_shopn_allproduct;
    private ShopDetailNData detailNData;
    private String productType = "";
    private String priceMode = "";
    private int page = 1;
    private PullToRefreshLayout ptrl;
    private ArrayList<ShopProduct> couponInfos = new ArrayList<>();
    private ArrayList<ShopProduct> couponInfosNew = new ArrayList<>();
    private ShopAllProductAdp shopProductAdp;
    private RelativeLayout re_nolist;
    private TextView tv_shopn_zonghe, tv_title_content, tv_title_right, tv_shopn_xinpin, tv_shopn_cuxiao, tv_shopn_jiage;
    private ImageView iv_price_type, iv_title_right;
    private LinearLayout lin_price_shop;
    private String moid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopproduct_aty);
        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");
        moid = intent.getStringExtra("moid");
        if (TextUtils.isEmpty(sid)) {
            App.getInstance().showToast("店铺id为空");
            finish();
        }
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        lin_price_shop = (LinearLayout) findViewById(R.id.lin_price_shop);
        lin_price_shop.setOnClickListener(this);

        iv_title_right = (ImageView) findViewById(R.id.iv_title_right);
        iv_title_right.setOnClickListener(this);
        iv_title_right.setBackgroundResource(R.drawable.a_shoptypennew);
        iv_title_right.setVisibility(View.VISIBLE);
        iv_price_type = (ImageView) findViewById(R.id.iv_price_type);
        re_nolist = (RelativeLayout) findViewById(R.id.re_nocouponlist);
        tv_shopn_zonghe = (TextView) findViewById(R.id.tv_shopn_zonghe);
        tv_shopn_xinpin = (TextView) findViewById(R.id.tv_shopn_xinpin);
        tv_shopn_cuxiao = (TextView) findViewById(R.id.tv_shopn_cuxiao);
        tv_shopn_jiage = (TextView) findViewById(R.id.tv_shopn_jiage);
//        tv_title_right = (TextView) findViewById(R.id.tv_title_right);
//        tv_title_right.setText("分类");
//        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("店铺商品列表");
        tv_shopn_zonghe.setOnClickListener(this);
        tv_shopn_xinpin.setOnClickListener(this);
        tv_shopn_cuxiao.setOnClickListener(this);
        // tv_shopn_jiage.setOnClickListener(this);
        gv_shopn_allproduct = (GridView) findViewById(R.id.gv_shopn_products);
        gv_shopn_allproduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("pid", couponInfosNew.get(i).getPid());
                intent.putExtra("pt", couponInfosNew.get(i).getPt());
                if (!TextUtils.isEmpty(couponInfosNew.get(i).getPt())) {
                    if ("2".equals(couponInfosNew.get(i).getPt())) {
                        intent.setClass(ShopDetailProductListActivity.this, ProductExchangeAty.class);
                        startActivity(intent);
                    } else if ("3".equals(couponInfosNew.get(i).getPt())) {
                        intent.setClass(ShopDetailProductListActivity.this, ProductDetailAty.class);
                        startActivity(intent);
                    }

                }


            }
        });
        ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_store_view));
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (couponInfosNew.size() > 0) {
                    couponInfosNew.clear();
                }
                CityCodestr = LocalSave.getValue(ShopDetailProductListActivity.this, AppConfig.basefile, "CityCode", "");
                page = 1;
                if (getNetWifi()) {
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                    getShopDetailN();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
                shopProductAdp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

                if (couponInfos != null) {
                    if (couponInfos.size() > 0) {
                        couponInfos.clear();
                    }
                    page = page + 1;
                    if (getNetWifi()) {
                        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                        getShopDetailN();
                    } else {
                        App.getInstance().showToast("网络不给力，请检查网络设置");
                    }
                    shopProductAdp.appendData(couponInfos);
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
        CityCodestr = LocalSave.getValue(ShopDetailProductListActivity.this, AppConfig.basefile, "CityCode", "");
        if (couponInfosNew != null) {
            couponInfosNew.clear();
        }
        if (couponInfos != null) {
            couponInfos.clear();
        }
        shopProductAdp = new ShopAllProductAdp(this, couponInfosNew);
        gv_shopn_allproduct.setAdapter(shopProductAdp);
        if (getNetWifi()) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            tv_shopn_zonghe.setTextColor(this.getResources().getColor(R.color.title_bar_bg));
            tv_shopn_xinpin.setTextColor(this.getResources().getColor(R.color.hui));
            tv_shopn_cuxiao.setTextColor(this.getResources().getColor(R.color.hui));
            tv_shopn_jiage.setTextColor(this.getResources().getColor(R.color.hui));
            getShopDetailN();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }
    }

    @Override
    public boolean isNoTitle() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }
    private void getShopDetailN() {


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
                    ptrl.setVisibility(View.VISIBLE);
                    if (couponInfosNew != null & couponInfos != null) {
                        if (couponInfosNew.size() == 0 & couponInfos.size() == 0) {
                            re_nolist.setVisibility(View.VISIBLE);
                        } else {
                            re_nolist.setVisibility(View.GONE);
                        }

                    }
                    couponInfosNew.addAll(couponInfos);
                    Log.v("ceshi", "couponInfos" + couponInfos.size());
                    Log.v("ceshi", "couponInfosNew" + couponInfosNew.size());
                    shopProductAdp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    Log.v("ceshi", "2222222222");
                    Log.v("ceshi", "couponInfosNew11+" + couponInfosNew);
                    Log.v("ceshi", "couponInfos22" + couponInfos);
                    if (couponInfosNew != null) {
                        if (couponInfosNew.size() == 0 & couponInfos == null) {
                            re_nolist.setVisibility(View.VISIBLE);
                            ptrl.setVisibility(View.GONE);
                        }
                    }
                    if (couponInfosNew != null & couponInfos != null) {
                        Log.v("ceshi", "couponInfosNew.size()+" + couponInfosNew.size());
                        Log.v("ceshi", "couponInfos.size()" + couponInfos.size());
                        if (couponInfosNew.size() == 0 & couponInfos.size() == 0) {
                            //第一次进来的时候没一个
                            re_nolist.setVisibility(View.VISIBLE);
                            ptrl.setVisibility(View.GONE);
                        } else {
                            ptrl.setVisibility(View.VISIBLE);
                            re_nolist.setVisibility(View.GONE);
                        }
                        if (couponInfosNew.size() == 0) {
                            ptrl.setVisibility(View.GONE);
                            re_nolist.setVisibility(View.VISIBLE);
                        }
                    }
                    //  App.getInstance().showToast("暂无数据!");
                    ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
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
                    break;

            }

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_right:
                Intent intent = new Intent();
                intent.putExtra("sid", sid);
                intent.setClass(ShopDetailProductListActivity.this, ShopDetailAllCateActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_shopn_zonghe:
                //1综合 2促销 3 新品（必填）
                if (getNetWifi()) {
                    iv_price_type.setBackgroundResource(R.drawable.more_arrow);

                    tv_shopn_zonghe.setTextColor(ShopDetailProductListActivity.this.getResources().getColor(R.color.title_bar_bg));
                    tv_shopn_xinpin.setTextColor(ShopDetailProductListActivity.this.getResources().getColor(R.color.hui));
                    tv_shopn_cuxiao.setTextColor(ShopDetailProductListActivity.this.getResources().getColor(R.color.hui));
                    tv_shopn_jiage.setTextColor(ShopDetailProductListActivity.this.getResources().getColor(R.color.hui));
                    if (couponInfosNew != null) {
                        couponInfosNew.clear();
                    }
                    if (couponInfos != null) {
                        couponInfos.clear();
                    }
                    priceMode = "";
                    productType = "";
                    page = 1;
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                    getShopDetailN();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }

                break;
            case R.id.tv_shopn_xinpin:
                if (getNetWifi()) {
                    iv_price_type.setBackgroundResource(R.drawable.more_arrow);

                    tv_shopn_zonghe.setTextColor(ShopDetailProductListActivity.this.getResources().getColor(R.color.hui));
                    tv_shopn_xinpin.setTextColor(ShopDetailProductListActivity.this.getResources().getColor(R.color.title_bar_bg));
                    tv_shopn_cuxiao.setTextColor(ShopDetailProductListActivity.this.getResources().getColor(R.color.hui));
                    tv_shopn_jiage.setTextColor(ShopDetailProductListActivity.this.getResources().getColor(R.color.hui));
                    if (couponInfosNew != null) {
                        couponInfosNew.clear();
                    }
                    if (couponInfos != null) {
                        couponInfos.clear();
                    }
                    productType = "3";
                    priceMode = "";
                    page = 1;
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    getShopDetailN();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
                break;
            case R.id.tv_shopn_cuxiao:
                if (getNetWifi()) {
                    iv_price_type.setBackgroundResource(R.drawable.more_arrow);

                    tv_shopn_zonghe.setTextColor(ShopDetailProductListActivity.this.getResources().getColor(R.color.hui));
                    tv_shopn_xinpin.setTextColor(ShopDetailProductListActivity.this.getResources().getColor(R.color.hui));
                    tv_shopn_cuxiao.setTextColor(ShopDetailProductListActivity.this.getResources().getColor(R.color.title_bar_bg));
                    tv_shopn_jiage.setTextColor(ShopDetailProductListActivity.this.getResources().getColor(R.color.hui));
                    if (couponInfosNew != null) {
                        couponInfosNew.clear();
                    }
                    if (couponInfos != null) {
                        couponInfos.clear();
                    }
                    productType = "2";
                    priceMode = "";
                    page = 1;
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    getShopDetailN();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
                break;
            case R.id.lin_price_shop:
                if (getNetWifi()) {
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
                    tv_shopn_zonghe.setTextColor(ShopDetailProductListActivity.this.getResources().getColor(R.color.hui));
                    tv_shopn_xinpin.setTextColor(ShopDetailProductListActivity.this.getResources().getColor(R.color.hui));
                    tv_shopn_cuxiao.setTextColor(ShopDetailProductListActivity.this.getResources().getColor(R.color.hui));
                    tv_shopn_jiage.setTextColor(ShopDetailProductListActivity.this.getResources().getColor(R.color.title_bar_bg));
                    if (couponInfosNew != null) {
                        couponInfosNew.clear();
                    }
                    if (couponInfos != null) {
                        couponInfos.clear();
                    }

                    productType = "";
                    page = 1;
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                    getShopDetailN();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }

                break;


        }
    }
}
