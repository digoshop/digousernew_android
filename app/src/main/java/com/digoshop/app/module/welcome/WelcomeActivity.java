package com.digoshop.app.module.welcome;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.base.BaseActivity;
import com.digoshop.app.base.BaseInfo;
import com.digoshop.app.module.main.MainActivity;
import com.digoshop.app.module.setting.model.AppUpdata;
import com.digoshop.app.module.setting.update.CustomDialog;
import com.digoshop.app.module.setting.update.UpdateService;
import com.digoshop.app.module.welcome.module.DeviceInfo;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.Constants;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.db.AssetsDataBasesManage;
import com.digoshop.app.utils.db.SQLiteDataBases_db;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;

import java.util.List;

import static com.digoshop.app.utils.LocalSave.getValue;
import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * TODO 欢迎页面
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016-5-22 上午10:10:30
 * @version: V1.0
 */
public class WelcomeActivity extends BaseActivity {
    private CustomDialog mDialog = null;
    int screenWidth;
    int screenHeight;
    private static final int REQUEST_WRITE_STORAGE = 111;
    private AssetsDataBasesManage manage;
    private String versionName;
    private AppUpdata appUpdata;
    private String appmd5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AssetsDataBasesManage.initManager(WelcomeActivity.this);
        manage = AssetsDataBasesManage.getManager();
        StyledDialog.buildLoading(WelcomeActivity.this, App.getInstance().getString(R.string.onloading), true, false).show();
        getversionname();
        appmd5 = Tool.getSignMd5Str();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_welcome);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = screenWidth = display.getWidth();
        screenHeight = screenHeight = display.getHeight();
        //判断是否注册设备
        if (TextUtils.isEmpty(getValue(WelcomeActivity.this, AppConfig.basedevicefile, "devieuid", ""))) {
            //未登录则去注册设备
            if (getNetWifi()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        onRegister_By_Device();
                    }
                }).start();

            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        AppConfig.ipstr = Tool.getIP();
        if (getNetWifi()) {
            getAppUpdate();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
            //没有网络就没有意义去注册设备了。
            String isdelete = getValue(this, AppConfig.basedevicefile, "deletedb", "true");
            Log.v("lsq", "isdelete+" + isdelete);
            SQLiteDatabase db = manage.getDatabase(AssetsDataBasesManage.coursesDBName);
            boolean type = new SQLiteDataBases_db().checkColumnExists(db, "type");
            //判断数据库是否存在type列因为老的版本app中没有type列
            if (type) {
                handler.sendMessageDelayed(handler.obtainMessage(3), 500);
            } else {
                if ("true".equals(isdelete)) {
                    boolean isresult = manage.DeleteOldDb(AssetsDataBasesManage.coursesDBName);
                    Log.v("lsq", "isresult+" + isresult);
                    if (isresult) {
                        manage.closeDatabase(AssetsDataBasesManage.coursesDBName);
                        LocalSave.putValue(WelcomeActivity.this, AppConfig.basedevicefile, "deletedb", "false");
                        handler.sendMessageDelayed(handler.obtainMessage(3), 500);
                    } else {
                        handler.sendEmptyMessage(5);
                    }
                } else {
                    handler.sendMessageDelayed(handler.obtainMessage(3), 500);
                }
            }

        }
    }

    public void getAppUpdate() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                getAppUpdateapi(versionName);

            }
        }).start();

    }

    private void getversionname() {
        // 获取自己的版本信息
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            // 版本号
            int versionCode = packageInfo.versionCode;
            // 版本名
            versionName = packageInfo.versionName;
            Log.i("dddd", versionCode + "--" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void getAppUpdateapi(String versionName) {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            Log.i("zzr", "versionName" + versionName);
            Log.i("zzr", "appmd5" + appmd5);
            appUpdata = api.appUpdata(versionName, appmd5);
            if (appUpdata != null) {
                handler.sendEmptyMessage(12);
            } else {
                handler.sendEmptyMessage(10);
            }
        } catch (WSError wsError) {
            handler.sendEmptyMessage(10);
            wsError.printStackTrace();
        } catch (JSONException e) {
            handler.sendEmptyMessage(10);
            String code = e.getMessage();
            e.printStackTrace();
        }

    }

    // 设备信息注册
    private void onRegister_By_Device() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setChannel(Constants.CHANNEL);
        deviceInfo.setApp_version(Tool.getAppVersionName(WelcomeActivity.this, "name"));
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
                LocalSave.putValue(WelcomeActivity.this, AppConfig.basedevicefile, "devicetoken", baseInfo.getToken());
                LocalSave.putValue(WelcomeActivity.this, AppConfig.basedevicefile, "devieuid", baseInfo.getUid());
                String getduid = LocalSave.getValue(this, AppConfig.basedevicefile, "devieuid", "");
                String getdtoken = LocalSave.getValue(this, AppConfig.basedevicefile, "devicetoken", "");
                String useruid = LocalSave.getValue(WelcomeActivity.this, AppConfig.basefile, "userid", "");
                AppConfig.userid = getduid;
                AppConfig.usertoken = getdtoken;
                if (TextUtils.isEmpty(useruid)) {
                    //保存登录的用户token
                    LocalSave.putValue(this, AppConfig.basefile, "usertoken", getdtoken);
                    //保存登录的用户uid
                    LocalSave.putValue(this, AppConfig.basefile, "userid", getduid);
                }
                Log.v("lsq", "baseInfo+" + getduid);
                Log.v("lsq", "baseInfo+" + getdtoken);
            } else {
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (WSError e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    App.getInstance().showToast("设备注册失败，请退出后重新进入!");
                    break;
                case 3:
                    AppConfig.userid = getValue(WelcomeActivity.this, AppConfig.basefile, "userid", "");
                    AppConfig.usertoken = getValue(WelcomeActivity.this, AppConfig.basefile, "usertoken", "");
                    Intent intentto = new Intent();
                    intentto.setClass(WelcomeActivity.this, MainActivity.class);
                    startActivity(intentto);
                    finish();
                    break;

                case 5:
                    App.getInstance().showToast("数据库更新失败,请重新启动app");
                    AppConfig.userid = getValue(WelcomeActivity.this, AppConfig.basefile, "userid", "");
                    AppConfig.usertoken = getValue(WelcomeActivity.this, AppConfig.basefile, "usertoken", "");
                    Intent intenttoa = new Intent();
                    intenttoa.setClass(WelcomeActivity.this, MainActivity.class);
                    startActivity(intenttoa);
                    finish();
                    break;
                case 10:
                    //拷贝数据库
                    String citydb = getValue(WelcomeActivity.this, AppConfig.basedevicefile, "citydb", "");
                    //第一次考培没有记录
                    if (!"true".equals(citydb)) {
                        //    App.getInstance().showToast("第一次打开App需要初始化，请耐心等待!");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 将assets里的数据拷贝到内存
                                manage.safeCopyCourseDB();
                            }
                        }).start();
                        LocalSave.putValue(WelcomeActivity.this, AppConfig.basedevicefile, "citydb", "true");
                        handler.sendMessageDelayed(handler.obtainMessage(3), 500);

                    } else {
                        SQLiteDatabase db = manage.getDatabase(AssetsDataBasesManage.coursesDBName);
                        boolean type = new SQLiteDataBases_db().checkColumnExists(db, "type");
                        if (type) {
                            handler.sendMessageDelayed(handler.obtainMessage(3), 500);

                        } else {
                            String isdelete = getValue(WelcomeActivity.this, AppConfig.basedevicefile, "deletedb", "true");
                            Log.v("lsq", "isdelete+" + isdelete);
                            if ("true".equals(isdelete)) {
                                boolean isresult = manage.DeleteOldDb(AssetsDataBasesManage.coursesDBName);
                                Log.v("lsq", "isresult+" + isresult);
                                if (isresult) {
                                    manage.closeDatabase(AssetsDataBasesManage.coursesDBName);
                                    LocalSave.putValue(WelcomeActivity.this, AppConfig.basedevicefile, "deletedb", "false");
                                    handler.sendMessageDelayed(handler.obtainMessage(3), 500);

                                } else {
                                    App.getInstance().showToast("数据库更新失败,请重新启动app");
                                    handler.sendMessageDelayed(handler.obtainMessage(3), 500);

                                }
                            } else {
                                handler.sendMessageDelayed(handler.obtainMessage(3), 500);

                            }
                        }

                    }
                    break;

                case 12:
                    StyledDialog.dismissLoading();
                    Log.i("zzr", "0sss" + appUpdata.isUpdate());
                    if (appUpdata.isUpdate()) {
                        String appmd5 = getValue(WelcomeActivity.this, AppConfig.basedevicefile, "appmd5", "");
                        if (TextUtils.isEmpty(appmd5)) {
                            if (isForeground(WelcomeActivity.this, "com.digoshop.app.module.welcome.WelcomeActivity")) {
                                chechVersion();
                            } else {
                                handler.sendEmptyMessage(10);
                            }

                        } else {
                            if (appmd5.equals(appUpdata.getNew_md5())) {
                                handler.sendEmptyMessage(10);
                            } else {
                                chechVersion();
                            }
                        }
                    } else {
                        handler.sendEmptyMessage(10);

                    }
                    break;
            }
        }
    };

    private void chechVersion() {
        mDialog = new CustomDialog(WelcomeActivity.this);
        mDialog.setTitle("发现新版本");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContent(appUpdata.getU_log());
        mDialog.setCancelButton("暂不更新", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(10);
                LocalSave.putValue(WelcomeActivity.this, AppConfig.basedevicefile, "appmd5", appUpdata.getNew_md5());
                mDialog.dismiss();

            }
        });
        mDialog.setOKButton("立即更新", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StyledDialog.buildLoading(WelcomeActivity.this, App.getInstance().getString(R.string.onloading), true, false).show();

                //请求存储权限
                boolean hasPermission = (ContextCompat.checkSelfPermission(WelcomeActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                Log.i("zzr", "hasPermission+" + hasPermission);
                if (!hasPermission) {
                    ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                    ActivityCompat.shouldShowRequestPermissionRationale(WelcomeActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    //下载
                    startDownload();
                    handler.sendEmptyMessage(10);
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
                    Toast.makeText(WelcomeActivity.this, "不授予存储权限将无法进行下载!", Toast.LENGTH_SHORT).show();
                }
                handler.sendEmptyMessage(10);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        StyledDialog.dismissLoading();

    }

    /**
     * 启动下载
     */
    private void startDownload() {
        Intent it = new Intent(WelcomeActivity.this, UpdateService.class);

        //下载地址
        it.putExtra("apkUrl", appUpdata.getDown_url());
        startService(it);
        mDialog.dismiss();
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     */
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
    public boolean isNoTitle() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }
}
