package com.digoshop.app.module.looksales.xianshi;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.arrountshops.view.PullLayout;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.home.GlideImageLoader;
import com.digoshop.app.module.login.Loginaty;
import com.digoshop.app.module.looksales.model.AuctionBean;
import com.digoshop.app.module.looksales.model.FlashSaleDetailBean;
import com.digoshop.app.module.looksales.xianshi.bean.AddPriceBean;
import com.digoshop.app.module.shopdetailnew.ShopDetailNewSNActivity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.DensityUtil;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;

/**
 * 竞拍商品详情页面
 */
public class PaiDetailsActivity extends TabActivity implements
        OnCheckedChangeListener, OnClickListener {
    private TabHost tabHost;
    private RadioGroup radioderGroup;
    private TextView tv_fsale_name, tv_fsale_startprice, tv_fsale_getprice, tv_fsale_attr, tv_fsalse_name, tv_rest_goods;
    private TextView tv_fsale_hprice, tv_fsale_shoprice, tv_fsale_addnum, tv_fsale_firstprice, tv_fsale_outprice, tv_fsale_firstuser, tv_fsale_outuser;
    private AtomicInteger what = new AtomicInteger(0);
    private String pid;
    private String pt;
    private Banner banner;
    private FlashSaleDetailBean limitedDetails;
    private List<AuctionBean> auctionBeenListdetail;
    private LinearLayout lin_fsale_addnum;
    private TextView tv_flashsale_typename;
    private String qipaiprice;//起拍价
    private String lingxianprice;//领先价格
    private String fuduprice;//加价幅度
    private int chengjiaoprice;//成交价格
    private CountdownView mCvCountdownViewTest4;
    private TextView tv_coutdownstype;
    private PullLayout refresh_store_view;
    private ScrollView pullsv;
    private LinearLayout lin_jingpainame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉窗口标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sale_pai_details);

        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("拍品详情");
        pullsv = (ScrollView) findViewById(R.id.pullsv);
        lin_jingpainame = (LinearLayout) findViewById(R.id.lin_jingpainame);
        lin_jingpainame.setOnClickListener(this);
        mCvCountdownViewTest4 = (CountdownView) findViewById(R.id.cv_countdownViewTest4);
        mCvCountdownViewTest4.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                getPaiDetail();
            }
        });
        init();
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        pt = intent.getStringExtra("pt");
        if (TextUtils.isEmpty(pid)) {
            App.getInstance().showToast("商品id为空!");
            finish();
            return;
        }
        if (TextUtils.isEmpty(pt)) {
            App.getInstance().showToast("商品id为空!");
            finish();
            return;
        }
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        getPaiDetail();
        refresh_store_view = ((PullLayout) findViewById(R.id.refresh_store_view));
        refresh_store_view.setOnRefreshListener(new PullLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullLayout pullToRefreshLayout) {
                getPaiDetail();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        });
    }

    public void OnTitleReturnClick(View view) {
        finish();
    }

    private void getPaiDetailApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            limitedDetails = api.getPeriodDetail(pid, pt);
            if (limitedDetails != null) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(4);
            }

        } catch (WSError w) {
            if (w.getMessage().equals("-1512")) {
                handler.sendEmptyMessage(2);
            }

            w.printStackTrace();
        } catch (Exception e) {
            handler.sendEmptyMessage(4);
            e.printStackTrace();
        }
    }

    public void getPaiDetail() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                getPaiDetailApi();
            }
        }).start();
    }


    private void init() {
        tv_flashsale_typename = (TextView) findViewById(R.id.tv_flashsale_typename);
        lin_fsale_addnum = (LinearLayout) findViewById(R.id.lin_fsale_addnum);
        lin_fsale_addnum.setOnClickListener(this);
        tv_fsale_name = (TextView) findViewById(R.id.tv_fsale_name);
        tv_fsale_attr = (TextView) findViewById(R.id.tv_fsale_attr);
        tv_coutdownstype = (TextView) findViewById(R.id.tv_coutdownstype);

        tv_fsale_startprice = (TextView) findViewById(R.id.tv_fsale_startprice);
        tv_fsale_getprice = (TextView) findViewById(R.id.tv_fsale_getprice);
        tv_fsalse_name = (TextView) findViewById(R.id.tv_fsalse_name);
        tv_rest_goods = (TextView) findViewById(R.id.tv_rest_goods);

        tv_fsale_hprice = (TextView) findViewById(R.id.tv_fsale_hprice);
        tv_fsale_shoprice = (TextView) findViewById(R.id.tv_fsale_shoprice);
        tv_fsale_addnum = (TextView) findViewById(R.id.tv_fsale_addnum);
        tv_fsale_firstprice = (TextView) findViewById(R.id.tv_fsale_firstprice);
        tv_fsale_firstuser = (TextView) findViewById(R.id.tv_fsale_firstuser);
        tv_fsale_outprice = (TextView) findViewById(R.id.tv_fsale_outprice);
        tv_fsale_outuser = (TextView) findViewById(R.id.tv_fsale_outuser);
    }

    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    pullsv.smoothScrollTo(0, 10);
                    //初始化倒计时:
//                    long time4 = (long)150 * 24 * 60 * 60 * 1000;
//                    mCvCountdownViewTest4.start(time4);
//                    如果当前时间小于开始时间则未开始，提示：距离开始：apsd-nte 然后倒计时
//                    如果当前时间大于开始时间且小于结束时间，提示：距离结束： aped-(nte-apsd)
//                    如果当前时间大于开始时间且大于结束时间则  提示：已结束
                    long nte = limitedDetails.getAuctionProductInfo().getNte();
                    Log.v("ceshi", "nte" + nte);
                    long apsd = limitedDetails.getAuctionProductInfo().getApsd();
                    long aped = limitedDetails.getAuctionProductInfo().getAped();
                    long daojishitime = 0;
                    if (nte < apsd) {
                        tv_coutdownstype.setText("距离开始: ");
                        daojishitime = (apsd - nte);
                    } else if (nte > apsd & nte < aped) {
                        tv_coutdownstype.setText("距离结束: ");
                        daojishitime = (aped - nte);
                    } else if (nte > aped) {
                        tv_coutdownstype.setText("已结束 ");
                        daojishitime = 0;
                    }
                    boolean daytrue = false;
                    if (daojishitime > 24 * 60 * 60) {
                        daytrue = true;
                    }
                    boolean hourtrue = false;
                    if (daojishitime > 60 * 60) {
                        hourtrue = true;
                    }
                    DynamicConfig.Builder dynamicConfigBuilder = new DynamicConfig.Builder();
                    dynamicConfigBuilder.setShowDay(daytrue)
                            .setShowHour(hourtrue)
                            .setShowMinute(true)
                            .setShowSecond(true)
                            .setShowMillisecond(false);
                    if (daojishitime != 0) {
                        mCvCountdownViewTest4.dynamicShow(dynamicConfigBuilder.build());
                        mCvCountdownViewTest4.start(daojishitime * 1000);
                    } else {
                        mCvCountdownViewTest4.setVisibility(View.GONE);
                    }

                    //更新
                    //mCvCountdownViewTest4.updateShow();
                    Log.v("lsq", "daojishitime+" + daojishitime);

                    //竞拍状态（4未开始 5竞拍中 6已结束)
                    if ("4".equals(limitedDetails.getAuctionProductInfo().getAps())) {
                        tv_flashsale_typename.setText("未开始");
                    } else if ("5".equals(limitedDetails.getAuctionProductInfo().getAps())) {
                        tv_flashsale_typename.setText("竞拍中");
                    } else if ("6".equals(limitedDetails.getAuctionProductInfo().getAps())) {
                        tv_flashsale_typename.setText("已结束");
                    }
//                    addData(limitedDetails.getAuctionProductInfo().getPna(), limitedDetails.getAuctionProductInfo().getPat().toString(),
//                            limitedDetails.getAuctionProductInfo().getAplg(), limitedDetails.getAuctionProductInfo().getApp(), limitedDetails.getTargetName(), limitedDetails.getApsc());
                    //limitedDetails.getAuctionProductInfo().getAplg()
                    qipaiprice = limitedDetails.getAuctionProductInfo().getAplg();
                    fuduprice = limitedDetails.getAuctionProductInfo().getApg();
                    addPai(limitedDetails.getAuctionProductInfo().getPpr(), limitedDetails.getAuctionProductInfo().getApg(), "158*****594", "22", "130*****457");
                    setBanner();
                    auctionLogList();
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    App.getInstance().showToast("超出规定的竞拍次数");
                    StyledDialog.dismissLoading();

                    finish();
                    break;
                case 3:
                    App.getInstance().showToast("加价成功!");
                    tabHost.setCurrentTabByTag("1");
                    radioderGroup.check(R.id.shopdetails);
                    StyledDialog.dismissLoading();
                    if (addPriceBean != null) {
                        SpannableString styledText = new SpannableString("剩余" + addPriceBean.getApsc() + "次");
                        styledText.setSpan(new TextAppearanceSpan(PaiDetailsActivity.this,
                                        R.style.score_yellow), 2, 2 + addPriceBean.getApsc().length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        styledText.setSpan(new AbsoluteSizeSpan(DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_30)), true), 2, 2 + addPriceBean.getApsc().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tv_rest_goods.setText(styledText, TextView.BufferType.SPANNABLE);

                        if (addPriceBean != null) {
                            if (addPriceBean.getAuctionBeenList() != null) {
                                if (addPriceBean.getAuctionBeenList().size() > 0) {
                                    lingxianprice = addPriceBean.getAuctionBeenList().get(0).getPpr();
                                    tv_fsale_firstprice.setText("领先 " + addPriceBean.getAuctionBeenList().get(0).getPpr());
                                    tv_fsale_firstuser.setText(addPriceBean.getAuctionBeenList().get(0).getNk());
                                }

                                if (addPriceBean.getAuctionBeenList().size() == 0) {
                                    tv_fsale_hprice.setText("最高出价:暂无");
                                } else {
                                    SpannableString styledTextp = new SpannableString("最高出价 " + addPriceBean.getAuctionBeenList().get(0).getPpr() + "币");
                                    styledTextp.setSpan(new TextAppearanceSpan(PaiDetailsActivity.this, R.style.score_yellow), 5, 6 + addPriceBean.getAuctionBeenList().get(0).getPpr().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    styledText.setSpan(new AbsoluteSizeSpan(DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_30)), true), 5, 6 + addPriceBean.getAuctionBeenList().get(0).getPpr().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    tv_fsale_hprice.setText(styledTextp, TextView.BufferType.SPANNABLE);
                                }

                            }
                            if (addPriceBean.getAuctionBeenList().size() > 1) {
                                tv_fsale_outprice.setText("出局 " + addPriceBean.getAuctionBeenList().get(1).getPpr());
                                tv_fsale_outuser.setText(addPriceBean.getAuctionBeenList().get(1).getNk());
                            }
                        }
                    }


                    break;
                case 4:
                    App.getInstance().showToast("解析异常!");
                    StyledDialog.dismissLoading();

                    break;
                case 5:
                    App.getInstance().showToast("竞拍失败!");
                    getPaiDetail();
                    StyledDialog.dismissLoading();

                    break;
                case 6:
                    App.getInstance().showToast("积分不足!");
                    getPaiDetail();
                    StyledDialog.dismissLoading();
                    break;
            }

            super.handleMessage(msg);
        }

    };

    private void auctionLogList() {
        Intent intent1 = new Intent(this, ShoptabdetailsActivity.class);
        intent1.putExtra("text", limitedDetails.getAuctionProductInfo().getPd());
        Intent intent2 = new Intent(this, ShoptabdetailsActivity.class);
        intent2.putExtra("text", limitedDetails.getAuctionProductInfo().getApd());
        Intent intent3 = new Intent(this, ShoptaListActivity.class);
        intent3.putExtra("pid", pid);
        tabHost = this.getTabHost();
        tabHost.addTab(tabHost.newTabSpec("1").setIndicator("1")
                .setContent(intent1));

        tabHost.addTab(tabHost.newTabSpec("2").setIndicator("2")
                .setContent(intent2));
        tabHost.addTab(tabHost.newTabSpec("3").setIndicator("3")
                .setContent(intent3));
        radioderGroup = (RadioGroup) findViewById(R.id.main_radio);
        radioderGroup.setOnCheckedChangeListener(this);
        radioderGroup.check(R.id.shopdetails);// 默认第一个按钮

    }

    public void setBanner() {
        banner = (Banner) findViewById(R.id.banner);
        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {

                //    Toast.makeText(PaiDetailsActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        List<String> imgs = limitedDetails.getAuctionProductInfo().getPa();
        banner.setImages(imgs).setImageLoader(new GlideImageLoader()).start();
    }

    private void addPai(String fsale_shoprice, String addscore,
                        String fsale_firstuser, String fsale_outprice, String fsale_outuser) {
        String hprice = "";
        if (limitedDetails != null) {
            if (limitedDetails.getAuctionBeenList() != null) {
                auctionBeenListdetail = limitedDetails.getAuctionBeenList();
                if (auctionBeenListdetail != null) {
                    if (auctionBeenListdetail.size() > 0) {
                        hprice = auctionBeenListdetail.get(0).getPpr();
                        lingxianprice = auctionBeenListdetail.get(0).getPpr();
                        tv_fsale_firstprice.setText("领先 " + auctionBeenListdetail.get(0).getPpr());
                        tv_fsale_firstuser.setText(auctionBeenListdetail.get(0).getNk());
                    }
                    if (auctionBeenListdetail.size() > 1) {
                        tv_fsale_outprice.setText("出局 " + auctionBeenListdetail.get(1).getPpr());
                        tv_fsale_outuser.setText(auctionBeenListdetail.get(1).getNk());
                    }
                }
            }
        }
        SpannableString styledText = new SpannableString("最高出价 " + hprice + " 币");
        styledText.setSpan(new TextAppearanceSpan(PaiDetailsActivity.this, R.style.score_yellow), 5, 6 + hprice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (TextUtils.isEmpty(hprice)) {
            tv_fsale_hprice.setText("最高出价:暂无");
        } else {
            tv_fsale_hprice.setText(styledText, TextView.BufferType.SPANNABLE);
        }
        tv_fsale_shoprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
        tv_fsale_shoprice.setText("市场价 ￥" + fsale_shoprice);

        styledText = new SpannableString(addscore + "币");
        styledText.setSpan(new TextAppearanceSpan(PaiDetailsActivity.this,
                        R.style.score_big), 0, addscore.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new AbsoluteSizeSpan(DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_24)), true), 0, addscore.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_fsale_addnum.setText(styledText, TextView.BufferType.SPANNABLE);


    }

    private void addData(String shopname, String nature, String fsale_startprice,
                         String fsale_getprice, String stailname, String lostsum) {
        tv_fsale_name.setText(shopname);
        tv_fsale_attr.setText("宝贝属性：" + nature);
        SpannableString styledText = new SpannableString("起拍价 " + fsale_startprice
                + " 币");

        styledText.setSpan(new TextAppearanceSpan(PaiDetailsActivity.this,
                        R.style.score_yellow), 4, 4 + fsale_startprice.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new AbsoluteSizeSpan(DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_30)), true), 4, 4 + fsale_startprice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_fsale_startprice.setText(styledText, TextView.BufferType.SPANNABLE);

        styledText = new SpannableString("领取金额  ￥" + fsale_getprice);

        styledText.setSpan(new TextAppearanceSpan(PaiDetailsActivity.this,
                        R.style.score_big), 6, 7 + fsale_getprice.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new AbsoluteSizeSpan(DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_30)), true), 6, 7 + fsale_getprice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_fsale_getprice.setText(styledText, TextView.BufferType.SPANNABLE);

        styledText = new SpannableString("剩余" + lostsum + "次");
        styledText.setSpan(new TextAppearanceSpan(PaiDetailsActivity.this,
                        R.style.score_yellow), 2, 2 + lostsum.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new AbsoluteSizeSpan(DensityUtil.px2dip(App.getInstance(), (float) getResources().getDimensionPixelSize(R.dimen.base_dimen_30)), true), 2, 2 + lostsum.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_rest_goods.setText(styledText, TextView.BufferType.SPANNABLE);
        tv_fsalse_name.setText(stailname);

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // TODO Auto-generated method stub
        switch (checkedId) {
            case R.id.shopdetails:
                tabHost.setCurrentTabByTag("1");
                break;
            case R.id.shoprule:
                tabHost.setCurrentTabByTag("2");
                break;
            case R.id.shophistory:
                tabHost.setCurrentTabByTag("3");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_jingpainame:
                if (limitedDetails != null) {
                    if (limitedDetails.getTargetId() != null) {
                        Intent intent = new Intent();
                        intent.setClass(PaiDetailsActivity.this, ShopDetailNewSNActivity.class);
                        intent.putExtra("sid", limitedDetails.getTargetId());
                        startActivity(intent);
                    }
                }
                break;
            case R.id.lin_fsale_addnum:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("tag", "PaiDetail");
                    intent.setClass(PaiDetailsActivity.this, Loginaty.class);
                    startActivity(intent);
                    return;
                }
                if (limitedDetails != null) {
                    if (limitedDetails.getApsc() != null) {
                        //limitedDetails.getApsc()
                        int lostnum = Integer.parseInt(limitedDetails.getApsc());
                        if (lostnum <= 0) {
                            App.getInstance().showToast("每个人只能竞拍10次");
                            return;
                        }
                    }
                }

                if (tv_flashsale_typename.getText().equals("竞拍中")) {
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                    addFlashSale();
                } else {
                    App.getInstance().showToast(tv_flashsale_typename.getText().toString());
                }

                break;
            default:
                break;
        }
    }

    private void addFlashSale() {

        if (!TextUtils.isEmpty(lingxianprice)) {
            chengjiaoprice = Integer.parseInt(lingxianprice) + Integer.parseInt(fuduprice);
        } else {
            chengjiaoprice = Integer.parseInt(qipaiprice) + Integer.parseInt(fuduprice);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                addFlashSaleApi();
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCvCountdownViewTest4 != null) {
            mCvCountdownViewTest4.stop();
        }
    }

    private AddPriceBean addPriceBean;

    private void addFlashSaleApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            addPriceBean = api.getAuctionCount(chengjiaoprice + "", pid);
            handler.sendEmptyMessage(3);
        } catch (WSError wsError) {
            String code = wsError.getMessage();
            if (code.equals("-1513")) {
                //您输入的用户名不符合规则
                handler.sendEmptyMessage(6);
            }
            wsError.printStackTrace();
        } catch (Exception e) {

            handler.sendEmptyMessage(4);
            e.printStackTrace();
        }
    }
}
