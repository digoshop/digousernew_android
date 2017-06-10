package com.digoshop.app.module.shopdetailnew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.customServices.adp.CategoryChooseAdp;
import com.digoshop.app.module.customServices.model.CategoryChooseBean;
import com.digoshop.app.module.home.model.ShowInfo;
import com.digoshop.app.module.login.Loginaty;
import com.digoshop.app.module.shopdetailnew.fragment.ShopMesstFragment;
import com.digoshop.app.module.shopdetailnew.fragment.ShopOneFragment;
import com.digoshop.app.module.shopdetailnew.fragment.ShopProductFragment;
import com.digoshop.app.module.shopdetailnew.fragment.ShopsFragmentPagerAdapter;
import com.digoshop.app.module.shopdetailnew.model.ShopDetailNData;
import com.digoshop.app.module.shopdetailnew.view.StickyNavLayout;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.DensityUtil;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.QRCodeUtil;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.gxz.PagerSlidingTabStrip;
import com.hss01248.dialog.StyledDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.SocializeUtils;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;

import static com.digoshop.app.utils.QRCodeUtil.getFileRoot;
import static com.digoshop.app.utils.Tool.getNetWifi;

public class ShopDetailNewSNActivity extends FragmentActivity implements View.OnClickListener {
    private LinearLayout lin_shopnew_one, lin_shopnew_type, lin_shopnew_message;
    private ImageView iv_shopdetail_shopimg;
    private RelativeLayout re_shopnew_title;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ViewPager mViewPager;
    private String sid;
    private RelativeLayout lin_shopn_tyep, re_shoopn_share;
    private DisplayImageOptions options;
    private ShopDetailNData detailNData;
    private TextView tv_shopdetail_shopname;
    private ScrollView sv_shopdetail_new;
    private PopupWindow popupWindowquyu;
    private PopupWindow popupWindowfeed;
    private ImageView iv_title_feed, iv_title_relation;
    private String isrelationstr = "false";
    private PopupWindow popWindow;
    private ArrayList<CategoryChooseBean> categoryChooseBeens = new ArrayList<>();
    private TextView tv_shoptypen;
    private RelativeLayout re_shoopn_back, re_shoopn_feed, re_shoopn_relation;
    PagerSlidingTabStrip pagerSlidingTabStrip;
    private String tag = "1";
    private UMShareAPI mShareAPI;
    private ProgressDialog dialog;
    int screenWidth, screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopdetailnewsn);
        re_shoopn_back = (RelativeLayout) findViewById(R.id.re_shoopn_back);
        re_shoopn_back.setOnClickListener(this);
        mShareAPI = UMShareAPI.get(this);
        dialog = new ProgressDialog(this);
        re_shoopn_share = (RelativeLayout) findViewById(R.id.re_shoopn_share);
        re_shoopn_share.setOnClickListener(this);
        re_shoopn_feed = (RelativeLayout) findViewById(R.id.re_shoopn_feed);
        re_shoopn_feed.setOnClickListener(this);
        re_shoopn_relation = (RelativeLayout) findViewById(R.id.re_shoopn_relation);
        re_shoopn_relation.setOnClickListener(this);
        Intent intent = getIntent();
        String action = intent.getAction();
        try {
            if (Intent.ACTION_VIEW.equals(action)) {
                Uri uri = intent.getData();
                if (uri != null) {
                    sid = uri.getQueryParameter("sid");
                }
            } else {
                sid = getIntent().getStringExtra("sid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            App.getInstance().showToast("请求失败，请稍后再试");
            finish();
        }


        fheingt = (float) getResources().getDimensionPixelOffset(R.dimen.base_dimen_85);
        tv_shoptypen = (TextView) findViewById(R.id.tv_shoptypen);
        sv_shopdetail_new = (ScrollView) findViewById(R.id.sv_shopdetail_new);
        iv_title_feed = (ImageView) findViewById(R.id.iv_title_feed);
        iv_title_relation = (ImageView) findViewById(R.id.iv_title_relation);
        tv_shopdetail_shopname = (TextView) findViewById(R.id.tv_shopdetail_shopname);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mViewPager.setOffscreenPageLimit(2);
        pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.id_stickynavlayout_indicator);
        stickyNavLayout = (StickyNavLayout) findViewById(R.id.id_stick);

        initFragment();

        lin_shopnew_one = (LinearLayout) findViewById(R.id.lin_shopnew_one);
        lin_shopnew_type = (LinearLayout) findViewById(R.id.lin_shopnew_type);
        lin_shopnew_message = (LinearLayout) findViewById(R.id.lin_shopnew_message);
        lin_shopn_tyep = (RelativeLayout) findViewById(R.id.lin_shopn_tyep);
        CategoryChooseBean categoryChooseBean = new CategoryChooseBean();
        categoryChooseBean.setName("商家已倒闭");
        categoryChooseBeensfeed.add(categoryChooseBean);
        CategoryChooseBean categoryChooseBean1 = new CategoryChooseBean();
        categoryChooseBean1.setName("商家位置不准");
        categoryChooseBeensfeed.add(categoryChooseBean1);
        CategoryChooseBean categoryChooseBean2 = new CategoryChooseBean();
        categoryChooseBean2.setName("商家电话不准");
        categoryChooseBeensfeed.add(categoryChooseBean2);
        CategoryChooseBean categoryChooseBean3 = new CategoryChooseBean();
        categoryChooseBean3.setName("商家服务不好");
        categoryChooseBeensfeed.add(categoryChooseBean3);
        lin_shopnew_one.setOnClickListener(this);
        lin_shopnew_type.setOnClickListener(this);
        lin_shopnew_message.setOnClickListener(this);
        iv_shopdetail_shopimg = (ImageView) findViewById(R.id.iv_shopdetail_shopimg);
        re_shopnew_title = (RelativeLayout) findViewById(R.id.re_shopnew_title);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        // int heightiv = screenWidth *2 / 3;
        int heightiv = screenWidth * 7 / 15;
        ViewGroup.LayoutParams params = iv_shopdetail_shopimg.getLayoutParams();
        params.height = heightiv;
        iv_shopdetail_shopimg.setLayoutParams(params);
        ViewGroup.LayoutParams reparams = re_shopnew_title.getLayoutParams();
        reparams.height = heightiv;
        re_shopnew_title.setLayoutParams(reparams);
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.home_adone)
                .showImageForEmptyUri(R.drawable.home_adone).showImageOnFail(R.drawable.home_adone).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
        if (getNetWifi()) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            getShopDetailN_aty();
            if (!TextUtils.isEmpty(sid)) {
                tag = "1";
                putusermessage();
            }
            getShopCateheight();
        } else {
            finish();
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }
    }

    private void getShopDetailN_aty() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getShopDetailN_atyApi();
            }
        }).start();
    }

    private void getShopCateheight() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getShopCateheightapi();
            }
        }).start();

    }

    private void getShopCateheightapi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {

            categoryChooseBeens = api.getShopCategory(sid);
            handler.sendEmptyMessage(10);
        } catch (JSONException e) {
            if (categoryChooseBeens != null) {
                categoryChooseBeens.clear();
            }
            CategoryChooseBean categoryChooseBean = new CategoryChooseBean();
            categoryChooseBean.setName("全部分类");
            categoryChooseBeens.add(categoryChooseBean);
            handler.sendEmptyMessage(10);
            e.printStackTrace();
        } catch (WSError e) {
            if (categoryChooseBeens != null) {
                categoryChooseBeens.clear();
            }
            CategoryChooseBean categoryChooseBean = new CategoryChooseBean();
            categoryChooseBean.setName("全部分类");
            categoryChooseBeens.add(categoryChooseBean);
            handler.sendEmptyMessage(10);
            e.printStackTrace();
        }
    }

    private void getShopCate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getShopCateapi();
            }
        }).start();
    }


    private void getShopCateapi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            categoryChooseBeens = api.getShopCategory(sid);
            handler.sendEmptyMessage(11);
        } catch (JSONException e) {
            if (categoryChooseBeens != null) {
                categoryChooseBeens.clear();
            }
            CategoryChooseBean categoryChooseBean = new CategoryChooseBean();
            categoryChooseBean.setName("全部分类");
            categoryChooseBeens.add(categoryChooseBean);
            handler.sendEmptyMessage(11);
            e.printStackTrace();
        } catch (WSError e) {
            if (categoryChooseBeens != null) {
                categoryChooseBeens.clear();
            }
            CategoryChooseBean categoryChooseBean = new CategoryChooseBean();
            categoryChooseBean.setName("全部分类");
            categoryChooseBeens.add(categoryChooseBean);
            handler.sendEmptyMessage(11);
            e.printStackTrace();
        }
    }

    private void getShopDetailN_atyApi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {

            detailNData = api.getShopDetailN_aty(sid);

            if (detailNData != null) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(4);
            }

        } catch (JSONException e) {
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            Log.v("niao", "+WSError+" + e.getMessage());
            if ("A502".equals(e.getMessage())) {
                handler.sendEmptyMessage(502);
                return;
            }
            handler.sendEmptyMessage(4);
            e.printStackTrace();
        }
    }

    ShowInfo showInfo;
    String filePath;
    private Bitmap shopbgbitmap = null;
    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    // filePath = getFileRoot(ShopDetailNewSNActivity.this);


                    showInfo = new ShowInfo();
                    showInfo.setProductimgurl(detailNData.getCover());
                    if (!TextUtils.isEmpty(detailNData.getName())) {
                        showInfo.setShopname(detailNData.getName());
                    } else {
                        showInfo.setShopname("暂无");
                    }
                    if (!TextUtils.isEmpty(detailNData.getOpen_time())) {
                        showInfo.setShoptime(detailNData.getOpen_time());
                    } else {
                        showInfo.setShoptime("暂无");

                    }
                    if (!TextUtils.isEmpty(detailNData.getContact())) {
                        showInfo.setShoptel(detailNData.getContact());
                    } else {
                        showInfo.setShoptel("暂无");
                    }
                    if (!TextUtils.isEmpty(detailNData.getAddress())) {
                        showInfo.setShopaddress(detailNData.getAddress());
                    } else {
                        showInfo.setShopaddress("暂无");
                    }
                    if (!TextUtils.isEmpty(detailNData.getDescription())) {
                        showInfo.setShopintroduce(detailNData.getDescription());
                    } else {
                        showInfo.setShopintroduce("暂无");
                    }
                    if (!TextUtils.isEmpty(sid)) {
                        showInfo.setQrcodecontent("http://m.digoshop.com/shop/getShopById?shopId=" + sid);
                    } else {
                        showInfo.setQrcodecontent("http://a.app.qq.com/o/simple.jsp?pkgname=com.digoshop");
                    }

                    if (!TextUtils.isEmpty(detailNData.getCover())) {

                        ImageLoader.getInstance().loadImage(detailNData.getCover(), new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {
                                //开始加载图片

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {
                                //图片加载失败
                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                //图片加载完成

                                iv_shopdetail_shopimg.setImageBitmap(bitmap);
                                shopbgbitmap = bitmap;

                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {
                                //图片加载取消
                            }
                        });
                    }


                    tv_shopdetail_shopname.setText(detailNData.getName());
                    if (!TextUtils.isEmpty(detailNData.getRelation())) {
                        isrelationstr = detailNData.getRelation();
                        setRelation(isrelationstr);
                    }
                    if (!TextUtils.isEmpty(detailNData.getSign())) {
                        lin_shopn_tyep.setVisibility(View.VISIBLE);
                        if ("1".equals(detailNData.getSign() + "")) {
                            lin_shopn_tyep.setBackgroundResource(R.drawable.a_shoptype_qinayue);
                            tv_shoptypen.setText("签约");
                            showInfo.setShoptype("签约");
                        } else {
                            tv_shoptypen.setText("认证");
                            showInfo.setShoptype("认证");
                            lin_shopn_tyep.setBackgroundResource(R.drawable.a_shoptype_renzheng);
                        }
                    } else {
                        lin_shopn_tyep.setVisibility(View.GONE);
                    }

                    StyledDialog.dismissLoading();
                    break;
                case 2:
                    StyledDialog.dismissLoading();
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
                case 502:
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                    StyledDialog.dismissLoading();
                    break;
                case 10:
                    Log.v("ceshi", ")))" + categoryChooseBeens.size());
                    if (categoryChooseBeens != null) {
                        heightpxquyu = (float) (fheingt * (categoryChooseBeens.size()));
                    } else {
                        heightpxquyu = (float) (fheingt * 1);
                    }
                    break;
                case 11:
                    if (categoryChooseAdp != null) {
                        categoryChooseAdp.notifyDataSetChanged();
                    }
                    StyledDialog.dismissLoading();
                    break;
                case 21:
                    App.getInstance().showToast("关注成功");
                    setRelation(isrelationstr);
                    break;
                case 22:
                    App.getInstance().showToast("关注已取消");
                    setRelation(isrelationstr);
                    break;
                case 23:
                    App.getInstance().showToast("关注已取消失败！");
                    break;
                case 24:
                    App.getInstance().showToast("关注失败");
                    break;
                case 31:
                    App.getInstance().showToast("反馈成功");
                    StyledDialog.dismissLoading();
                    break;
                case 32:
                    App.getInstance().showToast("反馈失败");
                    StyledDialog.dismissLoading();
                    break;
                case 33:
//                    Intent intent = new Intent();
//                    intent.putExtra("url", filePath);
//                    intent.setClass(ShopDetailNewSNActivity.this, TestAty.class);
//                    startActivity(intent);
                    //生成图片成功，弹出选择框
                    showPopwindow();
                    break;
                case 34:
                    //分享出现异常等
                    App.getInstance().showToast("分享失败，请稍后再试。");
                    if (sharepopWindow != null && sharepopWindow.isShowing()) {
                        sharepopWindow.dismiss();
                    }
                    break;
            }

        }
    };

    StickyNavLayout stickyNavLayout;

    private void initFragment() {
        Bundle data = new Bundle();
        data.putString("sid", sid);
        ShopOneFragment shopOneFragment = new ShopOneFragment();
        shopOneFragment.setArguments(data);
        fragments.add(shopOneFragment);
        ShopProductFragment productFragment = new ShopProductFragment();
        productFragment.setArguments(data);
        fragments.add(productFragment);
        ShopMesstFragment messtFragment = new ShopMesstFragment();
        messtFragment.setArguments(data);
        fragments.add(messtFragment);
        ArrayList<String> list = new ArrayList<>();
        list.add("店铺首页");
        list.add("全部商品");
        list.add("商家环境");


        ShopsFragmentPagerAdapter mAdapetr = new ShopsFragmentPagerAdapter(getSupportFragmentManager(), fragments, list);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);
        pagerSlidingTabStrip.setViewPager(mViewPager);
        // pagerSlidingTabStrip.setOnPageChangeListener(mPageChangeListener);
        pagerSlidingTabStrip.setBackgroundResource(R.color.white_text);
        // 设置Tab是自动填充满屏幕的
        pagerSlidingTabStrip.setShouldExpand(true);
//        // 设置Tab的分割线的颜色
        pagerSlidingTabStrip.setDividerColor(getResources().getColor(R.color.transparent));
        // 设置分割线的上下的间距,传入的是dp
        // pagerSlidingTabStrip.setDividerPaddingTopBottom(12);

        // 设置Tab底部线的高度,传入的是dp
        pagerSlidingTabStrip.setUnderlineHeight(1);
        //设置Tab底部线的颜色
        pagerSlidingTabStrip.setUnderlineColor(getResources().getColor(R.color.qianhui));

        // 设置Tab 指示器Indicator的高度,传入的是dp
        //  pagerSlidingTabStrip.setIndicatorHeight(2);
        // 设置Tab Indicator的颜色
        pagerSlidingTabStrip.setIndicatorColor(getResources().getColor(R.color.title_bar_bg));

        // 设置Tab标题文字的大小,传入的是sp
//        pagerSlidingTabStrip.setTextSize(16);
// 设置选中Tab文字的颜色
        pagerSlidingTabStrip.setSelectedTextColor(getResources().getColor(R.color.title_bar_bg));
//设置正常Tab文字的颜色
        pagerSlidingTabStrip.setTextColor(getResources().getColor(R.color.store_textColor_unselectedtwo));
        //设置Tab文字的左右间距,传入的是dp
//        pagerSlidingTabStrip.setTabPaddingLeftRight(24);

        //设置点击每个Tab时的背景色
        //pagerSlidingTabStrip.setTabBackground(R.drawable.background_tab);

//     //是否支持动画渐变(颜色渐变和文字大小渐变)
//        pagerSlidingTabStrip.setFadeEnabled(false);
//     // 设置最大缩放,是正常状态的0.3倍
//        pagerSlidingTabStrip.setZoomMax(0.3F);


        //stickyNavLayout.setStickNavAndScrollToNav();
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    };

    /**
     * ViewPager切换监听方法
     */
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            mViewPager.setCurrentItem(position);
//            if ("0".equals(position + "")) {
//                rb_shop_titleone.setChecked(true);
//                rb_shop_product.setChecked(false);
//                rb_shop_message.setChecked(false);
//            } else if ("1".equals(position + "")) {
//                rb_shop_titleone.setChecked(false);
//                rb_shop_product.setChecked(true);
//                rb_shop_message.setChecked(false);
//            } else if ("2".equals(position + "")) {
//                rb_shop_titleone.setChecked(false);
//                rb_shop_product.setChecked(false);
//                rb_shop_message.setChecked(true);
//            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private UMImage imageurl, imagelocal;
    private SHARE_MEDIA share_media;
    // share_media == SHARE_MEDIA.WEIXIN  微信好友分享
    //share_media == SHARE_MEDIA.WEIXIN_CIRCLE 微信朋友圈分享

    //    UMImage image = new UMImage(ShareActivity.this, "imageurl");//网络图片
//    UMImage image = new UMImage(ShareActivity.this, file);//本地文件
//    UMImage image = new UMImage(ShareActivity.this, R.drawable.xxx);//资源文件
//    UMImage image = new UMImage(ShareActivity.this, bitmap);//bitmap文件
//    UMImage image = new UMImage(ShareActivity.this, byte[]);//字节流
    boolean issharedigo = false;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.re_shoopn_share:

                if (!getNetWifi()) {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                    return;
                }
                if (shopbgbitmap == null) {
                    App.getInstance().showToast("加载中，请稍后！");
                } else {
                    try {
                        filePath = getFileRoot(ShopDetailNewSNActivity.this) + File.separator
                                + "qr_" + System.currentTimeMillis() + ".png";
                        Log.v("ceshi", "filePath+" + filePath);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                boolean success = QRCodeUtil.createShareShopImgNew(ShopDetailNewSNActivity.this, showInfo, filePath, shopbgbitmap);
                                Log.v("ceshi", "fddd++" + success);
                                if (success) {
                                    handler.sendEmptyMessage(33);
                                } else {
                                    handler.sendEmptyMessage(34);
                                }
                            }
                        }).start();

                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(34);
                    }
                }

                break;
            case R.id.lin_shareshop_weixinuser://分享到微信好友
                if (!getNetWifi()) {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                    return;
                }
                if (!mShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN)) {
                    App.getInstance().showToast("未检测到微信App,请安装");
                    return;
                }
                //要分享的图片
                if (TextUtils.isEmpty(filePath)) {
                    handler.sendEmptyMessage(34);
                    return;
                }
                File localimgfile = new File(filePath);
                if (!localimgfile.exists()) {
                    handler.sendEmptyMessage(34);
                    return;
                }

                imagelocal = new UMImage(this, localimgfile);
                //缩率图screenWidth,screenHeight
                imagelocal.setThumb(new UMImage(this, Tool.getImageThumbnail(filePath, screenWidth / 4, screenHeight / 4)));
                imagelocal.compressStyle = UMImage.CompressStyle.SCALE;
                share_media = SHARE_MEDIA.WEIXIN;
                new ShareAction(ShopDetailNewSNActivity.this).withMedia(imagelocal)
                        .setPlatform(share_media)
                        .setCallback(shareListener).share();

                break;
            case R.id.lin_shareshop_weixincri://分享到朋友圈
                if (!getNetWifi()) {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                    return;
                }
                if (!mShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN)) {
                    App.getInstance().showToast("未检测到微信App,请安装");
                    return;
                }
                //要分享的图片
                if (TextUtils.isEmpty(filePath)) {
                    handler.sendEmptyMessage(34);
                    return;
                }
                File localimgfile2 = new File(filePath);
                if (!localimgfile2.exists()) {
                    handler.sendEmptyMessage(34);
                    return;
                }
//                image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
//                image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享

                imagelocal = new UMImage(this, localimgfile2);
                imagelocal.compressStyle = UMImage.CompressStyle.SCALE;
                //缩率图 screenWidth,screenHeight
                imagelocal.setThumb(new UMImage(this, Tool.getImageThumbnail(filePath, screenWidth / 4, screenHeight / 4)));
                imagelocal.compressStyle = UMImage.CompressStyle.SCALE;
                share_media = SHARE_MEDIA.WEIXIN_CIRCLE;
                new ShareAction(ShopDetailNewSNActivity.this).withMedia(imagelocal)
                        .setPlatform(share_media)
                        .setCallback(shareListener).share();

                break;
            case R.id.btn_shareshop_pop_cancel:// 取消分享
                if (sharepopWindow != null && sharepopWindow.isShowing()) {
                    sharepopWindow.dismiss();
                }
                break;
            case R.id.re_shoopn_back:
                finish();
                break;

//            re_shoopn_feed.setOnClickListener(this);
//            re_shoopn_relation = (RelativeLayout) findViewById(R.id.re_shoopn_relation);
//            re_shoopn_relation.setOnClickListener(this);
            case R.id.re_shoopn_feed:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    if (getNetWifi()) {
                        getPopupWindowFeed();
                        popupWindowfeed.showAtLocation(view, Gravity.CENTER, 0, 0);

                    } else {
                        App.getInstance().showToast("网络不给力，请检查网络设置");
                    }
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("tag", "ShopDetail");
                    intent.setClass(ShopDetailNewSNActivity.this, Loginaty.class);
                    startActivity(intent);
                }


                break;

            case R.id.re_shoopn_relation:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    if (getNetWifi()) {
                        if ("false".equals(isrelationstr)) {
                            add_shop();
                        } else {
                            cancel_shop();
                        }
                    } else {
                        App.getInstance().showToast("网络不给力，请检查网络设置");
                    }
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("tag", "ShopDetail");
                    intent.setClass(ShopDetailNewSNActivity.this, Loginaty.class);
                    startActivity(intent);
                }
                break;
            case R.id.lin_shopnew_one:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.lin_shopnew_type:
                Log.v("ceshi", "lin_shopnew_type");
                getPopupWindowShopCagte();
                int popupWidth = popWindow.getWidth();
                int popupHeight = popWindow.getHeight();
                int width = lin_shopnew_type.getWidth();
                int height = lin_shopnew_type.getHeight();
                popWindow.showAsDropDown(lin_shopnew_type, (width - popupWidth) / 2, DensityUtil.floatToInt((float) getResources().getDimensionPixelOffset(R.dimen.base_dimen_30)));

                break;
            case R.id.lin_shopnew_message:
                if (detailNData != null) {
                    if (!TextUtils.isEmpty(detailNData.getContact())) {
                        getPopupWindowPhone();

                    }
                }

                break;
            case R.id.tv_shop_tel:
                //打电话行为记录
                if (getNetWifi()) {
                    if (!TextUtils.isEmpty(sid)) {
                        tag = "2";
                        putusermessage();
                    }
                }

                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    // url:统一资源定位符
                    // uri:统一资源标示符（更广）
                    intent.setData(Uri.parse("tel:"
                            + detailNData.getContact()));
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

    @Override
    protected void onResume() {
        super.onResume();
        if (sharepopWindow != null && sharepopWindow.isShowing()) {
            sharepopWindow.dismiss();
        }
        if (issharedigo) {
            //StyledDialog.dismissLoading();
            SocializeUtils.safeCloseDialog(dialog);
            issharedigo = false;
        }
    }

    PopupWindow sharepopWindow;

    private void showPopwindow() {
        View parent = ((ViewGroup) ShopDetailNewSNActivity.this.findViewById(android.R.id.content)).getChildAt(0);
        View sharepopView = ShopDetailNewSNActivity.this.getLayoutInflater().inflate(R.layout.shopsharepop, null,
                false);

        LinearLayout lin_shareshop_weixinuser = (LinearLayout) sharepopView.findViewById(R.id.lin_shareshop_weixinuser);
        LinearLayout lin_shareshop_weixincri = (LinearLayout) sharepopView.findViewById(R.id.lin_shareshop_weixincri);
        Button btn_shareshop_pop_cancel = (Button) sharepopView.findViewById(R.id.btn_shareshop_pop_cancel);
        lin_shareshop_weixinuser.setOnClickListener(this);
        lin_shareshop_weixincri.setOnClickListener(this);
        btn_shareshop_pop_cancel.setOnClickListener(this);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        sharepopWindow = new PopupWindow(sharepopView, width, height, true);
        sharepopWindow.setAnimationStyle(R.style.AnimBottom);
        sharepopWindow.setFocusable(true);
        sharepopWindow.setOutsideTouchable(true);// 设置允许在外点击消失
        ColorDrawable dw = new ColorDrawable(0x30000000);
        sharepopWindow.setBackgroundDrawable(dw);

        // 点击其他地方消失
        sharepopView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (sharepopWindow != null && sharepopWindow.isShowing()) {
                    sharepopWindow.dismiss();
                }
                return false;
            }
        });
        sharepopWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            issharedigo = true;
            SocializeUtils.safeShowDialog(dialog);
            // StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
//            StyledDialog.dismissLoading();
            SocializeUtils.safeCloseDialog(dialog);
            App.getInstance().showToast("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            StyledDialog.dismissLoading();
            SocializeUtils.safeCloseDialog(dialog);
            App.getInstance().showToast("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            StyledDialog.dismissLoading();
            SocializeUtils.safeCloseDialog(dialog);
            App.getInstance().showToast("分享取消");

        }
    };
    float fheingt;
    float heightpxquyu = 0;
    private CategoryChooseAdp categoryChooseAdp;

    private void CatePopwindow() {
        View contentView = getLayoutInflater().inflate(R.layout.pop_up, null,
                false);
        popWindow = new PopupWindow(contentView, DensityUtil.floatToInt(App.getInstance().getShopCateWeight()), DensityUtil.floatToInt(heightpxquyu));
        popWindow.setFocusable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置动画效果
        popWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        categoryChooseAdp = new CategoryChooseAdp(ShopDetailNewSNActivity.this, categoryChooseBeens);
        ListView lv_shopncate = (ListView) contentView.findViewById(R.id.lv_shopncate);
        lv_shopncate.setAdapter(categoryChooseAdp);
        if (getNetWifi()) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            getShopCate();
        } else {
            handler.sendEmptyMessage(10);
        }
        lv_shopncate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ("全部分类".equals(categoryChooseBeens.get(i).getName())) {
                    Intent intent = new Intent();
                    intent.putExtra("sid", sid);
                    intent.setClass(ShopDetailNewSNActivity.this, ShopDetailAllCateActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("sid", sid);
                    intent.putExtra("moid", categoryChooseBeens.get(i).getMoid());
                    intent.setClass(ShopDetailNewSNActivity.this, ShopDetailProductListActivity.class);
                    startActivity(intent);
                }

                if (null != popWindow & popWindow.isShowing()) {
                    popWindow.dismiss();
                    return;
                }

            }
        });
    }

    private void add_shop() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                shopDetailFollow();
            }
        }).start();

    }

    //取消关注
    private void cancel_shop() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                cancel_shop_api();
            }
        }).start();
    }

    public void cancel_shop_api() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {

            String datastr = api.cancel_shop(sid);
            if ("true".equals(datastr)) {//取消成功
                handler.sendEmptyMessage(22);
                isrelationstr = "false";
            } else {
                handler.sendEmptyMessage(23);
            }

        } catch (WSError wsError) {
            handler.sendEmptyMessage(23);
            wsError.printStackTrace();
        } catch (JSONException e) {
            handler.sendEmptyMessage(23);
            String code = e.getMessage();
            if (code.equals("-1701")) {

            }
            e.printStackTrace();
        }

    }

    public void shopDetailFollow() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {

            boolean Focuson = api.add_shop(sid);
            if (Focuson) {
                isrelationstr = "true";
                handler.sendEmptyMessage(21);
            } else {
                handler.sendEmptyMessage(24);
            }

        } catch (WSError wsError) {
            handler.sendEmptyMessage(24);
            wsError.printStackTrace();
        } catch (JSONException e) {
            handler.sendEmptyMessage(24);
            String code = e.getMessage();
            if (code.equals("-1701")) {

            }
            e.printStackTrace();
        }

    }

    public void setRelation(String str) {
        if ("true".equals(str)) {
            iv_title_relation.setBackgroundResource(R.drawable.a_shop_reon);
        } else {
            iv_title_relation.setBackgroundResource(R.drawable.a_shop_reun);
        }
    }

    private void getPopupWindowShopCagte() {
        if (null != popWindow) {
            Log.v("ceshi", "lin_shopnew_type1");
            popWindow.dismiss();
            return;
        } else {
            Log.v("ceshi", "lin_shopnew_type2");
            CatePopwindow();
        }
    }

    private void getPopupWindowPhone() {

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

    private void getPopupWindowFeed() {
        if (null != popupWindowfeed) {
            popupWindowfeed.dismiss();
            return;
        } else {
            initPopuptWindowfeed();
        }
    }

    CategoryChooseAdp categoryChooseAdpfeed;
    private String feeditemstr;
    private String feedcontent;//反馈内容拼接
    private ArrayList<CategoryChooseBean> categoryChooseBeensfeed = new ArrayList<>();

    /**
     * 创建区域pop
     */
    protected void initPopuptWindowfeed() {
        categoryChooseAdpfeed = new CategoryChooseAdp(ShopDetailNewSNActivity.this, categoryChooseBeensfeed);
        View popupWindow_view = getLayoutInflater().inflate(R.layout.a_shopn_feed, null,
                false);
        ListView lv_shopClassone = (ListView) popupWindow_view.findViewById(R.id.lv_shopClassone);
        lv_shopClassone.setAdapter(categoryChooseAdpfeed);
        lv_shopClassone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                feeditemstr = categoryChooseBeensfeed.get(i).getName();


                if (detailNData != null) {
                    feedcontent = detailNData.getName() + "," + feeditemstr;
                }
                if (getNetWifi()) {
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                    submit_Feedback();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
                if (popupWindowfeed != null && popupWindowfeed.isShowing()) {
                    popupWindowfeed.dismiss();
                    popupWindowfeed = null;
                }


            }
        });
        popupWindowfeed = new PopupWindow(popupWindow_view,
                DensityUtil.floatToInt(getResources().getDimensionPixelOffset(R.dimen.base_dimen_400)), DensityUtil.floatToInt(getResources().getDimensionPixelOffset(R.dimen.base_dimen_430)), true);
        popupWindowfeed.setFocusable(true);
        popupWindowfeed.setBackgroundDrawable(new BitmapDrawable());
        // 设置动画效果
        popupWindowfeed.setAnimationStyle(android.R.style.Animation_Dialog);
        // popupWindow.setAnimationStyle(R.style.AnimationFade);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindowfeed != null && popupWindowfeed.isShowing()) {
                    popupWindowfeed.dismiss();
                    popupWindowfeed = null;
                }
                return false;
            }
        });
    }

    private void submit_Feedback() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                get_Feedbackcomplaint();
            }
        }).start();
    }

    private void get_Feedbackcomplaint() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            boolean isboole = api.feedback("9", feedcontent, "", sid);
            if (isboole) {
                handler.sendEmptyMessage(31);
            } else {
                handler.sendEmptyMessage(32);
            }
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(32);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(32);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 创建区域pop
     */
    protected void initPopuptWindow() {
        View parent = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        View popupWindow_view = getLayoutInflater().inflate(R.layout.a_shop_teldialog, null,
                false);
        TextView tv_shop_tel = (TextView) popupWindow_view.findViewById(R.id.tv_shop_tel);
        tv_shop_tel.setText(detailNData.getContact());
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
        popupWindowquyu.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

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

            api.putUserMessage("2", sid, tag);

        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            Log.v("ceshi", "WSError" + e.getMessage());
            e.printStackTrace();
        }
    }

}
