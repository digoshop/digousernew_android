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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.login.Loginaty;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;

import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * TODO 修改手机号
 */
public class ChangePhoneActivity extends BaseActivity implements View.OnClickListener{


    EditText edit_phone;
    EditText edit_phone_autncode;
    Button btn_code,btn_changephone;
    private TimeCount time;
    private BroadcastReceiver smsReceiver;
    private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";
    private String strContent;
    private IntentFilter filter2;
    private String etPhone;
    private String etCode;
    private Bundle bundleType;
    private String type;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(ChangePhoneActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(ChangePhoneActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    startActivity(Loginaty.class);
                    LocalSave.getEditor(ChangePhoneActivity.this, AppConfig.basefile).clear().commit();
                    finish();
                    break;
                case 3:
                    App.getInstance().showToast("获取验证码失败!");
                    break;
                case 98:
                    App.getInstance().showToast("同号码发送达到上限");
                    break;
                case 99:
                    App.getInstance().showToast("账户已存在");
                    break;
            }
        }
    };

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
        setContentView(R.layout.activity_change_phone);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("认证手机号码");
        btn_code = (Button) findViewById(R.id.btn_code);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_phone_autncode = (EditText) findViewById(R.id.edit_phone_autncode);

        btn_changephone = (Button) findViewById(R.id.btn_changephone);
        btn_code.setOnClickListener(this);
        btn_changephone.setOnClickListener(this);
        init();
        bundleType = getIntent().getExtras();
        int budType = bundleType.getInt("type");
        Log.i("zzrType", type + "");
        if (budType == 1) {
            type = "1";
        } else {
            type = "2";
        }
    }

    public void init() {

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





    public void changePhone() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("zzr", etPhone + "----" + etCode);
                final DigoIUserApiImpl digoApi = new DigoIUserApiImpl();

                try {
                    Boolean success = digoApi.set_changmobile(etPhone, etCode, type);
                    if (success) {
                        handler.sendEmptyMessage(2);
                    }
                    //"-112"已存在
                } catch (WSError wsError) {

                    wsError.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    // 发送短信验证码
    private void sendSms() {

        String phoneNum = edit_phone.getText().toString();
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {

            // check 当用户忘记密码传1
            Boolean issubmit = api.sendsms(phoneNum, 0, AppConfig.usertoken, AppConfig.userid);
            if (issubmit) {
                time.start();// 开始计时
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(3);
            }
        } catch (JSONException e) {
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            String code = e.getMessage();
            if (code.equals("-13")) {
                //您输入的用户名不符合规则
                handler.sendEmptyMessage(98);
            }else if(code.equals("-112")){
                handler.sendEmptyMessage(99);
            }else{
                handler.sendEmptyMessage(3);
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
            case R.id.btn_changephone:
                etPhone = edit_phone.getText().toString();
                etCode = edit_phone_autncode.getText().toString();
                changePhone();
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
            btn_code.setClickable(false);
            btn_code.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            // // 计时完毕
            btn_code.setText(getString(R.string.get_authcode_login));
            btn_code.setClickable(true);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
    }
}
