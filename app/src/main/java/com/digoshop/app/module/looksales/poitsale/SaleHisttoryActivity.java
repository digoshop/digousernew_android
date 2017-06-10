package com.digoshop.app.module.looksales.poitsale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.digoshop.R;
import com.digoshop.app.api.impl.DigoIProductApiImpl;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.looksales.adp.SaleHistAdp;
import com.digoshop.app.module.looksales.model.SaleHistoryBean;
import com.digoshop.app.utils.http.WSError;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by lsqbeyond on 2016/10/28.
 */

public class SaleHisttoryActivity extends Activity {
    private ListView lv_salehistory;
    private ArrayList<SaleHistoryBean> mlist;
    private RelativeLayout re_nolist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salehistoryaty);
        lv_salehistory   = (ListView) findViewById(R.id.lv_salehistory);
        re_nolist = (RelativeLayout) findViewById(R.id.re_nolist);


    }
    String pidstr;
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent =getIntent();
        pidstr = intent.getStringExtra("pidstr");
        android.util.Log.v("lsq","onResume+111");
        getSaleHistory();
    }
    private void getSaleHistory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getSaleHistToryApi();
            }
        }).start();
    }

    private void getSaleHistToryApi() {
        DigoIProductApiImpl api = new DigoIProductApiImpl();
        try {
            DigoIUserApiImpl uapi = new DigoIUserApiImpl();
            mlist = uapi.getSaleHistoryList(pidstr);
            handler.sendEmptyMessage(1);
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(2);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    android.util.Log.v("lsq","auctionBeenList+111");
                    if (mlist != null) {
                        android.util.Log.v("lsq","auctionBeenList+222");

                        if (mlist.size() > 0) {
                            lv_salehistory.setAdapter(new SaleHistAdp(SaleHisttoryActivity.this,mlist));
                            re_nolist.setVisibility(View.GONE);
                        } else {
                            re_nolist.setVisibility(View.VISIBLE);
                        }
                    } else {
                        re_nolist.setVisibility(View.VISIBLE);
                    }

                    break;
                case 2:
                    re_nolist.setVisibility(View.VISIBLE);
                    break;
            }

        }
    };
}
