package com.digoshop.app.module.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.digoshop.R;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.login.ModifPasswodrAty;
import com.digoshop.app.module.setting.util.DataCleanUtils;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * TODO 用户中心-设置-用户安全
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016-6-6 下午11:17:00
 * @version: V1.0
 */
public class AccountSafetyAty extends BaseActivity {


    @Bind(R.id.tv_username)
    TextView tvUsername;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    String moblile = null;//用户手机号
    Intent intent = new Intent();
    @Bind(R.id.tv_cache_num)
    TextView tvCacheNum;


    private String cacheSize;
    private int MOBLIETYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountsafetyaty);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        TextView    tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("账户与安全");
        cacheSize = DataCleanUtils.getTotalCacheSize(this);
        if (cacheSize.equals("")) {
            tvCacheNum.setText("0KB");
        } else {
            tvCacheNum.setText(cacheSize);
        }
        //登录注册返回的用户名
        String usernamestr = LocalSave.getValue(this, AppConfig.basefile, "login_user_account", "");//登录账号
        String userAccount =null;
        if(!TextUtils.isEmpty(usernamestr)){
            userAccount = usernamestr;
        }
//        //第三方登录返回的用户名
//        String umName = LocalSave.getValue(this, AppConfig.basefile, "login_um", "");//友盟登录昵称
//        if (TextUtils.isEmpty(userAccount)) {
//            if(!TextUtils.isEmpty(umName)){
//                userAccount = umName;
//            }
//
//        }
        //登录注册返回的手机号
        String user_moblile = LocalSave.getValue(this, AppConfig.basefile, "login_mobile", "");
        //手机快捷登录输入的手机号
        String sms_moblile = LocalSave.getValue(this, AppConfig.basefile, "login_sms", "");
        if (!TextUtils.isEmpty(userAccount)) {
            moblile = user_moblile;
            MOBLIETYPE = 2;
        } else if (!TextUtils.isEmpty(sms_moblile)) {
            moblile = sms_moblile;
            MOBLIETYPE = 2;
        }
        if ("".equals(userAccount)) {
            userAccount = "无";
        }
        if("".equals(user_moblile)&&"".equals(sms_moblile)){
            moblile = "无";
            MOBLIETYPE = 1;
        }
        if (!TextUtils.isEmpty(moblile) && moblile.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < moblile.length(); i++) {
                char c = moblile.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            tvPhone.setText(sb.toString());
        }else{
            tvPhone.setText(moblile);
        }
        tvUsername.setText(userAccount);
    }


    //绑定点击事件
    @OnClick({ R.id.rl_phone, R.id.rl_password, R.id.rl_cache})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.rl_phone:
                Log.i("zzrttt",MOBLIETYPE+"");
                Bundle bundle = new Bundle();
                bundle.putInt("type", MOBLIETYPE);
                if (MOBLIETYPE == 1) {
                    startActivity(ChangePhoneActivity.class, bundle);
                } else {
                    startActivity(MyPhoneActivity.class, bundle);
                }

                break;
            case R.id.rl_password:
                intent.setClass(AccountSafetyAty.this, ModifPasswodrAty.class);
                intent.putExtra("mobile", moblile);
                intent.putExtra("typecode", 2);
                startActivity(intent);

                //startActivity(ModifPasswodrAty.class);
                break;
            case R.id.rl_cache:
                DataCleanUtils.clearAllCache(this);
                Toast.makeText(this, "清除缓存成功", Toast.LENGTH_SHORT).show();
                tvCacheNum.setText("0KB");

                break;

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}