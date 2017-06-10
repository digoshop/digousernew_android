package com.digoshop.app.module.setting;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.CannedAccessControlList;
import com.alibaba.sdk.android.oss.model.CreateBucketRequest;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.storedetail.model.UserInfoData;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.FileUtils;
import com.digoshop.app.utils.ImageUtil;
import com.digoshop.app.utils.KeyboardUtils;
import com.digoshop.app.utils.RoundImageView;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static com.digoshop.app.utils.AppConfig.accessKeyId;
import static com.digoshop.app.utils.AppConfig.accessKeySecret;
import static com.digoshop.app.utils.AppConfig.endpoint;
import static com.digoshop.app.utils.AppConfig.testBucket;
import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * TODO 用户中心-设置-更改用户信息
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016-6-6 下午11:16:27
 * @version: V1.0
 */
public class ModifUserInfoAty extends TakePhotoActivity implements OnClickListener {
    private TextView tv_title_right, tv_location, tv_user_sex, tv_user_birthday, tv_user_marry;
    private RoundImageView user_Headportrait;
    private TextView tv_user_village, tv_edit_user_quyu;
    private EditText edit_user_name, edit_user_nicheng, edit_user_detailaddres,
            edit_user_job, edit_user_interest;
    private UserInfoData userinfo;
    private DisplayImageOptions options;
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mLocationClient = null;
    SharedPreferences mySharedPreferences;
    private Context mContext;
    private Bitmap mBitmap;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;
    private String avatar, wedding, occipaction, birthday, village, area, gen, hobby, nick, realname, address;
    private OSS oss;
    // uploadObject 这个前面不能加/，且里面的"2016-09-05/beyond001.jpg"是咱们自己定义的可以随便写啥
    // 但是为了同意规定，规则是日期/图片名/加后缀
    private static String uploadObject;
    private String andoridimgpath = "android_user/modifuser/";
    private static String uploadObjecturl = "";
    private static final String downloadObject = "2016-09-05/beyond.jpg";
    private String province = "", citycode, city = "", district = "";
    Uri mPhotoUri;
    // 获取一个日历对象
    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
    private String urlpath;
    public static String VILLAGESTR = null;//小区
    // 当点击DatePickerDialog控件的设置按钮时，调用该方法
    DatePickerDialog.OnDateSetListener selectdate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // 修改日历控件的年，月，日
            // 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            tv_user_birthday.setText(df.format(dateAndTime.getTime()));
        }
    };
    DatePickerDialog.OnDateSetListener selectdates = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // 修改日历控件的年，月，日
            // 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            tv_user_marry.setText(df.format(dateAndTime.getTime()));
        }
    };

    public void OnTitleReturnClick(View view) {
        KeyboardUtils.hideSoftInput(ModifUserInfoAty.this);

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifuserinfoaty);

        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("完善个人资料");
        mContext = ModifUserInfoAty.this;
        mImageList = new ArrayList<Bitmap>();
        mFileList = new ArrayList<String>();
        tv_title_right = (TextView) findViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setOnClickListener(this);
        tv_title_right.setText("保存");
        user_Headportrait = (RoundImageView) findViewById(R.id.user_Headportrait);
        edit_user_name = (EditText) findViewById(R.id.edit_user_name);
        edit_user_nicheng = (EditText) findViewById(R.id.edit_user_nicheng);
        tv_user_sex = (TextView) findViewById(R.id.tv_user_sex);
        tv_edit_user_quyu = (TextView) findViewById(R.id.tv_edit_user_quyu);
        edit_user_detailaddres = (EditText) findViewById(R.id.edit_user_detailaddres);
        tv_user_village = (TextView) findViewById(R.id.tv_user_village);
        tv_user_village.setOnClickListener(this);
        tv_user_birthday = (TextView) findViewById(R.id.tv_user_birthday);
        edit_user_job = (EditText) findViewById(R.id.edit_user_job);
        tv_user_marry = (TextView) findViewById(R.id.tv_user_marry);
        edit_user_interest = (EditText) findViewById(R.id.edit_user_interest);
        tv_location = (TextView) findViewById(R.id.tv_location);
        user_Headportrait.setOnClickListener(this);
        tv_user_sex.setOnClickListener(this);
        tv_user_birthday.setOnClickListener(this);
        tv_user_marry.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        userinfo = new UserInfoData();
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.grzx_touxiang)
                .showImageForEmptyUri(R.drawable.grzx_touxiang).showImageOnFail(R.drawable.grzx_touxiang).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
        if(getNetWifi()){
            get_ModiUserinfo();
        }else{
            App.getInstance().showToast("网络不给力，请检查网络设置");
            finish();
        }

        tv_user_birthday.setClickable(true);
        tv_user_birthday.setFocusable(true);
        tv_user_marry.setClickable(true);
        tv_user_marry.setFocusable(true);
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider, conf);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (VILLAGESTR != null) {
            tv_user_village.setText(VILLAGESTR);
        }
//        // 先隐藏键盘
//        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
//                .hideSoftInputFromWindow(ModifUserInfoAty.this.getCurrentFocus()
//                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.sendEmptyMessage(9);

    }

    private void get_ModiUserinfo() {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                get_UserInfo();
            }
        }).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ImageLoader.getInstance().displayImage(userinfo.getAvatar(), user_Headportrait, options);
                    edit_user_name.setText(userinfo.getReal_name());
                    edit_user_nicheng.setText(userinfo.getNick());
                    String sex = userinfo.getGen();
                    if (!TextUtils.isEmpty(sex)) {
                        if (sex.equals("0")) {
                            tv_user_sex.setText("保密");
                        } else if (sex.equals("1")) {
                            tv_user_sex.setText("男");
                        } else if (sex.equals("2")) {
                            tv_user_sex.setText("女");
                        }
                    }
                    tv_edit_user_quyu.setText(userinfo.getProvince() + userinfo.getCity() + userinfo.getDistrict());
                    edit_user_detailaddres.setText(userinfo.getAddress());
                    if (!TextUtils.isEmpty(userinfo.getVillage())) {
                        tv_user_village.setText(userinfo.getVillage());

                    }
                    if (!TextUtils.isEmpty(userinfo.getBirthday())) {
                        tv_user_birthday.setText(userinfo.getBirthday());

                    }
                    if (!TextUtils.isEmpty(userinfo.getWedding())) {
                        tv_user_marry.setText(userinfo.getWedding());

                    }
                    edit_user_job.setText(userinfo.getOccupation());
                    edit_user_interest.setText(userinfo.getHobby());
                    StyledDialog.dismissLoading();
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
                case 5:
                    //   LocalSave.putValue(ModifUserInfoAty.this, AppConfig.basefile, "login_user_account", edit_user_name.getText().toString());
                    App.getInstance().showToast("保存成功");
                    StyledDialog.dismissLoading();

                    if (mFileList != null) {
                        mFileList.clear();
                    }
                    if (mImageList != null) {
                        mImageList.clear();
                    }
                    urlpath = "";
                    KeyboardUtils.hideSoftInput(ModifUserInfoAty.this);

                    finish();

                    break;
                case 6:
                    App.getInstance().showToast("保存失败");
                    StyledDialog.dismissLoading();
                    break;
                case 7:
                    uploadObjecturl = AppConfig.pj + uploadObject;
                    update_Userinfo(AppConfig.pj + uploadObject);
                    break;
                case 8:
                    App.getInstance().showToast("图片上传失败!");
                    StyledDialog.dismissLoading();

                    break;
                case 9:
                    getGpsPermission();

                    break;
            }
        }
    };

    private void getGpsPermission() {
        //http://blog.csdn.net/lmj623565791/article/details/50709663
        MPermissions.requestPermissions(ModifUserInfoAty.this, 4, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @PermissionGrant(4)
    public void requestContactSuccess() {
        if (getNetWifi()) {
            Log.v("ceshi","点我啦");
            // 初始化定位
            mLocationClient = new AMapLocationClient(ModifUserInfoAty.this);
            // 设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);
            setUpMap();
        } else {
            App.getInstance().showToast("网络不给力，请检查网络设置");
        }

    }



    @PermissionDenied(4)
    public void requestContactFailed() {
        App.getInstance().showToast("获取权限失败!");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void get_UserInfo() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            userinfo = api.get_user("1", "0");
            if (userinfo != null) {
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
    public void onClick(View v) {
        DatePickerDialog dateDlg;
        switch (v.getId()) {
            case R.id.tv_title_right:
                MakeSure();
                break;
            case R.id.tv_location:
                if (mLocationClient != null) {
                    mLocationClient.stopLocation();// 停止定位
                }
                App.getInstance().showToast("获取定位");
                handler.sendEmptyMessage(9);
                break;
            case R.id.tv_user_birthday:
                dateDlg = new DatePickerDialog(this, selectdate, dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH));
                //现在时间
                dateDlg.getDatePicker().setMaxDate((new Date()).getTime());
                dateDlg.show();
                break;
            case R.id.tv_user_marry:
                dateDlg = new DatePickerDialog(this, selectdates, dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH));
                dateDlg.getDatePicker().setMaxDate((new Date()).getTime());
                dateDlg.show();
                break;
            case R.id.tv_user_sex:
                SexDialog();
                break;
            case R.id.user_Headportrait:
                showPopwindow();
                break;
            case R.id.tv_user_village:
                Intent intentm = new Intent();
                intentm.setClass(ModifUserInfoAty.this, SelectShequListActivity.class);
                startActivity(intentm);
                break;
            default:
                break;
        }
    }

    TakePhoto takePhoto;
    Uri imageUri;
    private List<Bitmap> mImageList;
    private List<String> mFileList;

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        setImageToView(result.getImages());
    }

    private void showPopwindow() {
        KeyboardUtils.hideSoftInput(ModifUserInfoAty.this);
        View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(this, R.layout.camera_pop_menu, null);
        Button btnCamera = (Button) popView.findViewById(R.id.btn_camera_pop_camera);
        Button btnAlbum = (Button) popView.findViewById(R.id.btn_camera_pop_album);
        Button btnCancel = (Button) popView.findViewById(R.id.btn_camera_pop_cancel);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        final PopupWindow popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);// 设置允许在外点击消失
        // 图片存贮路径：
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        imageUri = Uri.fromFile(file);
        takePhoto = getTakePhoto();
        View.OnClickListener listener = new View.OnClickListener() {

            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_camera_pop_camera:
                        if (takePhoto != null) {
                            CompressConfig config = new CompressConfig.Builder()
                                    .setMaxSize(51200)
                                    .setMaxPixel(500)
                                    .create();
                            takePhoto.onEnableCompress(config, true);
                        }
                        if (mImageList != null) {
                            if (mImageList.size() == 1) {
                                App.getInstance().showToast("最多选择1张照片!");
                            } else {
                                //打开摄像头
                                takePhoto.onPickFromCapture(imageUri);
                            }
                        } else {
                            //打开摄像头
                            takePhoto.onPickFromCapture(imageUri);
                        }

                        break;
                    case R.id.btn_camera_pop_album:
                        if (takePhoto != null) {
                            CompressConfig config = new CompressConfig.Builder()
                                    .setMaxSize(51200)
                                    .setMaxPixel(500)
                                    .create();
                            takePhoto.onEnableCompress(config, true);
                        }
//                        CompressConfig config = new CompressConfig.Builder()
//                                .setMaxSize(102400)
//                                .setMaxPixel(600 >= 600 ? 600 : 600)
//                                .create();
//                        takePhoto.onEnableCompress(config, true);
                        //  是否使用自带相册
                        takePhoto.setTakePhotoOptions(new TakePhotoOptions.Builder().setWithOwnGallery(true).create());
//                //从相册
//                takePhoto.onPickFromGallery();
                        // 选择最多选择几张
                        takePhoto.onPickMultiple(1);

                        break;
                    case R.id.btn_camera_pop_cancel:

                        break;
                }
                popWindow.dismiss();
            }
        };

        btnCamera.setOnClickListener(listener);
        btnAlbum.setOnClickListener(listener);
        btnCancel.setOnClickListener(listener);

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void showOOs() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v("TEST", "path+" + urlpath);
                uploadObject = andoridimgpath + Tool.getUninxToJavaDaya() + "/" + Tool.getUninxTimea() + ".jpg";
                Log.i("TEST", "---uploadObject----" + uploadObject);
                PutObjectRequest put = new PutObjectRequest(testBucket, uploadObject, urlpath);
                // 异步上传时可以设置进度回调
                put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                        Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                    }
                });
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(testBucket);
                createBucketRequest.setBucketACL(CannedAccessControlList.PublicRead);
                // 指定Bucket的ACL权限
                createBucketRequest.setLocationConstraint("oss-cn-hangzhou");
                // 指定Bucket所在的数据中心
                OSSAsyncTask task = oss.asyncPutObject(put,
                        new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                            @Override
                            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                                int requ = result.getStatusCode();
                                if (requ == 200) {
                                    handler.sendEmptyMessage(7);
                                } else {
                                    handler.sendEmptyMessage(8);
                                }
                            }

                            @Override
                            public void onFailure(PutObjectRequest request, ClientException clientExcepion,
                                                  ServiceException serviceException) {
                                // 请求异常
                                if (clientExcepion != null) {
                                    // 本地异常如网络异常等
                                    clientExcepion.printStackTrace();
                                }
                                if (serviceException != null) {
                                    // 服务异常
                                    Log.e("ErrorCode", serviceException.getErrorCode());
                                    Log.e("RequestId", serviceException.getRequestId());
                                    Log.e("HostId", serviceException.getHostId());
                                    Log.e("RawMessage", serviceException.getRawMessage());
                                }
                            }
                        });
            }
        }).start();
    }

    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     *
     * @return int 得到的字符串长度
     */
    public static int Length(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    private void SexDialog() {
        final String items[] = {"保密", "男", "女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // 先得到构造器
        builder.setTitle("修改性别"); // 设置标题
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_user_sex.setText(items[which] + "");
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void MakeSure() {
        //avatar如果等于空说明用户没有默认头像
        //如果urlpath等于空说明用户也没有选择图片
        if (TextUtils.isEmpty(userinfo.getAvatar()) & TextUtils.isEmpty(urlpath)) {
            App.getInstance().showToast("请设置头像!");
            return;
        }
        //用户姓名
        realname = edit_user_name.getText().toString();
        if (TextUtils.isEmpty(realname)) {
            App.getInstance().showToast("请输入2-16位姓名");
            return;
        } else {
            if (TextUtils.isEmpty(realname.replaceAll(" ", ""))) {
                App.getInstance().showToast("请输入2-16位姓名");
                return;
            }
        }
        realname = realname.replaceAll(" ", "");
        int lengname = Length(realname);
        Log.v("ceshi", "***" + lengname);
        if (lengname < 4 || lengname > 32) {
            App.getInstance().showToast("请输入2-16位姓名");
            return;
        }

        //昵称
        nick = edit_user_nicheng.getText().toString();
        if (TextUtils.isEmpty(nick)) {
            App.getInstance().showToast("请输入2-16位昵称");
            return;
        } else {
            if (TextUtils.isEmpty(nick.replaceAll(" ", ""))) {
                App.getInstance().showToast("请输入2-16位昵称");
                return;
            }
        }
        nick = nick.replaceAll(" ", "");
        int lengnick = Length(nick);
        Log.v("ceshi", "lengnick+" + lengnick);
        if (lengnick < 4 || lengnick > 32) {
            App.getInstance().showToast("请输入2-16位昵称");
            return;
        }

        //性别
        gen = tv_user_sex.getText().toString();
        if (TextUtils.isEmpty(gen)) {
            App.getInstance().showToast("请选择性别!");
            return;
        }
        if (gen.equals("保密")) {
            gen = "0";
        } else if (gen.equals("男")) {
            gen = "1";
        } else if (gen.equals("女")) {
            gen = "2";
        }
        //所在区域
        area = tv_edit_user_quyu.getText().toString();
        //详细地址
        address = edit_user_detailaddres.getText().toString();
//        if (TextUtils.isEmpty(address)) {
//            App.getInstance().showToast("请填写详细地址!");
//            return;
//        }
        //所在小区
        village = tv_user_village.getText().toString();
        if ("请选择小区".equals(village)) {
            village = "";
        }
        //生日
        birthday = tv_user_birthday.getText().toString();
        if ("请选择生日".equals(birthday)) {
            App.getInstance().showToast("请选择生日");
            return;
        }
        //职业
        occipaction = edit_user_job.getText().toString();

        //结婚纪念日
        wedding = tv_user_marry.getText().toString();
        if ("请选择结婚纪念日".equals(wedding)) {
            wedding = "";
//            App.getInstance().showToast("请选择结婚纪念日");
//            return;
        }
        //兴趣
        hobby = edit_user_interest.getText().toString();

        if (urlpath == null) {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

            //执行到这里说明userinfo.getAvatar()不为空，及用处存在默认头像
            update_Userinfo(userinfo.getAvatar());
        } else {
            StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();

            //说明用户通过选择本地或者拍照更改了头像，所以要重新上传图片在提交接口
            //上传头像头像自动调用更新用户资料接口
            showOOs();
        }
    }

    private void update_Userinfo(final String uploadurl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                update_UserinfoApi(uploadurl);
            }
        }).start();
    }

    private void update_UserinfoApi(String uploadurl) {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            Log.i("TEST", "--uploadObjecturl--" + uploadObjecturl);
            UserInfoData userinfo = api.update_user_info(realname, nick, area, address, village, birthday, occipaction,
                    wedding, gen, uploadurl, hobby, province, citycode, city, district);
            if (userinfo != null) {
                if (!TextUtils.isEmpty(userinfo.getUid())) {
                    handler.sendEmptyMessage(5);
                } else {
                    handler.sendEmptyMessage(6);
                }
            } else {
                handler.sendEmptyMessage(6);
            }

        } catch (JSONException e) {
            handler.sendEmptyMessage(6);
            Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            handler.sendEmptyMessage(6);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }

    // 声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    // 定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();// 获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();// 获取纬度
                    amapLocation.getLongitude();// 获取经度
                    amapLocation.getAccuracy();// 获取精度信息
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());
                    df.format(date);// 定位时间
                    amapLocation.getAddress();// 地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    amapLocation.getCountry();// 国家信息
                    province = amapLocation.getProvince();// 省信息
                    city = amapLocation.getCity();// 城市信息
                    Log.v("Amap", "getCity+" + amapLocation.getCity());
                    district = amapLocation.getDistrict();// 城区信息
                    amapLocation.getStreet();// 街道信息
                    amapLocation.getStreetNum();// 街道门牌号信息
                    citycode = amapLocation.getCityCode();// 城市编码

                    Log.v("Amap", "getCityCode+" + amapLocation.getCityCode());

                    amapLocation.getAdCode();// 地区编码
                    amapLocation.getAoiName();// 获取当前定位点的AOI信息
                    if (TextUtils.isEmpty(province)) {
                        province = "";
                    }
                    if (TextUtils.isEmpty(city)) {
                        city = "";
                    }
                    if (TextUtils.isEmpty(district)) {
                        district = "";
                    }
                    Log.v("ceshi","点我啦"+province);
                    if (TextUtils.isEmpty(province + city + district)) {
                        tv_edit_user_quyu.setText("定位失败!");
                    } else {
                        tv_edit_user_quyu.setText(province + city + district);

                    }

                    double lat = amapLocation.getLatitude();
                    double lon = amapLocation.getLongitude();
                    Log.v("Amap", "lat : " + lat + " lon : " + lon);
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    // 用putString的方法保存数据
                    editor.putString("CityCode", amapLocation.getCityCode());
                    editor.putString("lat", lat + "");
                    editor.putString("lon", lon + "");
                    // 提交当前数据
                    editor.commit();

                } else {
                    tv_edit_user_quyu.setText("定位失败!");
                    // 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }

    };

    private void setUpMap() {
        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        // 设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        // 设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mLocationClient.startLocation();
    }


    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(ArrayList<TImage> images) {
        Bitmap bitmap;
        if (images != null) {
            if (images.size() > 0) {
                Log.v("ceshi", "ddddd" + images.size());
                if (mFileList != null) {
                    mFileList.clear();
                }
                if (mImageList != null) {
                    mImageList.clear();
                }
                //  bitmap = BitmapFactory.decodeFile(images.get(0).getPath());
                bitmap = ModifyBitmap(images.get(0).getPath());
                mImageList.add(bitmap);
                mFileList.add(images.get(0).getPath());
                WindowManager wm = this.getWindowManager();
                @SuppressWarnings("deprecation")
                int width = wm.getDefaultDisplay().getWidth();
                Bitmap photo = ImageUtil.zoomImgForW(bitmap, width);
                // Drawable drawable = new BitmapDrawable(photo);
                urlpath = saveBitmapFile(photo).getAbsolutePath();
                // 这里图片是方形的，可以用一个工具类处理成圆形（很多头像都是圆形，这种工具类网上很多不再详述）
                Log.i("TEST", "---urlpath---" + urlpath);
                user_Headportrait.setImageBitmap(photo);// 显示图片
                // 在这个地方可以写上上传该图片到服务器的代码，后期将单独写一篇这方面的博客，敬请期待...
            }
        }
    }

    private Bitmap ModifyBitmap(String absolutePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        // 这个isjustdecodebounds很重要
        opt.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeFile(absolutePath, opt);

        // 获取到这个图片的原始宽度和高度
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;

        // 获取屏的宽度和高度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        opt.inSampleSize = 2;
        // 根据屏的大小和图片大小计算出缩放比例
        if (picWidth > picHeight) {
            if (picWidth > screenWidth)
                opt.inSampleSize = picWidth / screenWidth;
        } else {
            if (picHeight > screenHeight)

                opt.inSampleSize = picHeight / screenHeight;
        }

        // 这次再真正地生成一个有像素的，经过缩放了的bitmap
        opt.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(absolutePath, opt);
        return bm;
    }

    public File saveBitmapFile(Bitmap image) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String imgaddress = sDateFormat.format(new java.util.Date());
        File file = new File(FileUtils.SDPATH + imgaddress + ".JPEG");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            image.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
        }
        return file;
    }


    /**
     * 判断是否是合法（允许4~32字节，允许字母数字下划线）
     *
     * @param account：帐号
     * @return boolean
     */
    private boolean isAccountRegex(String account) {
        String accountPattern = "^\\w{2,16}$";
        return Pattern.matches(accountPattern, account);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();// 停止定位
        }

    }
}

