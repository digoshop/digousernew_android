package com.digoshop.app.module.userCenter.module;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.home.CouponHomeHuaAty;
import com.digoshop.app.module.home.couponactive.ActivityDetails;
import com.digoshop.app.module.home.model.ActivityDetailBean;
import com.digoshop.app.module.login.Loginaty;
import com.digoshop.app.module.looksales.poitsale.SaleActivity;
import com.digoshop.app.module.userCenter.adp.Point_AtyAdp;
import com.digoshop.app.module.userCenter.model.SingData;
import com.digoshop.app.module.userCenter.model.UserStatBean;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.ArrayList;

import static com.digoshop.app.utils.Tool.getNetWifi;


/**
 * 签到页面
 *
 * @author Administrator
 */
@SuppressLint("ResourceAsColor")
public class PointsActivity extends BaseActivity implements OnClickListener {
    private LinearLayout Preferentialactivity, Rollcollarcore;
    private TextView tv_Sign, tv_integral;
    private String mypoint, gold;
    private SingData isSing;
    private RadioButton rb_signOne, rb_anotherday, rb_Threedays, rb_Fourdays, rb_Fivedays, rb_Sixdays, rb_sevendays;
    private TextView tv_oneday, tv_anotherday, tv_Threedays, tv_Fourdays, tv_Fivedays, tv_Sixdays, tv_sevendays;
    private UserStatBean userStatBean;
    private String CityCodestr = "";
    private ListView lv_couponactive;
    int screenWidth;
    private ScrollView sv_point_fr;
    private RelativeLayout re_ponit_aty, re_sign;
    private String latstr = "";
    private String logstr = "";
    private ImageView iv_sign_allow_one, iv_sign_allow_two, iv_sign_allow_three, iv_sign_allow_four, iv_sign_allow_five, iv_sign_allow_six, iv_sign_allow_seven;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_point);
        iv_sign_allow_one = (ImageView) findViewById(R.id.iv_sign_allow_one);
        iv_sign_allow_two = (ImageView) findViewById(R.id.iv_sign_allow_two);
        iv_sign_allow_three = (ImageView) findViewById(R.id.iv_sign_allow_three);
        iv_sign_allow_four = (ImageView) findViewById(R.id.iv_sign_allow_four);
        iv_sign_allow_five = (ImageView) findViewById(R.id.iv_sign_allow_five);
        iv_sign_allow_six = (ImageView) findViewById(R.id.iv_sign_allow_six);
        iv_sign_allow_seven = (ImageView) findViewById(R.id.iv_sign_allow_seven);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("赚迪币");
        sv_point_fr = (ScrollView) findViewById(R.id.sv_point_fr);
        re_ponit_aty = (RelativeLayout) findViewById(R.id.re_ponit_aty);
        re_sign = (RelativeLayout) findViewById(R.id.re_sign);
        re_sign.setOnClickListener(this);
        lv_couponactive = (ListView) findViewById(R.id.lv_couponactive);
        lv_couponactive.setFocusable(false);
        lv_couponactive.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ActivityDetailBean homeactivityinfo = new ActivityDetailBean();
                Intent intentaty = new Intent();
                homeactivityinfo.setMnid(atyinfos.get(i).getMnid());
                intentaty.setClass(PointsActivity.this, ActivityDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("atycontent", homeactivityinfo);
                intentaty.putExtras(bundle);
                startActivity(intentaty);
            }
        });
        Rollcollarcore = (LinearLayout) findViewById(R.id.Rollcollarcore);
        Rollcollarcore.setOnClickListener(this);
        Preferentialactivity = (LinearLayout) findViewById(R.id.Preferentialactivity);
        Preferentialactivity.setOnClickListener(this);
        tv_Sign = (TextView) findViewById(R.id.tv_Sign);
        tv_integral = (TextView) findViewById(R.id.tv_integral);

        rb_signOne = (RadioButton) findViewById(R.id.rb_signOne);
        tv_oneday = (TextView) findViewById(R.id.tv_oneday);

        rb_anotherday = (RadioButton) findViewById(R.id.rb_anotherday);
        tv_anotherday = (TextView) findViewById(R.id.tv_anotherday);

        rb_Threedays = (RadioButton) findViewById(R.id.rb_Threedays);
        tv_Threedays = (TextView) findViewById(R.id.tv_Threedays);

        rb_Fourdays = (RadioButton) findViewById(R.id.rb_Fourdays);
        tv_Fourdays = (TextView) findViewById(R.id.tv_Fourdays);

        rb_Fivedays = (RadioButton) findViewById(R.id.rb_Fivedays);
        tv_Fivedays = (TextView) findViewById(R.id.tv_Fivedays);

        rb_Sixdays = (RadioButton) findViewById(R.id.rb_Sixdays);
        tv_Sixdays = (TextView) findViewById(R.id.tv_Sixdays);

        rb_sevendays = (RadioButton) findViewById(R.id.rb_sevendays);
        tv_sevendays = (TextView) findViewById(R.id.tv_sevendays);
        CityCodestr = LocalSave.getValue(PointsActivity.this, AppConfig.basefile, "CityCode", "");
        DisplayMetrics dm = new DisplayMetrics();
        PointsActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        latstr = LocalSave.getValue(PointsActivity.this, AppConfig.basefile, "lat", "");
        logstr = LocalSave.getValue(PointsActivity.this, AppConfig.basefile, "lon", "");

        isSing = new SingData();
    }

    @Override
    public void onResume() {
        super.onResume();

        //用户登录状态，登录成功未true ,默认未false
        AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
        if ("true".equals(AppConfig.isusertype)) {
            SingState();
            mypoint = LocalSave.getValue(PointsActivity.this, AppConfig.basefile, "mypointstr", "0");
            tv_integral.setText("我的积分:" + mypoint);

            if (getNetWifi()) {
                get_stat();
                //   StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
            } else {
                App.getInstance().showToast("网络不给力，请检查网络设置");
            }
        } else {
            vistView("0");
            tv_Sign.setText("签到");
            tv_integral.setText("我的积分:" + "0");
        }
        if (getNetWifi()) {
            atyinfo();
        }
    }


    private void atyinfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AtyinfoApi();
            }
        }).start();

    }

    ArrayList<ActivityDetailBean> atyinfos = null;

    private void AtyinfoApi() {

        //DigoIProductApiImpl api = new DigoIProductApiImpl();
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            atyinfos = api.getLookSeekAty("", CityCodestr, latstr, logstr, "SY_MY_SHOP_ATY");

            if (atyinfos != null) {

                handler.sendEmptyMessage(9);
            }
        } catch (JSONException e) {
            android.util.Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            android.util.Log.v("ceshi", "WSError5555");
            e.printStackTrace();
        }

    }


    private void SingState() {
        // 同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences = PointsActivity.this.getSharedPreferences("DIGO", Context.MODE_MULTI_PROCESS);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        String count = sharedPreferences.getString("count", "");
        String time = sharedPreferences.getString("time", "");
        // Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
        // cal.add(Calendar.DAY_OF_MONTH, -1);//取当前日期的前一天.
        // Calendar caltay = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
        // caltay.add(Calendar.DAY_OF_MONTH, -1);//取当前日期的前一天.
        // java.text.SimpleDateFormat format = new
        // java.text.SimpleDateFormat("yyyy-MM-dd");
        // System.out.println("yesterday is:"+format.format(cal.getTime()));
        vistView(count);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {

            case R.id.Rollcollarcore://优惠券
                intent.setClass(PointsActivity.this, CouponHomeHuaAty.class);
                startActivity(intent);
                break;
            case R.id.Preferentialactivity://活动
                intent.setClass(PointsActivity.this, SaleActivity.class);
                startActivity(intent);
                break;
            case R.id.re_sign:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    get_sign();
                } else {
                    intent.setClass(PointsActivity.this, Loginaty.class);
                    startActivity(intent);
                }

                break;
            default:
                break;
        }

    }

    private void get_sign() {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getSignPoints();
            }
        }).start();
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

    private void get_statapi() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            userStatBean = api.get_stat();
            if (userStatBean != null) {
                handler.sendEmptyMessage(8);
            }

        } catch (JSONException e) {
            Log.v("ceshi", "JSONException888");
            e.printStackTrace();
        } catch (WSError e) {
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }

    }

    Handler handler = new Handler() {
        @SuppressWarnings("deprecation")
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    get_stat();
                    gold = isSing.getGold();
                    String count = isSing.getCount();
                    String sign_time = isSing.getSign_time();
                    String time = Tool.getUninxToJavaDaya();
                    tv_Sign.setText("+" + gold + "\n" + "签到");
                    // 实例化SharedPreferences对象（第一步）
                    SharedPreferences mySharedPreferences = PointsActivity.this.getSharedPreferences("DIGO",
                            Context.MODE_MULTI_PROCESS);
                    // 实例化SharedPreferences.Editor对象（第二步）
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    // 用putString的方法保存数据
                    editor.putString("count", count);
                    editor.putString("time", time);
                    // 提交当前数据
                    editor.commit();
                    vistView(count);

                    App.getInstance().showToast("签到成功!");
                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    App.getInstance().showToast("已签到");
                    StyledDialog.dismissLoading();

                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
                case 4:
                    App.getInstance().showToast("签到失败！");
                    StyledDialog.dismissLoading();

                    break;
                case 8:

                    String mypointstr = userStatBean.getTotal_intg();
                    tv_integral.setText("我的积分：" + mypointstr);
                    LocalSave.putValue(PointsActivity.this, AppConfig.basefile, "mypointstr", mypointstr);
                    break;
                case 9:
                    if (atyinfos != null) {
                        if (atyinfos.size() > 0) {
                            re_ponit_aty.setVisibility(View.VISIBLE);
                            Point_AtyAdp atyadp = new Point_AtyAdp(PointsActivity.this, atyinfos, screenWidth, screenWidth * 3 / 7);
                            lv_couponactive.setAdapter(atyadp);
                        }
                    }
                    StyledDialog.dismissLoading();
                    sv_point_fr.smoothScrollTo(0, 20);
                    // sv_point_fr.scrollTo(0,20);
                    break;
            }
        }
    };

    private void getSignPoints() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            isSing = api.sing_in();
            if (isSing != null) {
                if (TextUtils.isEmpty(isSing.getDesc()) | "".equals(isSing.getDesc())) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }
            } else {
                handler.sendEmptyMessage(4);
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


    private void vistView(String count) {
        Resources resources;
        Drawable drawable;
        if (count.equals("1")) {
            tv_oneday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.title_bar_bg));
            rb_signOne.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.white_text));
            rb_signOne.setBackgroundResource(R.drawable.a_shape_yuanlan);
            iv_sign_allow_one.setVisibility(View.VISIBLE);
            iv_sign_allow_two.setVisibility(View.GONE);
            iv_sign_allow_three.setVisibility(View.GONE);
            iv_sign_allow_four.setVisibility(View.GONE);
            iv_sign_allow_five.setVisibility(View.GONE);
            iv_sign_allow_six.setVisibility(View.GONE);
            iv_sign_allow_seven.setVisibility(View.GONE);
        } else if (count.equals("2")) {
            iv_sign_allow_one.setVisibility(View.GONE);
            iv_sign_allow_two.setVisibility(View.VISIBLE);
            iv_sign_allow_three.setVisibility(View.GONE);
            iv_sign_allow_four.setVisibility(View.GONE);
            iv_sign_allow_five.setVisibility(View.GONE);
            iv_sign_allow_six.setVisibility(View.GONE);
            iv_sign_allow_seven.setVisibility(View.GONE);
            tv_oneday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_signOne.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_signOne.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_anotherday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.title_bar_bg));
            rb_anotherday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.white_text));
            rb_anotherday.setBackgroundResource(R.drawable.a_shape_yuanlan);

        } else if (count.equals("3")) {
            iv_sign_allow_one.setVisibility(View.GONE);
            iv_sign_allow_two.setVisibility(View.GONE);
            iv_sign_allow_three.setVisibility(View.VISIBLE);
            iv_sign_allow_four.setVisibility(View.GONE);
            iv_sign_allow_five.setVisibility(View.GONE);
            iv_sign_allow_six.setVisibility(View.GONE);
            iv_sign_allow_seven.setVisibility(View.GONE);
            tv_oneday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_signOne.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_signOne.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_anotherday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_anotherday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_anotherday.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Threedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.title_bar_bg));
            rb_Threedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.white_text));
            rb_Threedays.setBackgroundResource(R.drawable.a_shape_yuanlan);

        } else if (count.equals("4")) {
            iv_sign_allow_one.setVisibility(View.GONE);
            iv_sign_allow_two.setVisibility(View.GONE);
            iv_sign_allow_three.setVisibility(View.GONE);
            iv_sign_allow_four.setVisibility(View.VISIBLE);
            iv_sign_allow_five.setVisibility(View.GONE);
            iv_sign_allow_six.setVisibility(View.GONE);
            iv_sign_allow_seven.setVisibility(View.GONE);
            tv_oneday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_signOne.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_signOne.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_anotherday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_anotherday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_anotherday.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Threedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_Threedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_Threedays.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Fourdays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.title_bar_bg));
            rb_Fourdays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.white_text));
            rb_Fourdays.setBackgroundResource(R.drawable.a_shape_yuanlan);

        } else if (count.equals("5")) {
            iv_sign_allow_one.setVisibility(View.GONE);
            iv_sign_allow_two.setVisibility(View.GONE);
            iv_sign_allow_three.setVisibility(View.GONE);
            iv_sign_allow_four.setVisibility(View.GONE);
            iv_sign_allow_five.setVisibility(View.VISIBLE);
            iv_sign_allow_six.setVisibility(View.GONE);
            iv_sign_allow_seven.setVisibility(View.GONE);
            tv_oneday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_signOne.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_signOne.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_anotherday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_anotherday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_anotherday.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Threedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_Threedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_Threedays.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Fourdays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_Fourdays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_Fourdays.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Fivedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.title_bar_bg));
            rb_Fivedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.white_text));
            rb_Fivedays.setBackgroundResource(R.drawable.a_shape_yuanlan);

        } else if (count.equals("6")) {
            iv_sign_allow_one.setVisibility(View.GONE);
            iv_sign_allow_two.setVisibility(View.GONE);
            iv_sign_allow_three.setVisibility(View.GONE);
            iv_sign_allow_four.setVisibility(View.GONE);
            iv_sign_allow_five.setVisibility(View.GONE);
            iv_sign_allow_six.setVisibility(View.VISIBLE);
            iv_sign_allow_seven.setVisibility(View.GONE);
            tv_oneday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_signOne.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_signOne.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_anotherday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_anotherday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_anotherday.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Threedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_Threedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_Threedays.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Fourdays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_Fourdays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_Fourdays.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Fivedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_Fivedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_Fivedays.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Sixdays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.title_bar_bg));
            rb_Sixdays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.white_text));

            rb_Sixdays.setBackgroundResource(R.drawable.a_shape_yuanlan);
        } else if (count.equals("7")) {
            iv_sign_allow_one.setVisibility(View.GONE);
            iv_sign_allow_two.setVisibility(View.GONE);
            iv_sign_allow_three.setVisibility(View.GONE);
            iv_sign_allow_four.setVisibility(View.GONE);
            iv_sign_allow_five.setVisibility(View.GONE);
            iv_sign_allow_six.setVisibility(View.GONE);
            iv_sign_allow_seven.setVisibility(View.VISIBLE);
            tv_oneday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_signOne.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_signOne.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_anotherday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_anotherday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_anotherday.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Threedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_Threedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_Threedays.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Fourdays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_Fourdays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_Fourdays.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Fivedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_Fivedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_Fivedays.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Sixdays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_Sixdays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_Sixdays.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_sevendays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.title_bar_bg));
            rb_sevendays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.white_text));
            rb_sevendays.setBackgroundResource(R.drawable.a_shape_yuanlan);
        } else if (count.equals("0")) {
            iv_sign_allow_one.setVisibility(View.GONE);
            iv_sign_allow_two.setVisibility(View.GONE);
            iv_sign_allow_three.setVisibility(View.GONE);
            iv_sign_allow_four.setVisibility(View.GONE);
            iv_sign_allow_five.setVisibility(View.GONE);
            iv_sign_allow_six.setVisibility(View.GONE);
            iv_sign_allow_seven.setVisibility(View.GONE);
            tv_oneday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_signOne.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_signOne.setBackgroundResource(R.drawable.a_shape_yuanhui);
            tv_anotherday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_anotherday.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_anotherday.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Threedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_Threedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_Threedays.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Fourdays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_Fourdays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_Fourdays.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Fivedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_Fivedays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_Fivedays.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_Sixdays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_Sixdays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));

            rb_Sixdays.setBackgroundResource(R.drawable.a_shape_yuanhui);

            tv_sevendays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_sevendays.setTextColor(ContextCompat.getColor(PointsActivity.this, R.color.hui));
            rb_sevendays.setBackgroundResource(R.drawable.a_shape_yuanhui);
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
