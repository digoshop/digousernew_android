package com.digoshop.app.module.seekservice;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.adp.ArountShop_textAdp;
import com.digoshop.app.module.arrountshops.adp.ArroutShopAdp;
import com.digoshop.app.module.arrountshops.model.ShopInfoBean;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.customServices.adp.CategoryChooseAdp;
import com.digoshop.app.module.customServices.adp.CategoryChooseTwoAdp;
import com.digoshop.app.module.customServices.model.CategoryChooseBean;
import com.digoshop.app.module.shopdetailnew.ShopDetailNewSNActivity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.DensityUtil;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.db.AssetsDataBasesManage;
import com.digoshop.app.utils.db.CityBean;
import com.digoshop.app.utils.http.WSError;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class SeekServiceListAty extends BaseActivity implements OnClickListener, OnItemClickListener {
    private TextView tv_title_content;// 三级类别名字
    private String CityCodestr = "";
    private String latstr = "";
    private String logstr = "";
    private String regionIdstr = "";// 区域id
    private AssetsDataBasesManage manage;
    private SQLiteDatabase db;
    private ArrayList<ShopInfoBean> shopInfoBeans;
    private ArrayList<ShopInfoBean> shopInfoBeansNew = new ArrayList<>();
    private String brandIdstr = "";
    private String categorystr;//类别筛选的名字
    private String mfidstr = "";
    private String operateIdstr;
    private String operatename;
    private String sortstr = "";
    private ArroutShopAdp arroutShopAdp;
    private RelativeLayout re_nolist;
    private ListView lv_seekshoplist;
    private ArrayList<CityBean> quyulists;
    private RelativeLayout lLayout_serlist_zonghe, lLayout_serlist_chengqu, lLayout_serlist_service;
    private PopupWindow popupWindowquyu;
    private ArountShop_textAdp arountShop_textAdp;
    private ArountShop_textAdp arountShop_textbarAdp;
    private ArrayList<CityBean> shopinfobard;// 区域数据集合
    private PopupWindow popupWindowcate;
    private String mlvlstr = "1";
    private ArrayList<CategoryChooseBean> categoryChooseBeans;
    private TextView tv_arrountshop_category, tv_arrountshop_area, tv_arrountshop_quyu;
    private int page = 1;
    private int page_length = 10;
    private PullToRefreshLayout ptrl;
    private ArrayList<CityBean> quyulistsNew = new ArrayList<>();
    private ArountShop_textAdp arountShop_textquyuAdp;
    float fheingt;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seekserlistaty);
        fheingt = App.getInstance().getSelectHeight();
        Intent intent = getIntent();
        tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("个人中心");
        operateIdstr = intent.getStringExtra("operateId");
        operatename = intent.getStringExtra("operatename");
        Log.v("ceshi", "operatename+" + operatename);
        categoryChooseBeans = (ArrayList<CategoryChooseBean>) intent.getSerializableExtra("categoryChooseBeans");

        tv_arrountshop_quyu = (TextView) findViewById(R.id.tv_arrountshop_quyu);
        tv_arrountshop_category = (TextView) findViewById(R.id.tv_arrountshop_category);
        tv_arrountshop_area = (TextView) findViewById(R.id.tv_arrountshop_area);


        re_nolist = (RelativeLayout) findViewById(R.id.re_nolist);
        lv_seekshoplist = (ListView) findViewById(R.id.lv_seekshoplist);
        lv_seekshoplist.setOnItemClickListener(this);
        lLayout_serlist_zonghe = (RelativeLayout) findViewById(R.id.lLayout_serlist_zonghe);
        lLayout_serlist_chengqu = (RelativeLayout) findViewById(R.id.lLayout_serlist_chengqu);
        lLayout_serlist_service = (RelativeLayout) findViewById(R.id.lLayout_serlist_service);
        lLayout_serlist_zonghe.setOnClickListener(this);
        lLayout_serlist_chengqu.setOnClickListener(this);
        lLayout_serlist_service.setOnClickListener(this);
        tv_title_content.setText(operatename);
        tv_arrountshop_category.setText(operatename);
        AssetsDataBasesManage.initManager(this);
        manage = AssetsDataBasesManage.getManager();
        db = manage.getDatabase(AssetsDataBasesManage.coursesDBName);
        CityCodestr = LocalSave.getValue(SeekServiceListAty.this, AppConfig.basefile, "CityCode", "");
        latstr = LocalSave.getValue(SeekServiceListAty.this, AppConfig.basefile, "lat", "");
        logstr = LocalSave.getValue(SeekServiceListAty.this, AppConfig.basefile, "lon", "");
        handler.sendEmptyMessage(5); // 获取区域列表表
        ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (shopInfoBeansNew.size() > 0) {
                    shopInfoBeansNew.clear();
                }
                page = 1;
                page_length = 10;
                queryShopList();
                arroutShopAdp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (shopInfoBeans != null) {
                    if (shopInfoBeans.size() > 0) {
                        shopInfoBeans.clear();
                    }

                    page = page + 1;
                    page_length = 10;
                    queryShopList();
                    arroutShopAdp.appendData(shopInfoBeans);

                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);


            }
        });
        arroutShopAdp = new ArroutShopAdp(SeekServiceListAty.this,
                shopInfoBeansNew);
        lv_seekshoplist.setAdapter(arroutShopAdp);


        if (getNetWifi()) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            queryShopList();
            upDBNEW();
        } else {
            heightpxquyu = (float) fheingt;
            ;
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }
        getCateGoryStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

    float heightpxquyu = 0;

    private void queryShopList() {
        if (TextUtils.isEmpty(CityCodestr)) {
            App.getInstance().showToast("定位获取失败,默认北京");
            CityCodestr = "010";
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                queryShopListApi();
            }
        }).start();

    }

    private void queryShopListApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            shopInfoBeans = api.queryShopMulti(brandIdstr, latstr, logstr, mfidstr, operateIdstr, mlvlstr, regionIdstr,
                    sortstr, "2", CityCodestr, "" + page, "" + page_length);
            if (shopInfoBeans == null || "".equals(shopInfoBeans)) {
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

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    re_nolist.setVisibility(View.GONE);
                    shopInfoBeansNew.addAll(shopInfoBeans);
                    arroutShopAdp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    App.getInstance().showToast("没有数据了");
                    StyledDialog.dismissLoading();

                    break;
                case 3:
                    re_nolist.setVisibility(View.VISIBLE);
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
                case 4:
                    re_nolist.setVisibility(View.VISIBLE);
                    StyledDialog.dismissLoading();

                    break;
                case 7:
                    StyledDialog.dismissLoading();

                    shopinfobard = new ArrayList<CityBean>();
                    CityBean cityBean0 = new CityBean();
                    cityBean0.setNn("全部");
                    cityBean0.setMidstr("");
                    shopinfobard.add(cityBean0);
                    getPopupWindowBrand(viewbrand);

                    break;
                case 8:
                    shopinfobard = new ArrayList<CityBean>();
                    CityBean cityBean1 = new CityBean();
                    cityBean1.setNn("全部");
                    cityBean1.setMidstr("");
                    shopinfobard.add(cityBean1);
                    for (int i = 0; i < categoryChooseBeans.size(); i++) {
                        CityBean cityBean = new CityBean();
                        cityBean.setNn(categoryChooseBeans.get(i).getName());
                        cityBean.setMidstr(categoryChooseBeans.get(i).getMoid());
                        shopinfobard.add(cityBean);
                    }
                    getPopupWindowBrand(viewbrand);
                    StyledDialog.dismissLoading();

                    break;
                case 17:
                    CityBean cityBeanf = new CityBean();
                    cityBeanf.setNn("全部");
                    cityBeanf.setId("");
                    quyulistsNew.add(cityBeanf);
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
                case 21:
                    Log.v("ceshi", ")))))))))))" + tv_arrountshop_category.getText().toString());
                    StyledDialog.dismissLoading();
                    Log.v("ceshi", "&&&" + categoryChooseBeansfuwu.size());
                    if (TextUtils.isEmpty(tv_arrountshop_category.getText().toString())) {
                        if (oneadapter != null) {
                            oneadapter.setChoicePos(0);
                            oneadapter.notifyDataSetChanged();
                        }
                    } else {
                        String isSelect;
                        if (TextUtils.isEmpty(nameOne)) {
                            isSelect = operatename;
                        } else {
                            isSelect = nameOne;
                        }
                        for (int i = 0; i < categoryChooseBeansfuwu.size(); i++) {
                            if (categoryChooseBeansfuwu.get(i).getName().equals(isSelect)) {
                                oneadapter.setChoicePos(i);
                                oneadapter.notifyDataSetChanged();
                                Log.v("lsq", "&&&" + i);
                                Log.v("lsq", "&&&" + categoryChooseBeansfuwu.get(i).getName());

                                getCategoryTwo("2", categoryChooseBeansfuwu.get(i).getMoid());
                            }
                            //nameOne
                        }
                    }


                    //operatename

                    //  handler.sendEmptyMessage(9);

                    break;

                case 22:
                    App.getInstance().showToast("暂无数据！");
                    StyledDialog.dismissLoading();
                    break;
                case 23:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();
                    break;
                case 24:
                    App.getInstance().showToast("请求异常");
                    StyledDialog.dismissLoading();
                    break;
                case 25:
                    lv_Classtwo.setVisibility(View.VISIBLE);
                    Twoadapter = new CategoryChooseTwoAdp(SeekServiceListAty.this, categoryChooseBeanssahngpin);
                    lv_Classtwo.setAdapter(Twoadapter);
                    tv_dataTwo.setVisibility(View.GONE);
                    Twoadapter.notifyDataSetChanged();
                    StyledDialog.dismissLoading();

                    break;
                case 26:
                    tv_dataTwo.setVisibility(View.VISIBLE);
                    lv_Classtwo.setVisibility(View.GONE);
                    StyledDialog.dismissLoading();

                    break;
                case 30:
                    if (null != popupWindowSc) {
                        popupWindowSc.dismiss();
                        popupWindowSc = null;
                        // return;
                    }
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                    if (shopInfoBeans != null) {
                        shopInfoBeans.clear();
                    }
                    page = 1;
                    if (shopInfoBeansNew != null) {
                        shopInfoBeansNew.clear();
                    }
                    //moidname.equals("全部")
                    if ("全部".equals(moidname)) {
                        mlvlstr = "1";
                    } else {
                        mlvlstr = "2";
                    }
                    operateIdstr = moidTwe;
                    queryShopList();
                    arroutShopAdp.notifyDataSetChanged();
                    break;
            }

        }
    };

    private PopupWindow popupWindowbrand;

    /***
     * 获取PopupWindow实例
     */
    private void getPopupWindowBrand(View v) {
        if (null != popupWindowbrand) {
            popupWindowbrand.dismiss();
            return;
        } else {
            initPopuptWindowBrand(v);
        }
    }

    /**
     * 创建业态商圈pop
     */
    protected void initPopuptWindowBrand(View v) {
        arountShop_textbarAdp = new ArountShop_textAdp(SeekServiceListAty.this, shopinfobard);
        // TODO Auto-generated method stub
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(R.layout.fragment_arountshop_listview, null, false);
        final ListView listView = (ListView) popupWindow_view.findViewById(R.id.lv_arountshop);
        listView.setAdapter(arountShop_textbarAdp);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
                if (null != popupWindowbrand) {
                    popupWindowbrand.dismiss();
                    popupWindowbrand = null;
                    // return;
                }

                operateIdstr = shopinfobard.get(postion).getMidstr();
                tv_title_content.setText(shopinfobard.get(postion).getNn());
                queryShopList();
            }
        });

        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindowbrand = new PopupWindow(popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
                true);

        popupWindowbrand.setFocusable(true);
        popupWindowbrand.setBackgroundDrawable(new BitmapDrawable());
        // 设置动画效果
        popupWindowbrand.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindowbrand.showAsDropDown(v);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindowbrand != null && popupWindowbrand.isShowing()) {
                    popupWindowbrand.dismiss();
                    popupWindowbrand = null;
                }
                return false;
            }
        });

    }

    @Override
    public boolean isNoTitle() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isFullScreen() {
        // TODO Auto-generated method stub
        return false;
    }

    View viewbrand;

    /***
     * 获取综合搜索pop
     */
    private void getPopupWindowcategory() {
        if (null != popupWindowcate) {
            popupWindowcate.dismiss();
            return;
        } else {
            initPopuptWindowzonghe();
        }
    }

    /**
     * 创建商品类别pop
     */
    protected void initPopuptWindowzonghe() {
        ArrayList<CityBean> shopinfoarea = new ArrayList<>();
        CityBean cb1 = new CityBean();
        cb1.setNn("距离最近");
        shopinfoarea.add(cb1);
        CityBean cb2 = new CityBean();
        cb2.setNn("好评优先");
        shopinfoarea.add(cb2);
        ArountShop_textAdp arountShop_textAdp = new ArountShop_textAdp(SeekServiceListAty.this, shopinfoarea);

        View popupWindow_view = SeekServiceListAty.this.getLayoutInflater().inflate(
                R.layout.fragment_arountshop_listview, null, false);
        final ListView listView = (ListView) popupWindow_view.findViewById(R.id.lv_arountshop);
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
                        // return;
                    }
                    page = 1;
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    sortstr = "0";
                    if (shopInfoBeans != null) {
                        shopInfoBeans.clear();
                    }
                    if (shopInfoBeansNew != null) {
                        shopInfoBeansNew.clear();
                    }
                    tv_arrountshop_quyu.setText("离我最近");
                    queryShopList();
                    arroutShopAdp.notifyDataSetChanged();
                } else if (postion == 1) {
                    if (null != popupWindowcate) {
                        popupWindowcate.dismiss();
                        // return;
                    }
                    page = 1;
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                    sortstr = "1";
                    if (shopInfoBeans != null) {
                        shopInfoBeans.clear();
                    }
                    if (shopInfoBeansNew != null) {
                        shopInfoBeansNew.clear();
                    }
                    tv_arrountshop_quyu.setText("好评优先");
                    queryShopList();
                    arroutShopAdp.notifyDataSetChanged();

                }

            }
        });
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindowcate = new PopupWindow(popupWindow_view,
                LayoutParams.MATCH_PARENT, DensityUtil.floatToInt(fheingt * 2), true);

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

    /**
     * 创建区域pop
     */
    protected void initPopuptWindow() {
        CityCodestr = LocalSave.getValue(this, AppConfig.basefile, "CityCode", "");
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        if (getNetWifi()) {
            upDB();
        } else {
            handler.sendEmptyMessage(17);
        }
        if (quyulistsNew == null) {
            CityBean cityBeanf = new CityBean();
            cityBeanf.setNn("全部");
            cityBeanf.setId("");
            quyulistsNew.add(cityBeanf);
        } else {
            if (quyulistsNew.size() == 0) {
                CityBean cityBeanf = new CityBean();
                cityBeanf.setNn("全部");
                cityBeanf.setId("");
                quyulistsNew.add(cityBeanf);
            }
        }
        arountShop_textquyuAdp = new ArountShop_textAdp(this, quyulistsNew);
        View popupWindow_view = getLayoutInflater().inflate(R.layout.fragment_arountshop_listview, null, false);
        final ListView listView = (ListView) popupWindow_view.findViewById(R.id.lv_arountshop);
        listView.setAdapter(arountShop_textquyuAdp);

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
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
                if (null != popupWindowquyu) {
                    popupWindowquyu.dismiss();
                    popupWindowquyu = null;
                    // return;
                }
                page = 1;
                StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                if (shopInfoBeans != null) {
                    shopInfoBeans.clear();
                }
                if (shopInfoBeansNew != null) {
                    shopInfoBeansNew.clear();
                }

                tv_arrountshop_area.setText(quyulistsNew.get(postion).getNn());
                regionIdstr = quyulistsNew.get(postion).getId();
                queryShopList();
                arroutShopAdp.notifyDataSetChanged();
            }
        });

        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindowquyu = new PopupWindow(popupWindow_view, LayoutParams.MATCH_PARENT, DensityUtil.floatToInt(heightpxquyu), true);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lLayout_serlist_zonghe:// 默认排序
                getPopupWindowcategory();
                // 这里是位置显示方式,在屏幕的下方
                popupWindowcate.showAsDropDown(v);
                break;
            case R.id.lLayout_serlist_service:// 类别筛选
                if (!getNetWifi()) {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                    return;
                }
                getPopupWindowSeeKService(v);
//                Intent intent = new Intent();
//                intent.setClass(SeekServiceListAty.this, CategoryChooseServiceActivity.class);
//                startActivityForResult(intent, 1);
                break;
            case R.id.lLayout_serlist_chengqu:// 区域搜索
                getPopupWindowQuyu();
                // 这里是位置显示方式,在屏幕的下方
                popupWindowquyu.showAsDropDown(v);
                break;
            default:
                break;
        }

    }

    private PopupWindow popupWindowSc;

    /***
     * 获取PopupWindow实例
     */
    private void getPopupWindowSeeKService(View v) {
        if (null != popupWindowSc && popupWindowSc.isShowing()) {
            popupWindowSc.dismiss();
            return;
        } else {
            initPopuptWindowSc(v);
        }
    }

    private CategoryChooseAdp oneadapter;
    private CategoryChooseTwoAdp Twoadapter;
    private ArrayList<CategoryChooseBean> categoryChooseBeansfuwu = new ArrayList<CategoryChooseBean>();
    private ArrayList<CategoryChooseBean> categoryChooseBeanssahngpin = new ArrayList<CategoryChooseBean>();
    private String moidOne, moidTwe, moidname;
    private String nameOne = "";
    private TextView tv_dataTwo;
    private ListView lv_Classtwo;

    /**
     * 类别选择
     */
    protected void initPopuptWindowSc(View v) {
        oneadapter = new CategoryChooseAdp(SeekServiceListAty.this, categoryChooseBeansfuwu);

        getCategory("2");
        View popupWindow_view = getLayoutInflater().inflate(R.layout.categorychooseatynew, null, false);
        Button iv_category_bottom = (Button) popupWindow_view.findViewById(R.id.iv_category_bottom);
        iv_category_bottom.getBackground().setAlpha(100);//0~255透明度值
        ListView lv_Classone = (ListView) popupWindow_view.findViewById(R.id.lv_Classone);
        lv_Classtwo = (ListView) popupWindow_view.findViewById(R.id.lv_Classtwo);
        tv_dataTwo = (TextView) popupWindow_view.findViewById(R.id.tv_dataTwo);
        lv_Classone.setAdapter(oneadapter);
        lv_Classtwo.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int location, long l) {
                Twoadapter.setChoicePos(location);
                Twoadapter.notifyDataSetChanged();

                moidname = categoryChooseBeanssahngpin.get(location).getName();
                Log.v("lsq", "moidname+" + moidname);
                if (moidname.equals("全部")) {
                    if (TextUtils.isEmpty(nameOne)) {
                        tv_arrountshop_category.setText(operatename);
                        tv_title_content.setText(operatename);
                    } else {
                        tv_arrountshop_category.setText(nameOne);
                        tv_title_content.setText(nameOne);
                    }

                    moidTwe = moidOne;
                } else {
                    tv_arrountshop_category.setText(moidname);
                    tv_title_content.setText(moidname);
                    moidTwe = categoryChooseBeanssahngpin.get(location).getMoid();
                }

                handler.sendMessageDelayed(handler.obtainMessage(30), 300);


            }
        });

        lv_Classone.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {

                oneadapter.setChoicePos(postion);
                oneadapter.notifyDataSetChanged();
                moidOne = categoryChooseBeansfuwu.get(postion).getMoid();
                nameOne = categoryChooseBeansfuwu.get(postion).getName();
                getCategoryTwo("2", moidOne);


            }
        });

        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindowSc = new PopupWindow(popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                true);

        popupWindowSc.setFocusable(true);
        popupWindowSc.setBackgroundDrawable(new BitmapDrawable());
        // 设置动画效果
        popupWindowSc.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindowSc.showAsDropDown(v);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindowSc != null && popupWindowSc.isShowing()) {
                    popupWindowSc.dismiss();
                    popupWindowSc = null;
                }
                return false;
            }
        });

    }

    private void getCateGoryStart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCategoryChooseStart("2");
            }
        }).start();
    }

    private void getCategory(final String types) {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCategoryChoose(types);
            }
        }).start();
    }

    private void getCategoryChooseStart(String typeid) {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            categoryChooseBeansfuwu = api.get_managing_type(typeid, "1", "", "", "", "30", "1");
//            if (categoryChooseBeansfuwu != null) {
//                handler.sendEmptyMessage(21);
//            } else {
//                handler.sendEmptyMessage(22);
//            }
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            // handler.sendEmptyMessage(23);
            e.printStackTrace();
        } catch (WSError e) {
            // handler.sendEmptyMessage(24);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }

    private void getCategoryChoose(String typeid) {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            categoryChooseBeansfuwu = api.get_managing_type(typeid, "1", "", "", "", "30", "1");
            if (categoryChooseBeansfuwu != null) {
                handler.sendEmptyMessage(21);
            } else {
                handler.sendEmptyMessage(22);
            }
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(23);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(24);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }

    private void getCategoryTwo(final String type2, final String moid2) {
        StyledDialog.buildLoading(SeekServiceListAty.this, SeekServiceListAty.this.getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCategoryChooseTwo(type2, moid2);
            }
        }).start();
    }

    private void getCategoryChooseTwo(String type2, String moidtow) {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            categoryChooseBeanssahngpin = api.get_managing_type(type2, "2", moidtow, "", "", "30", "1");
            if (categoryChooseBeanssahngpin == null || "".equals(categoryChooseBeanssahngpin)) {
                handler.sendEmptyMessage(26);
            } else {
                Log.v("lsq", "categoryChooseBeanssahngpin1+" + categoryChooseBeanssahngpin.size());
                CategoryChooseBean categoryChooseBean = new CategoryChooseBean();
                categoryChooseBean.setName("全部");
                categoryChooseBeanssahngpin.add(0, categoryChooseBean);
                handler.sendEmptyMessage(25);
            }
        } catch (JSONException e) {
            handler.sendEmptyMessage(23);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(24);
            e.printStackTrace();
        } catch (Exception e) {
            handler.sendEmptyMessage(24);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent intent = new Intent();
        intent.putExtra("gpsjui", Tool.getGpskmorm(shopInfoBeansNew.get(arg2).getDistance()));
        intent.putExtra("sid", shopInfoBeansNew.get(arg2).getSid());
        intent.setClass(SeekServiceListAty.this, ShopDetailNewSNActivity.class);
        startActivity(intent);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SeekServiceListAty Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


}