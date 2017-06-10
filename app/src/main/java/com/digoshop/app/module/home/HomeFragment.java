package com.digoshop.app.module.home;

import android.Manifest;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseFragment;
import com.digoshop.app.common.widget.Utility;
import com.digoshop.app.module.arrountshops.model.ShopInfoBean;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.home.adp.HomeRecomendAdp;
import com.digoshop.app.module.home.adp.HomeShopItemAdapter;
import com.digoshop.app.module.home.adp.HomeShopItemAdapter.MyClickListener;
import com.digoshop.app.module.home.cityselect.CityPickerActivity;
import com.digoshop.app.module.home.cityselect.model.HotCityBean;
import com.digoshop.app.module.home.couponactive.ActivityDetails;
import com.digoshop.app.module.home.hscrollview.ClubsHorizonScrollView;
import com.digoshop.app.module.home.model.ActivityDetailBean;
import com.digoshop.app.module.home.model.Adimg;
import com.digoshop.app.module.home.model.AdsData;
import com.digoshop.app.module.home.model.HomeInfo;
import com.digoshop.app.module.home.model.ShopDetailInfo;
import com.digoshop.app.module.home.view.AutoVerticalScrollTextView;
import com.digoshop.app.module.login.Loginaty;
import com.digoshop.app.module.looksales.model.PoitGoodBean;
import com.digoshop.app.module.looksales.poitsale.SaleActivity;
import com.digoshop.app.module.search.SearchActivity;
import com.digoshop.app.module.seekservice.SeekServiceActivitynew;
import com.digoshop.app.module.seekshop.SearchStoreAty;
import com.digoshop.app.module.shopdetailnew.ShopDetailNewSNActivity;
import com.digoshop.app.module.userCenter.model.UserStatBean;
import com.digoshop.app.module.userCenter.module.MyMessageActivity;
import com.digoshop.app.module.userCenter.module.MyMessageDetailAty;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.DensityUtil;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.db.AssetsDataBasesManage;
import com.digoshop.app.utils.db.SQLiteDataBases_db;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONException;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * TODO 首页
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016年6月22日 下午8:57:02
 * @version: V1.0
 */
public class HomeFragment extends BaseFragment implements OnClickListener {

    private LinearLayout ll_shop, ll_service;
    private LinearLayout ll_guang_home, ll_custom_home, ll_point_home, ll_coupon_home, ll_aty_home, ll_all_home;
    private GridView home_gv_findshop;
    private GridView home_gv_seekservice;
    private GridView home_gv_brandshow;
    private ListView home_lv_commendshop;
    private HomeShopItemAdapter homeShopItemAdapter_pro;
    private HomeShopItemAdapter homeShopItemAdapter_service;
    private HomeShopItemAdapter homeShopItemAdapter_zhekou;
    private ArrayList<ShopDetailInfo> shopProInfos;
    private ArrayList<ShopDetailInfo> shopSerInfos;
    private ArrayList<ShopDetailInfo> shopzheInfos;
    private ArrayList<ShopDetailInfo> shopsaleInfos;
    private HomeInfo homeInfo;
    private RelativeLayout lin_home_message, re_home_saleaty;
    private LinearLayout re_home_search;
    private TextView tv_home_tvgps;
    private View rootView;
    private List<AdsData> mDatas = new ArrayList<>();
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mLocationClient = null;
    private String citycode;
    List<ActivityDetailBean> atyinfos;
    private String[] atystrs;
    private AutoVerticalScrollTextView verticalScrollTV;
    private boolean isRunning = true;
    private int number = 0;
    private int page = 1;
    private LinearLayout lin_home_saleproduct_in;
    private RelativeLayout re_home_jingpai, re_homeseekservice, re_home_seekshop;
    private LinearLayout lin_homejingpai;
    private UserStatBean userStatBean;
    private ImageView iv_homemessagetype;
    private Banner banner;//https://github.com/youth5201314/banner
    private AssetsDataBasesManage manage;
    private SQLiteDatabase db;
    private String digocitycode;
    private boolean ishttp = true;
    private ArrayList<HotCityBean> hotCityBeens;
    private PullToRefreshLayout refresh_store_view;
    private ImageView iv_homeads_big;
    private View view_homeads;
    private static boolean isatythread = true;
    private RelativeLayout re_home_title_bar;
    private int screenWidth;
    private int screenHeight;
    private LinearLayout lin_homesalepro_home, lin_home_salepro_in;
    private ClubsHorizonScrollView mClubsHorizonScrollView;
    private HomeRecomendAdp homeRecomendAdp;
    ArrayList<ShopInfoBean> shopInfoBeenlist;
    ArrayList<ShopInfoBean> shopInfoBeenlistNew = new ArrayList<>();
    private LinearLayout lin_homezhekou;
    private int itemivheignt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        // 调用多次的代码判断
//        if (null != rootView) {
//            ViewGroup parent = (ViewGroup) rootView.getParent();
//            if (null != parent) {
//                parent.removeView(rootView);
//            }
//        } else {
        // CrashReport.testJavaCrash();
//      String sss= null;
//              Log.v("lsq",sss.equals("")+"");
        rootView = inflater.inflate(R.layout.fragment_home, container,
                false);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        int DimensionPixelSize = DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_20));
        itemivheignt = ((screenWidth / 2) - (DimensionPixelSize * 3 / 2)) * 2 / 3 - 2 * DimensionPixelSize;
        screenHeight = dm.heightPixels;
        AssetsDataBasesManage.initManager(getActivity());
        manage = AssetsDataBasesManage.getManager();
        db = manage.getDatabase(AssetsDataBasesManage.coursesDBName);
        view_homeads = rootView.findViewById(R.id.view_homeads);
        banner = (Banner) rootView.findViewById(R.id.banner);
        re_home_saleaty = (RelativeLayout) rootView.findViewById(R.id.re_home_saleaty);
        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                if (mDatas != null) {
                    if (mDatas.size() > 0) {
                        if (mDatas != null & !"".equals(mDatas)) {
                            if (strbiglist != null) {
                                if (strbiglist.size() > 0) {
                                    Intent intent = new Intent();
                                    intent = new Intent(getActivity(), MyMessageDetailAty.class);
                                    intent.putExtra("weburl", mDatas.get(position - 1).imgid);
                                    intent.putExtra("title", mDatas.get(position - 1).title);
                                    startActivity(intent);
                                }
                            }

                        }
                    }
                }

            }
        });
        verticalScrollTV = (AutoVerticalScrollTextView) rootView
                .findViewById(R.id.textview_auto_roll);
        verticalScrollTV.setText("正在加载");
        verticalScrollTV.setOnClickListener(this);
        // 初始化定位
        mLocationClient = new AMapLocationClient(getActivity());
        // 设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        initView(rootView);
        get_stat();

        getGpsPermission();
        // initTest();
        // }
        refresh_store_view = ((PullToRefreshLayout) rootView
                .findViewById(R.id.refresh_store_view));
        refresh_store_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (getNetWifi()) {
                    page = 1;
                    if (shopInfoBeenlistNew != null) {
                        if (shopInfoBeenlistNew.size() > 0) {
                            shopInfoBeenlistNew.clear();
                        }
                    }
                    int DimensionPixelSize = DensityUtil.dip2px(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_10));
                    int getDimensionPixelOffset = DensityUtil.dip2px(App.getInstance(), (float) getResources().getDimensionPixelOffset(R.dimen.base_dimen_10));
                    int getDimension = DensityUtil.dip2px(App.getInstance(), (float) getResources().getDimension(R.dimen.base_dimen_10));
                    getCommendList();
                    homeRecomendAdp.notifyDataSetChanged();
                    noReadMessage();
                    homeinfoapi();
                    getSaleProcutList();
                    bigimg();
                    get_stat();
                    atyinfo();
                    ishttp = false;
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);

                }

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (shopInfoBeenlist != null) {
                    if (getNetWifi()) {
                        if (shopInfoBeenlist.size() > 0) {
                            shopInfoBeenlist.clear();
                        }
                        page = page + 1;
                        getCommendList();
                        homeRecomendAdp.appendData(shopInfoBeenlist);
                    } else {
                        App.getInstance().showToast("网络不给力，请检查网络设置");
                    }

                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);


            }
        });

        ViewGroup.LayoutParams params = view_homeads.getLayoutParams();
        params.height = screenWidth * 7 / 15;
        //params.height = screenWidth * 2 / 3;
        params.width = screenWidth;
        view_homeads.setLayoutParams(params);

        return rootView;
    }


    private void getHotCity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                gethotcityapi();
            }
        }).start();

    }

    private void gethotcityapi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            hotCityBeens = api.getHotcitys(digocitycode);

        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }

    private void getGpsPermission() {
        //http://blog.csdn.net/lmj623565791/article/details/50709663
        MPermissions.requestPermissions(HomeFragment.this, 4, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @PermissionGrant(4)
    public void requestContactSuccess() {
        if (getNetWifi()) {

            setUpMap();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }

    }

    boolean istimes = true;

    @PermissionDenied(4)
    public void requestContactFailed() {
        tv_home_tvgps.setText("定位北京");
        digocitycode = "010";
        if (istimes) {
            LocalSave.putValue(getActivity(), AppConfig.basefile, "CityCode", digocitycode);
            App.getInstance().showToast("获取权限失败!默认加载北京.");

            if (getNetWifi()) {
                atyinfo();
                getCommendList();
                homeinfoapi();
                getSaleProcutList();
                bigimg();
                get_stat();
                ishttp = false;

            } else {
                App.getInstance().showToast("网络不给力，请检查网络设置");
            }
            istimes = false;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // 获取消息未读数
    private void noReadMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NoReadMessagApi();
            }
        }).start();
    }

    private void NoReadMessagApi() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            String noreadmessstr = null;
            noreadmessstr = api.NoReadMessag();
            if (noreadmessstr != null) {
                if (noreadmessstr.equals("0")) {
                    //说明不存在未读消息
                    handler.sendEmptyMessage(9);
                } else if (!noreadmessstr.equals("0")) {
                    //说明存在未读消息
                    handler.sendEmptyMessage(8);
                } else {
                    //说明不存在未读消息
                    handler.sendEmptyMessage(9);
                }
            } else {
                //说明不存在未读消息
                handler.sendEmptyMessage(9);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (WSError e) {
            e.printStackTrace();
        }

    }

    // 获取用户基本消息
    private void get_stat() {
        //用户登录状态，登录成功未true ,默认未false
        AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
        if ("true".equals(AppConfig.isusertype)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    get_statapi();
                }
            }).start();
        } else {
        }

    }

    private String mypointstr;

    private void get_statapi() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            userStatBean = api.get_stat();
            if (userStatBean != null) {
                mypointstr = userStatBean.getTotal_intg();

                handler.sendEmptyMessage(7);
            }

        } catch (JSONException e) {
            Log.v("ceshi", "JSONException888");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }

    }

    boolean isgpstype = true;
    String latstr;
    String lonstr;
    // 声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {

                if (amapLocation.getErrorCode() == 0) {
                    // 定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();// 获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();// 获取纬度
                    amapLocation.getLongitude();// 获取经度
                    amapLocation.getAccuracy();// 获取精度信息
                    SimpleDateFormat df = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());
                    df.format(date);// 定位时间
                    amapLocation.getAddress();// 地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    amapLocation.getCountry();// 国家信息
                    amapLocation.getProvince();// 省信息

//					select * from CityTable where    citycode = '0434' and nn like '双辽市' and tag='1'  如果能查到就把nc当成citycode传入到接口
//							nationcode中，如果不存在则还是传citycode到参数nationcode
                    amapLocation.getCity();// 城市信息
                    Log.i("Amap", "getCity+" + amapLocation.getCity());

                    amapLocation.getDistrict();// 城区信息
                    amapLocation.getStreet();// 街道信息
                    amapLocation.getStreetNum();// 街道门牌号信息
                    citycode = amapLocation.getCityCode();// 城市编码
                    Log.v("Amap", "getCityCode+" + amapLocation.getCityCode());

                    amapLocation.getAdCode();// 地区编码
                    amapLocation.getAoiName();// 获取当前定位点的AOI信息
                    double lat = amapLocation.getLatitude();
                    double lon = amapLocation.getLongitude();
                    latstr = lat + "";
                    lonstr = lon + "";
                    Log.v("getDigoCityCode", "lat : " + lat + " lon : " + lon);
                    String lincitycode = amapLocation.getCityCode();
                    digocitycode = new SQLiteDataBases_db().getDigoCityCode(db, amapLocation.getCityCode(), amapLocation.getDistrict());
                    String cityname = null;
                    if (!lincitycode.equals(digocitycode)) {
                        cityname = amapLocation.getDistrict();
                    } else {
                        cityname = amapLocation.getCity();
                    }
                    if (ishttp) {
                        atyinfo();
                        getCommendList();
                        homeinfoapi();
                        getSaleProcutList();
                        bigimg();
                        getHotCity();
                        ishttp = false;
                        //正式版
                        tv_home_tvgps.setText(cityname);

                        LocalSave.putValue(getActivity(), AppConfig.basefile, "CityCode", digocitycode);
                        LocalSave.putValue(getActivity(), AppConfig.basefile, "lat", lat + "");
                        LocalSave.putValue(getActivity(), AppConfig.basefile, "lon", lon + "");
                    }
                    // 提交当前数据
                } else {
                    tv_home_tvgps.setText("北京");
                    digocitycode = "010";
                    if (isgpstype) {
                        LocalSave.putValue(getActivity(), AppConfig.basefile, "CityCode", digocitycode);
                        App.getInstance().showToast("定位失败,默认加载北京");

                        // 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                        atyinfo();
                        getCommendList();
                        homeinfoapi();
                        getSaleProcutList();
                        bigimg();
                        getHotCity();
                        isgpstype = false;
                        ishttp = false;
                    }
                }
                if (mLocationClient != null) {
                    mLocationClient.stopLocation();// 停止定位
                }
            }
        }

    };

    private void setUpMap() {
        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption
                .setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        // 设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(false);
        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        // 设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();// 停止定位
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();// 销毁定位客户端。
            mLocationClient = null;
        }

    }


    // 首页活动列表接口
    private void atyinfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AtyinfoApi();
            }
        }).start();

    }

    // 获取首页-限时竞拍，推荐活动两个优惠券，推荐产品店铺列表，推荐服务店铺列表
    private void homeinfoapi() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                homeInfoapi();
            }
        }).start();
    }

    // 滚动大图接口
    private void bigimg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bigImgapi();
            }
        }).start();
    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.v("test", "handelr55555");
                    showAdsPager(mDatas);
                    break;
                case 2:
                    App.getInstance().showToast("解析失败");
                    StyledDialog.dismissLoading();

                    break;
                case 3:
                    App.getInstance().showToast("请求失败");
                    StyledDialog.dismissLoading();
                    break;
                case 4:
                    if (atystrs == null || "".equals(atystrs)) {
                        re_home_saleaty.setVisibility(View.GONE);
                        verticalScrollTV.setText("暂无优惠活动");
                        isRunning = false;
                    } else {
                        re_home_saleaty.setVisibility(View.VISIBLE);
                        isRunning = true;
                        verticalScrollTV.setText(atystrs[0]);
                        if (isatythread) {
                            new Thread() {
                                @Override
                                public void run() {
                                    isatythread = false;
                                    while (isRunning) {
                                        SystemClock.sleep(3000);
                                        handler.sendEmptyMessage(199);
                                    }
                                }
                            }.start();
                        }

                    }

                    break;
                case 6:


                    if (shopProInfos != null) {
                        if (shopProInfos.size() > 0) {
                            re_home_seekshop.setVisibility(View.VISIBLE);
                        } else {
                            re_home_seekshop.setVisibility(View.GONE);

                        }
                    }
                    if (shopSerInfos != null) {
                        if (shopSerInfos.size() > 0) {
                            re_homeseekservice.setVisibility(View.VISIBLE);
                        } else {
                            re_homeseekservice.setVisibility(View.GONE);

                        }
                    }


                    // 适配找商铺/寻服务数据
                    homeShopItemAdapter_pro = new HomeShopItemAdapter(itemivheignt);
                    homeShopItemAdapter_pro.setData(getActivity(), shopProInfos,
                            mProListener);

                    home_gv_findshop.setAdapter(homeShopItemAdapter_pro);

                    homeShopItemAdapter_service = new HomeShopItemAdapter(itemivheignt);
                    homeShopItemAdapter_service.setData(getActivity(),
                            shopSerInfos, mSerListener);
                    home_gv_seekservice.setAdapter(homeShopItemAdapter_service);
                    if (shopzheInfos != null) {
                        if (shopzheInfos.size() > 0) {
                            lin_homezhekou.setVisibility(View.VISIBLE);
                        } else {
                            lin_homezhekou.setVisibility(View.GONE);

                        }
                    }
                    homeShopItemAdapter_zhekou = new HomeShopItemAdapter(itemivheignt);
                    homeShopItemAdapter_zhekou.setData(getActivity(), shopzheInfos, mzheListener);
                    home_gv_brandshow.setAdapter(homeShopItemAdapter_zhekou);


                    StyledDialog.dismissLoading();


                    break;
                case 199:
                    //strings==null||"".equals(strings)||Array.getLength(strings)==0
                    if (atystrs == null || "".equals(atystrs) || Array.getLength(atystrs) == 0) {

                    } else {
                        verticalScrollTV.next();
                        number++;
                        verticalScrollTV.setText(atystrs[number % atystrs.length]);
                    }
                    break;
                case 7:
                    mypointstr = userStatBean.getTotal_intg();
                    LocalSave.putValue(App.getInstance(), AppConfig.basefile, "mypointstr", mypointstr);
                    break;
                case 8:// 存在未读消息
                    iv_homemessagetype.setVisibility(View.VISIBLE);
                    break;
                case 9:// 不存在未读消息
                    iv_homemessagetype.setVisibility(View.GONE);
                    break;
                case 10:
                    Log.v("ceshi", "*aaaa#" + "10");
                    lin_home_salepro_in.setVisibility(View.GONE);
                    break;
                case 11:
                    lin_home_salepro_in.setVisibility(View.VISIBLE);
                    if (poitGoodBeanArrayList != null) {
                        if (poitGoodBeanArrayList.size() > 0) {
                            Log.v("ceshi", "*aaaa#" + "11++"+poitGoodBeanArrayList.size());
                            mClubsHorizonScrollView.setListData(poitGoodBeanArrayList);
                        }
                    }
                    break;
                case 12:
                    // App.getInstance().showToast("没有数据了12!");
                    if (shopInfoBeenlist != null) {
                        // App.getInstance().showToast("没有数据了!");
                    } else {
                        if (shopInfoBeenlistNew.size() > 0) {
                            //  App.getInstance().showToast("没有数据了!");
                        } else {
                            lin_home_saleproduct_in.setVisibility(View.GONE);
                        }

                    }

                    StyledDialog.dismissLoading();

                    break;
                case 13:
                    lin_home_saleproduct_in.setVisibility(View.VISIBLE);
                    if (shopInfoBeenlistNew != null) {
                        if (shopInfoBeenlist != null) {
                            shopInfoBeenlistNew.addAll(shopInfoBeenlist);
                        }
                    } else {
                        shopInfoBeenlistNew = new ArrayList<>();
                        if (shopInfoBeenlist != null) {
                            shopInfoBeenlistNew.addAll(shopInfoBeenlist);
                        }
                    }

                    if (homeRecomendAdp != null) {
                        homeRecomendAdp.notifyDataSetChanged();
                    } else {
                        homeRecomendAdp = new HomeRecomendAdp(getActivity(), shopInfoBeenlistNew);
                    }
                    StyledDialog.dismissLoading();

                    Utility.setListViewHeightBasedOnChildren(home_lv_commendshop);
                    break;


            }
        }
    };

    private void getSaleProcutList() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                getSaleProcutListApi();
            }
        }).start();
    }

    ArrayList<PoitGoodBean> poitGoodBeanArrayList;

    private void getSaleProcutListApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            poitGoodBeanArrayList = api.getHomeSaleList(digocitycode, "", "1");
            if (poitGoodBeanArrayList != null) {
                if (poitGoodBeanArrayList.size() > 0) {
                    handler.sendEmptyMessage(11);
                } else {
                    handler.sendEmptyMessage(10);
                }
            } else {
                handler.sendEmptyMessage(10);
            }
        } catch (JSONException e) {
            handler.sendEmptyMessage(10);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(10);
            e.printStackTrace();
        }
    }

    private void getCommendList() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                getCommendListApi();
            }
        }).start();
    }


    private void getCommendListApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            shopInfoBeenlist = api.getHomeCommendShop(digocitycode, page + "");
            if (shopInfoBeenlist != null) {
                handler.sendEmptyMessage(13);
            } else {
                handler.sendEmptyMessage(12);
            }
        } catch (JSONException e) {
            handler.sendEmptyMessage(12);
            e.printStackTrace();
            handler.sendEmptyMessage(12);
        } catch (WSError e) {
            handler.sendEmptyMessage(12);
            e.printStackTrace();
        }
    }

    private void homeInfoapi() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            homeInfo = api.queryRecommendShop(digocitycode);
            if (homeInfo == null) {
                handler.sendEmptyMessage(3);
            } else {
                shopProInfos = homeInfo.getShopProInfos();
                shopSerInfos = homeInfo.getShopSerInfos();
                shopzheInfos = homeInfo.getShopRebate();
                shopsaleInfos = homeInfo.getShopGoos();
                Log.v("ceshi", "shopProInfos+" + shopProInfos.size());
                Log.v("ceshi", "shopSerInfos+" + shopSerInfos.size());

                handler.sendEmptyMessage(6);
            }
        } catch (JSONException e) {
            handler.sendEmptyMessage(2);
            Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(3);
            Log.i("ceshi", "WSError22222" + e.getMessage());
            e.printStackTrace();
        }

    }

    private void AtyinfoApi() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            atyinfos = api.getLookSeekAty("", digocitycode, latstr, lonstr, "SY_MR_ACTIVITY_TEXT");

            if (atyinfos != null) {
                if (atyinfos.size() > 0) {
                    atystrs = null;
                    atystrs = new String[atyinfos.size()];
                    for (int i = 0; i < atyinfos.size(); i++) {
                        atystrs[i] = atyinfos.get(i).getMnti();
                    }
                } else {
                    atystrs = null;
                }

            } else {
                atystrs = null;
            }
            handler.sendEmptyMessage(4);
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            Log.v("ceshi", "WSError5555");
            e.printStackTrace();
        }

    }

    private void bigImgapi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            //  List<Adimg> lists = api.queryRecommendMerchant(digocitycode);
            List<Adimg> lists = api.getNewHomeBanner(digocitycode);
            mDatas = new ArrayList<AdsData>();
            if (mDatas != null) {
                mDatas.clear();
            }
            if (lists != null) {
                if (lists.size() > 0) {
                    for (int i = 0; i < lists.size(); i++) {
                        AdsData d = new AdsData();
                        d.imgurl = lists.get(i).getCover();
                        d.imgid = lists.get(i).getUri();
                        d.infourl = lists.get(i).getInfo();
                        d.title = lists.get(i).getTitle();
                        mDatas.add(d);
                    }
                    handler.sendEmptyMessage(1);

                } else {
                    mDatas = null;
                    handler.sendEmptyMessage(1);
                }
            } else {
                mDatas = null;
                handler.sendEmptyMessage(1);
            }

        } catch (JSONException e) {
            handler.sendEmptyMessage(2);
            Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        }
    }

    void initView(View v) {

        lin_homezhekou = (LinearLayout) v.findViewById(R.id.lin_homezhekou);
        lin_home_saleproduct_in = (LinearLayout) v.findViewById(R.id.lin_home_saleproduct_in);
        home_lv_commendshop = (ListView) v.findViewById(R.id.home_lv_commendshop);
        homeRecomendAdp = new HomeRecomendAdp(getActivity(), shopInfoBeenlistNew);
        home_lv_commendshop.setAdapter(homeRecomendAdp);
        home_lv_commendshop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent();
                intent.putExtra("sid", shopInfoBeenlistNew.get(position).getSid());
                intent.setClass(getActivity(), ShopDetailNewSNActivity.class);
                startActivity(intent);
            }
        });
        mClubsHorizonScrollView = (ClubsHorizonScrollView) v.findViewById(R.id.clubs_horizon_scrollview);

        lin_home_salepro_in = (LinearLayout) v.findViewById(R.id.lin_home_salepro_in);
        lin_homesalepro_home = (LinearLayout) v.findViewById(R.id.lin_homesalepro_home);
        lin_homesalepro_home.setOnClickListener(this);
        home_gv_brandshow = (GridView) v.findViewById(R.id.home_gv_brandshow);
        ll_guang_home = (LinearLayout) v.findViewById(R.id.ll_guang_home);
        ll_guang_home.setOnClickListener(this);
        ll_custom_home = (LinearLayout) v.findViewById(R.id.ll_custom_home);
        ll_custom_home.setOnClickListener(this);
        ll_point_home = (LinearLayout) v.findViewById(R.id.ll_point_home);
        ll_point_home.setOnClickListener(this);
        ll_coupon_home = (LinearLayout) v.findViewById(R.id.ll_coupon_home);
        ll_coupon_home.setOnClickListener(this);

        ll_all_home = (LinearLayout) v.findViewById(R.id.ll_all_home);
        ll_all_home.setOnClickListener(this);
        ll_aty_home = (LinearLayout) v.findViewById(R.id.ll_aty_home);
        ll_aty_home.setOnClickListener(this);
        iv_homeads_big = (ImageView) v.findViewById(R.id.iv_homeads_big);
        re_home_jingpai = (RelativeLayout) v.findViewById(R.id.re_home_jingpai);
        re_homeseekservice = (RelativeLayout) v.findViewById(R.id.re_homeseekservice);
        re_home_seekshop = (RelativeLayout) v.findViewById(R.id.re_home_seekshop);
        re_home_jingpai.setOnClickListener(this);
        lin_homejingpai = (LinearLayout) v.findViewById(R.id.lin_homejingpai);
        lin_homejingpai.setOnClickListener(this);
        lin_homejingpai.setEnabled(false);
        lin_home_message = (RelativeLayout) v
                .findViewById(R.id.lin_home_message);
        lin_home_message.setOnClickListener(this);
        iv_homemessagetype = (ImageView) v
                .findViewById(R.id.iv_homemessagetype);
        tv_home_tvgps = (TextView) v.findViewById(R.id.tv_home_tvgps);
        re_home_title_bar = (RelativeLayout) v.findViewById(R.id.re_home_title_bar);
        tv_home_tvgps.setOnClickListener(this);
        re_home_search = (LinearLayout) v.findViewById(R.id.re_home_search);
        re_home_search.setOnClickListener(this);
        ll_shop = (LinearLayout) v.findViewById(R.id.ll_shop);
        ll_service = (LinearLayout) v.findViewById(R.id.ll_service);
        ll_shop.setOnClickListener(this);
        ll_service.setOnClickListener(this);
        home_gv_findshop = (GridView) v.findViewById(R.id.home_gv_findshop);
        home_gv_seekservice = (GridView) v
                .findViewById(R.id.home_gv_seekservice);
    }

    ArrayList<String> strbiglist;

    void showAdsPager(List<AdsData> mDatas) {

        if (mDatas == null) {
            // re_home_title_bar.setBackgroundResource(R.color.title_search_bg);

            iv_homeads_big.setVisibility(View.VISIBLE);
//            ViewGroup.LayoutParams iv_homeads_bigparams = iv_homeads_big.getLayoutParams();
//            iv_homeads_bigparams.height = screenWidth * 2 / 3;
//            iv_homeads_bigparams.width = screenWidth;
//            iv_homeads_big.setLayoutParams(iv_homeads_bigparams);

            if (banner != null) {
                banner.setVisibility(View.GONE);
            }
            //banner.setImages(strlist).setImageLoader(new GlideImageLoader()).start();
        } else {
            if (banner != null) {
                if (mDatas != null) {
                    if (mDatas.size() == 1) {
                        if (TextUtils.isEmpty(mDatas.get(0).imgurl)) {
                            iv_homeads_big.setVisibility(View.VISIBLE);
                            if (banner != null) {
                                banner.setVisibility(View.GONE);
                            }
                            return;
                        }
                    }
                }
                re_home_title_bar.setBackgroundResource(R.color.transparent);
                strbiglist = new ArrayList<String>();
                if (strbiglist != null) {
                    strbiglist.clear();
                }

                banner.setVisibility(View.VISIBLE);
                iv_homeads_big.setVisibility(View.GONE);
                for (int i = 0; i < mDatas.size(); i++) {
                    Log.v("test", "mDatas.get(i).imgurl" + mDatas.get(i).imgurl);
                    if (!TextUtils.isEmpty(mDatas.get(i).imgurl)) {
                        strbiglist.add(mDatas.get(i).imgurl);
                    }
                }
//                ViewGroup.LayoutParams iv_homeads_bigparams = iv_homeads_big.getLayoutParams();
//                iv_homeads_bigparams.height = screenWidth * 7 / 15;
//                iv_homeads_bigparams.width = screenWidth;
//                iv_homeads_big.setLayoutParams(iv_homeads_bigparams);
//                ViewGroup.LayoutParams params = banner.getLayoutParams();
//                params.height = screenWidth * 7 / 15;
//                params.width = screenWidth;
//                banner.setLayoutParams(params);
                banner.update(strbiglist);
                banner.setImageLoader(new GlideImageLoader()).start();

                // banner.setImages(strbiglist).setImageLoader(new GlideImageLoader()).start();

            }
        }


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.v("change", "www");
//            if (getNetWifi()) {
//                noReadMessage();
//                atyinfo();
//            }

        } else {
            Log.v("change", "www222");
            if (getNetWifi()) {
                noReadMessage();
                atyinfo();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private static final int Mars = 0;

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.re_home_search://搜索
                intent.setClass(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.textview_auto_roll://推荐活动
                if (atystrs != null && !"".equals(atystrs)) {
                    if (atyinfos != null) {
                        ActivityDetailBean homeActivityInfo = atyinfos.get(number
                                % atystrs.length);
                        Intent intentaty = new Intent();
                        intentaty.setClass(getActivity(), ActivityDetails.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("atycontent", homeActivityInfo);
                        intentaty.putExtras(bundle);
                        startActivity(intentaty);
                    }
                }
                break;
            case R.id.ll_shop:// 找商铺
                intent.setClass(getActivity(), SearchStoreAty.class);
                startActivity(intent);
                break;
            case R.id.ll_service:// 寻服务
                intent.setClass(getActivity(), SeekServiceActivitynew.class);
                startActivity(intent);
                break;
            case R.id.ll_guang_home://逛商场
                intent.setClass(getActivity(), LookStoreAty.class);
                startActivity(intent);
                break;
            case R.id.ll_custom_home://定制介绍和选择
                intent.setClass(getActivity(), CustomContentAty.class);
                startActivity(intent);
                break;
            case R.id.ll_point_home://积分兑
                intent.setClass(getActivity(), SaleActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_homesalepro_home://更多兑换
                intent.setClass(getActivity(), SaleActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_coupon_home://优惠券
                intent.setClass(getActivity(), CouponHomeHuaAty.class);
                startActivity(intent);
                break;
            case R.id.ll_aty_home://活动购
                intent.setClass(getActivity(), AtyHomeAty.class);
                startActivity(intent);
                break;
            case R.id.ll_all_home://全部
//                intent.setClass(getActivity(), TestAty.class);
//                startActivity(intent);
                intent.setClass(getActivity(), AllHomeAty.class);
                startActivity(intent);
                break;
            case R.id.tv_home_tvgps://重新定位
                //mLocationClient.setLocationListener(mLocationListener);
                //App.getInstance().showToast("获取定位");
                intent.setClass(getActivity(), CityPickerActivity.class);
                intent.putExtra("hotCityBeens", hotCityBeens);
                startActivityForResult(intent, Mars);

                break;


            case R.id.lin_home_message:// 我的消息
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(getActivity(), MyMessageActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), Loginaty.class);
                    startActivity(intent);
                }

                break;

            default:
                break;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || data.equals("")) {
            // tv_home_tvgps.setText("获取失败");
        } else {
            if (shopInfoBeenlistNew != null) {
                shopInfoBeenlistNew.clear();
            }
            if (homeRecomendAdp != null) {
                homeRecomendAdp.notifyDataSetChanged();
            }
            String city = data.getExtras().getString("cityname");// 得到新Activity关闭后返回的数据

            String citycode = data.getExtras().getString("citycode");
            Log.v("test", "2citycode" + citycode);
            tv_home_tvgps.setText(city);
            digocitycode = citycode;
            Log.v("niao", "digocitycoda+" + digocitycode);
            // 用putString的方法保存数据
            LocalSave.putValue(getActivity(), AppConfig.basefile, "CityCode", digocitycode);
            if (getNetWifi()) {
                StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                bigimg();
                atyinfo();
                getCommendList();
                homeinfoapi();
                getSaleProcutList();
                getHotCity();
            } else {
                App.getInstance().showToast("网络不给力，请检查网络设置");
            }

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isRunning = false;
    }

    /**
     * 实现类，响应按钮点击事件 店铺监听
     */
    private MyClickListener mProListener = new MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            Intent intent = new Intent();
            intent.putExtra("sid", shopProInfos.get(position).getSid());
            intent.setClass(getActivity(), ShopDetailNewSNActivity.class);
            startActivity(intent);
        }
    };
    /**
     * 实现类，响应按钮点击事件 服务监听
     */
    private MyClickListener mSerListener = new MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            Intent intent = new Intent();
            intent.putExtra("sid", shopSerInfos.get(position).getSid());
            intent.setClass(getActivity(), ShopDetailNewSNActivity.class);
            startActivity(intent);
        }
    };
    /**
     * 实现类，响应按钮点击事件 店铺监听
     */
    private MyClickListener mzheListener = new MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            Intent intent = new Intent();
            intent.putExtra("sid", shopzheInfos.get(position).getSid());
            intent.setClass(getActivity(), ShopDetailNewSNActivity.class);
            startActivity(intent);
        }
    };
    /**
     * 实现类，响应按钮点击事件 店铺监听
     */
    private MyClickListener msaleListener = new MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            Intent intent = new Intent();
            intent.putExtra("sid", shopsaleInfos.get(position).getSid());
            intent.setClass(getActivity(), ShopDetailNewSNActivity.class);
            startActivity(intent);
        }
    };

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

}
