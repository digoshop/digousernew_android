package com.digoshop.app.module.userCenter.module;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.model.ShopInfoBean;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.shopdetailnew.ShopDetailNewSNActivity;
import com.digoshop.app.module.userCenter.adp.ShopInviteListAdp;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;


/**
 * Created by lsqbeyond on 2016/10/27.
 * 商家邀请列表
 */

public class ShopInviteListActivity extends BaseActivity implements  AdapterView.OnItemClickListener {
    private ListView lv_shopinvitelistaty;
    private ArrayList<ShopInfoBean> shopInfoBeans;
    private ArrayList<ShopInfoBean> shopInfoBeansNew = new ArrayList<>();
    private RelativeLayout re_nolist;
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private int page_length = 10;
    private ShopInviteListAdp shopinadp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopinvitelistaty);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("商家邀请");
        lv_shopinvitelistaty = (ListView) findViewById(R.id.lv_shopinvitelistaty);
        lv_shopinvitelistaty.setOnItemClickListener(this);
        re_nolist = (RelativeLayout) findViewById(R.id.re_nolist);

        if (getNetWifi()) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            getShopInviteList();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
            finish();
        }

        shopinadp = new ShopInviteListAdp(ShopInviteListActivity.this, shopInfoBeansNew, mListener);
        lv_shopinvitelistaty.setAdapter(shopinadp);
        ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (getNetWifi()) {
                    if (shopInfoBeansNew.size() > 0) {
                        shopInfoBeansNew.clear();
                    }
                    page = 1;
                    page_length = 10;
                    getShopInviteList();
                    shopinadp.notifyDataSetChanged();
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }

          }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (getNetWifi()) {
                    if (shopInfoBeans != null) {

                        if (shopInfoBeans.size() > 0) {
                            shopInfoBeans.clear();
                        }
                        page = page + 1;
                        page_length = 10;
                        getShopInviteList();
                        shopinadp.appendData(shopInfoBeans);
                    }

                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }



            }
        });
    }

    private void getShopInviteList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getShopInviteListapi();
            }
        }).start();
    }

    private void getShopInviteListapi() {
        {
            DigoIUserApiImpl api = new DigoIUserApiImpl();
            try {
                shopInfoBeans = api.getShopInviteList(page + "", page_length + "");
                if (shopInfoBeans != null & shopInfoBeans.size() > 0) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }

            } catch (JSONException e) {
                Log.v("ceshi", "JSONException");
                handler.sendEmptyMessage(3);
                e.printStackTrace();
            } catch (WSError e) {
                String code = e.getMessage();
                // if (code.equals("-4")||code.equals("0")) {
                handler.sendEmptyMessage(2);
                //  }
                Log.v("ceshi", "WSError");
                Log.i("zzreeee", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    shopInfoBeansNew.addAll(shopInfoBeans);
                    shopinadp.notifyDataSetChanged();
                    re_nolist.setVisibility(View.GONE);
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    //  re_nolist.setVisibility(View.VISIBLE);
                    App.getInstance().showToast("没有数据了!");
                    StyledDialog.dismissLoading();

                    break;
                case 4:

                    Toast.makeText(ShopInviteListActivity.this, "解析异常!", Toast.LENGTH_SHORT).show();
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */


    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent();
//        intent.putExtra("sid",shopInfoBeans.get(position).getSid());
//        intent.putExtra("name",shopInfoBeans.get(position).getName());
//        intent.putExtra("cover",shopInfoBeans.get(position).getCover());
//        intent.setClass(ShopInviteListActivity.this, ShopInviteDetailActivity.class);
//        startActivity(intent);
    }

    private ShopInviteListAdp.ShopInlistClickListener mListener = new ShopInviteListAdp.ShopInlistClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            switch (v.getId()) {

                case R.id.lin_assect_name:
                    Intent intent = new Intent();
                    intent.putExtra("gpsjui", Tool.getGpskmorm(shopInfoBeansNew.get(position).getDistance()));
                    intent.putExtra("sid", shopInfoBeansNew.get(position).getSid());
                    intent.putExtra("name", shopInfoBeansNew.get(position).getName());
                    intent.putExtra("cover", shopInfoBeansNew.get(position).getCover());
                    intent.setClass(ShopInviteListActivity.this, ShopDetailNewSNActivity.class);
                    startActivity(intent);
                    break;
                case R.id.iv_shopinvite_item_img:
                    Intent intenta = new Intent();
                    intenta.putExtra("gpsjui", Tool.getGpskmorm(shopInfoBeansNew.get(position).getDistance()));
                    intenta.putExtra("sid", shopInfoBeansNew.get(position).getSid());
                    intenta.putExtra("name", shopInfoBeansNew.get(position).getName());
                    intenta.putExtra("cover", shopInfoBeansNew.get(position).getCover());
                    intenta.setClass(ShopInviteListActivity.this, ShopDetailNewSNActivity.class);
                    startActivity(intenta);
                    break;
                case R.id.tv_acceptet:
                    Intent intentb = new Intent();
                    intentb.putExtra("gpsjui", Tool.getGpskmorm(shopInfoBeansNew.get(position).getDistance()));

                    intentb.putExtra("sid", shopInfoBeansNew.get(position).getSid());
                    intentb.putExtra("name", shopInfoBeansNew.get(position).getName());
                    intentb.putExtra("cover", shopInfoBeansNew.get(position).getCover());
                    intentb.setClass(ShopInviteListActivity.this, ShopInviteDetailActivity.class);
                    startActivity(intentb);
                    finish();
                    break;


            }

        }
    };
}
