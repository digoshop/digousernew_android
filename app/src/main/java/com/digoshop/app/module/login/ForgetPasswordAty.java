package com.digoshop.app.module.login;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.userCenter.model.UserDataBean;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPasswordAty extends BaseActivity implements OnClickListener {
    private Button get_autncode_btn, login_btn;
    private EditText edit_phone_num, edit_phone_autncode;

    private String edit_phone_numstr; //快捷登录手机号
    private String edit_phone_autncodestr;//验证码
    private TimeCount time;
    private Handler handler;
    private BroadcastReceiver smsReceiver;
    private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";
    private String strContent;
    private IntentFilter filter2;
    private String mobile;

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
        setContentView(R.layout.forgetpasswodraty);
        time = new TimeCount(60000, 1000);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("快捷登录");
        get_autncode_btn = (Button) findViewById(R.id.get_autncode_btn);
        login_btn = (Button) findViewById(R.id.login_btn);
        edit_phone_num = (EditText) findViewById(R.id.edit_phone_num);
        edit_phone_autncode = (EditText) findViewById(R.id.edit_phone_autncode);
        get_autncode_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);


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


        handler = new Handler() {
            Intent intent = new Intent();

            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        Toast.makeText(ForgetPasswordAty.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                        StyledDialog.dismissLoading();

                        break;
                    case 2:
                        Toast.makeText(ForgetPasswordAty.this, "解析异常", Toast.LENGTH_SHORT).show();
                        StyledDialog.dismissLoading();

                        break;
                    case 3:
                        edit_phone_autncode.setText(strContent);
                        break;
                    case 4:
                        Toast.makeText(ForgetPasswordAty.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                        StyledDialog.dismissLoading();

                        break;
                    case 5:
                        intent.setClass(ForgetPasswordAty.this, ModifPasswodrAty.class);
                        intent.putExtra("mobile", edit_phone_numstr);
                        intent.putExtra("smscode", edit_phone_autncodestr);
                        intent.putExtra("typecode", 1);
                        startActivity(intent);
                        StyledDialog.dismissLoading();

                        finish();
                        break;
                    case 6:
                        Toast.makeText(ForgetPasswordAty.this, "登录失败", Toast.LENGTH_SHORT).show();
                        StyledDialog.dismissLoading();
                        break;
                    case 7:
                        Toast.makeText(ForgetPasswordAty.this, "用户不存在!", Toast.LENGTH_SHORT).show();
                        StyledDialog.dismissLoading();
                        break;
                    case 8:
                        Toast.makeText(ForgetPasswordAty.this, "短信验证码错误!", Toast.LENGTH_SHORT).show();
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
                    case 98:
                        StyledDialog.dismissLoading();
                        App.getInstance().showToast("同号码发送达到上限");
                        break;
                    case 100:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sendSms();
                            }
                        }).start();
                        break;
                    case 101:
                        StyledDialog.dismissLoading();
                        App.getInstance().showToast("该手机号尚未注册，请先进行注册!");
                        break;
                }
            }
        };
        editListener();

    }


    public void editListener() {
        edit_phone_autncode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String phonenumstr = edit_phone_num.getText().toString();
                if (!TextUtils.isEmpty(phonenumstr)) {

                    login_btn.setBackgroundResource(R.drawable.bigbtn_on);
                } else {
                    login_btn.setBackgroundResource(R.drawable.bigbtn_off);
                }

            }
        });
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
     private void isphonealive(){
         new Thread(new Runnable() {
             @Override
             public void run() {
                 isphoneapi();
             }
         }).start();
     }
    private void isphoneapi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            String getduid = LocalSave.getValue(ForgetPasswordAty.this, AppConfig.basedevicefile, "devieuid", "");
            String getdtoken = LocalSave.getValue(ForgetPasswordAty.this, AppConfig.basedevicefile, "devicetoken", "");
            // check 当用户忘记密码传1
            Boolean issubmit = api.PutPoneisAlive(edit_phone_numstr,getdtoken,getduid);
            if(issubmit){
                //说明这个号没有注册过
                handler.sendEmptyMessage(101);
            }else{
                handler.sendEmptyMessage(4);
            }

        } catch (JSONException e) {
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        } catch (WSError e) {
            String code = e.getMessage();
            if (code.equals("-112")) {
                //说明注册过，则发送验证码
                handler.sendEmptyMessage(100);
            }else{
                handler.sendEmptyMessage(4);
            }
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {

            case R.id.get_autncode_btn:// 获取验证码
                edit_phone_numstr = edit_phone_num.getText().toString().trim();
                Log.i("zzr", edit_phone_numstr);
                if (TextUtils.isEmpty(edit_phone_numstr) || edit_phone_numstr.length() != 11) {
                    Toast.makeText(ForgetPasswordAty.this, "请输入正确手机号码!", Toast.LENGTH_SHORT).show();
                    return;
                }
                isphonealive();

                break;
            case R.id.login_btn:// 下一步
                edit_phone_numstr = edit_phone_num.getText().toString();
                edit_phone_autncodestr = edit_phone_autncode.getText().toString();
                loginTest();
                StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        login_sms();
                    }
                }).start();
                break;
            default:
                break;
        }

    }

    // 发送短信验证码
    private void sendSms() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            String getduid = LocalSave.getValue(ForgetPasswordAty.this, AppConfig.basedevicefile, "devieuid", "");
            String getdtoken = LocalSave.getValue(ForgetPasswordAty.this, AppConfig.basedevicefile, "devicetoken", "");
            // check 当用户忘记密码传1
            Boolean issubmit = api.sendsms(edit_phone_numstr, 0, getdtoken, getduid);
            if (issubmit) {
                time.start();// 开始计时
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(4);
            }
        } catch (JSONException e) {
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        } catch (WSError e) {
            String code = e.getMessage();
            Log.v("zzr", "WSError" + code);
            if (code.equals("-109")) {
                handler.sendEmptyMessage(7);
            } else if (code.equals("-101")) {
                handler.sendEmptyMessage(8);
            } else if (code.equals("-13")) {
                //您输入的用户名不符合规则
                handler.sendEmptyMessage(98);
            } else {
                handler.sendEmptyMessage(2);
            }
            e.printStackTrace();
        }
    }

    // 短信验证码登录
    private void login_sms() {

        DigoIUserApiImpl api = new DigoIUserApiImpl();
        UserDataBean userInfo = new UserDataBean();
        userInfo.setMobile(edit_phone_numstr);
        userInfo.setSmscode(edit_phone_autncodestr);
        String getduid = LocalSave.getValue(ForgetPasswordAty.this, AppConfig.basedevicefile, "devieuid", "");
        String getdtoken = LocalSave.getValue(ForgetPasswordAty.this, AppConfig.basedevicefile, "devicetoken", "");
        userInfo.setDevicetoken(getdtoken);
        userInfo.setDeviceuid(getduid);
        try {
            UserDataBean returnuserInfo = api.login_sms(userInfo);
            if (returnuserInfo != null) {
                Log.i("zzr", returnuserInfo.getMobile() + returnuserInfo.getToken());
                LocalSave.putValue(this, AppConfig.basefile, "login_sms", edit_phone_numstr);
                String cookie = returnuserInfo.getCookie();
                JSONObject cookiejson = new JSONObject(cookie);
                //保存登录的用户token
                LocalSave.putValue(this, AppConfig.basefile, "usertoken", cookiejson.getString("token"));
                //保存登录的用户uid
                LocalSave.putValue(this, AppConfig.basefile, "userid", cookiejson.getString("uid"));


                handler.sendEmptyMessage(5);
            } else {
                handler.sendEmptyMessage(6);
            }
        } catch (JSONException e) {
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        } catch (WSError e) {
            if (e==null) {
                handler.sendEmptyMessage(2);
                return;
            }
            String code = e.getMessage();
            if (TextUtils.isEmpty(code) || "".equals(code)) {
                handler.sendEmptyMessage(2);
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

    private boolean loginTest() {

        if (TextUtils.isEmpty(edit_phone_numstr) || edit_phone_numstr.length() != 11) {
            Toast.makeText(ForgetPasswordAty.this, "请输入正确手机号码!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(edit_phone_autncodestr)) {
            Toast.makeText(ForgetPasswordAty.this, "验证码不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 计时过程
            get_autncode_btn.setClickable(false);
            get_autncode_btn.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            // // 计时完毕
            get_autncode_btn.setText(getString(R.string.get_authcode_login));
            get_autncode_btn.setClickable(true);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
    }
}
