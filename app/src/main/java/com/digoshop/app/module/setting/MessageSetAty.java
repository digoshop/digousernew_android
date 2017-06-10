package com.digoshop.app.module.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import ch.ielse.view.SwitchView;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * TODO 用户中心-设置-信息设置
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016-6-6 下午11:19:18
 * @version: V1.0
 */
public class MessageSetAty extends BaseActivity implements OnClickListener {
    private String shopboolean = "0";
    private String sysstemboolean = "0";
    private String selectbool = "0";
    private String isshop;
    private String issystem;
    private String istype;
    private SwitchView sv_shopbtn, sv_sysbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagesetaty);
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("消息设置");

        sv_sysbtn = (SwitchView) findViewById(R.id.sv_sysbtn);
        sv_shopbtn = (SwitchView) findViewById(R.id.sv_shopbtn);
        isshop = LocalSave.getValue(this, AppConfig.basefile, "isshop", "0");
        issystem = LocalSave.getValue(this, AppConfig.basefile, "issystem", "0");
        if ("1".equals(isshop)) {
            sv_shopbtn.setOpened(true);
            //essage_newm.setChecked(true);
        } else {
            sv_shopbtn.setOpened(false);
            //  essage_newm.setChecked(false);
        }
        if ("1".equals(issystem)) {
            sv_sysbtn.setOpened(true);
            //sb_message_mute.setChecked(true);
        } else {
            sv_sysbtn.setOpened(false);
        }
        //商家
        sv_shopbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean bool = sv_shopbtn.isOpened();
                istype = "mer";
                if (bool) {
                    shopboolean = "1";
                } else {
                    shopboolean = "0";
                }
                selectbool = shopboolean;
                if (getNetWifi()) {
                    StyledDialog.buildLoading(MessageSetAty.this, App.getInstance().getString(R.string.onloading), true, false).show();
                    setMessage();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
            }
        });
        //系统
        sv_sysbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean bool = sv_sysbtn.isOpened();
                istype = "sys";
                if (bool) {
                    sysstemboolean = "1";
                } else {
                    sysstemboolean = "0";
                }
                selectbool = sysstemboolean;
                if (getNetWifi()) {
                    StyledDialog.buildLoading(MessageSetAty.this, App.getInstance().getString(R.string.onloading), true, false).show();
                    setMessage();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }
            }
        });
    }

    private void setMessage() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                setMessageapi();
            }
        }).start();

    }

    private void setMessageapi() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            boolean isboll = api.MessAgeSet(istype, selectbool);

            if (isboll) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }

        } catch (JSONException e) {
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if ("mer".equals(istype)) {
                        if ("1".equals(shopboolean)) {
                            sv_shopbtn.setOpened(true);
                        } else {
                            sv_shopbtn.setOpened(false);
                        }
                        LocalSave.putValue(App.getInstance(), AppConfig.basefile, "isshop", shopboolean);
                    } else {
                        if ("1".equals(sysstemboolean)) {
                            sv_sysbtn.setOpened(true);
                        } else {
                            sv_sysbtn.setOpened(false);
                        }
                        LocalSave.putValue(App.getInstance(), AppConfig.basefile, "issystem", sysstemboolean);
                    }


                    StyledDialog.dismissLoading();

                    break;
                case 2:
                    StyledDialog.dismissLoading();
                    if ("mer".equals(istype)) {
                        if ("1".equals(isshop)) {
                            sv_shopbtn.setOpened(true);
                        } else {
                            sv_shopbtn.setOpened(false);
                        }
                    } else {
                        if ("1".equals(issystem)) {
                            sv_sysbtn.setOpened(true);
                        } else {
                            sv_sysbtn.setOpened(false);
                        }

                    }
                    App.getInstance().showToast("设置失败");

                    break;


            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            default:
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

}
