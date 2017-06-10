package com.digoshop.app.module.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.base.BaseInfo;
import com.digoshop.app.module.main.MainActivity;
import com.digoshop.app.module.userCenter.model.UserDataBean;
import com.digoshop.app.module.welcome.module.DeviceInfo;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.Constants;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class Loginaty extends BaseActivity implements android.view.View.OnClickListener {
    private Button get_autncode_btn, login_btn, regisever_tv_to, forgetpassword_btn;
    private ImageView ivlogin_qq, ivlogin_weixin, ivlogin_sina;
    private BroadcastReceiver smsReceiver;
    private IntentFilter filter2;
    private String strContent;
    private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";
    private EditText edit_shortcut_num;// 手机快捷登录账号
    private EditText edit_shortcut_password;// 快捷登录 密码

    private String edit_shortcut_numstr;// 手机快捷登录账号
    private String edit_shortcut_passwordstr;// 快捷登录 密码
    private int typeint = 1;// 1为登录 2为手机快捷登录
    // 第三方登陆
    private int thrid_type = 0;// 1微信，2QQ，3微博
    private boolean isOnclickThird = false;// 未点击
    private String uMBackName;

    @Override
    public boolean isNoTitle() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    private LinearLayout linthid_login;

    @Override
    protected void onResume() {
        super.onResume();
        isOnclickThird = false;
        StyledDialog.dismissLoading();
    }

    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginaty);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText(R.string.vip_login);
        Intent intent = getIntent();
        tag = intent.getStringExtra("tag");
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = screenWidth = display.getWidth();
        screenHeight = screenHeight = display.getHeight();

        linthid_login = (LinearLayout) findViewById(R.id.linthid_login);
        edit_shortcut_num = (EditText) findViewById(R.id.edit_shortcut_num);
        edit_shortcut_password = (EditText) findViewById(R.id.edit_shortcut_password);
        login_btn = (Button) findViewById(R.id.login_btn);
        regisever_tv_to = (Button) findViewById(R.id.regisever_tv_to);
        ivlogin_qq = (ImageView) findViewById(R.id.ivlogin_qq);
        ivlogin_weixin = (ImageView) findViewById(R.id.ivlogin_weixin);
        ivlogin_sina = (ImageView) findViewById(R.id.ivlogin_sina);
        get_autncode_btn = (Button) findViewById(R.id.get_autncode_btn);
        forgetpassword_btn = (Button) findViewById(R.id.forgetpassword_btn);
        forgetpassword_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        regisever_tv_to.setOnClickListener(this);
        ivlogin_qq.setOnClickListener(this);
        ivlogin_weixin.setOnClickListener(this);
        ivlogin_sina.setOnClickListener(this);
        filter2 = new IntentFilter();
        filter2.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter2.setPriority(Integer.MAX_VALUE);
        smsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Object[] objs = (Object[]) intent.getExtras().get("pdus");
                for (Object obj : objs) {
                    byte[] pdu = (byte[]) obj;
                    SmsMessage sms = SmsMessage.createFromPdu(pdu);
                    Log.i("zzr", sms + "");
                    // 短信的内容
                    String message = sms.getMessageBody();
                    Log.d("logo", "message     " + message);
                    // 短息的手机号。。+86开头？
                    String from = sms.getOriginatingAddress();
                    Log.d("logo", "from     " + from);
                    if (!TextUtils.isEmpty(from)) {
                        String code = patternCode(message);
                        if (!TextUtils.isEmpty(code)) {
                            strContent = code;
                            handler.sendEmptyMessage(1);
                        }
                    }
                }
            }
        };
        registerReceiver(smsReceiver, filter2);


        //密码edit监听
        edit_shortcut_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edit_shortcut_numstr = edit_shortcut_num.getText().toString();

                if (hasFocus) {
                    if (!TextUtils.isEmpty(edit_shortcut_numstr)) {
                        login_btn.setBackgroundResource(R.drawable.bigbtn_on);
                    } else {
                        login_btn.setBackgroundResource(R.drawable.bigbtn_off);
                    }
                } else {

                }
            }
        });

    }

    Handler handler = new Handler() {
        Intent intent = new Intent();

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(Loginaty.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(Loginaty.this, "密码错误", Toast.LENGTH_SHORT).show();
                    StyledDialog.dismissLoading();
                    break;
                case 3:
                    App.getInstance().showToast("解析异常！");
                    break;
                case 4:
                    Toast.makeText(Loginaty.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    StyledDialog.dismissLoading();
                    if (!TextUtils.isEmpty(tag)) {
                        if ("CouponDetail".equals(tag)) {
                            finish();
                        } else if ("SaleActivity".equals(tag)) {
                            finish();
                        } else {
                            finish();
                        }
                    } else {
//                        intent.setClass(Loginaty.this, MainActivity.class);
//                        startActivity(intent);

                        finish();
                    }

                    break;
                case 6:
                    Toast.makeText(Loginaty.this, "登录失败", Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    intent.setClass(Loginaty.this, MainActivity.class);
                    startActivity(intent);
                    StyledDialog.dismissLoading();

                    finish();
                    break;
                case 8:
                    Toast.makeText(Loginaty.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    StyledDialog.dismissLoading();

                    break;
                case 9:
                    Toast.makeText(Loginaty.this, "获取用户信息失败！", Toast.LENGTH_SHORT).show();
                    StyledDialog.dismissLoading();

                    break;
                case 20:
                    typeint = 1;
                    if (loginTest(typeint)) {
                        if (typeint == 1) {
                            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    login();
                                }
                            }).start();
                        }

                    }
                    break;
                case 21:
                    App.getInstance().showToast("设备注册失败");

                    break;
                case 11:
                    Log.v("lsq", "!!!!!!!!!!!!!!!!!!!!!!!1");
                    StyledDialog.dismissLoading();
                    break;
                case 91:
                    App.getInstance().showToast("您输入的用户名不符合规则");
                    StyledDialog.dismissLoading();
                    break;
                case 92:
                    App.getInstance().showToast("短信验证码错误");
                    StyledDialog.dismissLoading();
                    break;
                case 93:
                    App.getInstance().showToast("用户不存在！");
                    StyledDialog.dismissLoading();
                    break;
                case 94:
                    App.getInstance().showToast("用户名或密码错误");
                    StyledDialog.dismissLoading();
                    break;
                case 95:
                    App.getInstance().showToast("账户已存在");
                    StyledDialog.dismissLoading();
                    break;
                case 96:
                    App.getInstance().showToast("用户名或密码错误");
                    StyledDialog.dismissLoading();
                    break;
                case 97:
                    App.getInstance().showToast("注册失败");
                    StyledDialog.dismissLoading();
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        edit_shortcut_numstr = edit_shortcut_num.getText().toString();
        edit_shortcut_passwordstr = edit_shortcut_password.getText().toString();
//		edit_phone_numstr = edit_phone_num.getText().toString();// 账户
//		edit_phone_autncodestr = edit_phone_autncode.getText().toString();
        switch (v.getId()) {
            case R.id.regisever_tv_to:// 注册
                intent.setClass(Loginaty.this, Regeteraty.class);
                startActivity(intent);
                break;

            case R.id.login_btn:// 登录按钮// 登录
                if (getNetWifi()) {
                    if (TextUtils.isEmpty(LocalSave.getValue(this, AppConfig.basedevicefile, "devieuid", ""))) {
                        regisverDevier();
                    } else {
                        handler.sendEmptyMessage(20);
                    }
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }

                break;


            case R.id.forgetpassword_btn:// 忘记密码
                intent.setClass(Loginaty.this, ForgetPasswordAty.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void regisverDevier() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                onRegister_By_Device();
            }
        }).start();
    }

    int screenWidth;
    int screenHeight;

    // 设备信息注册
    private void onRegister_By_Device() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setChannel(Constants.CHANNEL);
        deviceInfo.setApp_version(Tool.getAppVersionName(Loginaty.this, "name"));
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
                LocalSave.putValue(Loginaty.this, AppConfig.basedevicefile, "devicetoken", baseInfo.getToken());
                LocalSave.putValue(Loginaty.this, AppConfig.basedevicefile, "devieuid", baseInfo.getUid());
                String getduid = LocalSave.getValue(this, AppConfig.basedevicefile, "devieuid", "");
                String getdtoken = LocalSave.getValue(this, AppConfig.basedevicefile, "devicetoken", "");

                Log.v("lsq", "baseInfo+" + getdtoken);
                handler.sendEmptyMessage(20);
            } else {
                handler.sendEmptyMessage(21);
            }

        } catch (JSONException e) {

            e.printStackTrace();
            handler.sendEmptyMessage(21);
        } catch (WSError e) {

            e.printStackTrace();
            handler.sendEmptyMessage(21);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnclickThird = false;
    }

    private boolean loginTest(int i) {
        if (i == 1) {// 登录验证
            if (TextUtils.isEmpty(edit_shortcut_numstr)) {
                Toast.makeText(Loginaty.this, "账号不能为空!", Toast.LENGTH_SHORT).show();
                return false;
            } else if (TextUtils.isEmpty(edit_shortcut_passwordstr) || edit_shortcut_passwordstr.length() < 6) {
                Toast.makeText(Loginaty.this, "密码输入有误!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    // 用户登录
    private void login() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        UserDataBean userInfo = new UserDataBean();
        userInfo.setDeviceuid(LocalSave.getValue(this, AppConfig.basedevicefile, "devieuid", ""));
        userInfo.setDevicetoken(LocalSave.getValue(this, AppConfig.basedevicefile, "devicetoken", ""));
        userInfo.setLogin_name(edit_shortcut_numstr);
        userInfo.setPassword(edit_shortcut_passwordstr);
        String getduid = LocalSave.getValue(this, AppConfig.basedevicefile, "devieuid", "");
        String getdtoken = LocalSave.getValue(this, AppConfig.basedevicefile, "devicetoken", "");
        Log.v("lsq", "baseInfo+" + getduid);
        Log.v("lsq", "baseInfo+" + getdtoken);
        Log.v("lsq", "deviceuid+" + userInfo.getDeviceuid());
        Log.v("lsq", "devicetoken+" + userInfo.getDevicetoken());
        try {
            UserDataBean returnuserInfo = api.login(userInfo);
            if (returnuserInfo != null) {

                String cookie = returnuserInfo.getCookie();
                if (cookie == null) {
                    handler.sendEmptyMessage(6);
                    return;
                }
                JSONObject cookiejson = new JSONObject(cookie);
                AppConfig.userid = cookiejson.getString("uid");
                AppConfig.usertoken = cookiejson.getString("token");
                //保存登录的手机号
                LocalSave.putValue(this, AppConfig.basefile, "login_mobile", returnuserInfo.getMobile());
                //保存用户名密码登录的用户名
                LocalSave.putValue(this, AppConfig.basefile, "login_user_account", returnuserInfo.getLgn());
                //保存登录的用户token
                LocalSave.putValue(this, AppConfig.basefile, "usertoken", cookiejson.getString("token"));
                //保存登录的用户uid
                LocalSave.putValue(this, AppConfig.basefile, "userid", cookiejson.getString("uid"));
                Log.v("lsq", "logintoken+" + AppConfig.usertoken);
                Log.v("lsq", "loginuid+" + AppConfig.userid);
                //用户登录状态，登录成功未true ,默认未false
                LocalSave.putValue(Loginaty.this, AppConfig.basedevicefile, "userlogintype", "true");
                String getduidtype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                Log.v("lsq", "getduidtype+" + getduidtype);
                handler.sendEmptyMessage(5);
            } else {
                handler.sendEmptyMessage(6);
            }
        } catch (JSONException e) {
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        } catch (WSError e) {

            String code = e.getMessage();
            if (code == null) {
                handler.sendEmptyMessage(6);
                return;
            }
            if (code.equals("-99")) {
                //您输入的用户名不符合规则
                handler.sendEmptyMessage(91);
            }
            //短信验证码错误
            if (code.equals("-101")) {
                handler.sendEmptyMessage(92);

            }
            //用户不存在！
            if (code.equals("-109")) {
                handler.sendEmptyMessage(93);

            }
            if (code.equals("-110")) {
                //用户名或密码错误
                handler.sendEmptyMessage(94);

            }
            if (code.equals("-112")) {
                //账户已存在
                handler.sendEmptyMessage(95);

            }
            if (code.equals("-113")) {
                //用户名或密码错误
                handler.sendEmptyMessage(96);

            }

            if (code.equals("800")) {
                //注册失败
                handler.sendEmptyMessage(97);

            }
            e.printStackTrace();
        }
    }

    // 第三方快捷登录
    private void login_third(final String access_token, final String uid) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                DigoIUserApiImpl api = new DigoIUserApiImpl();
                try {

                    String getduid = LocalSave.getValue(Loginaty.this, AppConfig.basedevicefile, "devieuid", "");
                    String getdtoken = LocalSave.getValue(Loginaty.this, AppConfig.basedevicefile, "devicetoken", "");
                    Log.v("lsq", "getduid3+" + getduid);
                    Log.v("lsq", "getdtoken3+" + getdtoken);
                    UserDataBean thirduserdatabean = api.login_third(access_token, thrid_type + "", uid, Tool.getAppVersionName(Loginaty.this, "name"), getdtoken, getduid);

                    if (thirduserdatabean != null) {

                        AppConfig.userid = thirduserdatabean.getUid();
                        AppConfig.usertoken = thirduserdatabean.getToken();
                        //保存登录的用户token
                        LocalSave.putValue(getApplication(), AppConfig.basefile, "usertoken", thirduserdatabean.getToken());
                        //保存登录的用户uid
                        LocalSave.putValue(getApplication(), AppConfig.basefile, "userid", thirduserdatabean.getUid());
                        Log.i("lsq", " userInfo2.getUi()+" + thirduserdatabean.getUid());
                        handler.sendEmptyMessage(5);
                    } else {
                        handler.sendEmptyMessage(6);
                    }
                } catch (JSONException e) {
                    handler.sendEmptyMessage(3);
                    e.printStackTrace();
                } catch (WSError e) {
                    if (e.getMessage().equals("-133")) {
                    }
                    handler.sendEmptyMessage(9);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
    }

    /**
     * 匹配短信中间的6个数字（验证码等）
     *
     * @param patternContent
     * @return
     */
    private String patternCode(String patternContent) {
        if (TextUtils.isEmpty(patternContent)) {
            return null;
        }
        Pattern p = Pattern.compile(patternCoder);
        Matcher matcher = p.matcher(patternContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        StyledDialog.dismissLoading();
        Log.v("UMAuthListener", "77777");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (!TextUtils.isEmpty(tag)) {
                Loginaty.this.finish();
            } else {
                Loginaty.this.finish();
                Intent intent = new Intent();
                intent.setClass(Loginaty.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
