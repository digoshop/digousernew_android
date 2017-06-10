package com.digoshop.app.module.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.login.Loginaty;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import static com.digoshop.app.utils.Tool.getNetWifi;

public class ComplaintFeedback extends BaseActivity implements OnClickListener {
    private Button bt_submit_evaluate;
    private EditText et_feedback, et_contactway;
    private String feedback, contactway;
    private String Choicetype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_feedback);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("投诉与反馈");
        bt_submit_evaluate = (Button) findViewById(R.id.bt_submit_evaluate);
        et_feedback = (EditText) findViewById(R.id.et_feedback);
        et_contactway = (EditText) findViewById(R.id.et_contactway);
        bt_submit_evaluate.setOnClickListener(this);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ComplaintFeedback.this, R.layout.simple_spinner_item);
        String level[] = getResources().getStringArray(R.array.languages);// 资源文件
        for (int i = 0; i < level.length; i++) {
            adapter.add(level[i]);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] languages = getResources().getStringArray(R.array.languages);
                if (languages[position] == languages[0]) {
                    Choicetype = "0";
                    Log.i("TEST", "languages[0]" + languages[0]);
                } else if (languages[position] == languages[1]) {
                    Choicetype = "1";
                    Log.i("TEST", "languages[0]" + languages[1]);
                } else if (languages[position] == languages[2]) {
                    Choicetype = "2";
                    Log.i("TEST", "languages[0]" + languages[2]);
                } else if (languages[position] == languages[3]) {
                    Choicetype = "3";
                    Log.i("TEST", "languages[0]" + languages[3]);
                } else if (languages[position] == languages[4]) {
                    Choicetype = "4";
                    Log.i("TEST", "languages[0]" + languages[4]);
                } else if (languages[position] == languages[5]) {
                    Choicetype = "5";
                    Log.i("TEST", "languages[0]" + languages[5]);
                } else if (languages[position] == languages[6]) {
                    Choicetype = "6";
                    Log.i("TEST", "languages[0]" + languages[6]);
                } else if (languages[position] == languages[7]) {
                    Choicetype = "7";
                    Log.i("TEST", "languages[0]" + languages[7]);
                } else if (languages[position] == languages[8]) {
                    Choicetype = "8";
                    Log.i("TEST", "languages[0]" + languages[8]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit_evaluate:
                Intent intent = new Intent();
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if (!"true".equals(AppConfig.isusertype)) {
                    intent.putExtra("tag","feedback");
                    intent.setClass(ComplaintFeedback.this, Loginaty.class);
                    startActivity(intent);
                    return;
                }

                feedback = et_feedback.getText().toString();
                contactway = et_contactway.getText().toString();
                if (!TextUtils.isEmpty(feedback)) {
                    if (TextUtils.isEmpty(feedback.toString().trim())) {
                        App.getInstance().showToast("请输入意见反馈内容！");
                        return;
                    }
                }
                if (Choicetype.equals("0")) {
                    App.getInstance().showToast("请选择意见类型！");
                } else if (TextUtils.isEmpty(feedback)) {
                    App.getInstance().showToast("请输入意见反馈内容！");
                }
//			else if (TextUtils.isEmpty(contactway)) {
//				App.getInstance().showToast("请输入联系方式！");
//			}
                else {
                    if (getNetWifi()) {
                        StyledDialog.buildLoading(ComplaintFeedback.this, App.getInstance().getString(R.string.onloading), true, false).show();
                        // StyledDialog.buildLoading(App.getInstance(), "加载中...", true, false).show();
                        get_Feedback();
                        //StyledDialog.buildLoading(getActivity(), "加载中...", true, false).show();            getShops();
                    } else {
                        App.getInstance().showToast("网络不给力，请检查网络设置");
                    }

                }
                break;
            default:
                break;
        }
    }

    private void get_Feedback() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                get_Feedbackcomplaint();
            }
        }).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    App.getInstance().showToast("提交成功");
                    StyledDialog.dismissLoading();

                    finish();
                    break;
                case 2:
                    App.getInstance().showToast("返回为空");
                    StyledDialog.dismissLoading();

                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();

                    break;
                case 4:
                    App.getInstance().showToast("请求异常");
                    StyledDialog.dismissLoading();

                    break;
            }
        }
    };

    private void get_Feedbackcomplaint() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            boolean isboole = api.feedback(Choicetype, feedback, contactway, "");
            if (isboole) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(4);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
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
}
