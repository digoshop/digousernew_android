package com.digoshop.app.module.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.setting.adp.MyAdapter;
import com.hss01248.dialog.StyledDialog;

import java.util.ArrayList;

/**
 * Created by lsqbeyond on 2016/11/1.
 */

public class SelectShequListActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {
    // 声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    // 定位参数配置
    private AMapLocationClientOption mLocationOption;
    // 纬度
    private double Latitude = 0;
    // 经度
    private double Longitude = 0;
    // 查询实例
    private PoiSearch.Query query;
    // 结果列表
    private ListView result_listview;
    private ArrayList<PoiItem> arrayListxiaoqu;
    private boolean istime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectshequlistaty);
        initLocation();
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("选择小区");
        result_listview = (ListView) findViewById(R.id.lv_result_listview);
        result_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ModifUserInfoAty.VILLAGESTR = String.valueOf(arrayListxiaoqu.get(position));
                finish();
            }
        });
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();


    }

    private void search() {
        query = new PoiSearch.Query("", "120203|120300|120301|120302|120303|120304", "");
        // keyWord表示搜索字符串，第二个参数表示POI搜索类型，默认为：生活服务、餐饮服务、商务住宅
        // 共分为以下20种：汽车服务|汽车销售|
        // 汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
        // 住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
        // 金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
        // cityCode表示POI搜索区域，（这里可以传空字符串，空字符串代表全国在全国范围内进行搜索）
        query.setPageSize(30);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);// 设置查第一页
        PoiSearch poiSearch = new PoiSearch(this, query);
        if (Latitude == 0 | Longitude == 0) {
            handler.sendEmptyMessage(3);
            return;
        }
        Log.v("ceshi", "poiSearch***+");

        // 如果不为空值
        if (Latitude != 0.0 && Longitude != 0.0) {
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(Latitude, Longitude), 1000));// 设置周边搜索的中心点以及区域
            poiSearch.setOnPoiSearchListener(this);// 设置数据返回的监听器
            poiSearch.searchPOIAsyn();// 开始搜索
            handler.sendMessageDelayed(handler.obtainMessage(4), 12000);
            Log.v("ceshi", "poiSearch***22+");
        } else {
            Log.v("ceshi", "poiSearch***333+");
            handler.sendEmptyMessage(3);
        }

    }

    private void initLocation() {
        // 初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        // 设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        // 设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        // 设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mLocationClient.startLocation();

    }

    // 声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    if (mLocationClient != null) {
                        mLocationClient.stopLocation();// 停止定位
                    }
                    // 定位成功回调信息，设置相关消息
                    Latitude = amapLocation.getLatitude();// 获取纬度
                    Longitude = amapLocation.getLongitude();// 获取经度
                    Log.v("ceshi", "Latitude***+" + Latitude);
                    handler.sendEmptyMessage(2);

                } else {
                    // 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                    Log.v("ceshi", "AmapError+" + "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                    handler.sendEmptyMessage(3);
                }
            }
        }
    };

    @Override
    public boolean isNoTitle() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void onPoiSearched(PoiResult result, int code) {
        Log.v("ceshi", "Result" + result.getPois().get(0).getLatLonPoint());
        Log.v("ceshi", "poiSearch***555+");
        arrayListxiaoqu = result.getPois();
        Log.v("ceshi", "Code" + code + "size" + arrayListxiaoqu.size());
        handler.sendEmptyMessage(1);

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    result_listview.setAdapter(new MyAdapter(SelectShequListActivity.this, arrayListxiaoqu));
                    StyledDialog.dismissLoading();
                    break;
                case 2:
                    search();
                    break;
                case 3:
                    App.getInstance().showToast("获取定位失败!");
                    StyledDialog.dismissLoading();

                    break;
                case 4:
                    if(arrayListxiaoqu!=null){
                        if(arrayListxiaoqu.size()==0){
                            App.getInstance().showToast("暂无数据！");
                            StyledDialog.dismissLoading();
                        }
                    }else{
                        App.getInstance().showToast("暂无数据！");
                        StyledDialog.dismissLoading();
                    }
                    break;

            }
        }
    };


    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
        Log.v("ceshi", "poiSearch***666+");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();// 停止定位
    }
}

