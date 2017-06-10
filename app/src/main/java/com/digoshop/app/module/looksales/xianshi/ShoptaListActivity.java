package com.digoshop.app.module.looksales.xianshi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.looksales.adp.AuctionAdapter;
import com.digoshop.app.module.looksales.model.AuctionBean;
import com.digoshop.app.module.looksales.view.MyListView;
import com.digoshop.app.utils.http.WSError;

import java.util.List;

/**
 * 竞拍详情的竞拍列表通过自己接口获取
 */
public class ShoptaListActivity extends Activity {

    private TextView Text;
    private String text;
    private List<AuctionBean> auctionBeenList;
    private AuctionAdapter auctionAdapter;
    private MyListView listView;
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private int page_length = 10;
    private String pid;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.v("lsq","auctionBeenList+111");
                    if (auctionBeenList != null) {
                        Log.v("lsq","auctionBeenList+222");

                        if (auctionBeenList.size() > 0) {
                            Log.v("lsq","auctionBeenList+"+auctionBeenList.size());
                            auctionAdapter = new AuctionAdapter(ShoptaListActivity.this, auctionBeenList);
                            listView.setAdapter(auctionAdapter);
                        } else {
                            Text.setVisibility(View.VISIBLE);
                            Text.setText("暂无竞拍记录!");
                        }
                    } else {
                        Text.setVisibility(View.VISIBLE);
                        Text.setText("暂无竞拍记录!");
                    }

                    break;
                case 2:
                    Text.setVisibility(View.VISIBLE);
                    Text.setText("暂无竞拍记录!");
                    break;
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent =getIntent();
        pid = intent.getStringExtra("pid");
        Log.v("lsq","onResume+111");
        getPaiHistoryList();
    }

    private void getPaiHistoryList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPaiHistoryListApi();
            }
        }).start();

    }

    private void getPaiHistoryListApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            auctionBeenList = api.getAuction(pid, "1", "10");
            handler.sendEmptyMessage(1);
        } catch (WSError wsError) {
            handler.sendEmptyMessage(2);
            wsError.printStackTrace();
        } catch (Exception e) {
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_shoptab_details);
        Text = (TextView) findViewById(R.id.tv_shopsinodata);
        listView = (MyListView) findViewById(R.id.arountshops_lv_shops);
        //                    auctionAdapter = new AuctionAdapter(ShoptaListActivity.this,auctionBeenList);


    }

}
