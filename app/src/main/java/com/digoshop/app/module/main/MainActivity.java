package com.digoshop.app.module.main;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.digoshop.R;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseFragmentActivity;
import com.digoshop.app.base.BaseInfo;
import com.digoshop.app.module.arrountshops.ArountShopsNewFragment;
import com.digoshop.app.module.home.HomeFragment;
import com.digoshop.app.module.userCenter.UserCenterFragment;
import com.digoshop.app.module.userCenter.module.MyShopListFragment;
import com.digoshop.app.module.userCenter.module.PointFragment;
import com.digoshop.app.module.welcome.module.DeviceInfo;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.Constants;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;

import org.json.JSONException;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * TODO这个类是主页的activity
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016-5-31 下午9:52:01
 * @version: V1.0
 */

public class MainActivity extends BaseFragmentActivity {
    private HomeFragment mHomeFragment;
    private ArountShopsNewFragment mArountShopsFragment;
    private MyShopListFragment myShopListFragment;
    private PointFragment mpointFragment;
    private UserCenterFragment mUserCenterFragment;
    private FragmentManager mFragmentManager;
    private RadioGroup mRadioGroup;

    /**
     * 保存当前显示的是第几页
     */
    private int currentPage = R.id.tab_shouye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        if (null != savedInstanceState) {
            currentPage = savedInstanceState.getInt("neo");
        }

        mFragmentManager = getSupportFragmentManager();
        mHomeFragment = (HomeFragment) mFragmentManager.findFragmentByTag("home");
        mArountShopsFragment = (ArountShopsNewFragment) mFragmentManager.findFragmentByTag("arount");
        myShopListFragment = (MyShopListFragment) mFragmentManager.findFragmentByTag("myshop");
        mpointFragment = (PointFragment) mFragmentManager.findFragmentByTag("point");
        mUserCenterFragment = (UserCenterFragment) mFragmentManager.findFragmentByTag("user");

        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        changeFragment(R.id.tab_shouye);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                currentPage = checkedId;
                changeFragment(checkedId);

            }
        });

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
    int screenWidth;
    int screenHeight;
    @Override
    protected void onResume() {
        super.onResume();
        //判断是否注册设备
        if (TextUtils.isEmpty(LocalSave.getValue(MainActivity.this, AppConfig.basedevicefile, "devieuid", ""))) {
            //未登录则去注册设备
            if (getNetWifi()) {
                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                screenWidth = screenWidth = display.getWidth();
                screenHeight = screenHeight = display.getHeight();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        onRegister_By_Device();
                    }
                }).start();

            }
        }
    }
    // 设备信息注册
    private void onRegister_By_Device() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setChannel(Constants.CHANNEL);
        deviceInfo.setApp_version(Tool.getAppVersionName(MainActivity.this, "name"));
        deviceInfo.setType(AppConfig.phonetype);
        deviceInfo.setOs(Tool.getPhoneSystemVersion());
        //deviceInfo.setDid(Tool.getIMEI(WelcomeActivity.this));
        deviceInfo.setDid("");
        deviceInfo.setBrand(Tool.getPhoneModel());
        deviceInfo.setModel(Tool.getPhoneModel());
        deviceInfo.setSw(screenWidth);
        deviceInfo.setSh(screenHeight);
        try {
            BaseInfo baseInfo = api.register_by_device(AppConfig.ipstr, deviceInfo);
            if (baseInfo != null) {
                LocalSave.putValue(MainActivity.this, AppConfig.basedevicefile, "devicetoken", baseInfo.getToken());
                LocalSave.putValue(MainActivity.this, AppConfig.basedevicefile, "devieuid", baseInfo.getUid());
                String getduid = LocalSave.getValue(this, AppConfig.basedevicefile, "devieuid", "");
                String getdtoken = LocalSave.getValue(this, AppConfig.basedevicefile, "devicetoken", "");
                Log.v("lsq", "baseInfo+" + getduid);
                Log.v("lsq", "baseInfo+" + getdtoken);
            } else {
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (WSError e) {
            e.printStackTrace();
        }
    }
    public void hideAllFragment(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mArountShopsFragment != null) {
            transaction.hide(mArountShopsFragment);
        }
        if (myShopListFragment != null) {
            transaction.hide(myShopListFragment);
        }
        if (mpointFragment != null) {
            transaction.hide(mpointFragment);
        }

        if (mUserCenterFragment != null) {
            transaction.hide(mUserCenterFragment);
        }
    }

    public void changeFragment(int checkedId) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideAllFragment(transaction);
        switch (checkedId) {
            case R.id.tab_shouye:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();

                    transaction.add(R.id.main_content, mHomeFragment, "home");
                } else {
                    transaction.show(mHomeFragment);
                }
                break;
            case R.id.tab_zhoubian:
                if (mArountShopsFragment == null) {
                    mArountShopsFragment = new ArountShopsNewFragment();
                    transaction.add(R.id.main_content, mArountShopsFragment, "arount");
                } else {
                    transaction.show(mArountShopsFragment);
                }
                break;
            case R.id.tab_dingzhi:

                if (myShopListFragment == null) {
                    myShopListFragment = new MyShopListFragment();
                    transaction.add(R.id.main_content, myShopListFragment, "myshop");
                } else {
                    transaction.show(myShopListFragment);
                }

                break;
            case R.id.tab_qiandao:
                if (mpointFragment == null) {
                    mpointFragment = new PointFragment();
                    transaction.add(R.id.main_content, mpointFragment, "point");
                } else {
                    transaction.show(mpointFragment);
                }
                break;
            case R.id.tab_geren:
                if (mUserCenterFragment == null) {
                    mUserCenterFragment = new UserCenterFragment();
                    transaction.add(R.id.main_content, mUserCenterFragment, "user");
                } else {
                    transaction.show(mUserCenterFragment);
                }
                break;
        }
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("neo", currentPage);
        super.onSaveInstanceState(outState);
    }

}
