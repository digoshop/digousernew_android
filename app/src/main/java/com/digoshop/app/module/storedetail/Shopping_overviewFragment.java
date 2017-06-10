package com.digoshop.app.module.storedetail;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseFragment;
import com.digoshop.app.module.home.GlideImageLoader;
import com.digoshop.app.module.home.couponactive.ActivityDetails;
import com.digoshop.app.module.home.model.ActivityDetailBean;
import com.digoshop.app.module.home.model.NewsBeean;
import com.digoshop.app.module.shopdetail.bigimg.aty.ImagePagerActivity;
import com.digoshop.app.module.storedetail.model.FloorBean;
import com.digoshop.app.module.storedetail.model.Merchant;
import com.digoshop.app.module.storedetail.model.StoreDetailInfo;
import com.digoshop.app.module.storedetail.model.StoredetailMaInfo;
import com.digoshop.app.module.storedetail.view.GridViewForScrollView;
import com.digoshop.app.module.storedetail.view.MoreTextView;
import com.digoshop.app.utils.DigoGps;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;

import org.json.JSONException;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.digoshop.app.module.storedetail.StoreDetailActivity.screenWidthstore;
import static com.digoshop.app.utils.Tool.getNetWifi;


/**
 * TODO<商场详情-商场概况>
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016年7月21日 下午11:31:01
 * @version: V1.0
 */
@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Shopping_overviewFragment extends BaseFragment implements
        OnClickListener {
    private TextView tv_shopping, tv_storedetail_title, tv_storedetail_time,
            tv_storedeatil_address;
    private ImageView   iv_storedetail_tel ;
    private MoreTextView tv_MoreTextView;
    private GridViewForScrollView gridview_main;
    private ScrollView sv_storedetail;
    // 生成动态数组，并且转入数据
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private View rootView;
    private Merchant merchant;
    private StoreDetailInfo storeDetailInfo;
    private NewsBeean newsbean;
    private TextView tv_homediscount_hot;// 活动
    private TextView tv_storedetail_tel;// 电话
    private RelativeLayout re_storegps;
    private ArrayList<StoredetailMaInfo> urlbiglists;
    private List<String> imageUris;
    private TextView tv_storedetail_num;
    private ArrayList<FloorBean> floorBeans;
    private Banner banner;
    private String tag = "1";
    private Double latd, lgtd;
    private  ArrayList<String> imageurislist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 调用多次的代码判断
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_shopping_overview,
                    container, false);
            sv_storedetail = (ScrollView) rootView
                    .findViewById(R.id.sv_storedetail);
            tv_storedetail_num = (TextView) rootView
                    .findViewById(R.id.tv_storedetail_num);
            re_storegps = (RelativeLayout) rootView
                    .findViewById(R.id.re_storegps);
            re_storegps.setOnClickListener(this);

            banner = (Banner) rootView.findViewById(R.id.banner);
            banner.setOnBannerClickListener(new OnBannerClickListener() {
                @Override
                public void OnBannerClick(int position) {
                    if(imageurislist!=null){
                        if(imageurislist.size()>0){
                            Intent intenta = new Intent(getActivity(), ImagePagerActivity.class);
                            // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                            intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageurislist);
                            intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position-1);
                            getActivity().startActivity(intenta);
                        }
                    }

                }
            });
            tv_homediscount_hot = (TextView) rootView
                    .findViewById(R.id.tv_homediscount_hot);
            tv_homediscount_hot.setOnClickListener(this);
            tv_storedetail_tel = (TextView) rootView
                    .findViewById(R.id.tv_storedetail_tel);
            tv_storedetail_time = (TextView) rootView
                    .findViewById(R.id.tv_storedetail_time);
            iv_storedetail_tel = (ImageView) rootView
                    .findViewById(R.id.iv_storedetail_tel);
            iv_storedetail_tel.setOnClickListener(this);
            tv_storedetail_title = (TextView) rootView
                    .findViewById(R.id.tv_storedetail_title);
            tv_storedeatil_address = (TextView) rootView
                    .findViewById(R.id.tv_storedeatil_address);
            tv_MoreTextView = (MoreTextView) rootView
                    .findViewById(R.id.tv_MoreTextView);
            gridview_main = (GridViewForScrollView) rootView
                    .findViewById(R.id.gridview_main);
            TextView    tv_title_content = (TextView) rootView .findViewById(R.id.tv_title_content);
            tv_title_content.setText("商场详情");

            if (getNetWifi()) {
                getStoreDetailThread();
                putusermessage();
            } else {
                App.getInstance().showToast("网络不给力，请检查网络设置");
                getActivity().finish();
            }
            sv_storedetail.smoothScrollTo(0, 20);

        }
        return rootView;
    }



    private void putusermessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                putsuermessageapi();
            }
        }).start();
    }

    private void putsuermessageapi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            api.putUserMessage("1", StoreDetailActivity.storeid, tag);
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            Log.v("ceshi", "WSError" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void getStoreDetailThread() {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                getStoreDetail();
            }
        }).start();
    }

    private void getStoreDetail() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            storeDetailInfo = api
                    .getMerchantDetail(StoreDetailActivity.storeid);
            merchant = storeDetailInfo.getMerchant();

            newsbean = storeDetailInfo.getNewsBeean();
            urlbiglists = merchant.getUrlinfos();
            if (merchant != null) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }

        } catch (JSONException e) {
            handler.sendEmptyMessage(3);
            Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(4);
            Log.v("ceshi", "WSError" + e.getMessage());
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // 商场介绍
                    tv_MoreTextView.setText(merchant.getMd());
                    // 商场名字
                    tv_storedetail_title.setText(merchant.getMn());
                    shopnamestr  = merchant.getMn();
                    // 商场地址
                    tv_storedeatil_address.setText(merchant.getMadd());
                    // 营业时间
                    tv_storedetail_time.setText(merchant.getMdbt());
                    if (newsbean == null || "".equals(newsbean)) {
                        // 活动
                        tv_homediscount_hot.setText("暂无活动");
                    } else {
                        // 活动
                        tv_homediscount_hot.setText(Tool.isNullStr(newsbean.getMnti()));
                    }

                    // 电话
                    tv_storedetail_tel.setText(Tool.isNullStr(merchant
                            .getTelinfos().get(0).getTelstr()));
                    // 滚动大图
                  imageurislist = new ArrayList<String>();
                    for (int i = 0; i < urlbiglists.size(); i++) {
                        imageurislist.add(urlbiglists.get(i).getUrl());
                    }
                    ViewGroup.LayoutParams params = banner.getLayoutParams();
                    params.height=screenWidthstore*2/3;
                    params.width =screenWidthstore;
                    banner.setLayoutParams(params);
                    banner.setVisibility(View.VISIBLE);
                    banner.setImages(imageurislist).setImageLoader(new GlideImageLoader()).start();
                    // 入住店铺数量
                    tv_storedetail_num.setText(Tool.isNullStr(storeDetailInfo
                            .getTotal()));
                    // 初始化楼层
                    floorBeans = storeDetailInfo.getFloorBeans();
                    StoreDetailActivity.tatolstr = Tool.isNullStr(storeDetailInfo
                            .getTotal());
                    StoreDetailActivity.floorBeans = floorBeans;
                    if(floorBeans!=null){
                       if(floorBeans.size()>0){
                           try {
                               initview(floorBeans);
                           } catch (Exception e) {
                           }
                       }
                    }
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    App.getInstance().showToast("请求异常");
                    StyledDialog.dismissLoading();
                    getActivity().finish();
                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();
                    getActivity().finish();
                    break;
                case 4:
                    App.getInstance().showToast("请求异常");
                    StyledDialog.dismissLoading();
                    getActivity().finish();
                    break;
            }
        }
    };

    private void initview(final ArrayList<FloorBean> floorBeans) {
        for (int i = 0; i < floorBeans.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("itemtext", floorBeans.get(i).getFloorInfo().getMfltt()
                    + floorBeans.get(i).getFloorInfo().getMfln());
            map.put("itemtype", floorBeans.get(i).getFloorInfo().getMfn());
            list.add(map);
        }
        if(list!=null){
            // 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
            SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
                    R.layout.overview_gradeview_item,
                    new String[]{"itemtext", "itemtype"},
                    new int[]{R.id.gridview_textview, R.id.gridview_textviewtype}
            );
            // 添加并且显示
            gridview_main.setAdapter(adapter);
            // 添加消息处理
            gridview_main.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_storedetail_tel:
                try{
                    tag = "2";
                    putusermessage();
                    intent.setAction(Intent.ACTION_DIAL);
                    // url:统一资源定位符
                    // uri:统一资源标示符（更广）
                    intent.setData(Uri.parse("tel:"
                            + merchant.getTelinfos().get(0).getTelstr()));
                    // 开启系统拨号器
                    startActivity(intent);
                }catch(Exception e){
                    App.getInstance().showToast("找不到拨号软件");
                }

                break;
            case R.id.tv_homediscount_hot:// 商场活动
                if(newsbean!=null){
                    ActivityDetailBean homeActivityInfo = new ActivityDetailBean();
                    homeActivityInfo.setMnti(newsbean.getMnti());
                    homeActivityInfo.setMnid(newsbean.getMnid());
                    homeActivityInfo.setMnp(newsbean.getMnp());
                    intent.setClass(getActivity(), ActivityDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("atycontent", homeActivityInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.re_storegps:// 商场坐标
                lgtd = merchant.getGpsinfos().getGpsx();
                latd = merchant.getGpsinfos().getGpsy();
                showPopwindow();
                break;
            default:
                break;
        }
    }

    private void showPopwindow() {
        View parent = ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(getActivity(), R.layout.camera_pop_menu, null);

        Button btnCamera = (Button) popView.findViewById(R.id.btn_camera_pop_camera);
        Button btnAlbum = (Button) popView.findViewById(R.id.btn_camera_pop_album);
        Button btnCancel = (Button) popView.findViewById(R.id.btn_camera_pop_cancel);
        btnCamera.setText("高德地图");
        btnAlbum.setText("百度地图");
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        final PopupWindow popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);// 设置允许在外点击消失
        View.OnClickListener listener = new View.OnClickListener() {

            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_camera_pop_camera:
                        if (isInstallByread("com.autonavi.minimap")) {
                            openGaoDeMap();
                        } else {
                            App.getInstance().showToast("没有检测到高德地图,请安装高德地图!");
                        }
                        break;
                    case R.id.btn_camera_pop_album:
                        if (isInstallByread("com.baidu.BaiduMap")) {
                            openBaiduMap();
                        } else {
                            App.getInstance().showToast("没有检测到百度地图,请安百度地图!");
                        }

                        break;
                    case R.id.btn_camera_pop_cancel:

                        break;
                }
                popWindow.dismiss();
            }
        };

        btnCamera.setOnClickListener(listener);
        btnAlbum.setOnClickListener(listener);
        btnCancel.setOnClickListener(listener);

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
    private  String shopnamestr;
    private void openBaiduMap() {
        try {
            DigoGps gaodeigps = Tool.gcj02_To_Bd09New(latd, lgtd);
            Intent intent = Intent.getIntent("intent://map/marker?coord_type=gcj02&location="
                    + gaodeigps.getWgLat() + "," + gaodeigps.getWgLon() + "&title=" + shopnamestr + "&content="
                    + shopnamestr + "&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            startActivity(intent);
        } catch (URISyntaxException e) {
            App.getInstance().showToast("您尚未安装百度地图app或app版本过低!");
            e.printStackTrace();
        } catch (ActivityNotFoundException e) {
            App.getInstance().showToast("您尚未安装百度地图app或app版本过低!");
            e.printStackTrace();
        } catch (Exception e) {
            App.getInstance().showToast("您尚未安装百度地图app或app版本过低!");
            e.printStackTrace();
        }

    }

    private void openGaoDeMap() {
        try {
            Intent intent1 = Intent.getIntent("androidamap://viewMap?sourceApplication=" + "迪购" + "&poiname=" + shopnamestr + "&lat=" + latd + "&lon=" + lgtd + "&dev=0");
            startActivity(intent1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ActivityNotFoundException e) {
            App.getInstance().showToast("没有检测到高德地图,请安装高德地图!");
            e.printStackTrace();
        } catch (Exception e) {
            App.getInstance().showToast("您尚未安装高德地图app或app版本过低!");
            e.printStackTrace();
        }
    }
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
