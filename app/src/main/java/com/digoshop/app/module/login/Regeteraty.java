package com.digoshop.app.module.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class Regeteraty extends BaseActivity implements OnClickListener {
    private Button register_btn, get_autncode_btn;
    private TimeCount time;
    private EditText edit_phone_num;// 手机号
    private EditText edit_phone_autncode;// 手机验证码
    private EditText edit_username;// 用户名
    private EditText edit_shortcut_password;// 密码
    private BroadcastReceiver smsReceiver;
    private IntentFilter filter2;
    private String strContent;
    private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";
    String phonenumstr;
    String edit_phone_autncodestr;
    String edit_usernamestr;
    String edit_shortcut_passwordstr;
    private String CityCodestr = "";

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
        setContentView(R.layout.regeteraty);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("会员注册");
        CityCodestr = LocalSave.getValue(this, AppConfig.basefile, "CityCode", "");

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = screenWidth = display.getWidth();
        screenHeight = screenHeight = display.getHeight();
        time = new TimeCount(60000, 1000);
        edit_phone_num = (EditText) findViewById(R.id.edit_phone_num);
        edit_phone_autncode = (EditText) findViewById(R.id.edit_phone_autncode);
        edit_username = (EditText) findViewById(R.id.edit_username);
        edit_shortcut_password = (EditText) findViewById(R.id.edit_shortcut_password);
        register_btn = (Button) findViewById(R.id.register_btn);
        get_autncode_btn = (Button) findViewById(R.id.get_autncode_btn);
        register_btn.setOnClickListener(this);
        get_autncode_btn.setOnClickListener(this);

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
                    // 短信的内容
                    String message = sms.getMessageBody();
                    Log.d("ceshi", "message     " + message);
                    // 短息的手机号。。+86开头？
                    String from = sms.getOriginatingAddress();
                    Log.d("ceshi", "from     " + from);
                    if (!TextUtils.isEmpty(from)) {
                        String code = patternCode(message);
                        if (!TextUtils.isEmpty(code)) {
                            strContent = code;
                            handler.sendEmptyMessage(3);
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
                phonenumstr = edit_phone_num.getText().toString();
                edit_phone_autncodestr = edit_phone_autncode.getText().toString();
                edit_usernamestr = edit_username.getText().toString();
                if (hasFocus) {
                    if (!TextUtils.isEmpty(phonenumstr) && !TextUtils.isEmpty(edit_phone_autncodestr) && !TextUtils.isEmpty(edit_usernamestr)) {

                        register_btn.setBackgroundResource(R.drawable.bigbtn_on);
                    } else {
                        register_btn.setBackgroundResource(R.drawable.bigbtn_off);
                    }
                } else {

                }
            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    StyledDialog.dismissLoading();
                    Toast.makeText(Regeteraty.this, "验证码已发送.",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    StyledDialog.dismissLoading();

                    Toast.makeText(Regeteraty.this, "验证码错误", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 3:
                    StyledDialog.dismissLoading();

                    edit_phone_autncode.setText(strContent);
                    break;
                case 4:
                    get_autncode_btn.setEnabled(true);

                    StyledDialog.dismissLoading();

                    Toast.makeText(Regeteraty.this, "验证码发送失败.",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    StyledDialog.dismissLoading();

                    Toast.makeText(Regeteraty.this, "您已成功注册成为迪购会员", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(Regeteraty.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 6:
                    StyledDialog.dismissLoading();

                    Toast.makeText(Regeteraty.this, "您输入的手机已注册成为会员，请直接登录或重新输入其他号码！", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 7:
                    StyledDialog.dismissLoading();

                    Toast.makeText(Regeteraty.this, "您输入的用户名已被其他用户使用，请重新输入！", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 8:
                    Toast.makeText(Regeteraty.this, "您输入的用户名不符合规则!", Toast.LENGTH_SHORT)
                            .show();
                    StyledDialog.dismissLoading();

                    break;
                case 9:
                    get_autncode_btn.setEnabled(true);
                    Toast.makeText(Regeteraty.this, "该号码已注册,请直接登录", Toast.LENGTH_SHORT)
                            .show();
                    StyledDialog.dismissLoading();

                    break;
                case 10:
                    Toast.makeText(Regeteraty.this, "注册失败", Toast.LENGTH_SHORT)
                            .show();
                    StyledDialog.dismissLoading();

                    break;
                case 20:
                    if (registerTest()) {
                        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                register();
                            }
                        }).start();
                    }
                    break;
                case 21:
                    App.getInstance().showToast("设备注册失败");
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
                    App.getInstance().showToast("用户名已存在");
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
                case 98:
                    StyledDialog.dismissLoading();
                    App.getInstance().showToast("同号码发送达到上限");
                    break;
                case 301:
                    get_autncode_btn.setEnabled(false);
                    break;
                case 302:
                    get_autncode_btn.setEnabled(true);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        phonenumstr = edit_phone_num.getText().toString();
        edit_phone_autncodestr = edit_phone_autncode.getText().toString();
        edit_usernamestr = edit_username.getText().toString();
        edit_shortcut_passwordstr = edit_shortcut_password.getText().toString();
        switch (v.getId()) {
            case R.id.register_btn:// 注册按钮
                if (getNetWifi()) {
                    if (TextUtils.isEmpty(LocalSave.getValue(this, AppConfig.basedevicefile, "devieuid", ""))) {
                        regisverDevier();
                    } else {
                        handler.sendEmptyMessage(20);
                    }


                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }


                // Intent intent = new Intent();
                // intent.setClass(Regeteraty.this, DialogPhoneActivity.class);
                // startActivity(intent);
                break;

            case R.id.get_autncode_btn:// 获取验证码


                if (getNetWifi()) {
                    if (TextUtils.isEmpty(phonenumstr) || phonenumstr.length() != 11) {
                        Toast.makeText(Regeteraty.this, "请输入正确手机号码!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    handler.sendEmptyMessage(301);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendSms();
                        }
                    }).start();

                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }


                break;
            case R.id.reipone_btn:// 换个手机
                Toast.makeText(Regeteraty.this, "换个手机", Toast.LENGTH_SHORT).show();
                break;
            case R.id.now_login: // 现在登录
                finish();
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
        deviceInfo.setApp_version(Tool.getAppVersionName(Regeteraty.this, "name"));
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
                LocalSave.putValue(Regeteraty.this, AppConfig.basedevicefile, "devicetoken", baseInfo.getToken());
                LocalSave.putValue(Regeteraty.this, AppConfig.basedevicefile, "devieuid", baseInfo.getUid());
                String getduid = LocalSave.getValue(this, AppConfig.basedevicefile, "devieuid", "");
                String getdtoken = LocalSave.getValue(this, AppConfig.basedevicefile, "devicetoken", "");
                Log.v("lsq", "baseInfo+" + getduid);
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

    private void AlterDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View view = LayoutInflater.from(activity).inflate(
                R.layout.phone_dialog, null);
        View reipone_btn = view.findViewById(R.id.reipone_btn);
        View now_login = view.findViewById(R.id.now_login);
        reipone_btn.setOnClickListener(this);
        now_login.setOnClickListener(this);
        builder.setView(view);
        AlertDialog ad = builder.create();
        ad.setCanceledOnTouchOutside(true);
        ad.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        ad.show();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 计时过程
            handler.sendEmptyMessage(301);
            get_autncode_btn.setText(millisUntilFinished / 1000 + "秒");
            get_autncode_btn.setGravity(Gravity.CENTER);
        }

        @Override
        public void onFinish() {
            // // 计时完毕
            get_autncode_btn.setText(getString(R.string.get_authcode_login));
            handler.sendEmptyMessage(302);
            get_autncode_btn.setGravity(Gravity.CENTER);

        }
    }

    // 发送短信验证码
    private void sendSms() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            String getduid = LocalSave.getValue(Regeteraty.this, AppConfig.basedevicefile, "devieuid", "");
            String getdtoken = LocalSave.getValue(Regeteraty.this, AppConfig.basedevicefile, "devicetoken", "");
            // check 当用户忘记密码传1
            Boolean issubmit = api.sendsms(phonenumstr, 1, getdtoken, getduid);
            if (issubmit) {
                time.start();// 开始计时
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(4);
            }
        } catch (JSONException e) {
            handler.sendEmptyMessage(302);
            handler.sendEmptyMessage(4);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(302);
            String code = e.getMessage();
            if ("-112".equals(code)) {
                handler.sendEmptyMessage(9);
            } else if ("-13".equals(code)) {
                //您输入的用户名不符合规则
                handler.sendEmptyMessage(98);
            } else {
                handler.sendEmptyMessage(4);
            }

            e.printStackTrace();
        }
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

    // 注册验证
    private boolean registerTest() {
        if (TextUtils.isEmpty(phonenumstr) || phonenumstr.length() != 11) {
            Toast.makeText(Regeteraty.this, "您输入手机号无效，请重新输入!", Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else if (TextUtils.isEmpty(edit_phone_autncodestr)) {
            Toast.makeText(Regeteraty.this, "验证码不能为空!", Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else if (TextUtils.isEmpty(edit_usernamestr)) {
            Toast.makeText(Regeteraty.this, "用户名不能为空!", Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else if (checkUsername(edit_usernamestr) == false) {
            Toast.makeText(Regeteraty.this, "请输入6-16英文和数字用户名!", Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else if (TextUtils.isEmpty(edit_shortcut_passwordstr)
                || edit_shortcut_passwordstr.length() < 6) {
            Toast.makeText(Regeteraty.this, "密码输入有误!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 验证用户名
     *
     * @param username 用户名
     * @return boolean
     */
    public static boolean checkUsername(String username) {
        Log.v("lsq", "lenget+" + username.length());
        String regex = "([a-zA-Z0-9]{6,20})";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(username);
        return m.matches();
    }

    // 用户注册
    private void register() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        UserDataBean userInfo = new UserDataBean();
        userInfo.setMobile(phonenumstr);
        userInfo.setPassword(edit_shortcut_passwordstr);
        userInfo.setSmscode(edit_phone_autncodestr);
        userInfo.setLogin_name(edit_usernamestr);
        userInfo.setNationCode(CityCodestr);
        String getduid = LocalSave.getValue(Regeteraty.this, AppConfig.basedevicefile, "devieuid", "");
        String getdtoken = LocalSave.getValue(Regeteraty.this, AppConfig.basedevicefile, "devicetoken", "");
        userInfo.setDeviceuid(getduid);
        Log.v("lsq", "getdtoken+" + getdtoken);
        userInfo.setDevicetoken(getdtoken);
        try {
            UserDataBean returnuserInfo = api.register(userInfo);
            if (returnuserInfo != null) {
                String cookie = returnuserInfo.getCookie();
                JSONObject cookiejson = new JSONObject(cookie);
                AppConfig.userid = cookiejson.getString("uid");
                AppConfig.usertoken = cookiejson.getString("token");
                Log.i("lsq", "reduid+" + AppConfig.userid);
                Log.i("lsq", "retoken+" + AppConfig.usertoken);
                //保存用户注册的手机号
                LocalSave.putValue(this, AppConfig.basefile, "login_mobile", phonenumstr);
                //保存用户注册的用户名
                LocalSave.putValue(this, AppConfig.basefile, "login_user_account", edit_usernamestr);
                //保存注册用户token
                LocalSave.putValue(this, AppConfig.basefile, "usertoken", cookiejson.getString("token"));
                //保存注册用户uid
                LocalSave.putValue(this, AppConfig.basefile, "userid", cookiejson.getString("uid"));
                //用户登录状态，登录成功未true ,默认未false
                LocalSave.putValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "true");
                handler.sendEmptyMessage(5);
            } else {
                handler.sendEmptyMessage(6);
            }
            Log.v("ceshi", "userInfo2" + returnuserInfo.getUid());

        } catch (JSONException e) {
            Log.i("zzr2", e.getMessage() + "");
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        } catch (WSError e) {
            String code = e.getMessage();
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
}
