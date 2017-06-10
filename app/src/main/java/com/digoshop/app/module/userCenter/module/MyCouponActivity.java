package com.digoshop.app.module.userCenter.module;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.home.CouponHomeHuaAty;
import com.digoshop.app.module.home.adp.CouponactiveAdp;
import com.digoshop.app.module.home.couponactive.CouponDetailActivity;
import com.digoshop.app.module.userCenter.model.DiscountcouponData;
import com.digoshop.app.module.userCenter.model.Discountcoupons;
import com.digoshop.app.module.userCenter.model.NumBean;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * TODO 我的优惠券
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016-6-11 下午10:31:16
 * @version: V1.0
 */
@SuppressLint("ResourceAsColor")
public class MyCouponActivity extends BaseActivity implements OnClickListener {
    private RadioButton text_homeaty_titlea, text_homeaty_titleb, text_homeaty_titlec;
    private TextView tv_title_right;
    private ArrayList<Discountcoupons> DtcupList;
    private ArrayList<NumBean> numBeens;
    private ArrayList<Discountcoupons> DtcupListNew = new ArrayList<Discountcoupons>();
    private ListView lv_mycoupon;
    private String type = "3";
    private RelativeLayout re_nomycouponlist;
    private LinearLayout ll_list;
    private DiscountcouponData discountcouponData;
    private String totalstr;
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private int page_length = 10;
    private CouponactiveAdp myCouponAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter_mycoupon);
        text_homeaty_titlea = (RadioButton) findViewById(R.id.text_homeaty_titlea);
        text_homeaty_titleb = (RadioButton) findViewById(R.id.text_homeaty_titleb);
        text_homeaty_titlec = (RadioButton) findViewById(R.id.text_homeaty_titlec);
        text_homeaty_titlea.setOnClickListener(this);
        text_homeaty_titlea.setChecked(true);
        text_homeaty_titleb.setOnClickListener(this);
        text_homeaty_titlec.setOnClickListener(this);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("我的优惠券");
        tv_title_right = (TextView) findViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setOnClickListener(this);
        tv_title_right.setText("领券中心");
        lv_mycoupon = (ListView) findViewById(R.id.lv_mycoupon);
        re_nomycouponlist = (RelativeLayout) findViewById(R.id.re_nomycouponlist);
        ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
        if (getNetWifi()) {
            get_Discountcoupons(type);
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }
        myCouponAdp = new CouponactiveAdp(this, DtcupListNew,1);
        lv_mycoupon.setAdapter(myCouponAdp);
        Refresh();
        lv_mycoupon.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //DtcupListNew
                if ("3".equals(DtcupListNew.get(position).getStatus())) {
                    Intent intent = new Intent();
                    intent.putExtra("cdid", DtcupListNew.get(position).getCbid());
                    intent.setClass(MyCouponActivity.this, CouponDetailActivity.class);
                    startActivity(intent);
                    // holder.tv_couponactive_dazhe_dianji1.setText("已领取!");
                } else if ("0".equals(DtcupListNew.get(position).getStatus())) {
                } else if ("5".equals(DtcupListNew.get(position).getStatus())) {
                }

            }
        });

    }

    public void Refresh() {
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (DtcupListNew.size() > 0) {
                    DtcupListNew.clear();
                }
                page = 1;
                page_length = 10;
                get_Discountcoupons(type);
                myCouponAdp.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (DtcupList != null) {
                    if (DtcupList.size() > 0) {
                        DtcupList.clear();
                    }
                    page = page + 1;
                    page_length = 10;
                    get_Discountcoupons(type);
                    myCouponAdp.appendData(DtcupList);
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });
    }

    private void get_Discountcoupons(final String types) {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPay_Discount_coupons(types);
            }
        }).start();
    }

    private void visableBtn(String str) {
        //cs cs状态 3 未使用 4已使用 5 已过期
        if (!TextUtils.isEmpty(str) & str.equals("3")) {//3 未使用
            text_homeaty_titlea.setText("未使用(" + totalstr + ")");
        } else if (!TextUtils.isEmpty(str) & str.equals("5")) {//5 已过期
            text_homeaty_titlec.setText("已过期(" + totalstr + ")");
        } else {//全部
            text_homeaty_titleb.setText("已使用(" + totalstr + ")");
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    DtcupList = discountcouponData.getArrayList();
                    re_nomycouponlist.setVisibility(View.GONE);
                    visableBtn(type);
                    DtcupListNew.addAll(DtcupList);
                    myCouponAdp.notifyDataSetChanged();
                    StyledDialog.dismissLoading();
                    numBeens = discountcouponData.getNumBeens();
                    Log.v("ceshi","^^^^^^^&&&"+numBeens.size());
                    if (numBeens.size() > 0) {
                        for (int i = 0; i < numBeens.size(); i++) {
                            // //3 未使用 4已使用 5 已过期
                            if ("3".equals(numBeens.get(i).getType())) {
                                text_homeaty_titlea.setText("未使用(" + numBeens.get(i).getCount() + ")");
                            }
                            if ("4".equals(numBeens.get(i).getType())) {
                                text_homeaty_titleb.setText("已使用(" + numBeens.get(i).getCount() + ")");
                            }
                            if ("5".equals(numBeens.get(i).getType())) {
                                text_homeaty_titlec.setText("已过期(" + numBeens.get(i).getCount() + ")");
                            }
                        }
                    }

                    break;
                case 2:
                    visableBtn(type);
                    numBeens = discountcouponData.getNumBeens();
                    Log.v("ceshi","^^^^^^^&&&"+numBeens.size());
                    if (numBeens.size() > 0) {
                        for (int i = 0; i < numBeens.size(); i++) {
                            // //3 未使用 4已使用 5 已过期
                            if ("3".equals(numBeens.get(i).getType())) {
                                text_homeaty_titlea.setText("未使用(" + numBeens.get(i).getCount() + ")");
                            }
                            if ("4".equals(numBeens.get(i).getType())) {
                                text_homeaty_titleb.setText("已使用(" + numBeens.get(i).getCount() + ")");
                            }
                            if ("5".equals(numBeens.get(i).getType())) {
                                text_homeaty_titlec.setText("已过期(" + numBeens.get(i).getCount() + ")");
                            }
                        }
                    }
                    App.getInstance().showToast("没有数据了!");
                    ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    StyledDialog.dismissLoading();

                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    re_nomycouponlist.setVisibility(View.GONE);
                    StyledDialog.dismissLoading();

                    break;
                case 4:
                    re_nomycouponlist.setVisibility(View.GONE);
                    StyledDialog.dismissLoading();

                    break;
            }
        }
    };

    private void getPay_Discount_coupons(String dictype) {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        DtcupList = new ArrayList<Discountcoupons>();
        try {
            discountcouponData = api.getPayDiscouncoupons(dictype, page + "", page_length + "");
            Log.i("TEST", "当前状态" + dictype);
            if (discountcouponData != null  & discountcouponData.getArrayList().size() > 0) {
                if (TextUtils.isEmpty(discountcouponData.getTotal())) {
                    totalstr = "0";
                } else {
                    totalstr = discountcouponData.getTotal();
                }

                handler.sendEmptyMessage(1);
            } else {
                totalstr = "0";
                handler.sendEmptyMessage(2);
            }
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            String code = e.getMessage();
            if (code.contains("208") || code.contains("99")) {
                handler.sendEmptyMessage(4);
            }
            Log.v("ceshi", "WSError" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean isNoTitle() {
        return true;
    }

    public void tointent(View v) {
//		Intent intent = new Intent();
//		intent.setClass(MyCouponActivity.this, CouponDetailActivity.class);
//		startActivity(intent);
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //3 未使用 4已使用 5 已过期
            case R.id.text_homeaty_titlea:
                type = "3";
                page = 1;
                text_homeaty_titlea.setChecked(true);
                text_homeaty_titleb.setChecked(false);
                text_homeaty_titlec.setChecked(false);
                if (DtcupListNew.size() > 0) {
                    DtcupListNew.clear();
                }
                get_Discountcoupons(type);
                myCouponAdp.notifyDataSetChanged();
                break;
            case R.id.text_homeaty_titleb:
                type = "4";
                page = 1;
                text_homeaty_titlea.setChecked(false);
                text_homeaty_titleb.setChecked(true);
                text_homeaty_titlec.setChecked(false);
                if (DtcupListNew.size() > 0) {
                    DtcupListNew.clear();
                }
                get_Discountcoupons(type);
                myCouponAdp.notifyDataSetChanged();
                break;
            case R.id.text_homeaty_titlec:
                type = "5";
                text_homeaty_titlea.setChecked(false);
                text_homeaty_titleb.setChecked(false);
                text_homeaty_titlec.setChecked(true);
                page = 1;
                if (DtcupListNew.size() > 0) {
                    DtcupListNew.clear();
                }
                get_Discountcoupons(type);
                myCouponAdp.notifyDataSetChanged();
                break;
            case R.id.tv_title_right:
                Intent intent = new Intent();
                intent.setClass(MyCouponActivity.this, CouponHomeHuaAty.class);
                startActivity(intent);
                break;
//		case R.id.iv_coupon_no:
//			Intent intent1 = new Intent();
//			intent1.setClass(MyCouponActivity.this, CouponActyActivity.class);
//			startActivity(intent1);
//			break;
            default:
                break;
        }
    }

}
