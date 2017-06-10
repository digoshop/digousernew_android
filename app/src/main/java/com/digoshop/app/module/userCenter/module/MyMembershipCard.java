package com.digoshop.app.module.userCenter.module;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.userCenter.adp.MyMembreshipAdp;
import com.digoshop.app.module.userCenter.model.MembreEntity;
import com.digoshop.app.module.userCenter.model.MycardData;
import com.digoshop.app.module.userCenter.model.MymenbrescardData;
import com.digoshop.app.utils.Displayer;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class MyMembershipCard extends BaseActivity implements OnClickListener {
    private ImageView iv_sava;
    private TextView tv_name, tv_intg, tv_vip_level, tv_vip_code, tv_create_tim, tv_address, Integral_currency, consumption_Total;
    private DisplayImageOptions options;
    String vipid, creatime, name, address, sava, vipcode, Ytime;
    private ListView lv_customservice;
    MymenbrescardData mymenbres;

    private Button bt_Releasemembershicard_submit;
    private String intg, vip_level;
    private ArrayList<MembreEntity> McbearrayList;
    private ArrayList<MembreEntity> McbearrayListNew = new ArrayList<>();
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private int page_length = 10;
    private MyMembreshipAdp myMembreshipAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_card);
        TextView    tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("会员详情");
        iv_sava = (ImageView) findViewById(R.id.iv_sava);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_vip_code = (TextView) findViewById(R.id.tv_vip_code);
        tv_create_tim = (TextView) findViewById(R.id.tv_create_tim);
        tv_address = (TextView) findViewById(R.id.tv_address);
        lv_customservice = (ListView) findViewById(R.id.lv_customservice);
        consumption_Total = (TextView) findViewById(R.id.consumption_Total);
        Integral_currency = (TextView) findViewById(R.id.Integral_currency);
        tv_intg = (TextView) findViewById(R.id.tv_intg);
        tv_vip_level = (TextView) findViewById(R.id.tv_vip_level);
        bt_Releasemembershicard_submit = (Button) findViewById(R.id.bt_Releasemembershicard_submit);
        bt_Releasemembershicard_submit.setOnClickListener(this);
        ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
        myMembreshipAdp = new MyMembreshipAdp(this, McbearrayListNew);
        lv_customservice.setAdapter(myMembreshipAdp);
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (getNetWifi()) {
                    if (McbearrayListNew.size() > 0) {
                        McbearrayListNew.clear();
                    }
                    page = 1;
                    page_length = 10;
                    getMyMembershipCard();
                    myMembreshipAdp.notifyDataSetChanged();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (McbearrayList != null) {
                    if (McbearrayList.size() > 0) {
                        McbearrayList.clear();
                    }
                    page = page + 1;
                    page_length = 10;
                    getMyMembershipCard();
                    myMembreshipAdp.appendData(McbearrayList);
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
        initView();
    }

    private void initView() {
        //圆形图片
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.defaultyuan)
                .showImageForEmptyUri(R.drawable.defaultyuan)
                .showImageOnFail(R.drawable.defaultyuan)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)   //设置图片的解码类型
                .displayer(new Displayer(0))
                .build();
        Intent intent = getIntent();

        intg = intent.getStringExtra("intg");
        vip_level = intent.getStringExtra("vip_level");
        tv_intg.setText(intg);
        tv_vip_level.setText(vip_level);
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        sava = intent.getStringExtra("sava");
        vipcode = intent.getStringExtra("vipcode");
        creatime = intent.getStringExtra("createtime");
        vipid = intent.getStringExtra("vipid");
        ImageLoader.getInstance().displayImage(sava, iv_sava, options);
        tv_name.setText(name);
        tv_address.setText("办卡地点:" + address);
        Ytime = Tool.getUninxToJavaDayJ(creatime);
        tv_create_tim.setText("办卡时间:" + Ytime);
        tv_vip_code.setText("卡号：" + vipcode);
        if (getNetWifi()) {
            getMyMembershipCard();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }

    }

    private void getMyMembershipCard() {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                get_MembershipCard();
            }
        }).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    consumption_Total.setText(mymenbres.getTotal());
                    Integral_currency.setText(mymenbres.getIntg());
                    McbearrayListNew.addAll(McbearrayList);
                    myMembreshipAdp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    App.getInstance().showToast("暂无记录");
                    StyledDialog.dismissLoading();

                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
                case 4:
                    App.getInstance().showToast("请求异常");
                    StyledDialog.dismissLoading();

                    break;
                case 5:
                    App.getInstance().showToast("解除成功");
                    StyledDialog.dismissLoading();

                    finish();
                    break;
                case 6:
                    App.getInstance().showToast("解除失败");
                    StyledDialog.dismissLoading();

                    break;
            }
        }
    };

    private void get_MembershipCard() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        mymenbres = new MymenbrescardData();
        McbearrayList = new ArrayList<MembreEntity>();
        try {
            mymenbres = api.vip_detail(vipid, page + "", page_length + "");
            McbearrayList = mymenbres.getArrayList();
            if (McbearrayList != null & McbearrayList.size() > 0) {
                Log.v("TEST", "---MbeList---" + McbearrayList.size());
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Releasemembershicard_submit:
                if (getNetWifi()) {
                    Releasemembershicard();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }

                break;
            default:
                break;
        }
    }

    private void Releasemembershicard() {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                showReleasemembershicard();
            }
        }).start();
    }

    private void showReleasemembershicard() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        MycardData mycarddata = new MycardData();
        try {
            mycarddata = api.un_bind_vip(vipid);
            if (mycarddata != null) {
                if ("true".equals(mycarddata.getData()) ) {
                    handler.sendEmptyMessage(5);
                } else {
                    handler.sendEmptyMessage(6);
                }
            }
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(6);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(6);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
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

}
