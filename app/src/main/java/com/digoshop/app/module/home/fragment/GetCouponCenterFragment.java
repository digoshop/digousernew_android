package com.digoshop.app.module.home.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.arrountshops.adp.ArountShop_textAdp;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.home.adp.CouponactiveAdp;
import com.digoshop.app.module.home.couponactive.CouponDetailActivity;
import com.digoshop.app.module.userCenter.model.Discountcoupons;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.DensityUtil;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.db.CityBean;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class GetCouponCenterFragment extends Fragment implements OnClickListener, OnItemClickListener {
    private ArrayList<Discountcoupons> couponInfos;
    private ArrayList<Discountcoupons> couponInfosNew = new ArrayList<>();
    private CouponactiveAdp couadp;
    private ListView lv_citycouponlist;
    private RelativeLayout lLayout_coupon_category, lLayout_coupon_type;
    private String typeIdstr = "";//代金券/1000000 折扣券/1000001 满减券/1000002
    private String otype = "";//空全部 1 商品 2 服务类
    private String CityCodestr = "";
    private PopupWindow popupWindowcate;
    private PopupWindow pocouponcoupontype;
    private RelativeLayout re_nolist;
    private TextView tv_arrountshop_category, tv_arrountshop_area;
    private PullToRefreshLayout refresh_store_view;
    private int page = 1;
    private int page_length = 10;
    float fheingt ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_couponactive_coupon,
                null);
        fheingt = App.getInstance().getSelectHeight();
        initview(view);

        return view;
    }

    private void initview(View view) {
        re_nolist = (RelativeLayout) view.findViewById(R.id.re_nolist);
        CityCodestr = LocalSave.getValue(getActivity(), AppConfig.basefile, "CityCode", "");
        lv_citycouponlist = (ListView) view.findViewById(R.id.lv_citycouponlist);
        lv_citycouponlist.setOnItemClickListener(this);

        tv_arrountshop_area = (TextView) view.findViewById(R.id.tv_arrountshop_area);

        tv_arrountshop_category = (TextView) view.findViewById(R.id.tv_arrountshop_category);
        lLayout_coupon_category = (RelativeLayout) view.findViewById(R.id.lLayout_coupon_category);
        lLayout_coupon_type = (RelativeLayout) view.findViewById(R.id.lLayout_coupon_type);
        lLayout_coupon_category.setOnClickListener(this);
        lLayout_coupon_type.setOnClickListener(this);
        refresh_store_view = ((PullToRefreshLayout) view.findViewById(R.id.refresh_store_view));
        refresh_store_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (couponInfosNew.size() > 0) {
                    couponInfosNew.clear();
                }
//                otype = "";
//                typeIdstr = "";
                page = 1;
                getCouponList();
                couadp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (couponInfos != null) {
                    if (couponInfos.size() > 0) {
                        couponInfos.clear();
                    }
                    page = page + 1;
                    getCouponList();
                    couadp.appendData(couponInfos);
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
        couadp = new CouponactiveAdp(getActivity(), couponInfosNew);
        lv_citycouponlist.setAdapter(couadp);
        if (getNetWifi()) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            getCouponList();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }
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

    private void getCouponApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {

            couponInfos = api.getCouponList(CityCodestr, "",typeIdstr, otype, page + "",
                    page_length + "");
            if (couponInfos != null  ) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }

        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            if("A502".equals(e.getMessage())){
                handler.sendEmptyMessage(502);
                return;
            }
            couponInfos = null;
            handler.sendEmptyMessage(2);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    re_nolist.setVisibility(View.GONE);
                    couponInfosNew.addAll(couponInfos);
                    couadp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();
                    if(re_nolist.getVisibility()==View.VISIBLE){
                        refresh_store_view.setVisibility(View.GONE);
                    }else{
                        refresh_store_view.setVisibility(View.VISIBLE);
                    }
                    break;
                case 2:
                    if(couponInfos!=null){
                        App.getInstance().showToast("没有数据了!");
                    }else{
                        if(couponInfosNew.size()>0){
                            App.getInstance().showToast("没有数据了!");
                        }else{
                            re_nolist.setVisibility(View.VISIBLE);
                        }

                    }
                    if(re_nolist.getVisibility()==View.VISIBLE){
                        refresh_store_view.setVisibility(View.GONE);
                    }else{
                        refresh_store_view.setVisibility(View.VISIBLE);
                    }
                    refresh_store_view.loadmoreFinish(PullToRefreshLayout.SUCCEED);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lLayout_coupon_category://全部。服务，商品
                getPopupWindowcategory();
                // 这里是位置显示方式,在屏幕的下方
                popupWindowcate.showAsDropDown(v);
                break;
            case R.id.lLayout_coupon_type://代金券/1000000 折扣券/1000001 满减券/1000002
                getPopupWindowtype();
                // 这里是位置显示方式,在屏幕的下方
                pocouponcoupontype.showAsDropDown(v);
                break;

            default:
                break;
        }
    }

    /***
     * 获取区域pop
     */
    private void getPopupWindowcategory() {
        if (null != popupWindowcate) {
            popupWindowcate.dismiss();
            return;
        } else {
            initPopuptWindowcate();
        }
    }

    /***
     * 获取区域pop
     */
    private void getPopupWindowtype() {
        if (null != pocouponcoupontype) {
            pocouponcoupontype.dismiss();
            return;
        } else {
            initPopuptWindowtype();
        }
    }

    ArrayList<CityBean> quyulists;

    /**
     * 创建商品类别pop
     */
    protected void initPopuptWindowcate() {
        quyulists = new ArrayList<>();
        CityBean cb1 = new CityBean();
        cb1.setNn("全部分类");
        quyulists.add(cb1);
        CityBean cb2 = new CityBean();
        cb2.setNn("服务类");
        quyulists.add(cb2);
        CityBean cb3 = new CityBean();
        cb3.setNn("商品类");
        quyulists.add(cb3);
        CityBean cb4 = new CityBean();
        cb4.setNn("商场类");
        quyulists.add(cb4);
        ArountShop_textAdp arountShop_textAdp = new ArountShop_textAdp(getActivity(), quyulists);
        View popupWindow_view = getActivity().getLayoutInflater().inflate(R.layout.fragment_arountshop_listview,
                null, false);
        final ListView listView = (ListView) popupWindow_view.findViewById(R.id.lv_arountshop);
        listView.setAdapter(arountShop_textAdp);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
                if (null != popupWindowcate) {
                    popupWindowcate.dismiss();
                    popupWindowcate = null;
                }
                if (postion == 0) {
                    if (null != popupWindowcate) {
                        popupWindowcate.dismiss();
                    }
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    tv_arrountshop_category.setText(quyulists.get(postion).getNn().toString());
                    otype = "";
                    if (couponInfosNew.size() > 0) {
                        couponInfosNew.clear();
                    }
                    if (couponInfos != null) {
                        couponInfos.clear();
                    }
                    page = 1;
                    getCouponList();
                } else if (postion == 1) {
                    if (null != popupWindowcate) {
                        popupWindowcate.dismiss();
                    }
                    if (couponInfos != null) {
                        couponInfos.clear();
                    }
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    otype = "2";
                    page = 1;
                    if (couponInfosNew.size() > 0) {
                        couponInfosNew.clear();
                    }
                    if (couponInfos != null) {
                        couponInfos.clear();
                    }
                    getCouponList();
                    tv_arrountshop_category.setText(quyulists.get(postion).getNn().toString());

                } else if (postion == 2) {
                    if (null != popupWindowcate) {
                        popupWindowcate.dismiss();
                    }
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    otype = "1";
                    if (couponInfosNew.size() > 0) {
                        couponInfosNew.clear();
                    }
                    if (couponInfos != null) {
                        couponInfos.clear();
                    }
                    page = 1;
                    getCouponList();

                    tv_arrountshop_category.setText(quyulists.get(postion).getNn().toString());

                } else if (postion == 3) {
                    if (null != popupWindowcate) {
                        popupWindowcate.dismiss();
                    }
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    if (couponInfosNew.size() > 0) {
                        couponInfosNew.clear();
                    }
                    if (couponInfos != null) {
                        couponInfos.clear();
                    }
                    otype = "0";
                    page = 1;
                    getCouponList();
                    tv_arrountshop_category.setText(quyulists.get(postion).getNn().toString());

                }
                couadp.notifyDataSetChanged();

            }
        });
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindowcate = new PopupWindow(popupWindow_view, LayoutParams.MATCH_PARENT, DensityUtil.floatToInt( quyulists.size()*fheingt), true);
        popupWindowcate.setFocusable(true);
        popupWindowcate.setBackgroundDrawable(new BitmapDrawable());
        // 设置动画效果
        popupWindowcate.setAnimationStyle(android.R.style.Animation_Dialog);
        // popupWindow.setAnimationStyle(R.style.AnimationFade);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindowcate != null && popupWindowcate.isShowing()) {
                    popupWindowcate.dismiss();
                    popupWindowcate = null;
                }
                return false;
            }
        });
    }

    /**
     * 创建商品类别pop
     */
    protected void initPopuptWindowtype() {
        quyulists = new ArrayList<>();
        CityBean cb1 = new CityBean();
        cb1.setNn("全部品类");
        quyulists.add(cb1);
        CityBean cb2 = new CityBean();
        cb2.setNn("折扣券");
        quyulists.add(cb2);
        CityBean cb3 = new CityBean();
        cb3.setNn("代金券");
        quyulists.add(cb3);
        CityBean cb4 = new CityBean();
        cb4.setNn("满减券");
        quyulists.add(cb4);
        View popupWindow_view = getActivity().getLayoutInflater().inflate(R.layout.fragment_arountshop_listview,
                null, false);
        ArountShop_textAdp arountShop_textAdp = new ArountShop_textAdp(getActivity(), quyulists);
        final ListView listView = (ListView) popupWindow_view.findViewById(R.id.lv_arountshop);
        listView.setAdapter(arountShop_textAdp);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
                if (null != pocouponcoupontype) {
                    pocouponcoupontype.dismiss();
                    pocouponcoupontype = null;
                }
                if (postion == 0) {
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    tv_arrountshop_area.setText(quyulists.get(postion).getNn().toString());
                    typeIdstr = "";
                    if (couponInfosNew.size() > 0) {
                        couponInfosNew.clear();
                    }
                    if (couponInfos != null) {
                        couponInfos.clear();
                    }
                    page = 1;
                    getCouponList();
                } else if (postion == 1) {
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    typeIdstr = "1000001";//折扣
                    page = 1;
                    if (couponInfos != null) {
                        couponInfos.clear();
                    }
                    if (couponInfosNew.size() > 0) {
                        couponInfosNew.clear();
                    }
                    getCouponList();

                    tv_arrountshop_area.setText(quyulists.get(postion).getNn().toString());
                } else if (postion == 2) {
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    typeIdstr = "1000000";//代金
                    page = 1;
                    if (couponInfos != null) {
                        couponInfos.clear();
                    }
                    if (couponInfosNew.size() > 0) {
                        couponInfosNew.clear();
                    }
                    getCouponList();
                    tv_arrountshop_area.setText(quyulists.get(postion).getNn().toString());
                } else if (postion == 3) {
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    typeIdstr = "1000002";//满减
                    page = 1;
                    if (couponInfos != null) {
                        couponInfos.clear();
                    }
                    if (couponInfosNew.size() > 0) {
                        couponInfosNew.clear();
                    }
                    getCouponList();
                    tv_arrountshop_area.setText(quyulists.get(postion).getNn().toString());
                }
                couadp.notifyDataSetChanged();

            }
        });
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        pocouponcoupontype = new PopupWindow(popupWindow_view, LayoutParams.MATCH_PARENT,  DensityUtil.floatToInt( quyulists.size()*fheingt), true);

        pocouponcoupontype.setFocusable(true);
        pocouponcoupontype.setBackgroundDrawable(new BitmapDrawable());
        // 设置动画效果
        pocouponcoupontype.setAnimationStyle(android.R.style.Animation_Dialog);
        // popupWindow.setAnimationStyle(R.style.AnimationFade);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (pocouponcoupontype != null && pocouponcoupontype.isShowing()) {
                    pocouponcoupontype.dismiss();
                    pocouponcoupontype = null;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), CouponDetailActivity.class);
        intent.putExtra("cdid", couponInfosNew.get(arg2).getCbid());
        startActivity(intent);
    }
}
