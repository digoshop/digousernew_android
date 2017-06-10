package com.digoshop.app.module.userCenter.module;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.login.Loginaty;
import com.digoshop.app.module.userCenter.adp.MyGiftAdp;
import com.digoshop.app.module.userCenter.model.MybagEntity;
import com.digoshop.app.module.userCenter.model.MybagEntityData;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * TODO 我的礼包
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016-6-11 下午10:31:16
 * @version: V1.0
 */
public class MyGiftWalletActivity extends BaseActivity implements OnClickListener {
    private ListView lv_mygift;
    private ArrayList<MybagEntity> MbeList;
    private ArrayList<MybagEntity> MbeListNew = new ArrayList<>();
    private MyGiftAdp myGiftAdp;

    private PullToRefreshLayout ptrl;
    private int page = 1;
    private int page_length = 10;
    private String st;
    private Button btn_usercoupon_no_use, btn_usercoupon_past_due, btn_usercoupon_already_use;
    private RelativeLayout re_nomycouponlist;
    private TextView tv_shoplistlogin;

    @Override
    public boolean isNoTitle() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter_mygift);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("我的商品");
        re_nomycouponlist = (RelativeLayout) findViewById(R.id.re_nomycouponlist);
        ImageView iv_title_return = (ImageView) findViewById(R.id.iv_title_return);
        iv_title_return.setVisibility(View.VISIBLE);
        ImageView iv_title_right = (ImageView) findViewById(R.id.iv_title_right);
        iv_title_right.setBackgroundResource(R.drawable.grzx_quan);
        iv_title_right.setBackgroundResource(R.drawable.l_juan);
        btn_usercoupon_no_use = (Button) findViewById(R.id.btn_usercoupon_no_use);
        btn_usercoupon_no_use.setOnClickListener(this);
        btn_usercoupon_past_due = (Button) findViewById(R.id.btn_usercoupon_past_due);
        btn_usercoupon_past_due.setOnClickListener(this);
        btn_usercoupon_already_use = (Button) findViewById(R.id.btn_usercoupon_already_use);
        btn_usercoupon_already_use.setOnClickListener(this);
        iv_title_right.setVisibility(View.VISIBLE);

        iv_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype    = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype )) {
                    intent.setClass(MyGiftWalletActivity.this, MyCouponActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(MyGiftWalletActivity.this, Loginaty.class);
                    startActivity(intent);
                }

            }
        });
        lv_mygift = (ListView) findViewById(R.id.lv_mygift);
        ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
        tv_shoplistlogin = (TextView) findViewById(R.id.tv_shoplistlogin);
        tv_shoplistlogin.setOnClickListener(this);


        myGiftAdp = new MyGiftAdp(MyGiftWalletActivity.this, MbeListNew);
        lv_mygift.setAdapter(myGiftAdp);
        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        //用户登录状态，登录成功未true ,默认未false
        AppConfig.isusertype    = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
        if ("true".equals(AppConfig.isusertype )) {
            tv_shoplistlogin.setVisibility(View.GONE);
            if (getNetWifi()) {
                st = "0";
                page = 1;
                page_length = 10;
                StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                get_Mybag();
            } else {
                App.getInstance().showToast("网络不给力，请检查网络设置");
            }
        } else {
            tv_shoplistlogin.setVisibility(View.VISIBLE);
            if (MbeListNew.size() > 0) {
                MbeListNew.clear();
            }
            if(myGiftAdp!=null){
                myGiftAdp.notifyDataSetChanged();
            }
        }

    }

    private void get_Mybag() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                get_MybagWall();
            }
        }).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    MbeListNew.addAll(MbeList);
                    myGiftAdp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();
                    if (re_nomycouponlist != null) {
                        re_nomycouponlist.setVisibility(View.GONE);
                    }
                    if (MbeList != null) {
                        if (MbeListNew.size() == 0 & MbeList.size() == 0) {
                            re_nomycouponlist.setVisibility(View.VISIBLE);
                        }
                        if (MbeList.size() == 0 & MbeListNew.size() != 0) {
                            App.getInstance().showToast("没有数据了!");
                        }
                    }
                    btn_usercoupon_no_use.setText("未结算(" + notReceivenum + ")");
                    btn_usercoupon_past_due.setText("已结算(" + receivenum + ")");
                    btn_usercoupon_already_use.setText("已过期(" + expirednum + ")");

                    break;
                case 2:
                    if (MbeList != null) {
                        if (MbeListNew != null) {
                            if (MbeListNew.size() > 0) {
                                App.getInstance().showToast("没有数据了!");
                            } else {
                                re_nomycouponlist.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        if (MbeListNew.size() > 0) {
                            App.getInstance().showToast("没有数据了!");
                        } else {
                            re_nomycouponlist.setVisibility(View.VISIBLE);
                        }

                    }
                    StyledDialog.dismissLoading();
                    btn_usercoupon_no_use.setText("未结算(" + notReceivenum + ")");
                    btn_usercoupon_past_due.setText("已结算(" + receivenum + ")");
                    btn_usercoupon_already_use.setText("已过期(" + expirednum + ")");

                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
                case 4:
                    App.getInstance().showToast("请求异常");
                    StyledDialog.dismissLoading();

                    break;
            }
        }
    };
    private MybagEntityData mybagEntityData;
    //        public static final int STATUS_ALL   = -1;  // 全部
//        public static final int STATUS_NO     = 0;    // 未结算
//        public static final int STATUS_YES = 1;       // 已结算
//        public static final int STATUS_EXPIRED     = 2;    // 已过期
    private String receivenum = "0";
    private String expirednum = "0";
    private String notReceivenum = "0";

    private void get_MybagWall() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        MbeList = new ArrayList<MybagEntity>();
        try {
            mybagEntityData = api.getMybagWall(page + "", page_length + "", st);
            if (mybagEntityData != null) {
                if (TextUtils.isEmpty(mybagEntityData.getReceive())) {
                    receivenum = "0";
                } else {
                    receivenum = mybagEntityData.getReceive();
                }
                if (TextUtils.isEmpty(mybagEntityData.getExpired())) {
                    expirednum = "0";
                } else {
                    expirednum = mybagEntityData.getExpired();
                }
                if (TextUtils.isEmpty(mybagEntityData.getNotReceive())) {
                    notReceivenum = "0";
                } else {
                    notReceivenum = mybagEntityData.getNotReceive();
                }
                MbeList = mybagEntityData.getMybagEntities();
                if (MbeList.size() > 0) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }

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

    public void refresh() {
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (MbeListNew.size() > 0) {
                    MbeListNew.clear();
                }
                page = 1;
                page_length = 10;
                get_Mybag();
                myGiftAdp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (MbeList != null) {
                    if (MbeList.size() > 0) {
                        MbeList.clear();
                    }
                    page = page + 1;
                    page_length = 10;
                    get_Mybag();
                    myGiftAdp.appendData(MbeList);
                }

                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });

    }


    @Override
    public void onClick(View view) {
        //用户登录状态，登录成功未true ,默认未false
        AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
        if ("true".equals(AppConfig.isusertype)) {
        } else {
            Intent intent = new Intent();
            intent.setClass(MyGiftWalletActivity.this, Loginaty.class);
            startActivity(intent);
            return;
        }
        switch (view.getId()) {
            //= 0未结算    1已结算   2已过期
            case R.id.btn_usercoupon_no_use:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    btn_usercoupon_no_use.setBackgroundResource(R.drawable.categoryitmebgc);
                    btn_usercoupon_no_use.setTextColor(MyGiftWalletActivity.this.getApplication().getResources().getColor(R.color.stores_text_bottom));
                    btn_usercoupon_past_due.setBackgroundResource(R.drawable.categoryitmebg);
                    btn_usercoupon_past_due.setTextColor(MyGiftWalletActivity.this.getApplication().getResources().getColor(R.color.black_text));
                    btn_usercoupon_already_use.setBackgroundResource(R.drawable.categoryitmebg);
                    btn_usercoupon_already_use.setTextColor(MyGiftWalletActivity.this.getApplication().getResources().getColor(R.color.black_text));
                    st = "0";
                    page = 1;
                    if (MbeListNew.size() > 0) {
                        MbeListNew.clear();
                    }
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    get_Mybag();
                    myGiftAdp.notifyDataSetChanged();
                    tv_shoplistlogin.setVisibility(View.GONE);
                } else {
                    tv_shoplistlogin.setVisibility(View.VISIBLE);
                }


                break;
            case R.id.btn_usercoupon_past_due:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    st = "1";
                    btn_usercoupon_no_use.setBackgroundResource(R.drawable.categoryitmebg);
                    btn_usercoupon_no_use.setTextColor(MyGiftWalletActivity.this.getApplication().getResources().getColor(R.color.black_text));
                    btn_usercoupon_past_due.setBackgroundResource(R.drawable.categoryitmebgc);
                    btn_usercoupon_past_due.setTextColor(MyGiftWalletActivity.this.getApplication().getResources().getColor(R.color.stores_text_bottom));
                    btn_usercoupon_already_use.setBackgroundResource(R.drawable.categoryitmebg);
                    btn_usercoupon_already_use.setTextColor(MyGiftWalletActivity.this.getApplication().getResources().getColor(R.color.black_text));
                    page = 1;
                    if (MbeListNew.size() > 0) {
                        MbeListNew.clear();
                    }
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    get_Mybag();
                    myGiftAdp.notifyDataSetChanged();
                    tv_shoplistlogin.setVisibility(View.GONE);
                } else {
                    tv_shoplistlogin.setVisibility(View.VISIBLE);

                }


                break;
            case R.id.btn_usercoupon_already_use:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype    = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype )) {
                    btn_usercoupon_no_use.setBackgroundResource(R.drawable.categoryitmebg);
                    btn_usercoupon_no_use.setTextColor(MyGiftWalletActivity.this.getApplication().getResources().getColor(R.color.black_text));
                    btn_usercoupon_past_due.setBackgroundResource(R.drawable.categoryitmebg);
                    btn_usercoupon_past_due.setTextColor(MyGiftWalletActivity.this.getApplication().getResources().getColor(R.color.black_text));
                    btn_usercoupon_already_use.setBackgroundResource(R.drawable.categoryitmebgc);
                    btn_usercoupon_already_use
                            .setTextColor(MyGiftWalletActivity.this.getApplication().getResources().getColor(R.color.stores_text_bottom));
                    st = "2";
                    page = 1;
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    if (MbeListNew.size() > 0) {
                        MbeListNew.clear();
                    }
                    get_Mybag();
                    myGiftAdp.notifyDataSetChanged();
                    tv_shoplistlogin.setVisibility(View.GONE);
                } else {
                    tv_shoplistlogin.setVisibility(View.VISIBLE);
                }


                break;
            case R.id.tv_shoplistlogin:
                Intent intent = new Intent();
                intent.setClass(MyGiftWalletActivity.this, Loginaty.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
