package com.digoshop.app.module.home.couponactive;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.login.Loginaty;
import com.digoshop.app.module.shopdetail.view.FlowLayout;
import com.digoshop.app.module.shopdetailnew.ShopDetailNewSNActivity;
import com.digoshop.app.module.storedetail.StoreDetailActivity;
import com.digoshop.app.module.userCenter.model.CouponDetailData;
import com.digoshop.app.module.userCenter.model.UserStatBean;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.DensityUtil;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class CouponDetailActivity extends BaseActivity implements OnClickListener {
    private Button btn_submit_coupon;
    private String cdid;
    private CouponDetailData Cdetail;
    private DisplayImageOptions options;
    private ImageView img_tcover;
    private RelativeLayout re_shoopn_back;
    private TextView tv_ticket_coupondetai, tv_tname, tv_cbd, tv_bi_coupondetail, tv_Batchcbd, tv_cbvsd,
            tv_coupondetail_sysl, tv_Cbea, tv_condition_coupondetail;
    private boolean isAlive = true;
    private String mypointstr = "获取积分失败";
    public static boolean isling = false;
    private FlowLayout mFlowLayoutoper;
    private RelativeLayout re_coupondetail_shop;
    private String[] operarrays;
    private TextView tv_coupontype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupondetail);
        re_shoopn_back = (RelativeLayout) findViewById(R.id.re_shoopn_back);
        re_shoopn_back.setOnClickListener(this);
        tv_coupontype = (TextView) findViewById(R.id.tv_coupontype);
        mFlowLayoutoper = (FlowLayout) findViewById(R.id.flowlayout_coupon_oper);
        re_coupondetail_shop = (RelativeLayout) findViewById(R.id.re_coupondetail_shop);
        re_coupondetail_shop.setOnClickListener(this);
        btn_submit_coupon = (Button) findViewById(R.id.btn_submit_coupon);
        btn_submit_coupon.setOnClickListener(this);
        tv_tname = (TextView) findViewById(R.id.tv_tname);
        img_tcover = (ImageView) findViewById(R.id.img_tcover);
        tv_ticket_coupondetai = (TextView) findViewById(R.id.tv_ticket_coupondetail);
        tv_bi_coupondetail = (TextView) findViewById(R.id.tv_bi_coupondetail);
        tv_Batchcbd = (TextView) findViewById(R.id.tv_Batchcbd);
        tv_coupondetail_sysl = (TextView) findViewById(R.id.tv_coupondetail_sysl);
        tv_cbd = (TextView) findViewById(R.id.tv_cbd);
        tv_Cbea = (TextView) findViewById(R.id.tv_Cbea);
        tv_cbvsd = (TextView) findViewById(R.id.tv_cbvsd);
        tv_condition_coupondetail = (TextView) findViewById(R.id.tv_condition_coupondetail);
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.kcx_001)
                .showImageForEmptyUri(R.drawable.kcx_001).showImageOnFail(R.drawable.kcx_001).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
        Intent intent = getIntent();
        cdid = intent.getStringExtra("cdid");
        isling = false;

    }

    private void get_CouponDetail(final String id) {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPay_Coupon(id);
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if(isling){
//            isAlive = false;
//            btn_submit_coupon.setText("优惠券已领取！");
//        }
        //用户登录状态，登录成功未true ,默认未false
        AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
        if ("true".equals(AppConfig.isusertype)) {
            mypointstr = LocalSave.getValue(CouponDetailActivity.this, AppConfig.basefile, "mypointstr", "获取积分失败");
            get_stat();
        }
        get_CouponDetail(cdid);
    }

    // 获取用户基本消息
    private void get_stat() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                get_statapi();
            }
        }).start();
    }

    private UserStatBean userStatBean;

    private void get_statapi() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            userStatBean = api.get_stat();
            if (userStatBean != null) {
                mypointstr = userStatBean.getTotal_intg();

                handler.sendEmptyMessage(7);
            }

        } catch (JSONException e) {
            Log.v("ceshi", "JSONException888");
            e.printStackTrace();
        } catch (WSError e) {
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }

    }

    private void initChildViewsoper(String arrays[]) {

        mFlowLayoutoper.removeAllViews();
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        for (int i = 0; i < arrays.length; i++) {
            TextView view = new TextView(App.getInstance());
            view.setText(arrays[i]);

            view.setTextColor(getResources().getColor(R.color.textname));
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.base_dimen_30));
            view.setTag(i);
            view.setOnClickListener(this);
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_bg));
            mFlowLayoutoper.addView(view, lp);

        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    if (!TextUtils.isEmpty(Cdetail.getCbasc())) {
                        //叫cbsac 0是本店  1是全部连锁
                        if ("0".equals(Cdetail.getCbasc())) {
                            tv_coupontype.setText("仅限本店");
                        } else if ("1".equals(Cdetail.getCbasc())) {
                            tv_coupontype.setText("全部连锁");
                        }
                    }
                    operarrays = null;
                    if ("折扣券".equals(Cdetail.getCtn())) {
                        String strzhe = Cdetail.getCbr() * 10 + "";
                        if (strzhe != null) {
                            if (strzhe.length() >= 3) {
                                strzhe = strzhe.substring(0, 3);
                            }
                        }
                        tv_bi_coupondetail.setText(strzhe);
                    } else {
                        tv_bi_coupondetail.setText("¥" + Cdetail.getCbca());
                    }
                    tv_tname.setText(Cdetail.getCbn());
                    if (Cdetail.getOperstrs() != null) {
                        operarrays = new String[Cdetail.getOperstrs().size()];
                        Log.v("test", "size+" + Cdetail.getOperstrs().size());

                        for (int i = 0; i < Cdetail.getOperstrs().size(); i++) {
                            operarrays[i] = Cdetail.getOperstrs().get(i) + "";
                        }
                        Log.v("test", "operarrays+" + operarrays.length);
                        initChildViewsoper(operarrays);
                    }
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    int screenWidth = dm.widthPixels;
                    int screenHeight = dm.heightPixels;
                    ViewGroup.LayoutParams params = img_tcover.getLayoutParams();
                    params.height = screenWidth * 2 / 3;
                    params.width = screenWidth;
                    img_tcover.setLayoutParams(params);
                    img_tcover.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(Cdetail.getTcover(), img_tcover, options);// 店铺封面图
                    tv_ticket_coupondetai.setText(Cdetail.getCtn());
                    if ("代金券".equals(Cdetail.getCtn())) {
                        tv_condition_coupondetail.setText("");

                    } else {
                        tv_condition_coupondetail.setText("满" + Cdetail.getCbcf() + "元可用");

                    }


                    // 开始时间
                    long n = Long.parseLong(Cdetail.getCbvsd());
                    String Ntimekai = Tool.getUninxToJavaDay(n);
                    long nj = Long.parseLong(Cdetail.getCbved());
                    String Ntimej = Tool.getUninxToJavaDay(nj);
                    tv_cbvsd.setText(Ntimekai + "-" + Ntimej);// 开始时间

                    tv_Batchcbd.setText(Cdetail.getTname());
                    tv_coupondetail_sysl.setText(Cdetail.getCbsnu() + "张");
                    tv_Cbea.setText(Cdetail.getCbea() + "币");
                    if (!"获取积分失败".equals(mypointstr)) {
                        Log.v("ceshi", "mypointstr+" + mypointstr);
                        if (mypointstr != null & Cdetail.getCbea() != null) {
                            int imypoint = Integer.parseInt(mypointstr);
                            int iCbea = Integer.parseInt(Cdetail.getCbea());
                            if (imypoint < iCbea) {
                                isAlive = false;
                                btn_submit_coupon.setText("用户积分不足！");
                            }

                        }
                    }
                    tv_cbd.setText(Cdetail.getCbd());

                    StyledDialog.dismissLoading();

                    if ("3".equals(Cdetail.getStatus())) {
                        isAlive = false;
                        btn_submit_coupon.setText("优惠券已领取！");
                    } else if ("0".equals(Cdetail.getStatus())) {
                        isAlive = false;
                        btn_submit_coupon.setText("优惠券已抢光！");
                    } else if ("5".equals(Cdetail.getStatus())) {
                        isAlive = false;
                        btn_submit_coupon.setText("优惠券已过期！");
                    }
                    //// 用户状态 0已抢光 1 未领取 3已领取 5已过期
                    break;
                case 2:
                    App.getInstance().showToast("获取详情失败！");
                    StyledDialog.dismissLoading();

                    finish();
                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    finish();
                    break;
                case 4:
                    App.getInstance().showToast("请求异常");
                    StyledDialog.dismissLoading();

                    finish();
                    break;
                case 5:
                    App.getInstance().showToast("领取成功!");
                    StyledDialog.dismissLoading();
                    get_CouponDetail(cdid);
                    CouponDetailActivity.isling = true;
                    break;
                case 6:
                    App.getInstance().showToast("领取失败!");
                    StyledDialog.dismissLoading();
                    break;
                case 7:
                    mypointstr = userStatBean.getTotal_intg();
                    LocalSave.putValue(CouponDetailActivity.this, AppConfig.basefile, "mypointstr", mypointstr);
                    break;
                case 9:
                    App.getInstance().showToast("积分不足!");
                    StyledDialog.dismissLoading();
                    break;

            }
        }
    };

    private void getPay_Coupon(String id) {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            Cdetail = api.coupon_detail(id);
            if (Cdetail != null) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(4);
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
            boolean islingqu = api.get_coupon(Cdetail.getCbid());
            if (islingqu) {
                handler.sendEmptyMessage(5);
            } else {
                handler.sendEmptyMessage(6);
            }
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(6);
            e.printStackTrace();
        } catch (WSError e) {
            if ("-1513".equals(e.getMessage())) {
                handler.sendEmptyMessage(9);
            } else {
                handler.sendEmptyMessage(6);
            }
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_salechange_moreproduct:
                if (getNetWifi()) {
                    if (getNetWifi()) {
                        submitCoupon();
                    } else {
                        App.getInstance().showToast("网络不给力，请检查网络设置");
                    }
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
            case R.id.re_shoopn_back:
                finish();
                break;
            case R.id.btn_submit_coupon:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    if (isAlive) {
                        if (Cdetail != null) {
                            if (!TextUtils.isEmpty(Cdetail.getCbid())) {
                                getPopupWindowQuyu();
                                popupWindowquyu.showAtLocation(v, Gravity.CENTER, 0, 0);
                            } else {
                                App.getInstance().showToast("领取失败!");
                            }
                        } else {
                            App.getInstance().showToast("领取失败,请退出后重新进入.");
                        }


                    } else {
                        App.getInstance().showToast(btn_submit_coupon.getText().toString());
                    }
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("tag", "CouponDetail");
                    intent.setClass(CouponDetailActivity.this, Loginaty.class);
                    startActivity(intent);
                }
                break;
            case R.id.re_coupondetail_shop:
                if (Cdetail != null) {

                    Intent intent = new Intent();
                    if ("1".equals(Cdetail.getCbtt())) {
                        intent.putExtra("storeid", Cdetail.getCbtid());
                        intent.setClass(CouponDetailActivity.this, StoreDetailActivity.class);
                        startActivity(intent);
                    } else if ("2".equals(Cdetail.getCbtt())) {
                        intent.putExtra("sid", Cdetail.getCbtid());
                        intent.setClass(CouponDetailActivity.this, ShopDetailNewSNActivity.class);
                        startActivity(intent);
                    }


                }
                break;
            //cbtid;// 发行店铺ID
            //String cbtt;// 1 商户优惠券 2店铺优惠券

        }
    }

    /***
     * 获取PopupWindow实例
     */
    private void getPopupWindowQuyu() {
        if (null != popupWindowquyu) {
            popupWindowquyu.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }

    private PopupWindow popupWindowquyu;

    /**
     * 创建区域pop
     */
    protected void initPopuptWindow() {
        View popupWindow_view = getLayoutInflater().inflate(R.layout.activity_dialogcoupon, null,
                false);
        TextView tv_salechange_moreproduct = (TextView) popupWindow_view.findViewById(R.id.tv_salechange_moreproduct);
        TextView tv_salechange_mygift = (TextView) popupWindow_view.findViewById(R.id.tv_salechange_mygift);
        TextView tv_pointsale = (TextView) popupWindow_view.findViewById(R.id.tv_pointsale);
        tv_pointsale.setText("此次领取您将消费 " + Cdetail.getCbea() + " 币");
        tv_salechange_moreproduct.setOnClickListener(this);
        tv_salechange_mygift.setOnClickListener(this);
        popupWindowquyu = new PopupWindow(popupWindow_view,
                DensityUtil.floatToInt(getResources().getDimensionPixelOffset(R.dimen.base_dimen_560)), DensityUtil.floatToInt(getResources().getDimensionPixelOffset(R.dimen.base_dimen_260)), true);
        popupWindowquyu.setFocusable(true);
        popupWindowquyu.setBackgroundDrawable(new BitmapDrawable());
        // 设置动画效果
        popupWindowquyu.setAnimationStyle(android.R.style.Animation_Dialog);
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
    }
}