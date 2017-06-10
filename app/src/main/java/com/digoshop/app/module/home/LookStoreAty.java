package com.digoshop.app.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.home.adp.StoreListAdp;
import com.digoshop.app.module.storedetail.StoreDetailActivity;
import com.digoshop.app.module.storedetail.model.StoreDetailInfo;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class LookStoreAty extends BaseActivity {
    private String latstr = "";// 首页的维度
    private String lonstr = "";// 首页的经度
    private String CityCodestr = "";
    private ArrayList<StoreDetailInfo> storeDetailInfos;
    private ArrayList<StoreDetailInfo> storeDetailInfosNew = new ArrayList<>();
    private ListView lv_storelist;
    private int page = 1;
    private PullToRefreshLayout refresh_store_view;
    private StoreListAdp storeListAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_store_aty);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("逛商场");
        CityCodestr = LocalSave.getValue(LookStoreAty.this, AppConfig.basefile, "CityCode", "");
        lv_storelist = (ListView) findViewById(R.id.lv_storelist);
        refresh_store_view = ((PullToRefreshLayout) findViewById(R.id.refresh_store_view));
        refresh_store_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (storeDetailInfosNew.size() > 0) {
                    storeDetailInfosNew.clear();
                }
                page = 1;
                getStoreList();
                storeListAdp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (storeDetailInfos != null) {
                    if (storeDetailInfos.size() > 0) {
                        storeDetailInfos.clear();
                    }
                    page = page + 1;
                    getStoreList();
                    storeListAdp.appendData(storeDetailInfos);
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
        lv_storelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (storeDetailInfosNew != null) {
                    if (storeDetailInfosNew.size() > 0) {
                        Intent intent = new Intent();
                        intent.putExtra("storeid", storeDetailInfosNew.get(i).getS_mid());
                        intent.setClass(LookStoreAty.this, StoreDetailActivity.class);
                        startActivity(intent);
                    }

                }

            }
        });
        storeListAdp = new StoreListAdp(this, storeDetailInfosNew);
        lv_storelist.setAdapter(storeListAdp);
        latstr = LocalSave.getValue(this, AppConfig.basefile, "lat", "");
        lonstr = LocalSave.getValue(this, AppConfig.basefile, "lon", "");

        if (getNetWifi()) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            getStoreList();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
            finish();
        }
    }

    private void getStoreList() {
        if (TextUtils.isEmpty(CityCodestr)) {
            App.getInstance().showToast("定位获取失败,默认北京");
            CityCodestr = "010";
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                getStoreListApi();
            }
        }).start();
    }

    private void getStoreListApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            storeDetailInfos = api.getStoreList(CityCodestr, page + "", lonstr, latstr);
            if (storeDetailInfos != null) {
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

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    storeDetailInfosNew.addAll(storeDetailInfos);
                    Log.i("zzrsize", storeDetailInfosNew.size() + "");
                    storeListAdp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    App.getInstance().showToast("没有数据了!");
                    StyledDialog.dismissLoading();

                    break;

                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
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


}
