package com.digoshop.app.module.userCenter.module;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.userCenter.adp.MyCustomServiceAdp;
import com.digoshop.app.module.userCenter.model.MerchanRreplyEntity;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * TODO<我的定制列表>
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016年9月16日 上午1:21:54
 * @version: V1.0
 */
@SuppressLint("HandlerLeak")
public class MyCustiomGSListActivity extends BaseActivity implements OnClickListener {
    private ListView lv_customservice;

    private String type;
    private RelativeLayout re_custom_service_tab_good, re_custom_service_tab_good3, re_custom_service_tab_service,
            rl_nolist;// 需求已发布
    private ImageView iv_custom_sercvice_tab_good, iv_custom_sercvice_tab_good3, iv_custom_sercvice_tab_service;
    private TextView tv_custom_service_tab_good, tv_custom_service_tab_good3, tv_custom_service_tab_service;
    private TextView view_custom_service_line_good, view_custom_service_line_good3, view_custom_service_line_service;
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private int page_length = 10;
    private ArrayList<MerchanRreplyEntity> MreList;
    private ArrayList<MerchanRreplyEntity> MreListNew = new ArrayList<>();
    private MyCustomServiceAdp csadp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customservice_mycs);
        TextView    tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("我的定制");
        lv_customservice = (ListView) findViewById(R.id.lv_customservice);
        re_custom_service_tab_good = (RelativeLayout) findViewById(R.id.re_custom_service_tab_good);
        re_custom_service_tab_good3 = (RelativeLayout) findViewById(R.id.re_custom_service_tab_good3);
        re_custom_service_tab_service = (RelativeLayout) findViewById(R.id.re_custom_service_tab_service);
        rl_nolist = (RelativeLayout) findViewById(R.id.rl_nolist);

        iv_custom_sercvice_tab_good = (ImageView) findViewById(R.id.iv_custom_sercvice_tab_good);
        iv_custom_sercvice_tab_good3 = (ImageView) findViewById(R.id.iv_custom_sercvice_tab_good3);
        iv_custom_sercvice_tab_service = (ImageView) findViewById(R.id.iv_custom_sercvice_tab_service);

        tv_custom_service_tab_good = (TextView) findViewById(R.id.tv_custom_service_tab_good);
        tv_custom_service_tab_good3 = (TextView) findViewById(R.id.tv_custom_service_tab_good3);
        tv_custom_service_tab_service = (TextView) findViewById(R.id.tv_custom_service_tab_service);

        view_custom_service_line_good = (TextView) findViewById(R.id.view_custom_service_line_good);
        view_custom_service_line_good3 = (TextView) findViewById(R.id.view_custom_service_line_good3);
        view_custom_service_line_service = (TextView) findViewById(R.id.view_custom_service_line_service);

        re_custom_service_tab_good.setOnClickListener(this);
        re_custom_service_tab_good3.setOnClickListener(this);
        re_custom_service_tab_service.setOnClickListener(this);
        inittab();
        ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
        csadp = new MyCustomServiceAdp(this, MreListNew);
        lv_customservice.setAdapter(csadp);
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (getNetWifi()) {
                    if (MreListNew.size() > 0) {
                        MreListNew.clear();
                    }
                    page = 1;
                    page_length = 10;
                    getCustService(type);
                    csadp.notifyDataSetChanged();
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);

                } else {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }

          }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (getNetWifi()) {
                    if (MreList != null) {
                        if (MreList.size() > 0) {
                            MreList.clear();
                        }

                        page = page + 1;
                        page_length = 10;
                        getCustService(type);
                        csadp.appendData(MreList);
                    }
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

                } else {
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }

            }
        });

    }


    private void getCustService(final String typest) {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCustServiceList(typest);
            }
        }).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (MreListNew != null & MreList != null) {
                        if (MreListNew.size() == 0 & MreList.size() == 0) {
                            rl_nolist.setVisibility(View.VISIBLE);
                        } else {
                            rl_nolist.setVisibility(View.GONE);
                        }

                    }
                    lv_customservice.setVisibility(View.VISIBLE);
                    MreListNew.addAll(MreList);
                    csadp.notifyDataSetChanged();
                    rl_nolist.setVisibility(View.GONE);
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    if (MreListNew != null & MreList != null) {
                        if (MreListNew.size() == 0 & MreList.size() == 0) {
                            rl_nolist.setVisibility(View.VISIBLE);
                        } else {
                            App.getInstance().showToast("没有数据了!");
                            rl_nolist.setVisibility(View.GONE);
                        }
                    }
                    if(MreListNew==null&MreList==null){
                        rl_nolist.setVisibility(View.VISIBLE);
                    }
                    if(MreListNew!=null){
                        if(MreListNew.size()==0&MreList==null){
                            rl_nolist.setVisibility(View.VISIBLE);

                        }
                        if(MreListNew.size()>0&&MreList==null){
                            App.getInstance().showToast("已是最后一页");
                        }
                    }
                    ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    StyledDialog.dismissLoading();
                    break;
                case 3:
                    lv_customservice.setVisibility(View.GONE);
                    rl_nolist.setVisibility(View.VISIBLE);
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
                case 4:
                    App.getInstance().showToast("请求异常");
                    lv_customservice.setVisibility(View.GONE);
                    rl_nolist.setVisibility(View.VISIBLE);
                    StyledDialog.dismissLoading();

                    break;
            }
        }
    };

    private void getCustServiceList(String srt) {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        MreList = new ArrayList<MerchanRreplyEntity>();
        try {
            MreList = api.get_custservicelist(srt, page, page_length);
            if (MreList != null  ) {
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

    @Override
    public boolean isNoTitle() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_custom_service_tab_good:
                if (!getNetWifi()) {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                    return;
                }
                //已发布
                iv_custom_sercvice_tab_good.setImageResource(R.drawable.dzfw_fw_xuqiu);
                iv_custom_sercvice_tab_good3.setImageResource(R.drawable.dzfw_sp_goumai);
                iv_custom_sercvice_tab_service.setImageResource(R.drawable.dzfw_sp_huifu);

                tv_custom_service_tab_good.setTextColor(this.getResources().getColor(R.color.custom_tab_orange));
                tv_custom_service_tab_good3.setTextColor(this.getResources().getColor(R.color.custom_tab_blue));
                tv_custom_service_tab_service.setTextColor(this.getResources().getColor(R.color.custom_tab_blue));

                view_custom_service_line_good.setBackgroundResource(R.color.custom_tab_orange);
                view_custom_service_line_good3.setBackgroundResource(R.color.custom_tab_blue);
                view_custom_service_line_service.setBackgroundResource(R.color.custom_tab_blue);
                type = "0";
                page = 1;
                if (MreListNew != null) {
                    MreListNew.clear();
                }
                getCustService(type);
                csadp.notifyDataSetChanged();
                break;
            case R.id.re_custom_service_tab_good3:
                if (!getNetWifi()) {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                    return;
                }
                //已回复
                iv_custom_sercvice_tab_good.setImageResource(R.drawable.dzfw_sp_xuqiu);
                iv_custom_sercvice_tab_good3.setImageResource(R.drawable.dzfw_fw_goumai);
                iv_custom_sercvice_tab_service.setImageResource(R.drawable.dzfw_sp_huifu);

                tv_custom_service_tab_good.setTextColor(this.getResources().getColor(R.color.custom_tab_blue));
                tv_custom_service_tab_good3.setTextColor(this.getResources().getColor(R.color.custom_tab_orange));
                tv_custom_service_tab_service.setTextColor(this.getResources().getColor(R.color.custom_tab_blue));

                view_custom_service_line_good.setBackgroundResource(R.color.custom_tab_blue);
                view_custom_service_line_good3.setBackgroundResource(R.color.custom_tab_orange);
                view_custom_service_line_service.setBackgroundResource(R.color.custom_tab_blue);
                type = "1";
                page = 1;
                if (MreListNew != null) {
                    MreListNew.clear();
                }
                getCustService(type);
                csadp.notifyDataSetChanged();
                break;
            case R.id.re_custom_service_tab_service:
                if (!getNetWifi()) {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                    return;
                }
                //已过期 0获取发布列表 1获取商家回复的列表 -1获取已结束列表
                iv_custom_sercvice_tab_good.setImageResource(R.drawable.dzfw_sp_xuqiu);
                iv_custom_sercvice_tab_good3.setImageResource(R.drawable.dzfw_sp_goumai);
                iv_custom_sercvice_tab_service.setImageResource(R.drawable.dzfw_fw_huifu);

                tv_custom_service_tab_good.setTextColor(this.getResources().getColor(R.color.custom_tab_blue));
                tv_custom_service_tab_good3.setTextColor(this.getResources().getColor(R.color.custom_tab_blue));
                tv_custom_service_tab_service.setTextColor(this.getResources().getColor(R.color.custom_tab_orange));

                view_custom_service_line_good.setBackgroundResource(R.color.custom_tab_blue);
                view_custom_service_line_good3.setBackgroundResource(R.color.custom_tab_blue);
                view_custom_service_line_service.setBackgroundResource(R.color.custom_tab_orange);
                type = "-1";
                page = 1;
                if (MreListNew != null) {
                    MreListNew.clear();
                }
                getCustService(type);
                csadp.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    public void inittab() {
        iv_custom_sercvice_tab_good.setImageResource(R.drawable.dzfw_fw_xuqiu);
        iv_custom_sercvice_tab_good3.setImageResource(R.drawable.dzfw_sp_goumai);
        iv_custom_sercvice_tab_service.setImageResource(R.drawable.dzfw_sp_huifu);

        tv_custom_service_tab_good.setTextColor(this.getResources().getColor(R.color.custom_tab_orange));
        tv_custom_service_tab_good3.setTextColor(this.getResources().getColor(R.color.custom_tab_blue));
        tv_custom_service_tab_service.setTextColor(this.getResources().getColor(R.color.custom_tab_blue));

        view_custom_service_line_good.setBackgroundResource(R.color.custom_tab_orange);
        view_custom_service_line_good3.setBackgroundResource(R.color.custom_tab_blue);
        view_custom_service_line_service.setBackgroundResource(R.color.custom_tab_blue);
        type = "0";
        if (getNetWifi()) {
            getCustService(type);
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
            finish();
        }

    }
}
