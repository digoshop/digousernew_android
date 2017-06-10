package com.digoshop.app.module.seekshop;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.GridView;
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
import com.digoshop.app.module.seekshop.model.BrandDate;
import com.digoshop.app.module.shopdetailnew.ShopDetailNewSNActivity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.DensityUtil;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.db.AssetsDataBasesManage;
import com.digoshop.app.utils.db.CityBean;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class SeekShopListAty extends BaseActivity implements OnClickListener, OnItemClickListener {
    private TextView tv_title_content;// 三级类别名字
    private String CityCodestr = "";
    private String latstr = "";
    private String logstr = "";
    private String regionIdstr = "";// 区域id
    private AssetsDataBasesManage manage;
    private SQLiteDatabase db;
    private ArrayList<CityBean> quyulistsNew = new ArrayList<>();
    private String brandIdstr = "";
    private String mfidstr = "";
    private String operateIdstr;
    private String operatename;
    private String sortstr = "";

    private RelativeLayout re_nolist;

    private ArrayList<CityBean> quyulists;
    private RelativeLayout lLayout_storelist_zonghe, lLayout_storelist_pinpai,
            lLayout_storelist_chengqu;
    private PopupWindow popupWindowquyu;
    private ArountShop_textAdp arountShop_textAdp;
    private ArountShop_textAdp arountShop_textbarAdp;
    private ArrayList<CityBean> shopinfobard;// 区域数据集合
    private ArrayList<CityBean> shopinfobardNew = new ArrayList<>();// 区域数据集合
    private ArrayList<BrandDate> brandDates;
    private PopupWindow popupWindowcate;
    private boolean isOnclick = true;
    private PullToRefreshLayout ptrl;
    private ArrayList<ShopInfoBean> shopInfoBeans;
    private ArrayList<ShopInfoBean> shopInfoBeansNew = new ArrayList<>();
    private ArroutShopAdp arroutShopAdp;
    private ListView lv_seekshoplist;
    private int page = 1;
    private int page_length = 10;
    private TextView tv_arrountshop_quyu;
    private TextView tv_arrountshop_category;
    private TextView tv_arrountshop_area;
    private ArountShop_textAdp arountShop_textquyuAdp;
    private PullToRefreshLayout brandptrl;
    private int brandpage = 1;
    private int brandpage_length = 9;
    float heightpx = 0;
    float heightpxquyu = 0;
    float fheingt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seekshoplistaty);
        fheingt = App.getInstance().getSelectHeight();
        tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("个人中心");
        Intent intent = getIntent();
        operateIdstr = intent.getStringExtra("operateId");
        operatename = intent.getStringExtra("operatename");

        tv_arrountshop_category = (TextView) findViewById(R.id.tv_arrountshop_category);
        tv_arrountshop_area = (TextView) findViewById(R.id.tv_arrountshop_area);
        tv_arrountshop_quyu = (TextView) findViewById(R.id.tv_arrountshop_quyu);
        re_nolist = (RelativeLayout) findViewById(R.id.re_nolist);
        lv_seekshoplist = (ListView) findViewById(R.id.lv_seekshoplist);
        lv_seekshoplist.setOnItemClickListener(this);
        lLayout_storelist_zonghe = (RelativeLayout) findViewById(R.id.lLayout_storelist_zonghe);
        lLayout_storelist_pinpai = (RelativeLayout) findViewById(R.id.lLayout_storelist_pinpai);
        lLayout_storelist_chengqu = (RelativeLayout) findViewById(R.id.lLayout_storelist_chengqu);
        lLayout_storelist_chengqu.setOnClickListener(this);
        lLayout_storelist_pinpai.setOnClickListener(this);
        lLayout_storelist_zonghe.setOnClickListener(this);
        tv_title_content.setText(operatename);

        AssetsDataBasesManage.initManager(this);
        manage = AssetsDataBasesManage.getManager();
        db = manage.getDatabase(AssetsDataBasesManage.coursesDBName);
        CityCodestr = LocalSave.getValue(SeekShopListAty.this, AppConfig.basefile, "CityCode", "");
        latstr = LocalSave.getValue(SeekShopListAty.this, AppConfig.basefile, "lat", "");
        logstr = LocalSave.getValue(SeekShopListAty.this, AppConfig.basefile, "lon", "");
        arroutShopAdp = new ArroutShopAdp(SeekShopListAty.this,
                shopInfoBeansNew);
        lv_seekshoplist.setAdapter(arroutShopAdp);
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
        if (getNetWifi()) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            queryShopList();
            getBrandListnew();
            upDBNEW();
        } else {
            heightpxquyu = (float) fheingt;
            heightpx = (float) fheingt;
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }

    }

    // 获取开启城市的区县
    private void upDB() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updbapi();
            }
        }).start();

    }

    // 获取开启城市的区县
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
            shopInfoBeans = api.queryShopMulti(brandIdstr, latstr, logstr,
                    mfidstr, operateIdstr, "3", regionIdstr, sortstr, "1",
                    CityCodestr, "" + page, "" + page_length);
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
                    App.getInstance().showToast("没有数据了!");
                    re_nolist.setVisibility(View.GONE);
                    StyledDialog.dismissLoading();

                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
                case 4:
                    StyledDialog.dismissLoading();

                    break;

                case 6:
                    getBrandList();
                    break;
                case 7:
                    StyledDialog.dismissLoading();

                    shopinfobard = new ArrayList<CityBean>();
                    if (shopinfobardNew != null) {
                        if (shopinfobardNew.size() == 0) {
                            CityBean cityBean0 = new CityBean();
                            cityBean0.setNn("全部");
                            cityBean0.setMidstr("");
                            shopinfobard.add(cityBean0);
                            shopinfobardNew.addAll(shopinfobard);
                            arountShop_textbarAdp.notifyDataSetChanged();
                        }
                    }
                    break;
                case 8:

                    shopinfobard = new ArrayList<CityBean>();
                    if (shopinfobardNew != null) {
                        if (shopinfobardNew.size() == 0) {
                            CityBean cityBean1 = new CityBean();
                            cityBean1.setNn("全部");
                            cityBean1.setMidstr("");
                            shopinfobard.add(cityBean1);
                        }
                    }
                    for (int i = 0; i < brandDates.size(); i++) {
                        CityBean cityBean = new CityBean();
                        cityBean.setNn(brandDates.get(i).getBrand_name());
                        cityBean.setMidstr(brandDates.get(i).getBrand_id());
                        shopinfobard.add(cityBean);
                    }
                    shopinfobardNew.addAll(shopinfobard);
                    arountShop_textbarAdp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();
                    brandptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
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

    private void getBrandList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getBrandListapi(operateIdstr);
            }
        }).start();
    }

    private void getBrandListapi(String moids) {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            brandDates = api.getBrandToMoid(moids, brandpage + "", brandpage_length + "");
            if (brandDates == null || "".equals(brandDates)) {
                handler.sendEmptyMessage(7);
            } else {
                handler.sendEmptyMessage(8);
            }
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(7);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }

    }

    private PopupWindow popupWindowbrand;

    /***
     * 获取PopupWindow实例
     */
    private void getPopupWindowBrand(View v) {
        if (null != popupWindowbrand && popupWindowbrand.isShowing()) {
            popupWindowbrand.dismiss();
            return;
        } else {
            initPopuptWindowBrand(v);
        }
    }

    private void getBrandListnew() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getBrandListapinew(operateIdstr);
            }
        }).start();
    }

    private void getBrandListapinew(String moids) {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            brandDates = api.getBrandToMoid(moids, brandpage + "", brandpage_length + "");
            if (brandDates != null) {
                if((brandDates.size()+1)%2==0){
                    heightpx = (float) (fheingt * (brandDates.size()+1)/2);
                }else{
                    heightpx = (float) (fheingt * (brandDates.size()+2)/2);

                }


            } else {
                heightpx = (float) fheingt;
            }

        } catch (JSONException e) {
            heightpx = (float) fheingt;
            Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            heightpx = (float) fheingt;
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }

    }

    /**
     * 创建业态商圈pop
     */
    protected void initPopuptWindowBrand(View v) {
        if (shopinfobardNew != null) {
            shopinfobardNew.clear();
        }
        arountShop_textbarAdp = new ArountShop_textAdp(SeekShopListAty.this,
                shopinfobardNew);
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        brandpage = 1;
        getBrandList();
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(
                R.layout.fragment_arountshop_girdview, null, false);
        brandptrl = ((PullToRefreshLayout) popupWindow_view.findViewById(R.id.refresh_view_brand));
        brandptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (shopinfobardNew.size() > 0) {
                    shopinfobardNew.clear();
                }
                brandpage = 1;
                getBrandList();
                arountShop_textbarAdp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (shopinfobard != null) {
                    if (shopinfobard.size() > 0) {
                        shopinfobard.clear();
                    }
                    brandpage = brandpage + 1;
                    getBrandList();
                    arountShop_textbarAdp.appendData(shopinfobard);

                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
        final GridView gridViewView = (GridView) popupWindow_view
                .findViewById(R.id.gv_arountshop);
        gridViewView.setAdapter(arountShop_textbarAdp);
        gridViewView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int postion, long arg3) {
                if (null != popupWindowbrand) {
                    popupWindowbrand.dismiss();
                    //	popupWindowbrand = null;
                    // return;
                }
                StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                if (shopInfoBeans != null) {
                    shopInfoBeans.clear();
                }
                if (shopInfoBeansNew != null) {
                    shopInfoBeansNew.clear();
                }
                brandpage = 1;
                page = 1;
                tv_arrountshop_category.setText(shopinfobardNew.get(postion).getNn());
                brandIdstr = shopinfobardNew.get(postion).getMidstr();
                queryShopList();
                arroutShopAdp.notifyDataSetChanged();
            }
        });

        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindowbrand = new PopupWindow(popupWindow_view,
                LayoutParams.MATCH_PARENT, DensityUtil.floatToInt(heightpx), true);

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
                    //popupWindowbrand = null;
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
        ArountShop_textAdp arountShop_textAdp = new ArountShop_textAdp(SeekShopListAty.this, shopinfoarea);

        View popupWindow_view = SeekShopListAty.this.getLayoutInflater().inflate(
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
        arountShop_textquyuAdp = new ArountShop_textAdp(this, quyulistsNew);
        View popupWindow_view = getLayoutInflater().inflate(
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
                tv_arrountshop_area.setText(quyulistsNew.get(postion).getNn());
                regionIdstr = quyulistsNew.get(postion).getId();
                queryShopList();
                arroutShopAdp.notifyDataSetChanged();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lLayout_storelist_zonghe:// 综合搜索
                getPopupWindowcategory();
                // 这里是位置显示方式,在屏幕的下方
                popupWindowcate.showAsDropDown(v);
                break;
            case R.id.lLayout_storelist_pinpai:// 品牌搜索
                getPopupWindowBrand(v);
                break;
            case R.id.lLayout_storelist_chengqu:// 区域搜索
                getPopupWindowQuyu();
                // 这里是位置显示方式,在屏幕的下方
                popupWindowquyu.showAsDropDown(v);
                break;
            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent intent = new Intent();
        intent.putExtra("gpsjui", Tool.getGpskmorm(shopInfoBeansNew.get(arg2).getDistance()));
        intent.putExtra("sid", shopInfoBeansNew.get(arg2).getSid());
        intent.setClass(SeekShopListAty.this, ShopDetailNewSNActivity.class);
        startActivity(intent);
    }

}