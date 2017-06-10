package com.digoshop.app.module.home.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.arrountshops.adp.ArountShop_textAdp;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.home.couponactive.ActivityDetails;
import com.digoshop.app.module.home.model.ActivityDetailBean;
import com.digoshop.app.module.storedetail.Preferrential_GridViewAdapter;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.DensityUtil;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.db.AssetsDataBasesManage;
import com.digoshop.app.utils.db.CityBean;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * TODO<首页-够优惠--活动列表>
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016年9月17日 下午11:24:19
 * @version: V1.0
 */
public class GoodAtyFragment extends Fragment implements OnClickListener, OnItemClickListener {
    private ListView lv_couponactive;
    private LinearLayout lLayout_couponactive_quyu,
            lLayout_couponactive_category;
    private AssetsDataBasesManage manage;
    private SQLiteDatabase db;
    private String CityCodestr = "";
    private String latstr = "";
    private String logstr = "";
    private String operateTypestr = "";
    private String regionIdstr = "";
    private PopupWindow popupWindowquyu;
    private ArountShop_textAdp arountShop_textAdp;
    private PopupWindow popupWindowcate;
    private ArrayList<ActivityDetailBean> activityDetailBeans;
    private ArrayList<ActivityDetailBean> activityDetailBeansNew = new ArrayList<>();
    private RelativeLayout re_nolist;
    private PullToRefreshLayout refresh_store_view;
    private int page = 1;
    private int page_length = 10;
    private Preferrential_GridViewAdapter atyadp;
    private TextView tv_arrountshop_category, tv_arrountshop_quyu;
    private ArrayList<CityBean> quyulistsNew = new ArrayList<>();
    float fheingt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hotaty_atylist, null);
        fheingt = App.getInstance().getSelectHeight();

        tv_arrountshop_category = (TextView) view.findViewById(R.id.tv_arrountshop_category);
        tv_arrountshop_quyu = (TextView) view.findViewById(R.id.tv_arrountshop_quyu);
        re_nolist = (RelativeLayout) view.findViewById(R.id.re_nolist);
        lLayout_couponactive_quyu = (LinearLayout) view
                .findViewById(R.id.lLayout_couponactive_quyu);
        lLayout_couponactive_category = (LinearLayout) view
                .findViewById(R.id.lLayout_couponactive_category);
        lLayout_couponactive_quyu.setOnClickListener(this);
        lLayout_couponactive_category.setOnClickListener(this);
        lv_couponactive = (ListView) view.findViewById(R.id.lv_couponactive);
        lv_couponactive.setOnItemClickListener(this);
        AssetsDataBasesManage.initManager(getActivity());
        manage = AssetsDataBasesManage.getManager();
        db = manage.getDatabase(AssetsDataBasesManage.coursesDBName);

        CityCodestr = LocalSave.getValue(getActivity(), AppConfig.basefile, "CityCode", "");
        latstr = LocalSave.getValue(getActivity(), AppConfig.basefile, "lat", "");
        logstr = LocalSave.getValue(getActivity(), AppConfig.basefile, "lon", "");

        refresh_store_view = ((PullToRefreshLayout) view.findViewById(R.id.refresh_store_view));
        refresh_store_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (activityDetailBeansNew.size() > 0) {
                    activityDetailBeansNew.clear();
                }
//                operateTypestr = "";
//                regionIdstr = "";
                page = 1;
                getAtyList();
                atyadp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (activityDetailBeans != null) {
                    if (activityDetailBeans.size() > 0) {
                        activityDetailBeans.clear();
                    }
                    page = page + 1;
                    getAtyList();
                    atyadp.appendData(activityDetailBeans);
                }

                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        atyadp = new Preferrential_GridViewAdapter(getActivity(), activityDetailBeansNew, screenWidth, screenWidth * 3 / 7);
        lv_couponactive.setAdapter(atyadp);
        //   StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        getAtyList();
        if (getNetWifi()) {
            upDBNEW();
        } else {
            heightpxquyu = (float) fheingt;
        }
        return view;
    }

    float heightpxquyu = 0;

    /***
     * 获取PopupWindow实例
     */
    private void getPopupWindowQuyu() {
        if (null != popupWindowquyu) {
            popupWindowquyu.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }

    // 更新开启城市id
    private void upDB() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updbapi();
            }
        }).start();

    }

    // 更新开启城市id
    private void upDBNEW() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updbapiNew();
            }
        }).start();

    }

    private void updbapiNew() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            quyulistsNew = api.upDb(CityCodestr);
            if (quyulistsNew != null) {
                Log.v("lsq", "quyulistsNew.size()" + quyulistsNew.size());
                if (quyulistsNew.size() < 9) {
                    heightpxquyu = (float) (fheingt * (quyulistsNew.size()));
                } else {
                    heightpxquyu = (float) (fheingt * 8);
                }
            } else {
                heightpxquyu = (float) fheingt;
            }
        } catch (JSONException e) {
            heightpxquyu = (float) fheingt;
            android.util.Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            heightpxquyu = (float) fheingt;
            android.util.Log.v("ceshi", "WSError3333");
            e.printStackTrace();
        }
    }


    private void updbapi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            quyulistsNew = api.upDb(CityCodestr);
            handler.sendEmptyMessage(18);
        } catch (JSONException e) {
            handler.sendEmptyMessage(18);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(18);
            e.printStackTrace();
        }
    }

    private ArountShop_textAdp arountShop_textquyuAdp;

    /**
     * 创建区域pop
     */
    protected void initPopuptWindow() {
        CityCodestr = LocalSave.getValue(getActivity(), AppConfig.basefile, "CityCode", "");

        arountShop_textquyuAdp = new ArountShop_textAdp(getActivity(), quyulistsNew);

        View popupWindow_view = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_arountshop_listview, null, false);
        final ListView listView = (ListView) popupWindow_view
                .findViewById(R.id.lv_arountshop);
        listView.setAdapter(arountShop_textquyuAdp);
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        if (getNetWifi()) {
            upDB();
        } else {
            handler.sendEmptyMessage(17);
        }
        final PullToRefreshLayout refresh_view_brand = ((PullToRefreshLayout) popupWindow_view.findViewById(R.id.refresh_view_brand));
        refresh_view_brand.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int postion, long arg3) {
                if (null != popupWindowquyu) {
                    popupWindowquyu.dismiss();
                    popupWindowquyu = null;
                }
                StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                if (activityDetailBeansNew != null) {
                    activityDetailBeansNew.clear();
                }
                if (activityDetailBeans != null) {
                    activityDetailBeans.clear();
                }
                page = 1;
                regionIdstr = quyulistsNew.get(postion).getId();
                tv_arrountshop_quyu.setText(quyulistsNew.get(postion).getNn());
                getAtyList();
                atyadp.notifyDataSetChanged();
            }
        });

        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindowquyu = new PopupWindow(popupWindow_view,
                LayoutParams.MATCH_PARENT, DensityUtil.floatToInt(heightpxquyu), true);

        popupWindowquyu.setFocusable(true);
        popupWindowquyu.setBackgroundDrawable(new BitmapDrawable());
        // 设置动画效果
        popupWindowquyu.setAnimationStyle(android.R.style.Animation_Dialog);
        // popupWindow.setAnimationStyle(R.style.AnimationFade);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindowquyu != null && popupWindowquyu.isShowing()) {
                    popupWindowquyu.dismiss();
                    popupWindowquyu = null;
                }
                return false;
            }
        });
    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    re_nolist.setVisibility(View.GONE);
                    activityDetailBeansNew.addAll(activityDetailBeans);
                    atyadp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();
                    if (re_nolist.getVisibility() == View.VISIBLE) {
                        refresh_store_view.setVisibility(View.GONE);
                    } else {
                        refresh_store_view.setVisibility(View.VISIBLE);
                    }
                    break;
                case 2:
                    if (activityDetailBeans != null) {
                        App.getInstance().showToast("没有数据了!");
                    } else {
                        if (activityDetailBeansNew.size() > 0) {
                            App.getInstance().showToast("没有数据了!");
                        } else {
                            re_nolist.setVisibility(View.VISIBLE);
                        }

                    }
                    if (re_nolist.getVisibility() == View.VISIBLE) {
                        refresh_store_view.setVisibility(View.GONE);
                    } else {
                        refresh_store_view.setVisibility(View.VISIBLE);
                    }
                    refresh_store_view.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    StyledDialog.dismissLoading();


                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
                case 502:
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                    StyledDialog.dismissLoading();
                    break;
                case 17:
                    CityBean cityBean0 = new CityBean();
                    cityBean0.setNn("全部");
                    cityBean0.setId("");
                    quyulistsNew.add(cityBean0);
                    if (arountShop_textquyuAdp != null) {
                        arountShop_textquyuAdp.notifyDataSetChanged();
                    }
                    StyledDialog.dismissLoading();
                    break;
                case 18:
                    if (quyulistsNew != null) {
                        Log.v("lsq", "quyulistsNew.size()" + quyulistsNew.size());
                        if (quyulistsNew.size() < 9) {
                            heightpxquyu = (float) (fheingt * (quyulistsNew.size()));
                        } else {
                            heightpxquyu = (float) (fheingt * 8);
                        }
                    }
                    if (arountShop_textquyuAdp != null) {
                        arountShop_textquyuAdp.notifyDataSetChanged();
                    }
                    StyledDialog.dismissLoading();
                    break;

            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lLayout_couponactive_quyu:// 所在区域
                if (TextUtils.isEmpty(CityCodestr)) {
                    App.getInstance().showToast("定位获取失败,默认北京");
                    CityCodestr = "010";
                }
                getPopupWindowQuyu();
                // 这里是位置显示方式,在屏幕的下方
                popupWindowquyu.showAsDropDown(v);
                break;
            case R.id.lLayout_couponactive_category:// 类别筛选
                getPopupWindowcategory();
                // 这里是位置显示方式,在屏幕的下方
                popupWindowcate.showAsDropDown(v);
                break;

            default:
                break;
        }
    }

    private void getAtyList() {
        if (TextUtils.isEmpty(CityCodestr)) {
            App.getInstance().showToast("定位获取失败,默认北京");
            CityCodestr = "010";
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                queryNewsListapi();
            }
        }).start();
    }

    private void queryNewsListapi() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {

            activityDetailBeans = api.queryNewsList(CityCodestr,"", regionIdstr,
                    operateTypestr, latstr, logstr, page + "", page_length + "");
            if (activityDetailBeans == null || "".equals(activityDetailBeans)) {
                handler.sendEmptyMessage(2);
            } else {
                handler.sendEmptyMessage(1);
            }

        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            if ("A502".equals(e.getMessage())) {
                handler.sendEmptyMessage(502);
                return;
            }
            activityDetailBeans = null;
            handler.sendEmptyMessage(2);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }

    }

    /***
     * 获取PopupWindow实例
     */
    private void getPopupWindowcategory() {
        if (null != popupWindowcate) {
            popupWindowcate.dismiss();
            return;
        } else {
            initPopuptWindowcate();
        }
    }

    ArrayList<CityBean> catelists;

    /**
     * 创建PopupWindow
     */
    protected void initPopuptWindowcate() {
        catelists = new ArrayList<>();
        CityBean cb1 = new CityBean();
        cb1.setNn("全部类别");
        catelists.add(cb1);
        CityBean cb2 = new CityBean();
        cb2.setNn("服务类");
        catelists.add(cb2);
        CityBean cb3 = new CityBean();
        cb3.setNn("商品类");
        catelists.add(cb3);
        ArountShop_textAdp arountShop_textAdp = new ArountShop_textAdp(getActivity(), catelists);
        View popupWindow_view = getActivity().getLayoutInflater().inflate(R.layout.fragment_arountshop_listview,
                null, false);
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindowcate = new PopupWindow(popupWindow_view, LayoutParams.MATCH_PARENT, DensityUtil.floatToInt(catelists.size() * fheingt), true);
        final ListView listView = (ListView) popupWindow_view.findViewById(R.id.lv_arountshop);
        listView.setAdapter(arountShop_textAdp);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
                if (null != popupWindowcate) {
                    popupWindowcate.dismiss();
                    popupWindowcate = null;
                }
                StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                if (postion == 0) {
                    if (null != popupWindowcate) {
                        popupWindowcate.dismiss();
                    }
                    if (activityDetailBeansNew != null) {
                        activityDetailBeansNew.clear();
                    }
                    if (activityDetailBeans != null) {
                        activityDetailBeans.clear();
                    }
                    page = 1;
                    operateTypestr = "";
                    tv_arrountshop_category.setText(catelists.get(postion).getNn().toString());

                    getAtyList();
                } else if (postion == 1) {
                    if (null != popupWindowcate) {
                        popupWindowcate.dismiss();
                    }
                    if (activityDetailBeansNew != null) {
                        activityDetailBeansNew.clear();
                    }
                    if (activityDetailBeans != null) {
                        activityDetailBeans.clear();
                    }
                    page = 1;
                    operateTypestr = "2";
                    getAtyList();
                    tv_arrountshop_category.setText(catelists.get(postion).getNn().toString());

                } else if (postion == 2) {

                    if (null != popupWindowcate) {
                        popupWindowcate.dismiss();
                        // return;
                    }

                    if (activityDetailBeansNew != null) {
                        activityDetailBeansNew.clear();
                    }
                    if (activityDetailBeans != null) {
                        activityDetailBeans.clear();
                    }
                    page = 1;
                    operateTypestr = "1";
                    getAtyList();
                    tv_arrountshop_category.setText(catelists.get(postion).getNn().toString());
                }
                atyadp.notifyDataSetChanged();
            }
        });
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

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        ActivityDetailBean homeactivityinfo = new ActivityDetailBean();
        Intent intentaty = new Intent();
        homeactivityinfo.setMnid(activityDetailBeansNew.get(arg2).getMnid());
        intentaty.setClass(getActivity(), ActivityDetails.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("atycontent", homeactivityinfo);
        intentaty.putExtras(bundle);
        startActivity(intentaty);
    }
}
