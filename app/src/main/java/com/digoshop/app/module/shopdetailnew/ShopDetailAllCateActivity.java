package com.digoshop.app.module.shopdetailnew;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.customServices.model.CategoryChooseBean;
import com.digoshop.app.module.shopdetailnew.adp.ShopnAllCateAdp;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * Created by lsqbeyond on 2017/3/22.
 */

public class ShopDetailAllCateActivity extends BaseActivity implements View.OnClickListener {
    private String sid;
    private ArrayList<CategoryChooseBean> categoryChooseBeens;
    private TextView tv_title_content;
    private RelativeLayout re_shopallcate_all;
    private ListView lv_shopn_alllcate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopallcate);
        re_shopallcate_all = (RelativeLayout) findViewById(R.id.re_shopallcate_all);
        re_shopallcate_all.setOnClickListener(this);
        lv_shopn_alllcate = (ListView) findViewById(R.id.lv_shopn_alllcate);



        tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("商品分类");
        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");
        if (getNetWifi()) {
            if (!TextUtils.isEmpty(sid)) {
                StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                getShopAllCate();
            } else {
                finish();
                App.getInstance().showToast("店铺id为空");
            }
        } else {
            finish();
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }
    }

    private void getShopAllCate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getShopAllCateapi();
            }
        }).start();
    }

    private void getShopAllCateapi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            categoryChooseBeens = api.getShopAllCategory(sid);
            handler.sendEmptyMessage(1);
        } catch (JSONException e) {
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    lv_shopn_alllcate.setAdapter(new ShopnAllCateAdp(ShopDetailAllCateActivity.this,categoryChooseBeens,shoppAllcateClickListener));
                    StyledDialog.dismissLoading();
                    break;
                case 2:
                    StyledDialog.dismissLoading();
                    break;

            }

        }
    };
    private ShopnAllCateAdp.ShoppAllcateClickListener shoppAllcateClickListener = new ShopnAllCateAdp.ShoppAllcateClickListener() {
        @Override
        public void myOnClick(final int position, View v) {

                finish();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.re_shopallcate_all:
                Intent intent = new Intent();
                intent.putExtra("sid",sid);
                intent.putExtra("moid","");
                intent.setClass(ShopDetailAllCateActivity.this,ShopDetailProductListActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
