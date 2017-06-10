package com.digoshop.app.module.product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIProductApiImpl;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.home.GlideImageLoader;
import com.digoshop.app.module.login.Loginaty;
import com.digoshop.app.module.looksales.model.ExChangeBean;
import com.digoshop.app.module.looksales.model.ExChangeDetail_Data;
import com.digoshop.app.module.shopdetail.bigimg.aty.ImagePagerActivity;
import com.digoshop.app.module.shopdetailnew.ShopDetailNewSNActivity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.DensityUtil;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;

import org.json.JSONException;

import java.util.ArrayList;

import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * Created by lsqbeyond on 2017/3/16.
 */

public class ProductExchangeAty extends BaseActivity implements View.OnClickListener {
    private Banner banner;
    private ArrayList<String> strlist;
    private String pid, pt;
    private ExChangeDetail_Data exChangeDetail_data;
    private ExChangeBean exChangeBean;
    private TextView tv_prodcutexchange_name, tv_productnum, tv_product_epp, tv_product_epg, tv_homejingpaiday;
    private CountdownView cv_countdownViewproexchange;
    private ProgressBar probar_product;
    private TextView tv_pro_shopname, tv_pron_type, tv_proex_canshu, tv_proex_zixun, tv_shop_pronum, tv_shop_couponnum;
    private RelativeLayout re_go_shopndetail;
    private ImageView iv_proex_shopicon;
    private DisplayImageOptions options;
    private RelativeLayout re_pro_can_btn, re_pro_zixun_btn;
    private View v_proex_canshu, v_proex_zixun;
    private ScrollView sv_pron;
    private LinearLayout lin_shouhoutype, lin_canshutype, lin_pro_dui, lin_pron_shop, lin_pro_shoptel;
    public boolean istuihuan = true;
    private PopupWindow popupWindowquyu, popupWindowquyutel;
    private String epgstr = "";
    private TextView tv_pron_shouhou, tv_prode_priceoldold, tv_pron_shuxingtwo, tv_pronn_name, tv_pron_shuxing, tv_pron_cotnent, tv_pron_guize;
    private RelativeLayout re_shoopn_back;
    private TextView tv_propno_name, tv_prodexchage_oldprice, tv_prode_pricenow, tv_prode_pricecankao, tv_prode_bn, tv_prode_mon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productexchange);
        tv_prode_priceoldold = (TextView) findViewById(R.id.tv_prode_priceoldold);
        tv_propno_name = (TextView) findViewById(R.id.tv_propno_name);
        tv_prodexchage_oldprice = (TextView) findViewById(R.id.tv_prodexchage_oldprice);
        tv_prode_pricenow = (TextView) findViewById(R.id.tv_prode_pricenow);
        tv_prode_pricecankao = (TextView) findViewById(R.id.tv_prode_pricecankao);
        tv_prode_bn = (TextView) findViewById(R.id.tv_prode_bn);
        tv_prode_mon = (TextView) findViewById(R.id.tv_prode_mon);
        tv_pron_shuxingtwo = (TextView) findViewById(R.id.tv_pron_shuxingtwo);

        re_shoopn_back = (RelativeLayout) findViewById(R.id.re_shoopn_back);
        re_shoopn_back.setOnClickListener(this);
        lin_pro_dui = (LinearLayout) findViewById(R.id.lin_pro_dui);
        lin_pron_shop = (LinearLayout) findViewById(R.id.lin_pron_shop);
        lin_pro_shoptel = (LinearLayout) findViewById(R.id.lin_pro_shoptel);
        lin_pro_dui.setOnClickListener(this);
        lin_pron_shop.setOnClickListener(this);
        lin_pro_shoptel.setOnClickListener(this);
        sv_pron = (ScrollView) findViewById(R.id.sv_pron);
        lin_shouhoutype = (LinearLayout) findViewById(R.id.lin_shouhoutype);
        lin_canshutype = (LinearLayout) findViewById(R.id.lin_canshutype);
        v_proex_canshu = findViewById(R.id.v_proex_canshu);
        v_proex_zixun = findViewById(R.id.v_proex_zixun);
        tv_proex_canshu = (TextView) findViewById(R.id.tv_proex_canshu);
        tv_proex_zixun = (TextView) findViewById(R.id.tv_proex_zixun);
        tv_pron_type = (TextView) findViewById(R.id.tv_pron_type);
        tv_pron_shouhou = (TextView) findViewById(R.id.tv_pron_shouhou);

        tv_pronn_name = (TextView) findViewById(R.id.tv_pronn_name);
        tv_pron_shuxing = (TextView) findViewById(R.id.tv_pron_shuxing);
        tv_pron_cotnent = (TextView) findViewById(R.id.tv_pron_cotnent);
        tv_pron_guize = (TextView) findViewById(R.id.tv_pron_guize);
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
        re_pro_can_btn = (RelativeLayout) findViewById(R.id.re_pro_can_btn);
        re_pro_zixun_btn = (RelativeLayout) findViewById(R.id.re_pro_zixun_btn);
        re_pro_can_btn.setOnClickListener(this);
        re_pro_zixun_btn.setOnClickListener(this);
        iv_proex_shopicon = (ImageView) findViewById(R.id.iv_proex_shopicon);
        tv_pro_shopname = (TextView) findViewById(R.id.tv_pro_shopname);
        tv_shop_pronum = (TextView) findViewById(R.id.tv_shop_pronum);
        tv_shop_couponnum = (TextView) findViewById(R.id.tv_shop_couponnum);
        re_go_shopndetail = (RelativeLayout) findViewById(R.id.re_go_shopndetail);
        re_go_shopndetail.setOnClickListener(this);
        tv_prodcutexchange_name = (TextView) findViewById(R.id.tv_prodcutexchange_name);
        tv_product_epp = (TextView) findViewById(R.id.tv_product_epp);
        tv_product_epg = (TextView) findViewById(R.id.tv_product_epg_new);
        tv_homejingpaiday = (TextView) findViewById(R.id.tv_homejingpaiday);
        tv_productnum = (TextView) findViewById(R.id.tv_productnum);
        probar_product = (ProgressBar) findViewById(R.id.probar_product);

        cv_countdownViewproexchange = (CountdownView) findViewById(R.id.cv_countdownViewproexchange);
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        pt = intent.getStringExtra("pt");

        banner = (Banner) findViewById(R.id.banner);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        ViewGroup.LayoutParams params = banner.getLayoutParams();
        // params.height = screenWidth * 2 / 3;
        params.height = screenWidth;
        params.width = screenWidth;
        banner.setLayoutParams(params);
        banner.setVisibility(View.VISIBLE);

        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                if (strlist != null) {
                    if (strlist.size() > 0) {
                        Intent intenta = new Intent(ProductExchangeAty.this, ImagePagerActivity.class);
                        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                        intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, strlist);
                        intenta.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position - 1);
                        startActivity(intenta);
                    }
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != popupWindowquyutel) {
            if (popupWindowquyutel.isShowing()) {
                popupWindowquyutel.dismiss();
                return;
            }
        }
        if (null != popupWindowquyu) {
            if (popupWindowquyu.isShowing()) {
                popupWindowquyu.dismiss();
                return;
            }
        }



        if (!TextUtils.isEmpty(pid)) {
            if (getNetWifi()) {
                StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                getProductExchange();


            } else {
                App.getInstance().showToast("网络不给力，请检查网络设置");
            }


        } else {
            App.getInstance().showToast("产品类型id为空！");
            finish();
        }
    }

    private void getProductExchange() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getProductExchangeApi();
            }
        }).start();

    }

    private void getProductExchangeApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            exChangeDetail_data = api.getProductDetail(pid, pt );
            if (exChangeDetail_data != null) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(4);
            }

        } catch (JSONException e) {
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            if ("A502".equals(e.getMessage())) {
                handler.sendEmptyMessage(502);
                return;
            }
            handler.sendEmptyMessage(4);
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    exChangeBean = exChangeDetail_data.getExChangeBean();
                    //店铺封面
                    if (!TextUtils.isEmpty(exChangeDetail_data.getTargetSava())) {
                        ImageLoader.getInstance().displayImage(exChangeDetail_data.getTargetSava(), iv_proex_shopicon, options);
                    }
                    //店铺名字
                    if (!TextUtils.isEmpty(exChangeDetail_data.getTargetName())) {
                        tv_pro_shopname.setText(exChangeDetail_data.getTargetName());
                    } else {
                        tv_pro_shopname.setText("无");
                    }
                    //店铺优惠券数量
                    if (!TextUtils.isEmpty(exChangeDetail_data.getCouponCount())) {
                        tv_shop_couponnum.setText(exChangeDetail_data.getCouponCount());
                    } else {
                        tv_shop_couponnum.setText("0");
                    }
                    //店铺商品数量
                    if (!TextUtils.isEmpty(exChangeDetail_data.getProductCount())) {
                        tv_shop_pronum.setText(exChangeDetail_data.getProductCount());
                    } else {
                        tv_shop_pronum.setText("0");
                    }
                    if (exChangeBean != null) {
                        strlist = exChangeBean.getUrllist();
                        //商品名字
                        if (!TextUtils.isEmpty(exChangeBean.getPna())) {
                            tv_prodcutexchange_name.setText(exChangeBean.getPna());
                            tv_pronn_name.setText(exChangeBean.getPna());
                        } else {
                            tv_prodcutexchange_name.setText("无");
                            tv_pronn_name.setText("无");
                        }

                        //商品图片
                        if (strlist != null) {
                            banner.setImages(strlist).setImageLoader(new GlideImageLoader()).start();
                        }
                        //兑换价格
                        if (!TextUtils.isEmpty(exChangeBean.getEpp())) {
                            tv_product_epp.setText(exChangeBean.getEpp());
                            tv_prode_pricenow.setText("¥" + exChangeBean.getEpp());
                        }
                        //所需迪币
                        if (!TextUtils.isEmpty(exChangeBean.getEpg())) {
                            epgstr = exChangeBean.getEpg();
                            tv_product_epg.setText(" +" + exChangeBean.getEpg() + "币");
                            tv_prode_pricecankao.setText(exChangeBean.getEpg() + "币");
                        }
                        if (!TextUtils.isEmpty(exChangeBean.getPpr())) {
                            tv_prodexchage_oldprice.setVisibility(View.VISIBLE);
                            tv_prodexchage_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
                            tv_prodexchage_oldprice.setText( "¥" +exChangeBean.getPpr());
                            tv_prode_priceoldold.setText("¥" + exChangeBean.getPpr());
                        } else {
                            tv_prodexchage_oldprice.setVisibility(View.GONE);
                        }
                        //兑换状态
                        //根据eps控制用户是否可以兑换
                        String eps = exChangeBean.getEps();
                        if ("6".equals(eps)) {//
                            tv_pron_type.setText("已经兑换");
                            istuihuan = false;
                        } else if ("5".equals(eps)) {
                            tv_pron_type.setText("已经结束");
                            istuihuan = false;
                        } else if ("7".equals(eps)) {

                            tv_pron_type.setText("当天已兑光");
                            istuihuan = false;
                        } else if ("8".equals(eps)) {
                            //App.getInstance().showToast("未开始");
                            tv_pron_type.setText("还未开始");
                            istuihuan = false;
                        } else if ("4".equals(eps)) {
                            tv_pron_type.setText("我要兑换");
                            istuihuan = true;
                        }

                        //兑换时间cv_countdownViewproexchange

                        String timeday = "";
                        long nte = exChangeBean.getNte();
                        long apsd = exChangeBean.getEpsd();
                        long aped = exChangeBean.getEped();
                        long daojishitime = 0;
                        if (nte < apsd) {
                            timeday = "开始";
                            daojishitime = (apsd - nte);
                        } else if (nte > apsd & nte < aped) {
                            timeday = "结束";
                            daojishitime = (aped - nte);
                        } else if (nte > aped) {
                            timeday = "已结束";
                            daojishitime = 0;
                        }
                        //天数
                        int ii = 0;
                        Log.v("ceshi","daojishitime+"+daojishitime);
                        Log.v("ceshi","乘法+"+( 24 * 60 * 60));
                        if (daojishitime > 24 * 60 * 60) {
                            try {
                                ii = new Long(daojishitime / (24 * 60 * 60)).intValue();
                            } catch (Exception e) {
                                ii = 0;
                            }
                        }
                        if ("已结束".equals(timeday)) {
                            tv_homejingpaiday.setText("已结束");
                        } else {
                            if (ii == 0) {
                                tv_homejingpaiday.setText("距" + timeday + "还剩");
                            } else {
                                tv_homejingpaiday.setText("距" + timeday + "还剩" + ii + "天");
                            }

                            DynamicConfig.Builder dynamicConfigBuilder = new DynamicConfig.Builder();
                            dynamicConfigBuilder.setShowDay(false)
                                    .setShowHour(true)
                                    .setShowMinute(true)
                                    .setShowSecond(true)
                                    .setShowMillisecond(false);
                            cv_countdownViewproexchange.dynamicShow(dynamicConfigBuilder.build());
                            if("5".equals(eps)){
                                tv_homejingpaiday.setText("已结束");
                                cv_countdownViewproexchange.start(0);
                            }else{
                                cv_countdownViewproexchange.start(daojishitime * 1000);
                            }

                        }

                        //剩余份数 1常规兑换 2每天限量
                        if ("2".equals(exChangeBean.getEpm())) {
                            tv_productnum.setText("今日剩余" + exChangeBean.getEplnu());
                            //每天限量数
                            String everydaynum_str = exChangeBean.getEpnu();
                            int everydaynum_int = 0;
                            if (!TextUtils.isEmpty(everydaynum_str)) {
                                try {
                                    everydaynum_int = Integer.parseInt(everydaynum_str);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                probar_product.setMax(everydaynum_int);
                            }
                            //当天剩余数量
                            String everylastnum = exChangeBean.getEplnu();
                            Log.v("ceshi", "everylastnum" + everylastnum);
                            int everylastnumi = 0;
                            if (!TextUtils.isEmpty(everylastnum)) {
                                try {
                                    everylastnumi = Integer.parseInt(everylastnum);
                                    Log.v("ceshi", "11everynumi" + everylastnumi);
                                } catch (NumberFormatException e) {
                                    Log.v("ceshi", "NumberFormatException");
                                    e.printStackTrace();
                                }
                                Log.v("ceshi", "iii" + (everydaynum_int - everylastnumi));
                                Log.v("ceshi", "everynumiiii" + (everydaynum_int));
                                Log.v("ceshi", "everylastnumiiii" + (everylastnumi));
                                Log.v("ceshi", "eplnu" + exChangeBean.getEplnu());
                                Log.v("ceshi", "getEpnu" + exChangeBean.getEpnu());
                                probar_product.setProgress(everydaynum_int - everylastnumi);
                            }
                        } else {
                            int totalnum = 0;
                            if (!TextUtils.isEmpty(exChangeBean.getEnu())) {

                                try {
                                    totalnum = Integer.parseInt(exChangeBean.getEnu());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                probar_product.setMax(totalnum);
                            }
                            if (!TextUtils.isEmpty(exChangeBean.getElnu())) {
                                int totalevnum = 0;
                                try {
                                    totalevnum = Integer.parseInt(exChangeBean.getElnu());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                Log.v("ceshi", "totalnum-totalevnum" + (totalnum - totalevnum));
                                probar_product.setProgress(totalnum - totalevnum);
                            }
                            tv_productnum.setText("剩余数量" + exChangeBean.getElnu());
                        }


                        //商品编号
                        if (!TextUtils.isEmpty(exChangeBean.getPno())) {
                            tv_propno_name.setText(exChangeBean.getPno());
                        }
                        //商品品牌
                        if (!TextUtils.isEmpty(exChangeBean.getBn())) {
                            tv_prode_bn.setText(exChangeBean.getBn());
                        }//商品品类
                        if (!TextUtils.isEmpty(exChangeBean.getMon())) {
                            tv_prode_mon.setText(exChangeBean.getMon());
                        }

                        //商品属性
                        if (!TextUtils.isEmpty(exChangeBean.getPattr())) {
                            tv_pron_shuxing.setText(exChangeBean.getPattr());
                            tv_pron_shuxingtwo.setText("宝贝属性:" + exChangeBean.getPattr());
                        } else {
                            tv_pron_shuxingtwo.setVisibility(View.GONE);
                        }
                        //商品详情
                        if (!TextUtils.isEmpty(exChangeBean.getPd())) {
                            tv_pron_cotnent.setText(exChangeBean.getPd());
                        }
                        //兑换规则
                        if (!TextUtils.isEmpty(exChangeBean.getEpd())) {
                            tv_pron_guize.setText(exChangeBean.getEpd());
                        }
                        //售后
                        if (!TextUtils.isEmpty(exChangeBean.getPasa())) {
                            tv_pron_shouhou.setText(exChangeBean.getPasa());
                        }
                    }
                    sv_pron.smoothScrollTo(0, 0);
                    StyledDialog.dismissLoading();
                    break;
                case 2:
                    StyledDialog.dismissLoading();
                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    finish();
                    StyledDialog.dismissLoading();
                    break;
                case 4:
                    App.getInstance().showToast("请求异常,请稍后再试");
                    StyledDialog.dismissLoading();
                    finish();
                    break;
                case 5:
                    App.getInstance().showToast("解析异常!");
                    StyledDialog.dismissLoading();

                    break;
                case 6:
                    App.getInstance().showToast("兑换失败！");
                    StyledDialog.dismissLoading();

                    break;
                case 7:
                    App.getInstance().showToast("兑换成功!");
                    getProductExchange();
                    istuihuan = false;
                    tv_pron_type.setText("已经兑换");
                    StyledDialog.dismissLoading();
                    break;
                case 9:
                    App.getInstance().showToast("积分不足!");
                    istuihuan = false;
                    tv_pron_type.setText("积分不足!");
                    StyledDialog.dismissLoading();
                    break;
                case 502:
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                    StyledDialog.dismissLoading();
                    finish();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.re_shoopn_back:
                finish();
                break;
            case R.id.re_go_shopndetail:
                if (exChangeDetail_data != null) {
                    if (!TextUtils.isEmpty(exChangeDetail_data.getTargetId())) {
                        Intent intent = new Intent();
                        intent.putExtra("sid", exChangeDetail_data.getTargetId());
                        intent.setClass(ProductExchangeAty.this, ShopDetailNewSNActivity.class);
                        startActivity(intent);
                    } else {
                        App.getInstance().showToast("店铺id为空");
                    }
                }

                break;
            case R.id.re_pro_can_btn:
                v_proex_zixun.setBackgroundResource(R.color.transparent);
                v_proex_canshu.setBackgroundResource(R.color.title_bar_bg);
                tv_proex_canshu.setTextColor(this.getResources().getColor(R.color.title_bar_bg));
                tv_proex_zixun.setTextColor(this.getResources().getColor(R.color.qinahei));
                lin_shouhoutype.setVisibility(View.GONE);
                lin_canshutype.setVisibility(View.VISIBLE);
                break;
            case R.id.re_pro_zixun_btn:
                lin_shouhoutype.setVisibility(View.VISIBLE);
                lin_canshutype.setVisibility(View.GONE);
                v_proex_zixun.setBackgroundResource(R.color.title_bar_bg);
                v_proex_canshu.setBackgroundResource(R.color.transparent);
                tv_proex_canshu.setTextColor(this.getResources().getColor(R.color.qinahei));
                tv_proex_zixun.setTextColor(this.getResources().getColor(R.color.title_bar_bg));

                break;
            case R.id.lin_pro_dui:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype    = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype )) {
                    if (istuihuan) {
                        getPopupWindowQuyu();
                    } else {
                        App.getInstance().showToast(tv_pron_type.getText().toString());
                    }
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("tag", "SaleDetail");
                    intent.setClass(ProductExchangeAty.this, Loginaty.class);
                    startActivity(intent);
                }
                break;
            case R.id.lin_pron_shop:
                if (exChangeDetail_data != null) {
                    if (!TextUtils.isEmpty(exChangeDetail_data.getTargetId())) {
                        Intent intent = new Intent();
                        intent.putExtra("sid", exChangeDetail_data.getTargetId());
                        intent.setClass(ProductExchangeAty.this, ShopDetailNewSNActivity.class);
                        startActivity(intent);
                    } else {
                        App.getInstance().showToast("店铺id为空");
                    }
                }
                break;
            case R.id.lin_pro_shoptel:
                if (exChangeDetail_data != null) {
                    if (!TextUtils.isEmpty(exChangeDetail_data.getTargetSm())) {
                        getPopupWindowPhone();

                    }
                }
                break;
            case R.id.tv_shop_tel:
                //打电话行为记录
                if(exChangeDetail_data!=null){
                    if (!TextUtils.isEmpty(exChangeDetail_data.getTargetId())) {
                        putusermessage();
                    }
                }
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    // url:统一资源定位符
                    // uri:统一资源标示符（更广）
                    intent.setData(Uri.parse("tel:"
                            + exChangeDetail_data.getTargetSm()));
                    // 开启系统拨号器
                    startActivity(intent);
                } catch (Exception ignore) {
                    App.getInstance().showToast("找不到拨号软件");
                    ignore.printStackTrace();
                }

                if (popupWindowquyu != null && popupWindowquyu.isShowing()) {
                    popupWindowquyu.dismiss();
                    popupWindowquyu = null;
                }
                break;
            case R.id.tv_salechange_moreproduct:
                if (getNetWifi()) {
                    approveExchange();
                    if (popupWindowquyu != null && popupWindowquyu.isShowing()) {
                        popupWindowquyu.dismiss();
                        popupWindowquyu = null;
                    }

                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
                break;
            case R.id.tv_salechange_mygift:
                if (popupWindowquyu != null && popupWindowquyu.isShowing()) {
                    popupWindowquyu.dismiss();
                    popupWindowquyu = null;
                }
                break;


        }
    }

    private void getPopupWindowPhone() {
        if (null != popupWindowquyutel) {
            if (popupWindowquyutel.isShowing()) {
                popupWindowquyutel.dismiss();
                return;
            } else {
                initPopuptWindowtel();
            }

        } else {
            initPopuptWindowtel();
        }
    }

    /**
     * 创建区域pop
     */
    protected void initPopuptWindowtel() {
        View parent = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        View popupWindow_view = getLayoutInflater().inflate(R.layout.a_shop_teldialog, null,
                false);
        TextView tv_shop_tel = (TextView) popupWindow_view.findViewById(R.id.tv_shop_tel);
        tv_shop_tel.setText(exChangeDetail_data.getTargetSm());
        tv_shop_tel.setOnClickListener(this);
        tv_shop_tel.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.color.a_shop_tel);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.color.white_text);
                }
                return false;
            }
        });
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        popupWindowquyutel = new PopupWindow(popupWindow_view,
                width, height);
        popupWindowquyutel.setFocusable(true);
        popupWindowquyutel.setOutsideTouchable(true);// 设置允许在外点击消失
        ColorDrawable dw = new ColorDrawable(0x30000000);
        popupWindowquyutel.setBackgroundDrawable(dw);
        //添加pop窗口关闭事件
        popupWindowquyutel.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        // 设置动画效果
        popupWindowquyutel.setAnimationStyle(R.style.AnimBottom);
        // popupWindow.setAnimationStyle(R.style.AnimationFade);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindowquyutel != null && popupWindowquyutel.isShowing()) {
                    popupWindowquyutel.dismiss();
                    popupWindowquyutel = null;
                }
                return false;
            }
        });
        popupWindowquyutel.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    private void approveExchange() {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                approveExchangeapi();
            }
        }).start();
    }

    String intgstr;

    private void approveExchangeapi() {
        DigoIProductApiImpl api = new DigoIProductApiImpl();
        try {
            intgstr = api.Approve_Exchange(pid);
            if (intgstr != null & !TextUtils.isEmpty(intgstr)) {
                handler.sendEmptyMessage(7);

            } else {
                handler.sendEmptyMessage(6);
            }
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(5);
            e.printStackTrace();
        } catch (WSError e) {
            if ("-1513".equals(e.getMessage())) {
                handler.sendEmptyMessage(9);
            } else {
                handler.sendEmptyMessage(4);
            }

            e.printStackTrace();
        }

    }

    /***
     * 获取PopupWindow实例
     */
    private void getPopupWindowQuyu() {
        if (null != popupWindowquyu) {
            if (popupWindowquyu.isShowing()) {
                popupWindowquyu.dismiss();
                return;
            } else {
                initPopuptWindow();
            }

        } else {
            initPopuptWindow();
        }
    }

    /**
     * 创建区域pop
     */
    protected void initPopuptWindow() {
        View parent = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        View popupWindow_view = getLayoutInflater().inflate(R.layout.activity_salechangemakesure, null,
                false);
        TextView tv_salechange_moreproduct = (TextView) popupWindow_view.findViewById(R.id.tv_salechange_moreproduct);
        TextView tv_salechange_mygift = (TextView) popupWindow_view.findViewById(R.id.tv_salechange_mygift);
        TextView tv_pointsale = (TextView) popupWindow_view.findViewById(R.id.tv_pointsale);
        TextView tv_sale_dialog1 = (TextView) popupWindow_view.findViewById(R.id.tv_sale_dialog1);
        SpannableStringBuilder builder = new SpannableStringBuilder(tv_sale_dialog1.getText().toString());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.yellow));
        builder.setSpan(redSpan, 15, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_sale_dialog1.setText(builder);
        tv_pointsale.setText("此次兑换您将消费 " + epgstr + " 币");

        tv_salechange_moreproduct.setOnClickListener(this);
        tv_salechange_mygift.setOnClickListener(this);
        popupWindowquyu = new PopupWindow(popupWindow_view,
                DensityUtil.floatToInt((float) getResources().getDimensionPixelSize(R.dimen.base_dimen_560)), DensityUtil.floatToInt((float) getResources().getDimensionPixelSize(R.dimen.base_dimen_360)), true);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        popupWindowquyu = new PopupWindow(popupWindow_view,
                width, height);
        popupWindowquyu.setFocusable(true);
        popupWindowquyu.setOutsideTouchable(true);// 设置允许在外点击消失
        ColorDrawable dw = new ColorDrawable(0x30000000);
        popupWindowquyu.setBackgroundDrawable(dw);
        //添加pop窗口关闭事件
        popupWindowquyu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        // 设置动画效果
        popupWindowquyu.setAnimationStyle(R.style.AnimBottom);
        // popupWindow.setAnimationStyle(R.style.AnimationFade);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindowquyu != null && popupWindowquyu.isShowing()) {
                    popupWindowquyu.dismiss();
                    popupWindowquyu = null;
                }
                return false;
            }
        });
        popupWindowquyu.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    private void putusermessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                putusermessageapi();
            }
        }).start();
    }
    private void putusermessageapi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {

            api.putUserMessage("2", exChangeDetail_data.getTargetId(), "2");

        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            Log.v("ceshi", "WSError" + e.getMessage());
            e.printStackTrace();
        }
    }
}
