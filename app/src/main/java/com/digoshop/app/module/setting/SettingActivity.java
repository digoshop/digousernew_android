package com.digoshop.app.module.setting;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.login.Loginaty;
import com.digoshop.app.module.main.MainActivity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.DensityUtil;
import com.digoshop.app.utils.LocalSave;
import com.hss01248.dialog.StyledDialog;

import static com.digoshop.app.utils.LocalSave.getValue;

/**
 * TODO 用户中心-设置
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016-6-6 下午11:15:53
 * @version: V1.0
 */
public class SettingActivity extends BaseActivity implements OnClickListener {
    private RelativeLayout re_user_icon, re_account_safety, re_message_set,
            re_about_digo, re_exit_icon;
    private static final String EXITACTION = "action.exit";
    private static final String EXITACTION2 = "action.exitfragment";

    @Override
    public boolean isNoTitle() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("设置");
        re_user_icon = (RelativeLayout) findViewById(R.id.re_user_icon);
        re_account_safety = (RelativeLayout) findViewById(R.id.re_account_safety);
        re_message_set = (RelativeLayout) findViewById(R.id.re_message_set);
        re_about_digo = (RelativeLayout) findViewById(R.id.re_about_digo);
        re_exit_icon = (RelativeLayout) findViewById(R.id.re_exit_icon);
        re_user_icon.setOnClickListener(this);
        re_account_safety.setOnClickListener(this);
        re_message_set.setOnClickListener(this);
        re_about_digo.setOnClickListener(this);
        re_exit_icon.setOnClickListener(this);
    }

    protected void onResume() {
        super.onResume();
        //App.getInstance().showToast(LocalSave.getValue(SettingActivity.this, AppConfig.basefile, "userid", ""));
//        if (TextUtils.isEmpty(LocalSave.getValue(SettingActivity.this, AppConfig.basefile, "userid", ""))) {
//            //App.getInstance().showToast("用户id丢失!");
//            finish();
//        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.re_user_icon:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(SettingActivity.this, ModifUserInfoAty.class);
                    startActivity(intent);
                } else {
                    intent.putExtra("tag", "Setting");
                    intent.setClass(SettingActivity.this, Loginaty.class);
                    startActivity(intent);
                }

                break;
            case R.id.re_account_safety:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(SettingActivity.this, AccountSafetyAty.class);
                    startActivity(intent);
                } else {
                    intent.putExtra("tag", "Setting");
                    intent.setClass(SettingActivity.this, Loginaty.class);
                    startActivity(intent);
                }


                break;
            case R.id.re_message_set:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(SettingActivity.this, MessageSetAty.class);
                    startActivity(intent);
                } else {
                    intent.putExtra("tag", "Setting");
                    intent.setClass(SettingActivity.this, Loginaty.class);
                    startActivity(intent);
                }

                break;
            case R.id.re_about_digo:
                intent.setClass(SettingActivity.this, AboutDigoAty.class);
                startActivity(intent);
                break;
            case R.id.re_exit_icon:
                getPopupWindowQuyu();
                popupWindowquyu.showAtLocation(v, Gravity.CENTER, 0, 0);
                break;
            case R.id.re_exit_user:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    String CityCodestr = LocalSave.getValue(SettingActivity.this, AppConfig.basefile, "CityCode", "");
                    StyledDialog.buildLoading(SettingActivity.this, App.getInstance().getString(R.string.onloading), true, false).show();
                    LocalSave.getEditor(SettingActivity.this, AppConfig.basefile).clear().commit();
                    SettingActivity.this.finish();
                    intent.setClass(SettingActivity.this, MainActivity.class);
                    LocalSave.putValue(SettingActivity.this, AppConfig.basefile, "CityCode", CityCodestr);
                    String getduid = getValue(this, AppConfig.basedevicefile, "devieuid", "");
                    String getdtoken = getValue(this, AppConfig.basedevicefile, "devicetoken", "");
                    AppConfig.userid = getduid;
                    AppConfig.usertoken = getdtoken;
                    //保存设备的token到用户的token
                    LocalSave.putValue(this, AppConfig.basefile, "usertoken", getdtoken);
                    //保存设备的uid到用户uid
                    LocalSave.putValue(this, AppConfig.basefile, "userid", getduid);
                    //用户登录状态，登录成功为true ,退出为false，默认未false
                    LocalSave.putValue(SettingActivity.this, AppConfig.basedevicefile, "userlogintype", "false");
                    startActivity(intent);
                    StyledDialog.dismiss();
                    finish();
                } else {
                    App.getInstance().showToast("请先登录！");
                }

                break;
            case R.id.re_exit_app:


                StyledDialog.buildLoading(SettingActivity.this, App.getInstance().getString(R.string.onloading), true, false).show();

                LocalSave.getEditor(SettingActivity.this, AppConfig.basefile).clear().commit();
                String getduid = getValue(this, AppConfig.basedevicefile, "devieuid", "");
                String getdtoken = getValue(this, AppConfig.basedevicefile, "devicetoken", "");
                //保存设备的token到用户的token
                LocalSave.putValue(this, AppConfig.basefile, "usertoken", getdtoken);
                //保存设备的uid到用户uid
                LocalSave.putValue(this, AppConfig.basefile, "userid", getduid);
                //用户登录状态，登录成功为true ,退出为false，默认未false
                LocalSave.putValue(SettingActivity.this, AppConfig.basedevicefile, "userlogintype", "false");

                Intent intentu = new Intent();
                intentu.setAction(EXITACTION2);
                sendBroadcast(intentu);
                Intent intentv = new Intent();
                intentv.setAction(EXITACTION);
                sendBroadcast(intentv);

                // int currentVersion = android.os.Build.VERSION.SDK_INT;
                // if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
                // Intent startMain = new Intent(Intent.ACTION_MAIN);
                // startMain.addCategory(Intent.CATEGORY_HOME);
                // startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // startActivity(startMain);
                // System.exit(0);
                // } else {// android2.1
                // ActivityManager am = (ActivityManager)
                // getSystemService(ACTIVITY_SERVICE);
                // am.restartPackage(getPackageName());
                // }
                break;
            default:
                break;
        }

    }

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
        View popupWindow_view = getLayoutInflater().inflate(R.layout.exitdialogaty, null,
                false);
        LinearLayout lin_bgexit = (LinearLayout) popupWindow_view.findViewById(R.id.lin_bgexit);
        LinearLayout re_exit_user = (LinearLayout) popupWindow_view.findViewById(R.id.re_exit_user);
        LinearLayout re_exit_app = (LinearLayout) popupWindow_view.findViewById(R.id.re_exit_app);
        lin_bgexit.getBackground().setAlpha(200);
        re_exit_user.setOnClickListener(this);
        re_exit_app.setOnClickListener(this);
        popupWindowquyu = new PopupWindow(popupWindow_view,
                DensityUtil.floatToInt((float) getResources().getDimensionPixelSize(R.dimen.base_dimen_600)), DensityUtil.floatToInt((float) getResources().getDimensionPixelSize(R.dimen.base_dimen_210)), true);
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

    @Override
    protected void onStop() {
        super.onStop();
        StyledDialog.dismissLoading();

    }
}
