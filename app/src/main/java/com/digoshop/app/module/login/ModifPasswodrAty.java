package com.digoshop.app.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.digoshop.app.module.main.MainActivity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class ModifPasswodrAty extends BaseActivity implements OnClickListener {
    private Button login_btn;
    private EditText edit_shortcut_num, edit_shortcut_password;

    private String shortcut_num, shortcut_password;
    private String mobile;
    private String smscode;
    private Handler handler;
    int typecode;

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
        setContentView(R.layout.modifpasswordaty);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("修改密码");
        login_btn = (Button) findViewById(R.id.login_btn);
        edit_shortcut_num = (EditText) findViewById(R.id.edit_shortcut_num);
        edit_shortcut_password = (EditText) findViewById(R.id.edit_shortcut_password);
        login_btn.setOnClickListener(this);

        Bundle bundle = this.getIntent().getExtras();
        typecode = bundle.getInt("typecode");
        if (typecode == 1) {
            mobile = bundle.getString("mobile");
            smscode = bundle.getString("smscode");
            edit_shortcut_num.setHint("设置新密码(6-16数字 、字母和符号组合)");
            edit_shortcut_password.setHint("确认新密码！");
        } else if (typecode == 2) {
            mobile = bundle.getString("mobile");
            edit_shortcut_num.setHint("请输入旧密码！");
            edit_shortcut_password.setHint("确认新密码！");
        }
        //login_btn.setEnabled(false);

        Log.i("zzr1", mobile + smscode + AppConfig.usertoken);

        handler = new Handler() {
            Intent intent = new Intent();

            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        Toast.makeText(ModifPasswodrAty.this, "设置成功", Toast.LENGTH_SHORT).show();
                        intent.setClass(ModifPasswodrAty.this, MainActivity.class);
                        startActivity(intent);
                        StyledDialog.dismissLoading();

                        finish();
                        break;
                    case 2:
                        StyledDialog.dismissLoading();

                        Toast.makeText(ModifPasswodrAty.this, "设置失败", Toast.LENGTH_SHORT).show();
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
                        App.getInstance().showToast("旧密码错误");
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
        //密码edit监听
        edit_shortcut_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                shortcut_num = edit_shortcut_num.getText().toString();
                Log.i("zzr", hasFocus + "");
                if (hasFocus) {
                    if (!TextUtils.isEmpty(shortcut_num)) {
//
                        login_btn.setBackgroundResource(R.drawable.bigbtn_on);
                    } else {
                        login_btn.setBackgroundResource(R.drawable.bigbtn_off);

                    }
                } else {

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_btn:// 确定
                if (getNetWifi()) {
                    shortcut_num = edit_shortcut_num.getText().toString().trim();
                    shortcut_password = edit_shortcut_password.getText().toString().trim();
                    if (TextUtils.isEmpty(shortcut_num) || TextUtils.isEmpty(shortcut_password)) {
                        App.getInstance().showToast("请输入密码！");
                        return;
                    }
                    if (shortcut_password != null) {
                        if (shortcut_password.length() < 6) {
                            App.getInstance().showToast("请输入6-16英文和数字密码!");
                            return;
                        }
                    }
                    if (shortcut_num != null) {
                        if (shortcut_num.length() < 6) {
                            App.getInstance().showToast("请输入6-16英文和数字密码!");
                            return;
                        }
                    }
                    if ("1".equals(typecode + "")) {
                        if (!shortcut_password.equals(shortcut_num)) {
                            App.getInstance().showToast("两次密码不一致，请重新输入！");
                            return;
                        }
                    }
                    StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            forgetPassword();
                        }
                    }).start();

                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
                break;
            default:
                break;
        }

    }

    public void forgetPassword() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            Log.i("zzr1", shortcut_num);
            Log.i("zzr1", shortcut_password);
            Boolean code;
            String token = LocalSave.getValue(ModifPasswodrAty.this, "login", "login_user_token", "");
            Log.i("zzr11", token);
            code = api.resetpwd(mobile, shortcut_num, shortcut_password, smscode, typecode);
            if (code) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }


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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
