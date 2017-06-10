package com.digoshop.app.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.arrountshops.ArountActivity;
import com.digoshop.app.module.login.Loginaty;
import com.digoshop.app.module.looksales.poitsale.SaleActivity;
import com.digoshop.app.module.seekservice.SeekServiceActivitynew;
import com.digoshop.app.module.seekshop.SearchStoreAty;
import com.digoshop.app.module.setting.AboutDigoAty;
import com.digoshop.app.module.setting.AccountSafetyAty;
import com.digoshop.app.module.setting.AppIntroduceActivity;
import com.digoshop.app.module.setting.ComplaintFeedback;
import com.digoshop.app.module.setting.MessageSetAty;
import com.digoshop.app.module.setting.ModifUserInfoAty;
import com.digoshop.app.module.userCenter.module.MyCardActivity;
import com.digoshop.app.module.userCenter.module.MyCostHistoryActivity;
import com.digoshop.app.module.userCenter.module.MyCouponActivity;
import com.digoshop.app.module.userCenter.module.MyCustiomGSListActivity;
import com.digoshop.app.module.userCenter.module.MyGiftWalletActivity;
import com.digoshop.app.module.userCenter.module.MyJifenActivity;
import com.digoshop.app.module.userCenter.module.MyLikeActivity;
import com.digoshop.app.module.userCenter.module.MyMessageActivity;
import com.digoshop.app.module.userCenter.module.PointsActivity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;

public class AllHomeAty extends BaseActivity implements View.OnClickListener {

    @Override
    public boolean isNoTitle() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    private TextView tv_all_search_shop, tv_all_about, tv_all_feed, tv_all_appinfo, tv_messageset, tv_myinfo, tv_all_sale, tv_all_mycoupon, tv_zhuanpoint, tv_all_mylike, tv_all_mycard, tv_all_mycustom, tv_all_mygift, tv_all_history, tv_all_mypoint, tv_all_message, tv_all_custom, tv_all_aty, tv_all_point, tv_all_coupon, tv_all_search_service, tv_all_arount, tv_all_search_stroe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_home_aty);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("全部频道");
        tv_all_search_shop = (TextView) findViewById(R.id.tv_all_search_shop);
        tv_all_search_shop.setOnClickListener(this);
        tv_all_search_service = (TextView) findViewById(R.id.tv_all_search_service);
        tv_all_search_service.setOnClickListener(this);
        tv_all_search_stroe = (TextView) findViewById(R.id.tv_all_search_stroe);
        tv_all_search_stroe.setOnClickListener(this);
        tv_all_arount = (TextView) findViewById(R.id.tv_all_arount);
        tv_all_arount.setOnClickListener(this);
        tv_all_point = (TextView) findViewById(R.id.tv_all_point);
        tv_all_point.setOnClickListener(this);
        tv_all_coupon = (TextView) findViewById(R.id.tv_all_coupon);
        tv_all_coupon.setOnClickListener(this);
        tv_all_aty = (TextView) findViewById(R.id.tv_all_aty);
        tv_all_aty.setOnClickListener(this);
        tv_all_custom = (TextView) findViewById(R.id.tv_all_custom);
        tv_all_custom.setOnClickListener(this);
        tv_all_message = (TextView) findViewById(R.id.tv_all_message);
        tv_all_message.setOnClickListener(this);
        tv_all_history = (TextView) findViewById(R.id.tv_all_history);
        tv_all_history.setOnClickListener(this);
        tv_all_mypoint = (TextView) findViewById(R.id.tv_all_mypoint);
        tv_all_mypoint.setOnClickListener(this);
        tv_all_mygift = (TextView) findViewById(R.id.tv_all_mygift);
        tv_all_mygift.setOnClickListener(this);
        tv_all_mycustom = (TextView) findViewById(R.id.tv_all_mycustom);
        tv_all_mycustom.setOnClickListener(this);
        tv_all_mycard = (TextView) findViewById(R.id.tv_all_mycard);
        tv_all_mycard.setOnClickListener(this);
        tv_all_mycoupon = (TextView) findViewById(R.id.tv_all_mycoupon);
        tv_all_mycoupon.setOnClickListener(this);
        tv_all_mylike = (TextView) findViewById(R.id.tv_all_mylike);
        tv_all_mylike.setOnClickListener(this);
        tv_zhuanpoint = (TextView) findViewById(R.id.tv_zhuanpoint);
        tv_zhuanpoint.setOnClickListener(this);
        tv_myinfo = (TextView) findViewById(R.id.tv_myinfo);
        tv_myinfo.setOnClickListener(this);
        tv_all_sale = (TextView) findViewById(R.id.tv_all_sale);
        tv_all_sale.setOnClickListener(this);
        tv_messageset = (TextView) findViewById(R.id.tv_messageset);
        tv_messageset.setOnClickListener(this);
        tv_all_about = (TextView) findViewById(R.id.tv_all_about);
        tv_all_about.setOnClickListener(this);
        tv_all_appinfo = (TextView) findViewById(R.id.tv_all_appinfo);
        tv_all_appinfo.setOnClickListener(this);
        tv_all_feed = (TextView) findViewById(R.id.tv_all_feed);
        tv_all_feed.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_all_search_shop:
                intent.setClass(AllHomeAty.this, SearchStoreAty.class);
                startActivity(intent);

                break;
            case R.id.tv_all_search_service:
                intent.setClass(AllHomeAty.this, SeekServiceActivitynew.class);
                startActivity(intent);
                break;

            case R.id.tv_all_search_stroe:
                intent.setClass(AllHomeAty.this, LookStoreAty.class);
                startActivity(intent);
                break;
            case R.id.tv_all_arount:
                intent.setClass(AllHomeAty.this, ArountActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_all_point:
                intent.setClass(AllHomeAty.this, SaleActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_all_coupon:
                intent.setClass(AllHomeAty.this, CouponHomeHuaAty.class);
                startActivity(intent);
                break;
            case R.id.tv_all_aty:
                intent.setClass(AllHomeAty.this, AtyHomeAty.class);
                startActivity(intent);
                break;
            case R.id.tv_all_custom:
                intent.setClass(AllHomeAty.this, CustomContentAty.class);
                startActivity(intent);
                break;
            case R.id.tv_all_message:
                //用户登录状态，登录成功未true ,默认未false
                String isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(isusertype)) {
                    intent.setClass(AllHomeAty.this, MyMessageActivity.class);
                    startActivity(intent);
                } else {
                    intent.putExtra("tag", "AllHome");
                    intent.setClass(AllHomeAty.this, Loginaty.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_all_history:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(AllHomeAty.this, MyCostHistoryActivity.class);
                    startActivity(intent);
                } else {
                    intent.putExtra("tag", "AllHome");
                    intent.setClass(AllHomeAty.this, Loginaty.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_all_mypoint:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(AllHomeAty.this, MyJifenActivity.class);
                    startActivity(intent);
                } else {
                    intent.putExtra("tag", "AllHome");
                    intent.setClass(AllHomeAty.this, Loginaty.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_all_mygift:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(AllHomeAty.this, MyGiftWalletActivity.class);
                    startActivity(intent);
                } else {
                    intent.putExtra("tag", "AllHome");
                    intent.setClass(AllHomeAty.this, Loginaty.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_all_mycustom:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(AllHomeAty.this, MyCustiomGSListActivity.class);
                    startActivity(intent);
                } else {
                    intent.putExtra("tag", "AllHome");
                    intent.setClass(AllHomeAty.this, Loginaty.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_all_mycard:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(AllHomeAty.this, MyCardActivity.class);
                    startActivity(intent);
                } else {
                    intent.putExtra("tag", "AllHome");
                    intent.setClass(AllHomeAty.this, Loginaty.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_all_mycoupon:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(AllHomeAty.this, MyCouponActivity.class);
                    startActivity(intent);
                } else {
                    intent.putExtra("tag", "AllHome");
                    intent.setClass(AllHomeAty.this, Loginaty.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_all_mylike:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(AllHomeAty.this, MyLikeActivity.class);
                    startActivity(intent);
                } else {
                    intent.putExtra("tag", "AllHome");
                    intent.setClass(AllHomeAty.this, Loginaty.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_zhuanpoint:
//                //用户登录状态，登录成功未true ,默认未false
//                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
//                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(AllHomeAty.this, PointsActivity.class);
                    startActivity(intent);
//                } else {
//                    intent.putExtra("tag", "AllHome");
//                    intent.setClass(AllHomeAty.this, Loginaty.class);
//                    startActivity(intent);
//                }
                break;
            case R.id.tv_myinfo:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(AllHomeAty.this, ModifUserInfoAty.class);
                    startActivity(intent);
                } else {
                    intent.putExtra("tag", "AllHome");
                    intent.setClass(AllHomeAty.this, Loginaty.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_all_sale:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(AllHomeAty.this, AccountSafetyAty.class);
                    startActivity(intent);
                } else {
                    intent.putExtra("tag", "AllHome");
                    intent.setClass(AllHomeAty.this, Loginaty.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_messageset:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(AllHomeAty.this, MessageSetAty.class);
                    startActivity(intent);
                } else {
                    intent.putExtra("tag", "AllHome");
                    intent.setClass(AllHomeAty.this, Loginaty.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_all_about:
                intent.setClass(AllHomeAty.this, AboutDigoAty.class);
                startActivity(intent);
                break;
            case R.id.tv_all_appinfo:
                intent.setClass(AllHomeAty.this, AppIntroduceActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_all_feed:
                intent.setClass(AllHomeAty.this, ComplaintFeedback.class);
                startActivity(intent);
                break;
        }
    }
}
