package com.digoshop.app.module.setting;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.module.setting.model.AppUpdata;
import com.digoshop.app.module.setting.update.CustomDialog;
import com.digoshop.app.module.setting.update.UpdateService;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;

import org.json.JSONException;

import java.util.List;

import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * TODO 用户中心-设置-关于迪购
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016-6-6 下午11:23:55
 * @version: V1.0
 */


/**
 * TODO 用户中心-设置-关于迪购
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016-6-6 下午11:23:55
 * @version: V1.0
 */

public class AboutDigoAty extends BaseActivity implements OnClickListener {
    private RelativeLayout re_feedback, re_fun_info, re_app_update;
    private Intent intent;
    private String versionName;
    private AppUpdata appUpdata;
    private AlertDialog.Builder builder;
    // 下载应用的进度条
    private ImageView iv_aboutqrcode;
    private String appmd5;
    private TextView tv_appversioin;
    private CustomDialog mDialog = null;
    private String downapkname = "ZDHT.apk";
    private static final int REQUEST_WRITE_STORAGE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutdigoaty);
        TextView    tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("关于App");
        tv_appversioin = (TextView) findViewById(R.id.tv_appversioin);
        appmd5 = Tool.getSignMd5Str();
        initData();
        re_fun_info = (RelativeLayout) findViewById(R.id.re_fun_info);
        re_feedback = (RelativeLayout) findViewById(R.id.re_feedback);
        re_app_update = (RelativeLayout) findViewById(R.id.re_app_update);
        iv_aboutqrcode = (ImageView) findViewById(R.id.iv_aboutqrcode);
        iv_aboutqrcode.setBackgroundResource(R.drawable.my_qrcode);
        re_feedback.setOnClickListener(this);
        re_fun_info.setOnClickListener(this);
        re_app_update.setOnClickListener(this);
        builder = new AlertDialog.Builder(this);
    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.i("zzr", appUpdata.getU_log());

                    if (appUpdata.isUpdate()) {
                        String appmd5 = LocalSave.getValue(AboutDigoAty.this, AppConfig.basedevicefile, "appmd5", "");
                        if (appmd5.equals(appUpdata.getNew_md5())) {
                            handler.sendEmptyMessage(3);
                            return;
                       }else{
                       //    ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                       //     ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//                            Log.d("ceshi", "pkg:"+cn.getPackageName());
                   //        Log.d("ceshi", "cls:"+cn.getClassName());
                            if(isForeground(AboutDigoAty.this,"com.digoshop.app.module.setting.AboutDigoAty")){
                                chechVersion();
                            }
                        }
                    } else {
                        handler.sendEmptyMessage(3);
                    }
                    break;
                case 3:
                    App.getInstance().showToast("当前版本" + versionName + "已是最新版本!");
                    break;


            }

        }
    };
    private boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_fun_info:
                intent = new Intent(AboutDigoAty.this, AppIntroduceActivity.class);
                startActivity(intent);
                break;
            case R.id.re_feedback:
                intent = new Intent(AboutDigoAty.this, ComplaintFeedback.class);
                startActivity(intent);
                break;
            case R.id.re_app_update:
                if (getNetWifi()) {
                    getAppUpdate();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }

                break;
            default:
                break;
        }

    }

    private void initData() {
        // 获取自己的版本信息
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            // 版本号
            int versionCode = packageInfo.versionCode;
            // 版本名
            versionName = packageInfo.versionName;
            Log.i("dddd", versionCode + "--" + versionName);
            tv_appversioin.setText("当前版本:" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void chechVersion() {
        mDialog = new CustomDialog(AboutDigoAty.this);
        mDialog.setTitle("发现新版本");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContent(appUpdata.getU_log());
        mDialog.setCancelButton("暂不更新", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LocalSave.putValue(AboutDigoAty.this, AppConfig.basedevicefile, "appmd5", appUpdata.getNew_md5());
                mDialog.dismiss();

            }
        });
        mDialog.setOKButton("立即更新", new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //请求存储权限
                boolean hasPermission = (ContextCompat.checkSelfPermission(AboutDigoAty.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermission) {
                    ActivityCompat.requestPermissions(AboutDigoAty.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                    ActivityCompat.shouldShowRequestPermissionRationale(AboutDigoAty.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    //下载
                    startDownload();
                }

            }
        });
        mDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //获取到存储权限,进行下载
                    startDownload();
                } else {
                    Toast.makeText(AboutDigoAty.this, "不授予存储权限将无法进行下载!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 启动下载
     */
    private void startDownload() {
        Intent it = new Intent(AboutDigoAty.this, UpdateService.class);
        Log.v("ceshi","appUpdata.getDown_url()+"+appUpdata.getDown_url());
        //下载地址
        it.putExtra("apkUrl", appUpdata.getDown_url());
        startService(it);
        mDialog.dismiss();
    }


    public void getAppUpdate() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                getAppUpdateapi(versionName);

            }
        }).start();

    }

    public void getAppUpdateapi(String versionName) {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            appUpdata = api.appUpdata(versionName, appmd5);
            if (appUpdata != null) {
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(3);
            }


        } catch (WSError wsError) {
            handler.sendEmptyMessage(3);
            wsError.printStackTrace();
        } catch (JSONException e) {
            handler.sendEmptyMessage(3);
            String code = e.getMessage();
            //			if(){
            //
            //			}
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
