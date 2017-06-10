package com.digoshop.app.module.userCenter.module;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.looksales.poitsale.SaleActivity;
import com.digoshop.app.module.userCenter.adp.MyjifenAdp;
import com.digoshop.app.module.userCenter.model.JifenBean;
import com.digoshop.app.module.userCenter.model.PointsData;
import com.digoshop.app.module.userCenter.model.PointsEntity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO 我的积分
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016-6-11 下午10:31:16
 * @version: V1.0
 */
public class MyJifenActivity extends BaseActivity implements OnClickListener {
    private List<JifenBean> jifenBeans;
    private MyjifenAdp myjifenAdp;
    private ListView lv_myjifen;
    private TextView tv_title_right;
    private ArrayList<PointsEntity> PeList;
    private ArrayList<PointsEntity> perListNew = new ArrayList<>();
    private TextView tv_mypointmsg;
    private RelativeLayout re_productpoint, re_makepoint;
    private String mypoint;
    private String mypointstr;
    private PullToRefreshLayout refresh_store_view;
    private PointsData pointsData;
    private String max_id = "";
    private String min_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter_myjifen);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("我的积分");
        mypointstr = LocalSave.getValue(MyJifenActivity.this, AppConfig.basefile, "mypointstr", "获取积分失败");
        re_productpoint = (RelativeLayout) findViewById(R.id.re_productpoint);
        re_makepoint = (RelativeLayout) findViewById(R.id.re_makepoint);
        re_productpoint.setOnClickListener(this);
        re_makepoint.setOnClickListener(this);
        tv_title_right = (TextView) findViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setOnClickListener(this);
        tv_title_right.setText("积分说明");
        tv_mypointmsg = (TextView) findViewById(R.id.tv_mypointmsg);
        lv_myjifen = (ListView) findViewById(R.id.lv_myjifen);

        refresh_store_view = ((PullToRefreshLayout) findViewById(R.id.refresh_store_view));
        refresh_store_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (perListNew.size() > 0) {
                    perListNew.clear();
                }
                if (PeList != null) {
                    if (PeList.size() > 0) {
                        PeList.clear();
                    }
                }
                max_id = "";
                min_id = "";
                get_Mypoints();
                myjifenAdp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (PeList != null) {
                    if (PeList.size() > 0) {
                        PeList.clear();
                    }
                    get_Mypoints();
                    myjifenAdp.appendData(PeList);
                }

                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
        myjifenAdp = new MyjifenAdp(MyJifenActivity.this, perListNew);
        lv_myjifen.setAdapter(myjifenAdp);
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        get_Mypoints();
    }

    private void get_Mypoints() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getMypoints();
            }
        }).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (pointsData != null) {
                        mypointstr = pointsData.getTotal();
                        LocalSave.putValue(MyJifenActivity.this, AppConfig.basefile, "mypointstr", pointsData.getTotal().toString());
                        tv_mypointmsg.setText(pointsData.getTotal());
                    }
                    perListNew.addAll(PeList);
                    myjifenAdp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    App.getInstance().showToast("没有数据了!");
                    if (refresh_store_view != null) {
                        refresh_store_view.loadmoreFinish(PullToRefreshLayout.SUCCEED);

                    }
                    StyledDialog.dismissLoading();
                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;

            }
        }
    };

    private void getMypoints() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        PeList = new ArrayList<PointsEntity>();
        try {
            pointsData = api.get_My_points(max_id, min_id, 10, "", "");
            if (pointsData != null) {
                PeList = pointsData.getPointsEntities();
                max_id = pointsData.getMin_id();
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }
        } catch (JSONException e) {
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_title_right:
                intent.setClass(MyJifenActivity.this, MyjifenContentActivity.class);
                startActivity(intent);
                break;
            case R.id.re_productpoint:
                intent.setClass(MyJifenActivity.this, SaleActivity.class);
                startActivity(intent);
                break;
            case R.id.re_makepoint:
                intent.setClass(MyJifenActivity.this, PointsActivity.class);
                intent.putExtra("mypoint", mypointstr);
                startActivity(intent);
                break;
            default:
                break;
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

}
