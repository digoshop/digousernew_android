package com.digoshop.app.module.setting;

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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;

import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyPhoneActivity extends BaseActivity implements View.OnClickListener {
    EditText edit_phone_autncode;
    Button btnCode,editPhoneAutncode;
    TextView tips3;
    private TimeCount time;
    private String moblile = null;
    private BroadcastReceiver smsReceiver;
    private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";
    private String strContent;
    private IntentFilter filter2;
    private String phoneAutncode;
    private Bundle  bundleType;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(MyPhoneActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(MyPhoneActivity.this, "获取验证码失败", Toast.LENGTH_SHORT).show();

                    break;
                case 5:
                    Toast.makeText(MyPhoneActivity.this, "身份验证成功", Toast.LENGTH_SHORT).show();
                    startActivity(ChangePhoneActivity.class,bundleType);
                    finish();
                    break;
                case 6:
                    Toast.makeText(MyPhoneActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    Toast.makeText(MyPhoneActivity.this, "短信验证码错误", Toast.LENGTH_SHORT).show();
                    break;
                case 98:
                    App.getInstance().showToast("同号码发送达到上限");
                    break;
            }
        }
    };

    @Override
    public boolean isNoTitle() {
        return false;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_phone);
        TextView    tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("验证身份");
        edit_phone_autncode = (EditText) findViewById(R.id.edit_phone_autncode);
        btnCode = (Button) findViewById(R.id.btn_code);
        editPhoneAutncode= (Button) findViewById(R.id.editPhoneAutncode);
        tips3  = (TextView) findViewById(R.id.tips3);
        btnCode.setOnClickListener(this);
        editPhoneAutncode.setOnClickListener(this);
        init();
        bundleType=getIntent().getExtras();

    }

    public void init() {

        //登录注册返回的手机号
        String user_moblile = LocalSave.getValue(this, AppConfig.basefile, "login_mobile", "");
        //手机快捷登录输入的手机号
        String sms_moblile = LocalSave.getValue(this, AppConfig.basefile, "login_sms", "");
        if (!TextUtils.isEmpty(user_moblile)) {
            moblile = user_moblile;
        }
        if (!TextUtils.isEmpty(sms_moblile)) {
            moblile = sms_moblile;
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
            tips3.setText(sb.toString());
        }


        time = new TimeCount(60000, 1000);
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



    private void verify() {

        Log.i("zzzr",moblile);
        Log.i("zzzr",phoneAutncode);
        new Thread(new Runnable() {
            @Override
            public void run() {
                DigoIUserApiImpl api = new DigoIUserApiImpl();
                try {
                    Boolean isSuccess=api.verifysmscode(moblile, phoneAutncode);
                    if(isSuccess){
                        handler.sendEmptyMessage(5);
                    }else {
                        handler.sendEmptyMessage(6);
                    }
                } catch (WSError wsError) {
                    String code = wsError.getMessage();
                    if (code.equals("-101")) {
                        //您输入的用户名不符合规则
                        handler.sendEmptyMessage(7);
                    }else{
                        handler.sendEmptyMessage(6);
                    }

                    wsError.printStackTrace();
                } catch (JSONException e) {
                    handler.sendEmptyMessage(6);
                    e.printStackTrace();
                }

            }
        }).start();
    }


    // 发送短信验证码
    private void sendSms() {
        Log.i("zzrmobile", moblile);
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {

            // check 当用户忘记密码传1
            Boolean issubmit = api.sendsms(moblile, 0,AppConfig.usertoken,AppConfig.userid);
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
            if (code.equals("-13")) {
                //您输入的用户名不符合规则
                handler.sendEmptyMessage(98);
            }else{
                handler.sendEmptyMessage(2);
            }

            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_code:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendSms();
                    }
                }).start();
                break;
            case R.id.editPhoneAutncode:
                phoneAutncode = edit_phone_autncode.getText().toString();
                if(TextUtils.isEmpty(phoneAutncode)){
                    App.getInstance().showToast("请输入验证码");
                }else{
                    verify();
                }
                break;
        }
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 计时过程
            btnCode.setText(millisUntilFinished / 1000 + "秒");
            btnCode.setClickable(false);
            btnCode.setGravity(Gravity.CENTER);
        }

        @Override
        public void onFinish() {
            // // 计时完毕
            btnCode.setText(getString(R.string.get_authcode_login));
            btnCode.setClickable(true);
            btnCode.setGravity(Gravity.CENTER);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
    }
}
