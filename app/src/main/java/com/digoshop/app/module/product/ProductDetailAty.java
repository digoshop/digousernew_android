package com.digoshop.app.module.product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.home.GlideImageLoader;
import com.digoshop.app.module.home.adp.CouponactiveAdp;
import com.digoshop.app.module.login.Loginaty;
import com.digoshop.app.module.looksales.model.ExChangeDetail_Data;
import com.digoshop.app.module.looksales.model.ProductInfoDto;
import com.digoshop.app.module.product.view.ProCouponsHorizonScrollView;
import com.digoshop.app.module.shopdetail.bigimg.aty.ImagePagerActivity;
import com.digoshop.app.module.shopdetailnew.ShopDetailNewSNActivity;
import com.digoshop.app.module.userCenter.model.Discountcoupons;
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

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * Created by lsqbeyond on 2017/3/17.
 */

public class ProductDetailAty extends BaseActivity implements View.OnClickListener {
    private Banner banner;
    private ArrayList<String> strlist;
    private String pid, pt;
    private ExChangeDetail_Data exChangeDetail_data;
    private ProductInfoDto exChangeBean;
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
    private TextView tv_pron_shouhou, tv_pron_shuxingtwo, tv_product_pppr, tv_product_ppr, tv_pronn_name, tv_pron_shuxing, tv_pron_cotnent, tv_pron_guize;
    private ScrollView sv_pron_de;
    private ProCouponsHorizonScrollView ch_prodetail_coupon;
    private RelativeLayout re_shoopn_back;
    private TextView tv_propno_name,tv_product_ppr_rmb, tv_prode_pricenow, tv_prode_pricecankao, tv_prode_bn, tv_prode_mon;
    private View view_prode_couponline;
    private LinearLayout lin_prodetail_coupon;
    private ArrayList<Discountcoupons> couponInfos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producdetail);
        view_prode_couponline = findViewById(R.id.view_prode_couponline);
        lin_prodetail_coupon = (LinearLayout) findViewById(R.id.lin_prodetail_coupon);
        re_shoopn_back = (RelativeLayout) findViewById(R.id.re_shoopn_back);
        re_shoopn_back.setOnClickListener(this);
        ch_prodetail_coupon = (ProCouponsHorizonScrollView) findViewById(R.id.ch_prodetail_coupon);
        tv_pron_shouhou = (TextView) findViewById(R.id.tv_pron_shouhou);
        tv_propno_name = (TextView) findViewById(R.id.tv_propno_name);
        tv_pron_shuxingtwo = (TextView) findViewById(R.id.tv_pron_shuxingtwo);
        tv_product_ppr_rmb = (TextView) findViewById(R.id.tv_product_ppr_rmb);


        tv_prode_pricenow = (TextView) findViewById(R.id.tv_prode_pricenow);
        tv_prode_pricecankao = (TextView) findViewById(R.id.tv_prode_pricecankao);
        tv_prode_bn = (TextView) findViewById(R.id.tv_prode_bn);
        tv_prode_mon = (TextView) findViewById(R.id.tv_prode_mon);


        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
        lin_pron_shop = (LinearLayout) findViewById(R.id.lin_pron_shop);
        lin_pro_shoptel = (LinearLayout) findViewById(R.id.lin_pro_shoptel);
        lin_pron_shop.setOnClickListener(this);
        lin_pro_shoptel.setOnClickListener(this);
        re_pro_can_btn = (RelativeLayout) findViewById(R.id.re_pro_can_btn);
        re_pro_zixun_btn = (RelativeLayout) findViewById(R.id.re_pro_zixun_btn);
        re_pro_can_btn.setOnClickListener(this);
        re_pro_zixun_btn.setOnClickListener(this);
        lin_shouhoutype = (LinearLayout) findViewById(R.id.lin_shouhoutype);
        lin_canshutype = (LinearLayout) findViewById(R.id.lin_canshutype);

        tv_pronn_name = (TextView) findViewById(R.id.tv_pronn_name);
        tv_pron_shuxing = (TextView) findViewById(R.id.tv_pron_shuxing);
        tv_pron_cotnent = (TextView) findViewById(R.id.tv_pron_cotnent);
        tv_pron_guize = (TextView) findViewById(R.id.tv_pron_guize);
        v_proex_canshu = findViewById(R.id.v_proex_canshu);
        v_proex_zixun = findViewById(R.id.v_proex_zixun);
        tv_proex_canshu = (TextView) findViewById(R.id.tv_proex_canshu);
        tv_proex_zixun = (TextView) findViewById(R.id.tv_proex_zixun);
        sv_pron_de = (ScrollView) findViewById(R.id.sv_pron_de);
//        sv_pron_de.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                sv_pron_de.requestDisallowInterceptTouchEvent(true);
//                return true;
//            }
//        });
        tv_prodcutexchange_name = (TextView) findViewById(R.id.tv_prodcutexchange_name);
        tv_product_pppr = (TextView) findViewById(R.id.tv_product_pppr);
        tv_product_ppr = (TextView) findViewById(R.id.tv_product_ppr);
        iv_proex_shopicon = (ImageView) findViewById(R.id.iv_proex_shopicon);
        tv_pro_shopname = (TextView) findViewById(R.id.tv_pro_shopname);
        tv_shop_pronum = (TextView) findViewById(R.id.tv_shop_pronum);
        tv_shop_couponnum = (TextView) findViewById(R.id.tv_shop_couponnum);
        re_go_shopndetail = (RelativeLayout) findViewById(R.id.re_go_shopndetail);
        re_go_shopndetail.setOnClickListener(this);
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
                        Intent intenta = new Intent(ProductDetailAty.this, ImagePagerActivity.class);
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
        if (popWindow != null) {
            popWindow.dismiss();
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
                    exChangeBean = exChangeDetail_data.getProductInfoDto();
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
                    //店铺优惠券：
                    if (exChangeDetail_data.getDiscountcouponses() != null) {
                        view_prode_couponline.setVisibility(View.VISIBLE);
                        lin_prodetail_coupon.setVisibility(View.VISIBLE);
                        couponInfos = exChangeDetail_data.getDiscountcouponses();
                        ch_prodetail_coupon.setListData(exChangeDetail_data.getDiscountcouponses(), mListener, 0);
                    } else {
                        view_prode_couponline.setVisibility(View.GONE);
                        lin_prodetail_coupon.setVisibility(View.GONE);
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
                        //商品现价
                        if (!TextUtils.isEmpty(exChangeBean.getPpr())) {
                            Log.v("ceshi","&&&&");
                            tv_product_ppr_rmb.setVisibility(View.VISIBLE);
                            //android中为textview动态设置字体为粗体
                            if("到店咨询".equals(exChangeBean.getPpr())){
                                tv_product_ppr.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                            }else{
                               tv_product_ppr.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                            }
                            tv_product_ppr.setText(exChangeBean.getPpr());
                            tv_prode_pricenow.setText("¥" + exChangeBean.getPpr());
                        }else{
                            //设置不为加粗
                            tv_product_ppr.setText("999");
                            tv_product_ppr.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                    getResources().getDimensionPixelSize(R.dimen.base_dimen_30));
                            tv_prode_pricenow.setText(R.string.ddzx);
                        }
                        //商品原价
                        if (!TextUtils.isEmpty(exChangeBean.getPppr())) {
                            tv_product_pppr.setText(" ¥" + exChangeBean.getPppr());
                            tv_product_pppr.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            tv_prode_pricecankao.setText("¥" + exChangeBean.getPppr());
                        }else{
                            tv_prode_pricecankao.setText(R.string.ddzx);

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
                    sv_pron_de.smoothScrollTo(0, 0);
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
                    App.getInstance().showToast("请求异常");
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

                    StyledDialog.dismissLoading();
                    break;
                case 502:
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                    StyledDialog.dismissLoading();
                    finish();
                    break;
                case 15:
                    if(couponInfos!=null){
                        if(couponnum<couponInfos.size()){
                            Discountcoupons discountcoupon =  new Discountcoupons();
                            discountcoupon = couponInfos.get(couponnum);
                            discountcoupon.setStatus("3");
                            if(couponactiveAdp!=null){
                                couponactiveAdp.notifyDataSetChanged();
                            }
                        }
                    }
                    App.getInstance().showToast("领取成功!");
                    StyledDialog.dismissLoading();
                    break;
                case 16:
                    App.getInstance().showToast("领取失败!");
                    StyledDialog.dismissLoading();
                    break;

                case 19:
                    App.getInstance().showToast("积分不足!");
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
                        intent.setClass(ProductDetailAty.this, ShopDetailNewSNActivity.class);
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
            case R.id.lin_pron_shop:
                if (exChangeDetail_data != null) {
                    if (!TextUtils.isEmpty(exChangeDetail_data.getTargetId())) {
                        Intent intent = new Intent();
                        intent.putExtra("sid", exChangeDetail_data.getTargetId());
                        intent.setClass(ProductDetailAty.this, ShopDetailNewSNActivity.class);
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
                        popupWindowquyutel.showAtLocation(view, Gravity.CENTER, 0, 0);

                    } else {
                        App.getInstance().showToast("暂无联系方式");
                    }
                } else {
                    App.getInstance().showToast("暂无联系方式");
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
        }
    }

    private void getPopupWindowPhone() {
        if (null != popupWindowquyutel) {
            popupWindowquyutel.dismiss();
            return;
        } else {
            initPopuptWindowtel();
        }
    }

    /**
     * 创建区域pop
     */
    protected void initPopuptWindowtel() {
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
        popupWindowquyutel = new PopupWindow(popupWindow_view,
                DensityUtil.floatToInt(getResources().getDimensionPixelOffset(R.dimen.base_dimen_560)), DensityUtil.floatToInt(getResources().getDimensionPixelOffset(R.dimen.base_dimen_160)), true);
        popupWindowquyutel.setFocusable(true);
        popupWindowquyutel.setBackgroundDrawable(new BitmapDrawable());
        // 设置动画效果
        popupWindowquyutel.setAnimationStyle(android.R.style.Animation_Dialog);
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
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private ProCouponsHorizonScrollView.TextTypeClickListener mListener = new ProCouponsHorizonScrollView.TextTypeClickListener() {
        @Override
        public void myOnClick(int position, View v) {
//            int offest = (position - 1) * getResources().getDimensionPixelSize(R.dimen.base_dimen_60);
//            aty_textviewsc.smoothScrollTo((position - 1) * getResources().getDimensionPixelSize(R.dimen.base_dimen_100) + offest, 0);
            if (exChangeDetail_data != null) {
                if (exChangeDetail_data.getDiscountcouponses() != null) {
                    showPopwindow();
//                    Intent intent = new Intent();
//                    intent.putExtra("cdid", exChangeDetail_data.getDiscountcouponses().get(position).getCbid());
//                    intent.setClass(ProductDetailAty.this, CouponDetailActivity.class);
//                    startActivity(intent);
                }
            }

        }
    };
      PopupWindow popWindow;
    private int couponnum=0;
    CouponactiveAdp couponactiveAdp;
    private void showPopwindow() {
        View parent = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(ProductDetailAty.this, R.layout.prodetail_pop_coupon, null);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
          popWindow = new PopupWindow(popView, width, height);
        ListView lv_prodetail_coupon = (ListView) popView.findViewById(R.id.lv_prodetail_coupon);
        ImageView iv_shop_coupon_colose = (ImageView) popView.findViewById(R.id.iv_shop_coupon_colose);
        couponactiveAdp = new CouponactiveAdp(ProductDetailAty.this, couponInfos);
        lv_prodetail_coupon.setAdapter(couponactiveAdp);
        iv_shop_coupon_colose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindow != null) {
                    popWindow.dismiss();
                }
            }
        });
        lv_prodetail_coupon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype    = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype )) {
                    if (couponInfos != null) {
                        if("1".equals(couponInfos.get(i).getStatus())){
                            if (!TextUtils.isEmpty(couponInfos.get(i).getCbid())) {
                                //请求接口
                                couponid = couponInfos.get(i).getCbid();
                                couponnum = i;
                                if (getNetWifi()) {
                                    if (getNetWifi()) {
                                        submitCoupon();
                                    } else {
                                        App.getInstance().showToast("网络不给力，请检查网络设置");
                                    }


                                } else {
                                    App.getInstance().showToast("网络不给力，请检查网络设置");
                                }
                            } else {
                                App.getInstance().showToast("领取失败!");
                            }
                        }

                    } else {
                        App.getInstance().showToast("领取失败,请退出后重新进入.");
                    }
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("tag", "CouponDetail");
                    intent.setClass(ProductDetailAty.this, Loginaty.class);
                    startActivity(intent);
                }
            }
        });
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);// 设置允许在外点击消失
        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }
    private String couponid;
    private void submitCoupon() {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                submitCouponApi();
            }
        }).start();
    }
    private void submitCouponApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            boolean islingqu = api.get_coupon(couponid);
            if (islingqu) {
                handler.sendEmptyMessage(15);
            } else {
                handler.sendEmptyMessage(16);
            }
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(16);
            e.printStackTrace();
        } catch (WSError e) {
            if ("-1513".equals(e.getMessage())) {
                handler.sendEmptyMessage(19);
            } else {
                handler.sendEmptyMessage(16);
            }
            e.printStackTrace();
        }
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
