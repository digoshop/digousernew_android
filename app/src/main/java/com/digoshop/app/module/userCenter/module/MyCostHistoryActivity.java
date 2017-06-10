package com.digoshop.app.module.userCenter.module;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.login.Loginaty;
import com.digoshop.app.module.userCenter.adp.MyCostHistoryAdp;
import com.digoshop.app.module.userCenter.adp.MyCostHistoryAdp.MyCostHistoryClickListener;
import com.digoshop.app.module.userCenter.model.ConsumerSettlementrecords;
import com.digoshop.app.module.userCenter.model.CostHistorData;
import com.digoshop.app.module.userCenter.model.NumBean;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * TODO 消费结算记录
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016-6-11 下午10:31:16
 * @version: V1.0
 */
public class MyCostHistoryActivity extends BaseActivity implements
        OnClickListener {

    private CostHistorData commentdata;

    private String tatalnum, sid, eid;
    private String listcomment = "";
    private RadioButton text_homeaty_titlea, text_homeaty_titleb, text_homeaty_titlec;

    private RelativeLayout rl_nolist;
    private String max_id = "";
    private String min_id = "";
    private PullToRefreshLayout refresh_store_view;
    private ArrayList<ConsumerSettlementrecords> CstList;
    private ArrayList<NumBean> numBeens;
    private ArrayList<ConsumerSettlementrecords> CstListNew = new ArrayList<>();
    private ListView lv_costhistory;
    private MyCostHistoryAdp myCostHistoryAdp;
    private TextView tv_title_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter_mycosthistory);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("消费记录");
        tv_title_right = (TextView) findViewById(R.id.tv_title_right);
        tv_title_right.setText("我的积分");
        tv_title_right.setOnClickListener(this);
        tv_title_right.setVisibility(View.VISIBLE);
        text_homeaty_titlea = (RadioButton) findViewById(R.id.text_homeaty_titlea);
        text_homeaty_titleb = (RadioButton) findViewById(R.id.text_homeaty_titleb);
        text_homeaty_titlec = (RadioButton) findViewById(R.id.text_homeaty_titlec);
        text_homeaty_titlea.setOnClickListener(this);
        text_homeaty_titleb.setOnClickListener(this);
        text_homeaty_titlec.setOnClickListener(this);
        text_homeaty_titlea.setChecked(true);
        rl_nolist = (RelativeLayout) findViewById(R.id.rl_nolist);

        lv_costhistory = (ListView) findViewById(R.id.lv_costhistory);
        // list = getDate();
        // lv_costhistory.setAdapter(new
        // MyCostHistoryAdp(MyCostHistoryActivity.this, list,mListener));
        lv_costhistory.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // App.getInstance().showToast((arg2+1)+"个");
            }
        });

        refresh_store_view = ((PullToRefreshLayout) findViewById(R.id.refresh_store_view));
        refresh_store_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (CstListNew.size() > 0) {
                    CstListNew.clear();
                }
                if (CstList != null) {
                    if (CstList.size() > 0) {
                        CstList.clear();
                    }
                }
                max_id = "";
                min_id = "";
                StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                get_pay_list(listcomment);
                myCostHistoryAdp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (CstList != null) {
                    if (CstList.size() > 0) {
                        CstList.clear();
                    }
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    get_pay_list(listcomment);
                    myCostHistoryAdp.appendData(CstList);
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
        myCostHistoryAdp = new MyCostHistoryAdp(
                MyCostHistoryActivity.this, CstListNew, mListener);
        lv_costhistory.setAdapter(myCostHistoryAdp);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getNetWifi()) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            if (CstListNew.size() > 0) {
                CstListNew.clear();
            }
            if (CstList != null) {
                if (CstList.size() > 0) {
                    CstList.clear();
                }
            }
            max_id = "";
            min_id = "";
            get_pay_list(listcomment);
            myCostHistoryAdp.notifyDataSetChanged();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }
    }

    private void get_pay_list(final String listcomment) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPay_List(listcomment);
            }
        }).start();
    }

    private void visableBtn(String str) {
        if (!TextUtils.isEmpty(str) & str.equals("0")) {// 未评价
            text_homeaty_titleb.setText("待评价 (" + tatalnum + ")");
        } else if (!TextUtils.isEmpty(str) & str.equals("1")) {// 已评价
            text_homeaty_titlec.setText("已评价 (" + tatalnum + ")");
        } else {// 全部
            text_homeaty_titlea.setText("全部 (" + tatalnum + ")");
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.v("ceshi", "1111111");
                    refresh_store_view.setVisibility(View.VISIBLE);
                    rl_nolist.setVisibility(View.GONE);
                    CstListNew.addAll(CstList);
                    visableBtn(listcomment);
                    myCostHistoryAdp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();
                    if (numBeens != null) {
                        if (numBeens.size() > 0) {
                            for (int i = 0; i < numBeens.size(); i++) {
                                //is_comment 默认全部-不填 0未评价 1已评价
                                if ("0".equals(numBeens.get(i).getType())) {
                                    text_homeaty_titleb.setText("待评价 (" + numBeens.get(i).getCount() + ")");
                                }
                                if ("1".equals(numBeens.get(i).getType())) {
                                    text_homeaty_titlec.setText("已评价 (" + numBeens.get(i).getCount() + ")");
                                }

                            }
                        }
                    }

                    break;
                case 2:
                    Log.v("ceshi", "222");
                    if (numBeens != null) {
                        if (numBeens.size() > 0) {
                            for (int i = 0; i < numBeens.size(); i++) {
                                //is_comment 默认全部-不填 0未评价 1已评价
                                if ("0".equals(numBeens.get(i).getType())) {
                                    text_homeaty_titleb.setText("待评价 (" + numBeens.get(i).getCount() + ")");
                                }
                                if ("1".equals(numBeens.get(i).getType())) {
                                    text_homeaty_titlec.setText("已评价 (" + numBeens.get(i).getCount() + ")");
                                }

                            }
                        }
                    }

                    App.getInstance().showToast("没有数据了!");
                    if (CstList != null & CstListNew != null) {
                        if (CstList.size() == 0 & CstListNew.size() == 0) {
                            rl_nolist.setVisibility(View.VISIBLE);
                            refresh_store_view.setVisibility(View.GONE);
                        }

                    }
                    StyledDialog.dismissLoading();

                    break;
                case 3:
                    Log.v("ceshi", "3333");
                    if (numBeens != null) {
                        if (numBeens.size() > 0) {
                            for (int i = 0; i < numBeens.size(); i++) {
                                //is_comment 默认全部-不填 0未评价 1已评价
                                if ("0".equals(numBeens.get(i).getType())) {
                                    text_homeaty_titleb.setText("待评价 (" + numBeens.get(i).getCount() + ")");
                                }
                                if ("1".equals(numBeens.get(i).getType())) {
                                    text_homeaty_titlec.setText("已评价 (" + numBeens.get(i).getCount() + ")");
                                }

                            }
                        }
                    }
                    rl_nolist.setVisibility(View.VISIBLE);
                    StyledDialog.dismissLoading();

                    break;

            }
        }
    };

    private void getPay_List(String num) {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        CstList = new ArrayList<ConsumerSettlementrecords>();
        try {
            commentdata = api.get_pay_list(num, max_id, min_id, "10");
            if (commentdata != null) {
                CstList = commentdata.getArrayList();
                tatalnum = commentdata.getTotal();
                max_id = commentdata.getMin_id();
                numBeens = commentdata.getNumbeans();
                if (CstList != null & CstList.size() > 0) {
                    Log.v("TEST", "333" + CstList.size());
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
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_title_right:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(MyCostHistoryActivity.this, MyJifenActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(MyCostHistoryActivity.this, Loginaty.class);
                    startActivity(intent);
                }

                break;
            case R.id.text_homeaty_titlea:
                StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                text_homeaty_titlea.setChecked(true);
                text_homeaty_titleb.setChecked(false);
                text_homeaty_titlec.setChecked(false);
                listcomment = "";
                if (CstListNew.size() > 0) {
                    CstListNew.clear();
                }
                if (CstList != null) {
                    if (CstList.size() > 0) {
                        CstList.clear();
                    }
                }
                max_id = "";
                min_id = "";
                get_pay_list(listcomment);
                myCostHistoryAdp.notifyDataSetChanged();
                break;
            case R.id.text_homeaty_titleb: // is_comment 默认全部-不填 0未评价 1已评价
                StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                text_homeaty_titlea.setChecked(false);
                text_homeaty_titleb.setChecked(true);
                text_homeaty_titlec.setChecked(false);
                listcomment = "0";
                if (CstListNew.size() > 0) {
                    CstListNew.clear();
                }
                if (CstList != null) {
                    if (CstList.size() > 0) {
                        CstList.clear();
                    }
                }
                max_id = "";
                min_id = "";
                get_pay_list(listcomment);
                myCostHistoryAdp.notifyDataSetChanged();
                break;
            case R.id.text_homeaty_titlec:// //is_comment 默认全部-不填 0未评价 1已评价
                StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                text_homeaty_titlea.setChecked(false);
                text_homeaty_titleb.setChecked(false);
                text_homeaty_titlec.setChecked(true);
                listcomment = "1";
                if (CstListNew.size() > 0) {
                    CstListNew.clear();
                }
                if (CstList != null) {
                    if (CstList.size() > 0) {
                        CstList.clear();
                    }
                }
                max_id = "";
                min_id = "";
                get_pay_list(listcomment);
                myCostHistoryAdp.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private MyCostHistoryClickListener mListener = new MyCostHistoryClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            eid = CstListNew.get(position).getEid();
            sid = CstListNew.get(position).getSid();
            String iscomment = CstListNew.get(position).getIs_comment();
            Intent intent = new Intent();
            intent.putExtra("eid", eid);
            intent.putExtra("sid", sid);
            String logo = CstListNew.get(position).getLogo();
            android.util.Log.i("zzrdata", "lg111o" + logo);
            intent.putExtra("logo", logo);
            intent.putExtra("name", CstListNew.get(position).getShop_info());
            if ("0".equals(iscomment)) {
                intent.setClass(MyCostHistoryActivity.this, MyCommentActivity.class);
            } else {
                intent.setClass(MyCostHistoryActivity.this, MyCommentDetailAty.class);
            }
            startActivity(intent);
        }
    };
}
