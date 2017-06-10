package com.digoshop.app.module.userCenter;

import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.digoshop.R;
import com.digoshop.app.App;
import com.digoshop.app.api.impl.DigoIUserApiImpl;
import com.digoshop.app.module.arrountshops.view.PullLayout;
import com.digoshop.app.module.arrountshops.view.PullToRefreshLayout;
import com.digoshop.app.module.login.Loginaty;
import com.digoshop.app.module.setting.SettingActivity;
import com.digoshop.app.module.storedetail.model.UserInfoData;
import com.digoshop.app.module.userCenter.module.MyCardActivity;
import com.digoshop.app.module.userCenter.module.MyCostHistoryActivity;
import com.digoshop.app.module.userCenter.module.MyCouponActivity;
import com.digoshop.app.module.userCenter.module.MyCustiomGSListActivity;
import com.digoshop.app.module.userCenter.module.MyGiftWalletActivity;
import com.digoshop.app.module.userCenter.module.MyJifenActivity;
import com.digoshop.app.module.userCenter.module.MyLikeActivity;
import com.digoshop.app.module.userCenter.module.MyMessageActivity;
import com.digoshop.app.module.userCenter.module.MyReplyActivity;
import com.digoshop.app.module.userCenter.module.MyqecodeActivity;
import com.digoshop.app.utils.AppConfig;
import com.digoshop.app.utils.FileUtils;
import com.digoshop.app.utils.ImageUtil;
import com.digoshop.app.utils.LocalSave;
import com.digoshop.app.utils.RoundImageView;
import com.digoshop.app.utils.Tool;
import com.digoshop.app.utils.http.WSError;
import com.hss01248.dialog.StyledDialog;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoFragment;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.digoshop.app.utils.AppConfig.accessKeyId;
import static com.digoshop.app.utils.AppConfig.accessKeySecret;
import static com.digoshop.app.utils.AppConfig.endpoint;
import static com.digoshop.app.utils.AppConfig.testBucket;
import static com.digoshop.app.utils.Tool.getNetWifi;

/**
 * TODO<个人中心>
 *
 * @author liushengqiang lsqbeyond@yeah.net
 * @data: 2016年9月16日 上午1:23:00
 * @version: V1.0
 */
public class UserCenterFragment extends TakePhotoFragment implements OnClickListener {


    private LinearLayout ll_photo, ll_qrcode;
    private TextView tv_name, tv_id;
    private RelativeLayout rl_mymessage, rl_mycost, rl_myjifen, rl_msg, rl_mycustom, rl_mycard, rl_mygift, rl_mycoupon,
            rl_mylike;
    private RoundImageView iv_photo;
    private String mypointstr;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;
    private String urlpath;

    private OSS oss;

    // uploadObject 这个前面不能加/，且里面的"2016-09-05/beyond001.jpg"是咱们自己定义的可以随便写啥
    // 但是为了同意规定，规则是日期/图片名/加后缀
    private static String uploadObject;
    private String andoridimgpath = "android_user/usercenter/";
    private static String uploadObjecturl = "";
    private static final String downloadObject = "2016-09-05/beyond.jpg";
    private UserInfoData userinfo;
    private DisplayImageOptions options;
    private String moblile = null;
    private String usernamestr = null;
    private PullLayout refresh_store_view;
    private String umImage, userAccount;
    private ImageView iv_title_right;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_usercenter, null);
        AppConfig.userid = LocalSave.getValue(getActivity(), AppConfig.basefile, "userid", "");
        AppConfig.usertoken = LocalSave.getValue(getActivity(), AppConfig.basefile, "usertoken", "");
        umImage = LocalSave.getValue(App.getInstance(), AppConfig.basefile, "login_um_image", "");//友盟登录头像
        //登录注册返回的用户名
        userAccount = LocalSave.getValue(App.getInstance(), AppConfig.basefile, "login_user_account", "");//登录账号
        initView(v);
        return v;
    }

    void initView(View v) {
        mImageList = new ArrayList<Bitmap>();
        mFileList = new ArrayList<String>();
        ImageView iv_title_return = (ImageView) v.findViewById(R.id.iv_title_return);
        iv_title_return.setVisibility(View.GONE);
        TextView tv_title_content = (TextView) v.findViewById(R.id.tv_title_content);
        tv_title_content.setText("我的");
        iv_title_right = (ImageView) v.findViewById(R.id.iv_title_right);
        iv_title_right.setOnClickListener(this);
        iv_title_right.setVisibility(View.VISIBLE);
        iv_title_right.setBackgroundResource(R.drawable.grzx_shezhi);
//        ViewGroup.LayoutParams para = iv_title_right.getLayoutParams();
//
//
//        para.height = DensityUtil.dip2px(App.getInstance(), 20);
//        para.width = DensityUtil.dip2px(App.getInstance(), 20);
        //      iv_title_right.setLayoutParams(para);
        ll_photo = (LinearLayout) v.findViewById(R.id.ll_photo);
        ll_qrcode = (LinearLayout) v.findViewById(R.id.ll_qrcode);
        tv_name = (TextView) v.findViewById(R.id.tv_name);
        tv_id = (TextView) v.findViewById(R.id.tv_id);
        rl_mymessage = (RelativeLayout) v.findViewById(R.id.rl_mymessage);
        rl_mycost = (RelativeLayout) v.findViewById(R.id.rl_mycost);
        rl_myjifen = (RelativeLayout) v.findViewById(R.id.rl_myjifen);
        rl_msg = (RelativeLayout) v.findViewById(R.id.rl_msg);
        rl_mycustom = (RelativeLayout) v.findViewById(R.id.rl_mycustom);
        rl_mycard = (RelativeLayout) v.findViewById(R.id.rl_mycard);
        rl_mygift = (RelativeLayout) v.findViewById(R.id.rl_mygift);
        rl_mycoupon = (RelativeLayout) v.findViewById(R.id.rl_mycoupon);
        rl_mylike = (RelativeLayout) v.findViewById(R.id.rl_mylike);
        iv_photo = (RoundImageView) v.findViewById(R.id.iv_photo);
        ll_photo.setOnClickListener(this);
        ll_qrcode.setOnClickListener(this);
        rl_mymessage.setOnClickListener(this);
        rl_mycost.setOnClickListener(this);
        rl_myjifen.setOnClickListener(this);
        rl_msg.setOnClickListener(this);
        rl_mycustom.setOnClickListener(this);
        rl_mycard.setOnClickListener(this);
        rl_mygift.setOnClickListener(this);
        rl_mycoupon.setOnClickListener(this);
        rl_mylike.setOnClickListener(this);
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.grzx_touxiang)
                .showImageForEmptyUri(R.drawable.grzx_touxiang).showImageOnFail(R.drawable.grzx_touxiang).cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
                .build();
        iv_photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    showPopwindow();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), Loginaty.class);
                    startActivity(intent);
                }

            }
        });
        refresh_store_view = ((PullLayout) v
                .findViewById(R.id.refresh_store_view));
        refresh_store_view.setOnRefreshListener(new PullLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullLayout pullToRefreshLayout) {
                if (getNetWifi()) {
                    get_ModiUserinfo();
                } else {
                    App.getInstance().showToast("网络不给力，请检查网络设置");
                }

                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        });
        tv_name.setOnClickListener(this);
        tv_id.setOnClickListener(this);

    }


    private void get_ModiUserinfo() {
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
                    //  App.getInstance().showToast("获取用户信息成功!");
                    if (!TextUtils.isEmpty(umImage)) {
                        ImageLoader.getInstance().displayImage(umImage, iv_photo, options);
                    } else {
                        ImageLoader.getInstance().displayImage(userinfo.getAvatar(), iv_photo, options);
                    }
                    LocalSave.putValue(getActivity(), AppConfig.basefile, "userimg", userinfo.getAvatar());

                    if (!TextUtils.isEmpty(userAccount)) {
                        tv_name.setText(userAccount);
                        usernamestr = userAccount;
                    }
//                    //第三方登录返回的用户名
//                    String umName = LocalSave.getValue(App.getInstance(), AppConfig.basefile, "login_um", "");//友盟登录昵称
//                    if (!TextUtils.isEmpty(umName)) {
//                        tv_name.setText(umName);
//                        usernamestr = umName;
//                        tv_id.setVisibility(View.GONE);
//                    }
                    //接口返回的用户名:
                    String nickstr = userinfo.getNick();
                    if (!TextUtils.isEmpty(nickstr)) {
                        tv_name.setText(nickstr);
                        usernamestr = nickstr;
                    }
                    LocalSave.putValue(getActivity(), AppConfig.basefile, "userendname", tv_name.getText().toString());
                    //登录注册返回的手机号
                    String user_moblile = LocalSave.getValue(App.getInstance(), AppConfig.basefile, "login_mobile", "");
                    //手机快捷登录输入的手机号
                    String sms_moblile = LocalSave.getValue(App.getInstance(), AppConfig.basefile, "login_sms", "");
                    if (!TextUtils.isEmpty(user_moblile)) {
                        moblile = user_moblile;
                    }
                    if (!TextUtils.isEmpty(sms_moblile)) {
                        moblile = sms_moblile;
                    }
                    if (!TextUtils.isEmpty(userinfo.getMobile())) {
                        moblile = userinfo.getMobile();
                    }
                    if (!TextUtils.isEmpty(moblile)) {
                        StringBuilder sb = new StringBuilder();
                        if (moblile.length() > 6) {
                            for (int i = 0; i < moblile.length(); i++) {
                                char c = moblile.charAt(i);
                                if (i >= 3 && i <= 6) {
                                    sb.append('*');
                                } else {
                                    sb.append(c);
                                }
                            }
                        }
                        tv_id.setText(sb.toString());
                    }
                    StyledDialog.dismissLoading();
                    break;
                case 2:
                    //用户登录状态，登录成功未true ,默认未false
                    AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                    if ("true".equals(AppConfig.isusertype)) {
                    } else {
                        tv_id.setText("点击登录");
                        tv_name.setText("");
                        ImageLoader.getInstance().displayImage(null, iv_photo, options);
                    }
                    StyledDialog.dismissLoading();


                    break;
                case 3:
                    App.getInstance().showToast("解析异常");
                    StyledDialog.dismissLoading();
                    break;
                case 4:
                    if (mFileList != null) {
                        mFileList.clear();
                    }
                    if (mImageList != null) {
                        mImageList.clear();
                    }
                    App.getInstance().showToast("头像上传成功");
                    ImageLoader.getInstance().displayImage(AppConfig.pj + uploadObject, iv_photo, options);

                    get_UserCenter();
                    StyledDialog.dismissLoading();
                    break;
                case 5:
                    App.getInstance().showToast("头像上传失败!");
                    StyledDialog.dismissLoading();
                    break;
                case 7:
                    App.getInstance().showToast("头像上传失败!");
                    break;
            }

        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.v("ceshi", "user");
        } else {
            if (getNetWifi()) {
                get_ModiUserinfo();
            }
        }
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
            handler.sendEmptyMessage(2);
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }


    TakePhoto takePhoto;
    Uri imageUri;
    private List<Bitmap> mImageList;
    private List<String> mFileList;

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        setImageToView(result.getImages());
    }

    private void showPopwindow() {
        View parent = ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
        View popView = getActivity().getLayoutInflater().inflate(R.layout.camera_pop_menu, null,
                false);

        Button btnCamera = (Button) popView.findViewById(R.id.btn_camera_pop_camera);
        Button btnAlbum = (Button) popView.findViewById(R.id.btn_camera_pop_album);
        Button btnCancel = (Button) popView.findViewById(R.id.btn_camera_pop_cancel);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        final PopupWindow popWindow = new PopupWindow(popView, width, height, true);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);// 设置允许在外点击消失
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
//                        CompressConfig config = new CompressConfig.Builder()
//                                .setMaxSize(51200)
//                                .setMaxPixel(600 >= 600 ? 600 : 600)
//                                .create();
//                        takePhoto.onEnableCompress(config, true);
                        if (takePhoto != null) {
                            CompressConfig config = new CompressConfig.Builder()
                                    .setMaxSize(51200)
                                    .setMaxPixel(500)
                                    .create();
                            takePhoto.onEnableCompress(config, true);
                        }
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

        // 点击其他地方消失
        popView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                }
                return false;
            }
        });
        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(ArrayList<TImage> images) {
        Bitmap bitmap;
        if (images != null) {
            if (images.size() > 0) {
                if (mFileList != null) {
                    mFileList.clear();
                }
                if (mImageList != null) {
                    mImageList.clear();
                }
                // bitmap = BitmapFactory.decodeFile(images.get(0).getPath());
                bitmap = ModifyBitmap(images.get(0).getPath());

                mImageList.add(bitmap);
                mFileList.add(images.get(0).getPath());
                WindowManager wm = getActivity().getWindowManager();
                @SuppressWarnings("deprecation")
                int width = wm.getDefaultDisplay().getWidth();
                Bitmap photo = ImageUtil.zoomImgForW(bitmap, width);
                // Drawable drawable = new BitmapDrawable(photo);
                urlpath = saveBitmapFile(photo).getAbsolutePath();
                // 这里图片是方形的，可以用一个工具类处理成圆形（很多头像都是圆形，这种工具类网上很多不再详述）
                iv_photo.setImageBitmap(photo);// 显示图片
                // 在这个地方可以写上上传该图片到服务器的代码，后期将单独写一篇这方面的博客，敬请期待...
                showOOS();
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
        WindowManager windowManager = getActivity().getWindowManager();
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

    private void get_UserCenter() {
        StyledDialog.buildLoading(App.getInstance(), App.getInstance().getString(R.string.onloading), true, false).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                get_UserCenterPersonal();
            }
        }).start();
    }

    private void get_UserCenterPersonal() {
        DigoIUserApiImpl api = new DigoIUserApiImpl();
        try {
            uploadObjecturl = AppConfig.pj + uploadObject;
            Log.i("TEST", "--uploadObjecturl--" + uploadObjecturl);
            boolean IsUser = api.get_UserCenter(uploadObjecturl);
            if (IsUser) {
                handler.sendEmptyMessage(6);
            } else {
                handler.sendEmptyMessage(7);
            }
        } catch (JSONException e) {
            Log.v("ceshi", "JSONException");
            e.printStackTrace();
        } catch (WSError e) {
            Log.v("ceshi", "WSError");
            e.printStackTrace();
        }
    }

    private void showOOS() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(getActivity().getApplicationContext(), endpoint, credentialProvider, conf);
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadObject = andoridimgpath + Tool.getUninxToJavaDaya() + "/" + Tool.getUninxTimea() + ".jpg";
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
                                    handler.sendEmptyMessage(4);
                                } else {
                                    handler.sendEmptyMessage(5);
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
                                    Toast.makeText(getActivity(), "图片上传失败，请重新上传", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }).start();
    }

    public File saveBitmapFile(Bitmap image) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String address = sDateFormat.format(new java.util.Date());
        File file = new File(FileUtils.SDPATH + address + ".JPEG");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            image.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
        }
        return file;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_title_right: // 设置
                Log.v("lsq", "###################");
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_qrcode: // 二维码
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(getActivity(), MyqecodeActivity.class);
                    intent.putExtra("mobilnum", moblile);
                    intent.putExtra("userAccountstr", usernamestr);
                    if (userinfo != null) {
                        intent.putExtra("usericon", userinfo.getAvatar());
                    } else {
                        intent.putExtra("usericon", "");
                    }
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), Loginaty.class);
                    startActivity(intent);
                }

                break;

            case R.id.rl_mymessage: // 我的消息
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(getActivity(), MyMessageActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), Loginaty.class);
                    startActivity(intent);
                }

                break;
            case R.id.rl_mycost: // 消费记录
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(getActivity(), MyCostHistoryActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), Loginaty.class);
                    startActivity(intent);
                }

                break;
            case R.id.rl_myjifen: // 我的积分
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(getActivity(), MyJifenActivity.class);
                    intent.putExtra("mypoint", mypointstr);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), Loginaty.class);
                    startActivity(intent);
                }


                break;
            case R.id.rl_msg: // 我的评论
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(getActivity(), MyReplyActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), Loginaty.class);
                    startActivity(intent);
                }


                break;
            case R.id.rl_mycustom: // 我的定制
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(getActivity(), MyCustiomGSListActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), Loginaty.class);
                    startActivity(intent);
                }

                break;
            case R.id.rl_mycard: // 我的卡包
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(getActivity(), MyCardActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), Loginaty.class);
                    startActivity(intent);
                }


                break;
            case R.id.rl_mygift: // 我的礼包
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(getActivity(), MyGiftWalletActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), Loginaty.class);
                    startActivity(intent);
                }

                break;
            case R.id.rl_mycoupon:// 我的优惠券
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(getActivity(), MyCouponActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), Loginaty.class);
                    startActivity(intent);
                }

                break;
            case R.id.rl_mylike: // 我的关注
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                    intent.setClass(getActivity(), MyLikeActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), Loginaty.class);
                    startActivity(intent);
                }


                break;
            case R.id.tv_id:
                //用户登录状态，登录成功未true ,默认未false
                AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
                if ("true".equals(AppConfig.isusertype)) {
                } else {
                    intent.setClass(getActivity(), Loginaty.class);
                    startActivity(intent);
                }
                break;
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.v("ceshi", "LLLLLLLLLLLLLLLLLL");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("ceshi", "onResumeAAAAAAAAAAAAAAA");
        //用户登录状态，登录成功未true ,默认未false
        AppConfig.isusertype = LocalSave.getValue(App.getInstance(), AppConfig.basedevicefile, "userlogintype", "false");
        if ("true".equals(AppConfig.isusertype)) {
            if (getNetWifi()) {
                get_ModiUserinfo();
            } else {
                App.getInstance().showToast("网络不给力，请检查网络设置");
            }
        } else {
            tv_id.setText("点击登录");
            tv_name.setText("");
            ImageLoader.getInstance().displayImage(null, iv_photo, options);

        }
    }
}
