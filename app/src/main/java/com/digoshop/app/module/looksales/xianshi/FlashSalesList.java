package com.digoshop.app.module.looksales.xianshi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.home.GlideImageLoader;
import com.digoshop.app.module.home.couponactive.ActivityDetails;
import com.digoshop.app.module.home.model.ActivityDetailBean;
import com.digoshop.app.module.looksales.model.TiemTitleData;
import com.digoshop.app.module.looksales.model.TimeTitleBean;
import com.digoshop.app.module.looksales.xianshi.adp.NewsFragmentPagerAdapter;
import com.digoshop.app.module.looksales.xianshi.fragment.NewsFragment;
import com.digoshop.app.module.looksales.xianshi.view.ColumnHorizontalScrollView;
import com.digoshop.app.module.looksales.xianshi.xtool.BaseTools;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.DensityUtil;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.digoshop.app.utils.Tool.getNetWifi;
import static com.digoshop.app.utils.Tool.getStringToDate;


/**
 * 显示竞拍列表
 * author:RA
 * blog : http://blog.csdn.net/vipzjyno1/
 */
public class FlashSalesList extends FragmentActivity {
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    LinearLayout mRadioGroup_content;
    RelativeLayout rl_column;
    private ViewPager mViewPager;
    private ArrayList<TimeTitleBean> timeTitleBeens = new ArrayList<>();
    private int columnSelectIndex = 0;
    public ImageView shade_left;
    public ImageView shade_right;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth = 0;
    /**
     * Item宽度
     */
    private int mItemWidth = 0;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private String CityCodestr = "";
    private Banner banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashsaleslist);
        mScreenWidth = BaseTools.getWindowsWidth(this);
        latstr = LocalSave.getValue(this, AppConfig.basefile, "lat", "");
        logstr = LocalSave.getValue(this, AppConfig.basefile, "lon", "");

        mItemWidth = mScreenWidth / 6;// 一个Item宽度为屏幕的1/7
        initView();
    }
    private String latstr = "";
    private String logstr = "";
    /**
     * 初始化layout控件
     */
    private void initView() {
        banner = (Banner) findViewById(R.id.banner);
        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                ActivityDetailBean homeactivityinfo = new ActivityDetailBean();
                Intent intentaty = new Intent();
                homeactivityinfo.setMnid(atylist.get(position-1).getMnid());
                intentaty.setClass(FlashSalesList.this, ActivityDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("atycontent", homeactivityinfo);
                intentaty.putExtras(bundle);
                startActivity(intentaty);

            }
        });
        mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
        rl_column = (RelativeLayout) findViewById(R.id.rl_column);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        shade_left = (ImageView) findViewById(R.id.shade_left);
        shade_right = (ImageView) findViewById(R.id.shade_right);
        CityCodestr = LocalSave.getValue(FlashSalesList.this, AppConfig.basefile, "CityCode", "");
        if (getNetWifi()) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            getTitleTitleList();
            getFlashSaleAty();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }
    }
    private void getFlashSaleAty(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getFlashSaleAtyApi();
            }
        }).start();

    }
    ArrayList<ActivityDetailBean> atylist;
    private void getFlashSaleAtyApi(){
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            atylist = api.getLookSeekAty("",CityCodestr, latstr,logstr,"ER_INNER_PROMOTION");

            if(atylist!=null){
                if(atylist.size()>0){
                    handler.sendEmptyMessage(5);
                }else{
                    handler.sendEmptyMessage(6);
                }

            }else{
                handler.sendEmptyMessage(6);

            }

        } catch (WSError wsError) {
            wsError.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getTitleTitleList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getTitleTitleListapi();
            }
        }).start();
    }

    TiemTitleData timeTitleDats;

    public void getTitleTitleListapi() {
        //开始时间
        String nowtime = Tool.getCurrentDate();
        long startdata = getStringToDate(nowtime) / 1000;
        Date dt = new Date();
        dt.setDate(dt.getDate() + 2);
        //结束时间
        long enddata = dt.getTime() / 1000;
        try {
            DigoIUserApiImpl api = new DigoIUserApiImpl();
            timeTitleDats = api.getPeriodTitle(startdata + "", enddata + "", CityCodestr);
            if (timeTitleDats != null) {
                if (timeTitleDats.getTimeTitleBeen() != null) {
                    if (timeTitleDats.getTimeTitleBeen().size() > 0) {
                        timeTitleBeens = timeTitleDats.getTimeTitleBeen();
                        Log.v("lsq", "size+" +timeTitleBeens);
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(2);
                    }
                } else {
                    handler.sendEmptyMessage(2);
                }
            } else {
                handler.sendEmptyMessage(2);
            }


        } catch (WSError wsError) {
            handler.sendEmptyMessage(2);
            wsError.printStackTrace();
        } catch (JSONException e) {
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setChangelView();
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    App.getInstance().showToast("暂无数据!");
                    StyledDialog.dismissLoading();

                    break;
                case 3:
                    App.getInstance().showToast("解析异常!");
                    StyledDialog.dismissLoading();

                    break;
                case 5:
                    List<String> strlist = new ArrayList<String>();
                    for (int i = 0; i < atylist.size(); i++) {
                        ActivityDetailBean activityDetailBean = atylist.get(i);
                        strlist.add(atylist.get(i).getMnp());
                    }
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    int screenWidth = dm.widthPixels;
                    int screenHeight = dm.heightPixels;
                    ViewGroup.LayoutParams params = banner.getLayoutParams();
                    params.height=screenWidth*3/7;
                    params.width =screenWidth;
                    banner.setLayoutParams(params);
                    banner.setVisibility(View.VISIBLE);
                    banner.setImages(strlist).setImageLoader(new GlideImageLoader()).start();
                    break;
                case 6:
                    banner.setVisibility(View.GONE);
                    break;
            }
        }
    };

    /**
     * 当栏目项发生变化时候调用
     */
    private void setChangelView() {
        Log.v("lsq", "size333+");
        initTabColumn();
        initFragment();
    }


    /**
     * 初始化Column栏目项
     */
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count = timeTitleBeens.size();
        Log.v("lsq", "size444+" + count);
        mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, rl_column);
        for (int i = 0; i < count; i++) {
            Log.v("lsq", "size5555+" + i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth, LayoutParams.MATCH_PARENT);
            params.leftMargin = DensityUtil.px2dip(App.getInstance(),(float)getResources().getDimensionPixelSize(R.dimen.base_dimen_10));
            params.rightMargin =DensityUtil.px2dip(App.getInstance(),(float)getResources().getDimensionPixelSize(R.dimen.base_dimen_10));
            //	TextView localTextView = (TextView) mInflater.inflate(R.layout.column_radio_item, null);
            TextView localTextView = new TextView(this);
            localTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
            // localTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
            localTextView.setGravity(Gravity.CENTER);
            localTextView.setPadding(5, 0, 5, 0);
            localTextView.setLineSpacing(2.0f,1.2f);
            localTextView.setId(i);
            Log.v("lsq", "time+" + timeTitleBeens.get(i).getValuesstr() + "\n" + timeTitleBeens.get(i).getTypestr());
            String tittlestr = timeTitleBeens.get(i).getValuesstr() + "\n" + timeTitleBeens.get(i).getTypestr();
            SpannableString styledText = new SpannableString(tittlestr);
            styledText.setSpan(new TextAppearanceSpan(this, R.style.top_category_scroll_view_item_text), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(this, R.style.top_category_scroll_view_item_text_down), 5, tittlestr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            localTextView.setText(styledText, TextView.BufferType.SPANNABLE);
                localTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
            if (columnSelectIndex == i) {
                localTextView.setSelected(true);
            }
            localTextView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else {
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
                    //Toast.makeText(getApplicationContext(), timeTitleBeens.get(v.getId()).getTypestr(), Toast.LENGTH_SHORT).show();
                }
            });
            mRadioGroup_content.addView(localTextView, i, params);
        }
    }

    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            // rg_nav_content.getParent()).smoothScrollTo(i2, 0);
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
            // mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
            // mItemWidth , 0);
        }
        //判断是否选中
        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        int count = timeTitleBeens.size();
        for (int i = 0; i < count; i++) {
            Bundle data = new Bundle();
            //"2016-09-04 12:00"
            data.putString("text", timeTitleBeens.get(i).getDateandhourstr());
            NewsFragment newfragment = new NewsFragment();
            newfragment.setArguments(data);
            fragments.add(newfragment);
        }
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);
    }

    /**
     * ViewPager切换监听方法
     */
    public OnPageChangeListener pageListener = new OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            mViewPager.setCurrentItem(position);
            selectTab(position);
        }
    };
}

